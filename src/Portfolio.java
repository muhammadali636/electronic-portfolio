package ePortfolio;

//Files
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;

//exception
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner; 
import java.util.StringTokenizer;

/**
 * portfolio of investments including stocks and mutual funds,
 *plus a current/starting balance that persists between sessions.
 */
public class Portfolio {
    private ArrayList<Investment> investments = new ArrayList<>();    //array list to store all investments///

    //hash mappin
    private HashMap<String, Investment> symbolMap = new HashMap<>(); //investment symbols to objects
    private HashMap<String, ArrayList<Investment>> keywordMap = new HashMap<>();//mapkeywords to investments that contain those keywords in their names
    private double startingBalance = 0.0;   //track users starting/curent balance
    private double realizedGains = 0.0;             //tracks total realized gains so gains arent lost on selling


    /**
     *(ADDED) Loads portfolio data from an SQLite database, including realizedGains.
     *@param dbPath path to the SQLite database.
     *@throws Exception if there's a problem reading the database.
     */
    public void loadFromDatabase(String dbPath) throws Exception {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            //First clear any existing data in the portfolio (optional).
            clearAllInvestments();
            this.startingBalance = 0.0;
            this.realizedGains = 0.0;

            //Create tables if they don't exist:
            try (Statement stmt = conn.createStatement()) {
                //table for investments
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Investments (" +"symbol TEXT PRIMARY KEY," +"type TEXT," +"name TEXT," +"quantity INTEGER," +"price DOUBLE," +"bookValue DOUBLE" +");");

                //table for realized gains
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS RealizedMeta (" +"id INTEGER PRIMARY KEY," +"realizedGains DOUBLE" +");");

                //table for current balance
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS BalanceMeta (" +"id INTEGER PRIMARY KEY," +"currentBalance DOUBLE" +");");
            }

            //Load the investments
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT symbol,type,name,quantity,price,bookValue FROM Investments")) {
                while (rs.next()) {
                    String symbol = rs.getString("symbol");
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");
                    double bookValue = rs.getDouble("bookValue");

                    Investment investment;
                    if ("Stock".equalsIgnoreCase(type)) {
                        investment = new Stock(symbol, name, quantity, price);
                    } 
                    else if ("MutualFund".equalsIgnoreCase(type)) {
                        investment = new MutualFund(symbol, name, quantity, price);
                    } 
                    else {
                        continue; 
                    }

                    investment.setBookValue(bookValue);
                    investments.add(investment);
                    symbolMap.put(symbol.toLowerCase(), investment);
                    addToKeywordMap(name, investment);
                }
            }

            //load realized gains from RealizedMeta
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT realizedGains FROM RealizedMeta WHERE id=1")) {
                if (rs.next()) {
                    this.realizedGains = rs.getDouble("realizedGains");
                }
            }

            //load current balance from BalanceMeta
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT currentBalance FROM BalanceMeta WHERE id=1")) {
                if (rs.next()) {
                    this.startingBalance = rs.getDouble("currentBalance");
                }
            }

            System.out.println("Portfolio info loaded from database: " + dbPath);
        } 
        catch (SQLException e) {
            throw new Exception("Error loading from DB: " + e.getMessage());
        } 
        finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     *(ADDED) Saves portfolio data to the specified SQLite database, including realizedGains.
     *@param dbPath path to the SQLite database.
     *@throws Exception if there's a problem writing to the database.
     */
    public void saveToDatabase(String dbPath) throws Exception {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            try (Statement stmt = conn.createStatement()) {
                //table for investments
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Investments (" +"symbol TEXT PRIMARY KEY," + "type TEXT," +"name TEXT," +"quantity INTEGER," +"price DOUBLE," +"bookValue DOUBLE" +");");

                //table for realized gains
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS RealizedMeta (" +"id INTEGER PRIMARY KEY," +"realizedGains DOUBLE" +");");

                //table for current balance
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS BalanceMeta (" +"id INTEGER PRIMARY KEY," +"currentBalance DOUBLE" +");");
            }

            //clr existing data in Investments table
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM Investments;");
            }

            //insert (or update) each investment
            for (Investment inv : investments) {
                String typeStr = (inv instanceof Stock) ? "Stock" : "MutualFund";
                try (Statement stmt = conn.createStatement()) {
                    String sql = "INSERT OR REPLACE INTO Investments (symbol,type,name,quantity,price,bookValue) " +
                            "VALUES (" +"'" + inv.getSymbol().replace("'", "''") + "', " +"'" + typeStr + "', " +"'" + inv.getName().replace("'", "''") + "', " +
                            inv.getQuantity() + ", " + inv.getPrice() + ", " + inv.getBookValue() + ");";
                    stmt.executeUpdate(sql);
                }
            }

            //upsert realizedGains
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("INSERT INTO RealizedMeta (id, realizedGains) VALUES (1, " + this.realizedGains + ") " +
                        "ON CONFLICT(id) DO UPDATE SET realizedGains=" + this.realizedGains + ";");
            }

            //upsert currentBalance
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("INSERT INTO BalanceMeta (id, currentBalance) VALUES (1, " + this.startingBalance + ") " +
                        "ON CONFLICT(id) DO UPDATE SET currentBalance=" + this.startingBalance + ";");
            }

            System.out.println("Portfolio saved to database: " + dbPath);
        } 
        catch (SQLException e) {
            throw new Exception("Error saving to DB: " + e.getMessage());
        } 
        finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     *helper to extract value in lines like:
     *CURRENT_BALANCE = "1000.0"
     * or type = "stock"
     * @param line The line containing the key-value pair.
     * @return The extracted value as a String.
     */
    private String extractValue(String line) {
        return line.split("=", 2)[1].trim().replace("\"", "");
    }

    /**
     *Adds an investment's keywords to the keyword map.
     * @param name name of the investment.
     *  @param investment investment to add.
     */
    private void addToKeywordMap(String name, Investment investment) {
        StringTokenizer tokenizer = new StringTokenizer(name.toLowerCase());

        while (tokenizer.hasMoreTokens()) {
            String keyword = tokenizer.nextToken();
            ArrayList<Investment> investmentsList = keywordMap.computeIfAbsent(keyword, k -> new ArrayList<>());
            if (!investmentsList.contains(investment)) {
                investmentsList.add(investment);
            }
        }
    }

    /**
     *Removes an invstment keywords from the keyword map.
     *@param name  name of the investment.
     *@param investment investment to remove.
     */
    private void removeFromKeywordMap(String name, Investment investment) {
        StringTokenizer tokenizer = new StringTokenizer(name.toLowerCase());
        while (tokenizer.hasMoreTokens()) {
            String keyword = tokenizer.nextToken();
            ArrayList<Investment> investmentsList = keywordMap.get(keyword);
            if (investmentsList != null) {
                investmentsList.remove(investment);
                if (investmentsList.isEmpty()) {
                    keywordMap.remove(keyword);
                }
            }
        }
    }

    /**
     *Buys a new investment or adds to an existing one.
     * @param type  type of investment ("Stock" or "Mutual Fund").
     * @param symbol unique symbol for  investment.
     *@param name Name of the investment.
     * @param quantity Quantity to buy.
     *@param price Price per unit.
     * @return A message indicating the result of the operation.
     * @throws Exception if there is an error during the buy operation.
     */
    public String buy(String type, String symbol, String name, int quantity, double price) throws Exception {
        if (symbol == null || symbol.isEmpty()) {
            throw new Exception("Symbol cannot be null or empty.");
        }
        if (name == null || name.isEmpty()) {
            throw new Exception("Name cannot be null or empty.");
        }
        if (quantity <= 0) {
            throw new Exception("Quantity must be positive.");
        }
        if (price <= 0) {
            throw new Exception("Price must be positive.");
        }

        //calc base cost
        double cost = price * quantity;

        //Include commission or fee on buying:
        //If it's a stock, add $9.99; if it's a mutual fund, add $45.00
        if (type.equalsIgnoreCase("Stock")) {
            cost += 9.99;
        } 
        else if (type.equalsIgnoreCase("Mutual Fund")) {
            cost += 45.00;
        }

        //checs if the user has enough balance
        if (cost > this.startingBalance) {
            throw new Exception("Insufficient balance to buy. " + "Cost is $" + String.format("%.2f", cost) + " but current balance is $" + String.format("%.2f", this.startingBalance));
        }

        //sub cost from the starting balance
        this.startingBalance -= cost;

        Investment existingInvestment = symbolMap.get(symbol.toLowerCase());

        if (existingInvestment != null) {
            //make sure type and name
            if ((type.equalsIgnoreCase("Stock") && !(existingInvestment instanceof Stock)) || (type.equalsIgnoreCase("Mutual Fund") && !(existingInvestment instanceof MutualFund))) {
                throw new Exception("Error: Symbol " + symbol + " is already used for a different investment type.");
            }
            if (!existingInvestment.getName().equalsIgnoreCase(name)) {
                throw new Exception("Error: Investment with symbol " + symbol + " already exists with a different name.");
            }

            //update existing investment's quantity/price/bookValue
            existingInvestment.setQuantity(existingInvestment.getQuantity() + quantity);
            existingInvestment.setPrice(price);
            existingInvestment.setBookValue(
                existingInvestment.getBookValue() + existingInvestment.calculateBookValue(quantity, price)
            );

            //autosave changes (to DB or file if you prefer)
            saveToDatabase("lib/portfolio.db");

            return "Updated investment: " + existingInvestment + "\n";
        }

        //createa  new investment
        Investment newInvestment;
        if (type.equalsIgnoreCase("Stock")) {
            newInvestment = new Stock(symbol, name, quantity, price);
        } 
        else if (type.equalsIgnoreCase("Mutual Fund")) {
            newInvestment = new MutualFund(symbol, name, quantity, price);
        } 
        else {
            throw new Exception("Invalid investment type.");
        }

        investments.add(newInvestment);
        symbolMap.put(symbol.toLowerCase(), newInvestment);
        addToKeywordMap(name, newInvestment);

        //auto-save changes
        saveToDatabase("lib/portfolio.db");

        return "Added new investment: " + newInvestment + "\n";
    }

    /**
     *Sells some or all of an investment.
     *@param symbol  symbol of the investment to sell.
     *@param sellQuantity  quantity to sell.
     *@param sellPrice  price at which to sell each unit.
     *@return message indicating the result of the sale.
     *@throws Exception if there is an error during the sell operation.
     */
    public String sell(String symbol, int sellQuantity, double sellPrice) throws Exception {
        if (symbol == null || symbol.isEmpty()) {
            throw new Exception("Symbol cannot be null or empty.");
        }
        if (sellQuantity <= 0) {
            throw new Exception("Sell quantity must be positive.");
        }
        if (sellPrice <= 0) {
            throw new Exception("Sell price must be positive.");
        }

        Investment investment = symbolMap.get(symbol.toLowerCase());
        if (investment == null) {
            throw new Exception("Investment with symbol " + symbol + " not found.");
        }

        if (sellQuantity > investment.getQuantity()) {
            throw new Exception("Error: Selling quantity exceeds available quantity.");
        }

        int oldQuantity = investment.getQuantity();
        double oldBookValue = investment.getBookValue();
        double payment = sellPrice * sellQuantity;

        //fees
        if (investment instanceof Stock) {
            payment -= 9.99;
        } 
        else if (investment instanceof MutualFund) {
            payment -= 45.00;
        }
        if (payment < 0) {
            throw new Exception("Error: Fees exceed payment. Transaction aborted.");
        }

        double portionOfBookValue = oldBookValue * ((double) sellQuantity / oldQuantity);
        double realizedFromThisSale = payment - portionOfBookValue;
        realizedGains += realizedFromThisSale;

        //add  net payment to current balance
        this.startingBalance += payment;

        int newQuantity = oldQuantity - sellQuantity;
        if (newQuantity > 0) {
            double newBookValue = oldBookValue - portionOfBookValue;
            investment.setQuantity(newQuantity);
            investment.setBookValue(newBookValue);

            //autosav -save changes
            saveToDatabase("lib/portfolio.db");

            return "Payment received: $" + String.format("%.2f", payment) + "\nRealized gain from sale: $" + String.format("%.2f", realizedFromThisSale) + "\nUpdated investment: " + investment + "\n";
        } 
        else {
            //iff newQuantity == 0, fully remove the investment
            investments.remove(investment);
            symbolMap.remove(symbol.toLowerCase());
            removeFromKeywordMap(investment.getName(), investment);

            //autosave changes
            saveToDatabase("lib/portfolio.db");

            return "Payment received: $" + String.format("%.2f", payment) + "\nRealized gain from sale: $" + String.format("%.2f", realizedFromThisSale) + "\nInvestment sold completely and removed from portfolio.\n";
        }
    }

    /**
     *Updates the price of an investment by its position in the list.
     *@param index The index of the investment to update.
     *@param newPrice The new price to set.
     *@return A message indicating the result of the update.
     *@throws Exception if there is an error during the update.
     */
    public String updatePrice(int index, double newPrice) throws Exception {
        if (index < 0 || index >= investments.size()) {
            throw new Exception("Invalid investment index.");
        }
        if (newPrice <= 0) {
            throw new Exception("Price must be positive.");
        }
        Investment investment = investments.get(index);
        investment.setPrice(newPrice);

        //autosave changes
        saveToDatabase("lib/portfolio.db");

        return "Updated investment: " + investment + "\n";
    }

    /**
     *Calculates the total gain (realized + unrealized).
     *@return A string detailing individual and total gains.
     */
    public String getGain() {
        double totalGain = realizedGains;
        String result = "";
        for (Investment investment : investments) {
            double gain = investment.calculateGain();
            result += investment.getSymbol() + ": $" + String.format("%.2f", gain) + "\n";
            totalGain += gain;
        }
        result += "Total gain: $" + String.format("%.2f", totalGain) + "\n";
        return result;
    }

    /**
     *Searches for investments using symbol, keywords, or price range.
     *@param symbol The symbol to search for.
     *@param keywords Keywords in the name to search for.
     *@param lowPrice The lower bound for price.
     *@param highPrice The upper bound for price.
     *@return A string listing all matching investments.
     */
    public String search(String symbol, String keywords, double lowPrice, double highPrice) {
        ArrayList<Investment> matchedInvestments = new ArrayList<>();

        //serach by symbol
        if (!symbol.isEmpty()) {
            Investment investment = symbolMap.get(symbol.toLowerCase());
            if (investment != null) {
                matchedInvestments.add(investment);
            }
        }
        //search by keyword.
        else if (!keywords.isEmpty()) {
            String[] keywordArray = keywords.toLowerCase().split("\\s+");
            HashMap<Investment, Integer> investmentCount = new HashMap<>();
            for (String keyword : keywordArray) {
                ArrayList<Investment> investmentsList = keywordMap.get(keyword);
                if (investmentsList != null) {
                    for (Investment inv : investmentsList) {
                        investmentCount.put(inv, investmentCount.getOrDefault(inv, 0) + 1);
                    }
                }
            }
            for (Investment inv : investmentCount.keySet()) {
                if (investmentCount.get(inv) == keywordArray.length) {
                    matchedInvestments.add(inv);
                }
            }
        }
        //all searc.
        else {
            matchedInvestments.addAll(investments);
        }

        //filter by price range.
        ArrayList<Investment> finalMatchedInvestments = new ArrayList<>();
        for (Investment investment : matchedInvestments) {
            boolean withinLow = (lowPrice == -1) || (investment.getPrice() >= lowPrice);
            boolean withinHigh = (highPrice == -1) || (investment.getPrice() <= highPrice);
            if (withinLow && withinHigh) {
                finalMatchedInvestments.add(investment);
            }
        }

        if (finalMatchedInvestments.isEmpty()) {
            return "No investments match your search criteria.\n";
        }
        StringBuilder sb = new StringBuilder();
        for (Investment inv : finalMatchedInvestments) {
            sb.append(inv).append("\n");
        }
        return sb.toString();
    }

    /**
     *clear all investments from the portfolio.
     */
    public void clearAllInvestments() {
        investments.clear();
        symbolMap.clear();
        keywordMap.clear();
        realizedGains = 0.0;
    }

    /**
     *get back a copy of all investments.
     * @return A list of all investments.
     */
    public ArrayList<Investment> getInvestments() {
        return new ArrayList<>(investments);
    }

    /**
     *sets the starting balance.
     *@param balance The new starting balance.
     */
    public void setStartingBalance(double balance) {
        this.startingBalance = balance;
    }

    /**
     *retrieves the starting balance.
     *@return The starting balance.
     */
    public double getStartingBalance() {
        return startingBalance;
    }

    //OLD CODE -  COMMENTED CODE RELATED TO FILE HANDLING SWITCHED TO DATABASE. 

    /**
     *Loads portfolio data liek balance and investments from a text file.
     *  If the file doesnt exist it creates a new one.
     *@param filename the file name to load data from
     *@throws Exception if there is a problem reading the file
     */

     /* 
    public void loadFromFile(String filename) throws Exception {
        FileInputStream fileInput = null;
        Scanner fileScanner = null;

        try {
            File file = new File(filename);
            if (!file.exists()) {
                //if file doesnt exist we must create a new one with default settings
                saveToFile(filename);
                System.out.println("File not found: " + filename + ". Created a new file.");
                return;
            }

            fileInput = new FileInputStream(file);
            fileScanner = new Scanner(fileInput);

            boolean balanceLoaded = false;

            //Parsing machine
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue; //skips empty lines
                }
                //ioff we detect the CURRENT_BALANCE line, parse it/
                if (line.startsWith("CURRENT_BALANCE")) {
                    String balanceValue = extractValue(line);
                    this.startingBalance = Double.parseDouble(balanceValue);
                    //sets realizedGains to 0 or do not override if you prefer persisting gains
                    balanceLoaded = true;
                    continue;
                }

                //If not the balance line, it should be an investment record. Wwewil expect multiple lines for each investment.
                if (line.startsWith("type")) {
                    //read 6 lines total: type, symbol, name, quantity, price, bookValue
                    String typeLine = line;
                    if (!fileScanner.hasNextLine()) break;
                    String symbolLine = fileScanner.nextLine().trim();
                    if (!fileScanner.hasNextLine()) break;
                    String nameLine = fileScanner.nextLine().trim();
                    if (!fileScanner.hasNextLine()) break;
                    String quantityLine = fileScanner.nextLine().trim();
                    if (!fileScanner.hasNextLine()) break;
                    String priceLine = fileScanner.nextLine().trim();
                    if (!fileScanner.hasNextLine()) break;
                    String bookValueLine = fileScanner.nextLine().trim();

                    String assetType = extractValue(typeLine);
                    String symbol = extractValue(symbolLine);
                    String name = extractValue(nameLine);
                    int quantity = Integer.parseInt(extractValue(quantityLine));
                    double price = Double.parseDouble(extractValue(priceLine));
                    double bookValue = Double.parseDouble(extractValue(bookValueLine));

                    Investment investment;
                    if (assetType.equalsIgnoreCase("stock")) {
                        investment = new Stock(symbol, name, quantity, price);
                    } 
                    else if (assetType.equalsIgnoreCase("mutualfund")) {
                        investment = new MutualFund(symbol, name, quantity, price);
                    } 
                    else {
                        continue; //skip invalid
                    }

                    investment.setBookValue(bookValue);
                    investments.add(investment);
                    symbolMap.put(symbol.toLowerCase(), investment);
                    addToKeywordMap(name, investment);
                }
            }

            if (!balanceLoaded) {
                //if there's no balance line in file, default to 0
                this.startingBalance = 0.0;
            }

            System.out.println("Portfolio info loaded from " + filename);

        } 
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename + ". Creating a new file.");
            saveToFile(filename);
        } 
        catch (Exception e) {
            throw new Exception("Error loading portfolio: " + e.getMessage());
        } 
        finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
            if (fileInput != null) {
                fileInput.close();
            }
        }
    }
    */

    /**
     *Saves portfolio data (balance and investments) to a file.
     *@param filename the file name to save data to
     *@throws Exception if there is a problem saving the file
     */
    /* 
    public void saveToFile(String filename) throws Exception {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileOutputStream(new File(filename)));
            //1) write the current balance
            writer.println("CURRENT_BALANCE = \"" + this.startingBalance + "\"");
            writer.println();

            //2) write each investment.
            for (Investment investment : investments) {
                if (investment instanceof Stock) {
                    writer.println("type = \"stock\"");
                } 
                else if (investment instanceof MutualFund) {
                    writer.println("type = \"mutualfund\"");
                }

                writer.println("symbol = \"" + investment.getSymbol() + "\"");
                writer.println("name = \"" + investment.getName() + "\"");
                writer.println("quantity = \"" + investment.getQuantity() + "\"");
                writer.println("price = \"" + investment.getPrice() + "\"");
                writer.println("bookValue = \"" + investment.getBookValue() + "\"");
                writer.println();
            }

            System.out.println("Portfolio saved to " + filename);
        } 
        catch (FileNotFoundException e) {
            throw new Exception("Error cannot save file to " + filename);
        } 
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
        */
}
