package Simutrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;  //for files.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner; //still need it for files.
import java.util.StringTokenizer; //strtok

/**
 *portfolio of investments including stocks and mutual funds.
 * buying, selling, updating prices, calculating gains, and searching investments.
 */
public class Portfolio {
    //array list to store all investments
    private ArrayList<Investment> investments = new ArrayList<>();

    //Map. investment symbols w/ objects
    private HashMap<String, Investment> symbolMap = new HashMap<>();

    //map keywords with inv list with  those keywords in na,es
    private HashMap<String, ArrayList<Investment>> keywordMap = new HashMap<>();

    /**
     * Loads portfolio data from a file. If the file doesn't exist, it creates a new one.
     * 
     * @param filename the file name to load data from
     * @throws Exception if there is a problem reading the file
     */
    //load portfolio
    public void loadFromFile(String filename) throws Exception {
        FileInputStream fileInput = null;
        Scanner fileScanner = null;

        try {
            File file = new File("Simutrade/" + filename);

            //If file doesn't exist CREATE a new one
            if (!file.exists()) {
                saveToFile(filename);
                System.out.println("File not found in Simutrade/" + filename + ". Created a new file.");
                return;
            }

            fileInput = new FileInputStream(file);
            fileScanner = new Scanner(fileInput);
            //LOADING 
            //pasrse file contents line by line
            while (fileScanner.hasNextLine()) {//iterator
                String typeLine = fileScanner.nextLine().trim();
                if (typeLine.isEmpty()) {
                    continue; //skip empty lines
                }
                //extract form lines
                String symbolLine = fileScanner.nextLine().trim();
                String nameLine = fileScanner.nextLine().trim();
                String quantityLine = fileScanner.nextLine().trim();
                String priceLine = fileScanner.nextLine().trim();
                String bookValueLine = fileScanner.nextLine().trim();

                //parse investment details
                String assetType = extractValue(typeLine);
                String symbol = extractValue(symbolLine);
                String name = extractValue(nameLine);
                int quantity = Integer.parseInt(extractValue(quantityLine));
                double price = Double.parseDouble(extractValue(priceLine));
                double bookValue = Double.parseDouble(extractValue(bookValueLine));

                //inv obj based on type
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

                //book value set and update portfolio 
                investment.setBookValue(bookValue);
                investments.add(investment);
                symbolMap.put(symbol.toLowerCase(), investment);
                addToKeywordMap(name, investment);
            }
            System.out.println("Portfolio info loaded from Simutrade/" + filename);

        } 
        catch (FileNotFoundException e) {
            System.out.println("File not found in Simutrade/" + filename + ". Creating a new file.");
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
    //helperf
    /**
     * Gets the value after the equals sign in a string.
     * @param line the input line
     * @return the value part of the line
     */
    private String extractValue(String line) {
        return line.split("=", 2)[1].trim().replace("\"", "");
    }

    //ssave portfolio data to txt file
    /**
     * Saves portfolio data to a file.
     * @param filename the file name to save data to
     * @throws Exception if there is a problem saving the file
     */
    public void saveToFile(String filename) throws Exception {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileOutputStream(new File("Simutrade/" + filename)));
            //write each inv to file.s
            for (Investment investment : investments) {
                if (investment instanceof Stock) {
                    writer.println("type = \"stock\"");
                } else if (investment instanceof MutualFund) {
                    writer.println("type = \"mutualfund\"");
                }
                writer.println("symbol = \"" + investment.getSymbol() + "\"");
                writer.println("name = \"" + investment.getName() + "\"");
                writer.println("quantity = \"" + investment.getQuantity() + "\"");
                writer.println("price = \"" + investment.getPrice() + "\"");
                writer.println("bookValue = \"" + investment.getBookValue() + "\"");
                writer.println();
            }

            System.out.println("Portfolio saved to Simutrade/" + filename);
        } 
        catch (FileNotFoundException e) { //filenotfoundexception
            throw new Exception("Error cannot save file to Simutrade package/" + filename);
        } 
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    //addddddd investment to  keyword map 
      /**
     * Adds an investment's keywords to the keyword map.
     * @param name the name of the investment
     * @param investment the investment to add
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

    //investment removal from  keyword map
     //buy an investment or update an existing one
     /**
     * Removes an investment's keywords from the keyword map.
     * @param name the name of the investment
     * @param investment the investment to remove
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
     * Buys a new investment or adds to an existing one.
     * 
     * @param type the type of investment ("Stock" or "Mutual Fund")
     * @param symbol the unique symbol of the investment
     * @param name the name of the investment
     * @param quantity the amount to buy
     * @param price the price per unit
     * @return a message about the result of the buy action
     * @throws Exception if inputs are invalid or there are conflicts
     */
    public String buy(String type, String symbol, String name, int quantity, double price) throws Exception {
        //validate inputs
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

        // Check if the investment already exists
        Investment existingInvestment = symbolMap.get(symbol.toLowerCase());

        if (existingInvestment != null) {
            //validate type and name
            if ((type.equalsIgnoreCase("Stock") && !(existingInvestment instanceof Stock)) ||(type.equalsIgnoreCase("Mutual Fund") && !(existingInvestment instanceof MutualFund))) {
                throw new Exception("Error: Symbol " + symbol + " is already used for a different investment type.");
            }
            if (!existingInvestment.getName().equalsIgnoreCase(name)) {
                throw new Exception("Error: Investment with symbol " + symbol + " already exists with a different name.");
            }

            // Update existing investment
            existingInvestment.setQuantity(existingInvestment.getQuantity() + quantity);
            existingInvestment.setPrice(price);
            existingInvestment.setBookValue(existingInvestment.getBookValue() + 
                existingInvestment.calculateBookValue(quantity, price));

            return "Updated investment: " + existingInvestment + "\n";
        }

        //create new investment.f
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

        //add the neew investment to the portfolio
        investments.add(newInvestment);
        symbolMap.put(symbol.toLowerCase(), newInvestment);
        addToKeywordMap(name, newInvestment);

        return "Added new investment: " + newInvestment + "\n";
    }

    //sell an investment or lower its quant
    /**
     * Sells some or all of an investment.
     * 
     * @param symbol the symbol of the investment to sell
     * @param sellQuantity the amount to sell
     * @param sellPrice the price per unit
     * @return a message about the result of the sell action
     * @throws Exception if inputs are invalid or the sale cannot be completed
     */
    public String sell(String symbol, int sellQuantity, double sellPrice) throws Exception {
        // Validate input parameters
        if (symbol == null || symbol.isEmpty()) {
            throw new Exception("Symbol cannot be null or empty.");
        }
        if (sellQuantity <= 0) {
            throw new Exception("Sell quantity must be positive.");
        }
        if (sellPrice <= 0) {
            throw new Exception("Sell price must be positive.");
        }

        //find inv by symbol
        Investment investment = symbolMap.get(symbol.toLowerCase());

        if (investment == null) {
            throw new Exception("Investment with symbol " + symbol + " not found.");
        }

        //make sure right quantity for selling.
        if (sellQuantity > investment.getQuantity()) {
            throw new Exception("Error: Selling quantity exceeds available quantity.");
        }

        // calc payment
        double payment = sellPrice * sellQuantity; //calc
        if (investment instanceof Stock) {
            payment -= 9.99; //- the9.99 fee COMMISION
        } else if (investment instanceof MutualFund) {
            payment -= 45.00; //mutual fund REDEMPTION FEE
        }
        if (payment < 0) {
            throw new Exception("Error: Fees exceed payment. Transaction aborted.");
        }

        //update or remov inv
        int newQuantity = investment.getQuantity() - sellQuantity;
        if (newQuantity > 0) {
            double newBookValue = investment.getBookValue() * ((double) newQuantity / investment.getQuantity());
            investment.setQuantity(newQuantity);
            investment.setBookValue(newBookValue);
            return "Payment received: $" + String.format("%.2f", payment) + "\nUpdated investment: " + investment + "\n";
        }
        //for inv removal
        investments.remove(investment);
        symbolMap.remove(symbol.toLowerCase());
        removeFromKeywordMap(investment.getName(), investment);

        return "Payment received: $" + String.format("%.2f", payment) + "\nInvestment sold completely and removed from portfolio.\n";
    }

    //update price of investment
    /**
     * Updates the price of an investment by its position in the list.
     * @param index the position of the investment
     * @param newPrice the new price to set
     * @return a message about the result of the update
     * @throws Exception if the index is invalid or the price is not valid
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

        return "Updated investment: " + investment + "\n";
    }
    /**
     * Calculates the total gain for the portfolio and gains for each investment.
     * 
     * @return a string showing the gains for each investment and the total gain
     */
    //calc total gain
    public String getGain() {
        double totalGain = 0;
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
     * Searches for investments using symbol, keywords, or price range.
     * 
     * @param symbol the symbol to search for
     * @param keywords the keywords to search in names
     * @param lowPrice the minimum price or -1 for no limit
     * @param highPrice the maximum price or -1 for no limit
     * @return a string showing the search results
     */
    //search investments different search methods.
    public String search(String symbol, String keywords, double lowPrice, double highPrice) {
        ArrayList<Investment> matchedInvestments = new ArrayList<>();
        //srch symbol
        if (!symbol.isEmpty()) {
            Investment investment = symbolMap.get(symbol.toLowerCase());
            if (investment != null) {
                matchedInvestments.add(investment);
            }
        } 
        else if (!keywords.isEmpty()) {
            //keywords srch.
            String[] keywordArray = keywords.toLowerCase().split("\\s+");
            HashMap<Investment, Integer> investmentCount = new HashMap<>();
            for (String keyword : keywordArray) {
                ArrayList<Investment> investmentsList = keywordMap.get(keyword);
                if (investmentsList != null) {
                    for (Investment investment : investmentsList) {
                        investmentCount.put(investment, investmentCount.getOrDefault(investment, 0) + 1);
                    }
                }
            }
            for (Investment investment : investmentCount.keySet()) {
                if (investmentCount.get(investment) == keywordArray.length) {
                    matchedInvestments.add(investment);
                }
            }
        } 
        else {
            //return all investments if none filters.
            matchedInvestments.addAll(investments);
        }

        //filter by pricde range.
        ArrayList<Investment> finalMatchedInvestments = new ArrayList<>();
        for (Investment investment : matchedInvestments) {
            boolean withinLow = (lowPrice == -1) || (investment.getPrice() >= lowPrice);
            boolean withinHigh = (highPrice == -1) || (investment.getPrice() <= highPrice);
            if (withinLow && withinHigh) {
                finalMatchedInvestments.add(investment);
            }
        }
        //prep search results
        if (finalMatchedInvestments.isEmpty()) {
            return "No investments match your search criteria.\n";
        }
        String results = "";
        for (Investment inv : finalMatchedInvestments) {
            results += inv + "\n";
        }
        return results;
    }
    //get all investments. getterrrr
    /**
     * Gets all investments in the portfolio.
     * @return a list of all investments
     */
    public ArrayList<Investment> getInvestments() {
        return new ArrayList<>(investments);
    }
}
