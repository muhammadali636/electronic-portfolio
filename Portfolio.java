package Simutrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Portfolio {
    private ArrayList<Investment> investments = new ArrayList<>(); //ARRAYLIST OF ALL INVESTMENT
    private HashMap<String, Investment> symbolMap = new HashMap<>(); //MAPS FOR LOOKUP
    private HashMap<String, ArrayList<Integer>> keywordMap = new HashMap<>(); //KEYWORDMAP

    /**
     * Load investments from a file within the ePortfolio package. If the file does not exist, it creates a new file.
     * @param filename  filename to load from.
     */
    public void loadFromFile(String filename) {
        Scanner fileScanner = null;

        try {
            fileScanner = new Scanner(new FileInputStream(new File("ePortfolio/" + filename)));

            while (fileScanner.hasNextLine()) {
                //hash builda
                String whatsTheTypeLine = fileScanner.nextLine().trim();
                if (whatsTheTypeLine.isEmpty()) {
                    continue;
                }
                String symbolLine = fileScanner.nextLine().trim();
                String nameLine = fileScanner.nextLine().trim();
                String quantityLine = fileScanner.nextLine().trim();
                String priceLine = fileScanner.nextLine().trim();
                String bookValueLine = fileScanner.nextLine().trim();
                
                //get values 
                String assetType = whatsTheTypeLine.split("=", 2)[1].trim().replace("\"", "");
                String symbol = symbolLine.split("=", 2)[1].trim().replace("\"", "");
                String name = nameLine.split("=", 2)[1].trim().replace("\"", "");
                int quantity = Integer.parseInt(quantityLine.split("=", 2)[1].trim().replace("\"", ""));
                double price = Double.parseDouble(priceLine.split("=", 2)[1].trim().replace("\"", ""));
                double bookValue = Double.parseDouble(bookValueLine.split("=", 2)[1].trim().replace("\"", ""));

                Investment investment;
                if (assetType.equalsIgnoreCase("stock")) {
                    investment = new Stock(symbol, name, quantity, price);
                } 
                else if (assetType.equalsIgnoreCase("mutualfund")) {
                    investment = new MutualFund(symbol, name, quantity, price);
                } 
                else {
                    continue;
                }
                
                investment.setBookValue(bookValue); //set book val
                investments.add(investment); //add
                symbolMap.put(symbol, investment); //put on map
                addToKeywordMap(name, investments.size() - 1); 
            }
            System.out.println("Portfolio info loaded from ePortfolio/" + filename);

        } 
        //file not found exception then we create a new file
        catch (FileNotFoundException e) {
            System.out.println("File not found in ePortfolio/" + filename + ". Creating a new file.");
            saveToFile(filename);  //createsa new file if not found..
        }
        if (fileScanner != null) {
            fileScanner.close();
        }
    }

    /**
     * Save the CURRENT portfolio to a file within eportfolio package
     * @param filename The filename to save to.
     */
    public void saveToFile(String filename) {
        PrintWriter writer = null;
        //try to save to file
        try {
            writer = new PrintWriter(new FileOutputStream(new File("ePortfolio/" + filename)));

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
            System.out.println("Portfolio saved to ePortfolio/" + filename);

        }
        //if we cant save the file then exit the system and print an error message something is wrong
        catch (FileNotFoundException e) {
            System.out.println("ERROR!! CANT SAVE FILE - ePortfolio/" + filename);
            System.exit(0);
        }
        if (writer != null) {
            writer.close();
        }
    }

    /**
     * add keywords from the name to the keyword map.
     * @param name The name of the investment to index.
     * @param position The position of the investment in the investments list.
     */
    private void addToKeywordMap(String name, int position) {
        StringTokenizer tokenizer = new StringTokenizer(name.toLowerCase());
        while (tokenizer.hasMoreTokens()) {
            String keyword = tokenizer.nextToken();
            ArrayList<Integer> positions = keywordMap.get(keyword);
            if (positions == null) {
                positions = new ArrayList<>();
                keywordMap.put(keyword, positions);
            }
            if (!positions.contains(position)) {
                positions.add(position);
            }
        }
    }

    /**
     * Removes keywords from keyword map for a name.
     * @param name The name of the investment to remove from the index.
     */
    private void removeFromKeywordMap(String name) {
        int position = investments.indexOf(name);
        if (position == -1) {
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(name.toLowerCase());
        while (tokenizer.hasMoreTokens()) {
            String keyword = tokenizer.nextToken();
            ArrayList<Integer> positions = keywordMap.get(keyword);
            if (positions != null) {
                positions.remove((Integer) position);
                if (positions.isEmpty()) {
                    keywordMap.remove(keyword);
                }
            }
        }
    }

    /**
     * BUY METHOD: buy stocks or mutual funds.
     * @param keyboard Scanner for user input
     */
    public void buy(Scanner keyboard) {
        System.out.println("Enter the asset type (stock/s or mutualfund/m): ");
        String assetTypeInput = keyboard.nextLine().trim().toLowerCase();

        //defensive programming strategy, make sure they add stock/s or mutual fund/m:::
        String assetType;
        switch (assetTypeInput) {
            case "stock":
            case "s":
                assetType = "stock";
                break;
            case "mutualfund":
            case "m":
                assetType = "mutualfund";
                break;
            default:
                System.out.println("Unknown operation entered. Please enter either 'stock' or 'mutualfund'.");
                return;
        }

        System.out.println("Enter symbol: ");
        String symbol = keyboard.nextLine().trim();
        boolean found = false; //checks if stuff found

        if (symbolMap.containsKey(symbol)) {
            Investment investment = symbolMap.get(symbol);
            found = true;

            System.out.print("Enter quantity to buy: ");
            int additionalQuantity = keyboard.nextInt();
            System.out.print("Enter price: ");
            double newPrice = keyboard.nextDouble();

            // Defensive programming: Ensure valid quantity and price
            if (additionalQuantity < 0 || newPrice < 0) {
                System.out.println("Quantity and price must be non-negative.");
                return;
            }

            keyboard.nextLine(); //Eat newline

            //update ALL quantity, price, and book value
            investment.setQuantity(investment.getQuantity() + additionalQuantity);
            investment.setPrice(newPrice);
            investment.setBookValue(investment.getBookValue() + newPrice * additionalQuantity);

            System.out.println("Updated investment: " + investment);

        } 
        else {
            //if the investment not found make a new one
            System.out.print("Enter name: ");
            String name = keyboard.nextLine().trim();
            System.out.print("Enter quantity: ");
            int quantity = keyboard.nextInt();
            System.out.print("Enter price: ");
            double price = keyboard.nextDouble();

            // Defensive programming: Ensure valid quantity and price
            if (quantity < 0 || price < 0) {
                System.out.println("Quantity and price must be non-negative.");
                return;
            }

            keyboard.nextLine(); //EAT newline

            Investment newInvestment;
            //if stock type then make a new obj of type STOCK
            if (assetType.equals("stock")) {
                newInvestment = new Stock(symbol, name, quantity, price);
            } 
            //same thing but for mut funds
            else {
                newInvestment = new MutualFund(symbol, name, quantity, price);
            }

            investments.add(newInvestment);
            symbolMap.put(symbol, newInvestment);
            addToKeywordMap(name, investments.size() - 1);

            System.out.println("Added new investment: " + newInvestment);
        }
    }

    /**
     * SELL METHOD: sell stocks or mutual funds.
     * @param keyboard Scanner for user input
     */
    public void sell(Scanner keyboard) {
        System.out.println("Enter the symbol of the investment to sell: ");
        String symbol = keyboard.nextLine().trim();
        Investment investment = symbolMap.get(symbol);

        if (investment != null) {
            System.out.print("Enter quantity to sell: ");
            int sellQuantity = keyboard.nextInt();
            System.out.print("Enter selling price: ");
            double sellPrice = keyboard.nextDouble();
            keyboard.nextLine(); //EAT newline

            //check if sell quantity is valid
            if (sellQuantity > investment.getQuantity()) {
                System.out.println("Error: Selling quantity exceeds available quantity.");
                return;
            }

            //calc payment and apply fees
            double payment = sellPrice * sellQuantity;
            if (investment instanceof Stock) {
                payment -= 9.99; //stock commission from as 1
            } 
            else if (investment instanceof MutualFund) {
                payment -= 45.00; //redemption fee from as. 1 for  MutualFund
            }

            System.out.println("Payment received: $" + payment);

            int newQuantity = investment.getQuantity() - sellQuantity;
            if (newQuantity > 0) {
                double newBookValue = investment.getBookValue() * ((double) newQuantity / investment.getQuantity());
                investment.setQuantity(newQuantity);
                investment.setBookValue(newBookValue);
                System.out.println("Updated investment: " + investment);
            } 
            else {
                investments.remove(investment);
                symbolMap.remove(symbol);
                removeFromKeywordMap(investment.getName());
                System.out.println("Investment sold completely and removed from portfolio.");
            }
        } else {
            System.out.println("Investment with symbol " + symbol + " not found.");
        }
    }

    /**
     * Updates the prices of all investments based on user input.
     * @param keyboard Scanner for user input
     */
    public void update(Scanner keyboard) {
        for (Investment investment : investments) {
            System.out.print("Enter new price for " + investment.getSymbol() + ": ");
            double newPrice = keyboard.nextDouble();
            investment.setPrice(newPrice);
        }
        keyboard.nextLine(); //Eat newline
        System.out.println("All prices updated!!!");
    }

    /**
     * methiod to calc total gain of the portfolio. calc gain for both stocks and mutual funds.
     */
    public void getGain() {
        double totalGain = 0;
        for (Investment investment : investments) {
            totalGain += investment.calculateGain();
        }
        System.out.printf("Total gain of the portfolio: $%.2f\n", totalGain);
    }

    /**
     * Method for searching investments (BIG ONE). Searches by symbol (case insensitive), keywords in name, and price range.
     * @param keyboard Scanner for user input
     */
    public void search(Scanner keyboard) {
        System.out.print("Enter symbol (case-insensitive, leave empty all): ");
        String symbol = keyboard.nextLine().trim();
        System.out.print("Enter keyword(s) in name (case-insensitive, leave empty for all): ");
        String keywords = keyboard.nextLine().trim();

        ArrayList<Integer> matchedPositions = new ArrayList<>();
        if (!keywords.isEmpty()) {
            String[] keywordArray = keywords.toLowerCase().split("\\s+");

            for (String keyword : keywordArray) {
                ArrayList<Integer> positions = keywordMap.get(keyword);

                if (positions != null) {
                    for (int pos : positions) {
                        if (!matchedPositions.contains(pos)) {
                            matchedPositions.add(pos);
                        }
                    }
                }
            }
        } 
        else {
            for (int i = 0; i < investments.size(); i++) {
                matchedPositions.add(i);
            }
        }

        for (int position : matchedPositions) {
            Investment investment = investments.get(position);
            if (symbol.isEmpty() || investment.getSymbol().equalsIgnoreCase(symbol)) {
                System.out.println(investment);
            }
        }
    }
}
