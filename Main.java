package Simutrade;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    /**
     *main method
     */
    public static void main(String[] args) {
        Portfolio portfolio = new Portfolio(); //create portfolio object
        Scanner keyboard = new Scanner(System.in);
        String operator = "";
        //command loop 
        while (!operator.equalsIgnoreCase("quit") && !operator.equalsIgnoreCase("q")) {
            System.out.println("Enter an operator: buy/b sell update/u getGain/g search or quit/q"); //includes shorthands
            operator = keyboard.nextLine();
            //choose the operation
            switch (operator.toLowerCase()) {
                case "buy":
                case "b":
                    portfolio.buy(keyboard);
                    break;
                case "sell":
                    portfolio.sell(keyboard);
                    break;
                case "update":
                case "u":
                    portfolio.update(keyboard);
                    break;
                case "getgain":
                case "g":
                    portfolio.getGain();
                    break;
                case "search":
                    portfolio.search(keyboard);
                    break;
                case "s":
                    System.out.println("Please do not use shorthand for search or sell Enter either search or sell");
                    break;
                default:
                    System.out.println("Invalid operator try again with a valid operator");
            }
        }
        keyboard.close(); //close scanner
    }
}

/**
 *class for portfolio (this is a big one....)
 */
public class Portfolio {
    private ArrayList<Stock> stockList = new ArrayList<>(); //stock list
    private ArrayList<MutualFund> mutualFundList = new ArrayList<>(); //mutual fund list

    /**
     *Method 1 BUY METHOD: buy stocks or mutual funds
     *@param keyboard scanner for user input
     */
    public void buy(Scanner keyboard) {
        System.out.println("Enter the asset type (stock or mutualfund): ");
        String assetType = keyboard.nextLine();

        System.out.println("Enter symbol: ");
        String symbol = keyboard.nextLine();
        boolean found = false; //check if found

        //1a: buy stock
        if (assetType.equalsIgnoreCase("stock")) {
            for (Stock stock : stockList) { //check within stocklist
                //if stock found set found flag to true and add to existing quantity.
                if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                    found = true;
                    System.out.println("Stock symbol already in system.");
                    System.out.print("Enter a new quantity: ");
                    int newQuantity = keyboard.nextInt();
                    //defensive programming: make sure new quantity is not less then 0.
                    if (newQuantity < 0) {
                        System.out.println("New quantity cannot be less then 0!");
                        break;
                    }
                    System.out.print("Enter a new price: ");
                    double newPrice = keyboard.nextDouble();
                    //defensive programming: make sure price is not less then 0.
                    if (newPrice < 0) {
                        System.out.println("New price cannot be less then 0!");
                        break;
                    }
                    keyboard.nextLine(); //eat newline
                    //update quantity price and the book value.
                    double newBookValue = newQuantity * newPrice + 9.99;
                    stock.setBookValue(stock.getBookValue() + newBookValue);
                    stock.setQuantity(stock.getQuantity() + newQuantity);
                    stock.setPrice(newPrice);
                    System.out.println("Buy " + stock.getSymbol() + " " + stock.getQuantity() + " shares @ " + stock.getPrice());
                    break;
                }
            }
            //if the found flag is false meaning stock not found enter new stock details.
            if (!found) {
                System.out.println("Entered stock symbol not found.");
                System.out.print("Enter name: ");
                String name = keyboard.nextLine().trim();
                System.out.print("Enter quantity: ");
                int newQuantity = keyboard.nextInt();
                //defensive programming: make sure new quantity is not less then 0.
                if (newQuantity < 0) {
                    System.out.println("New quantity cannot be less then 0!");
                    return;
                }
                System.out.print("Enter price: ");
                double price = keyboard.nextDouble();
                //defensive programming: make sure price is not less then 0.
                if (price < 0) {
                    System.out.println("New price cannot be less then 0!");
                    return;
                }
                keyboard.nextLine(); //eat newline
                //add new stock object
                Stock newStock = new Stock(symbol, name, newQuantity, price); //construct object.
                stockList.add(newStock); //add to stock arraylist.
                System.out.println("Buy " + newStock.getSymbol() + " " + newStock.getQuantity() + " shares @ " + newStock.getPrice());
            }
        }
        //1b: buy mutual fund
        else if (assetType.equalsIgnoreCase("mutualfund")) {
            for (MutualFund mutualFund : mutualFundList) { //check within mutual fund list
                //if mutual fund found to be in list already update values.
                if (mutualFund.getSymbol().equalsIgnoreCase(symbol)) {
                    found = true;
                    System.out.println("Mutual fund symbol already in system.");
                    System.out.print("Enter a new quantity: ");
                    int newQuantity = keyboard.nextInt();
                    System.out.print("Enter a new price: ");
                    double newPrice = keyboard.nextDouble();
                    keyboard.nextLine(); //eat newline
                    //update quantity price book value
                    double newBookValue = newQuantity * newPrice;
                    mutualFund.setBookValue(mutualFund.getBookValue() + newBookValue);
                    mutualFund.setQuantity(mutualFund.getQuantity() + newQuantity);
                    mutualFund.setPrice(newPrice);
                    System.out.println("Buy " + mutualFund.getSymbol() + " " + mutualFund.getQuantity() + " shares @ " + mutualFund.getPrice());
                    break;
                }
            }
            //if the found flag is false meaning stock not found enter new stock details.
            if (!found) {
                System.out.println("Entered Mutual fund symbol not found.");
                System.out.print("Enter name: ");
                String name = keyboard.nextLine().trim();
                System.out.print("Enter quantity: ");
                int quantity = keyboard.nextInt();
                System.out.print("Enter price: ");
                double price = keyboard.nextDouble();
                keyboard.nextLine(); //eat newline

                //add new mutual fund
                MutualFund newMutualFund = new MutualFund(symbol, name, quantity, price); //construct new object.
                mutualFundList.add(newMutualFund);
                System.out.println("Buy " + newMutualFund.getSymbol() + " " + newMutualFund.getQuantity() + " shares @ " + newMutualFund.getPrice());
            }
        }
        //if the user did not enter either stock or mutual fund then send a message telling them they messed up.
        else {
            System.out.println("Unknown asset type enter either 'stock' or 'mutualfund'.");
        }
    }

    /**
     *Method 2 sell stocks or mutual funds method
     *@param keyboard scanner for user input
     */
    public void sell(Scanner keyboard) {
        System.out.println("Enter investment type (stock or mutualfund): ");
        String assetType = keyboard.nextLine().trim();
        System.out.println("Enter symbol: ");
        String symbol = keyboard.nextLine().trim();
        boolean found = false;
        double payment;
        double remainingBookValue;
        //sell stock
        if (assetType.equalsIgnoreCase("stock")) {
            for (int i = 0; i < stockList.size(); i++) {
                Stock stock = stockList.get(i);
                if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                    found = true;
                    System.out.println("Stock symbol found.");
                    System.out.print("Enter quantity to sell: ");
                    int sellQuantity = keyboard.nextInt();
                    System.out.print("Enter actual selling price: ");
                    double sellPrice = keyboard.nextDouble();
                    keyboard.nextLine(); //consume newline

                    //check if sell quantity is valid
                    if (sellQuantity > stock.getQuantity()) {
                        System.out.println("Quantity to sell exceeds available quantity.");
                        break;
                    }
                    //calculate payment: price * sellQuantity - $9.99 commission
                    payment = (sellPrice * sellQuantity) - 9.99;
                    System.out.println("Payment received: $" + payment);
                    //update quantity and book value
                    int newQuantity = stock.getQuantity() - sellQuantity;
                    if (newQuantity > 0) {
                        remainingBookValue = stock.getBookValue() * ((double) newQuantity / stock.getQuantity());
                        stock.setBookValue(remainingBookValue);
                        stock.setQuantity(newQuantity);
                        System.out.println("Sell " + stock.getSymbol() + " " + sellQuantity + " shares @ " + sellPrice);
                    }
                    else {
                        stockList.remove(i); //remove stock if all shares are sold
                        System.out.println("All shares sold Stock removed.");
                    }
                    break;
                }
            }
            if (!found) {
                System.out.println("Stock symbol not found.");
            }
        }
        //sell mutual fund
        else if (assetType.equalsIgnoreCase("mutualfund")) {
            for (int i = 0; i < mutualFundList.size(); i++) {
                MutualFund mutualFund = mutualFundList.get(i);
                if (mutualFund.getSymbol().equalsIgnoreCase(symbol)) {
                    found = true;
                    System.out.println("Mutual fund symbol found.");
                    System.out.print("Enter quantity to sell: ");
                    int sellQuantity = keyboard.nextInt();
                    System.out.print("Enter actual selling price: ");
                    double sellPrice = keyboard.nextDouble();
                    keyboard.nextLine(); //consume newline
                    //check if sell quantity is valid
                    if (sellQuantity > mutualFund.getQuantity()) {
                        System.out.println("Quantity to sell exceeds available quantity.");
                        break;
                    }
                    //calculate payment: price * sellQuantity - $45 redemption fee
                    payment = (sellPrice * sellQuantity) - 45.00;
                    System.out.println("Payment received: $" + payment);
                    //update quantity and book value
                    int newQuantity = mutualFund.getQuantity() - sellQuantity;
                    if (newQuantity > 0) { //update bookvalue and set newQuantity
                        remainingBookValue = mutualFund.getBookValue() * ((double) newQuantity / mutualFund.getQuantity());
                        mutualFund.setBookValue(remainingBookValue);
                        mutualFund.setQuantity(newQuantity);
                        System.out.println("Sell " + mutualFund.getSymbol() + " " + sellQuantity + " shares @ " + sellPrice);
                    }
                    else { //if newQuantity == 0 REMOVE the stock because its all sold :(
                        mutualFundList.remove(i); //remove mutual fund if all units are sold
                        System.out.println("All units sold Mutual fund removed.");
                    }
                    break;
                }
            }
            //if symbol for mutual fund not found.
            if (!found) {
                System.out.println("Mutual fund symbol not found.");
            }
        }
        else {
            System.out.println("Unknown asset type enter either 'stock' or 'mutualfund'.");
        }
    }

    /**
     *update prices for all investments
     *@param keyboard scanner for user input
     */
    public void update(Scanner keyboard) {
        //update stock prices
        for (Stock stock : stockList) {
            System.out.println("Enter new price for stock " + stock.getSymbol() + ": ");
            double newPrice = keyboard.nextDouble();
            stock.setPrice(newPrice);
        }
        keyboard.nextLine(); //eat newline
        //update mutual fund prices
        for (MutualFund mutualFund : mutualFundList) {
            System.out.println("Enter new price for mutual fund " + mutualFund.getSymbol() + ": ");
            double newPrice = keyboard.nextDouble();
            mutualFund.setPrice(newPrice);
        }
        keyboard.nextLine(); //eat newline
        System.out.println("Updated prices for all investments.");
    }

    /**
     *Method 3 calculate total gain of portfolio
     */
    public void getGain() {
        double stockGain = 0;
        double mutualFundGain = 0;
        double totalGain;
        //calc gain for stocks
        for (Stock stock : stockList) {
            stockGain += stock.calculateStockGain();
        }
        //calc gain for mutual funds
        for (MutualFund mutualFund : mutualFundList) {
            mutualFundGain += mutualFund.calculateMutualFundGain();
        }
        totalGain = stockGain + mutualFundGain;
        System.out.printf("Total gain of the portfolio: $%.2f\n", totalGain);
    }

    /**
     *Method 4 search method for investments (BIG ONE)
     * @param keyboard scanner for user input
     */
    public void search(Scanner keyboard) {
        int operator2 = -1;
        keyboard.nextLine(); //eat newline
        //secondary command loop for search loops until user chooses to exit.
        while (operator2 != 0) {
            System.out.println("1. Symbol search (case insensitive)");
            System.out.println("2. Keyword search in name (case sensitive)");
            System.out.println("3. Keyword search in name (case insensitive)");
            System.out.println("4. Price range search");
            System.out.println("5. Combined search");
            System.out.println("0. Exit search function");
            System.out.print("Choose a search option please: ");
            operator2 = keyboard.nextInt();
            keyboard.nextLine(); //eat newline
            switch (operator2) {
                case 1: //symbol search (case insensitive)
                    System.out.print("Enter symbol (case-insensitive): ");
                    String symbol = keyboard.nextLine().trim();
                    System.out.println("Searching for stocks and mutual funds with the symbol: " + symbol + " ...");
                    System.out.println("SEARCH RESULTS:");
                    searchSymbol(symbol);
                    break;
                case 2: //keyword search in name (case sensitive)
                    System.out.print("Enter keyword(s) for name (case-sensitive): "); //tokenize this in searchByKeywords method
                    String sensitiveKeyword = keyboard.nextLine().trim();
                    System.out.println("Searching for stocks and mutualfunds with keyword(s) (case-sensitive): " + sensitiveKeyword + " ...");
                    System.out.println("SEARCH RESULTS:");
                    searchByKeywords(sensitiveKeyword, true); //set caseSensitive flag to true for case sensitivity
                    break;
                case 3: //keyword search in name (case insensitive)
                    System.out.print("Enter keyword(s) for name (case insensitive): ");
                    String insensitiveKeyword = keyboard.nextLine().trim();
                    System.out.println("Searching for stocks and mutualfunds with keyword(s) (case-insensitive): " + insensitiveKeyword + " ...");
                    System.out.println("SEARCH RESULTS:");
                    searchByKeywords(insensitiveKeyword, false); //flag is false for case-insensitive search
                    break;
                case 4: //price range search
                    System.out.print("Enter price range (like 10.00-100.00 10.00- -100.00): ");
                    String priceRange = keyboard.nextLine().trim();
                    System.out.println("Searching for investments within price range: " + priceRange);
                    System.out.println("SEARCH RESULTS:");
                    priceRangeSearch(priceRange);
                    break;
                case 5: //combined search
                    System.out.print("Enter symbol (case-insensitive or leave empty to skip): ");
                    String combinedSymbol = keyboard.nextLine().trim();

                    System.out.print("Enter keyword(s) for name (case-insensitive or leave empty to skip): ");
                    String combinedKeywords = keyboard.nextLine().trim();

                    System.out.print("Enter price range (like 10.00-100.00 10.00- -100.00 or leave empty to skip): ");
                    String combinedPriceRange = keyboard.nextLine().trim();
                    System.out.println("SEARCH RESULTS:");
                    combinedSearch(combinedSymbol, combinedKeywords, combinedPriceRange);
                    break;
                case 0: //exit
                    System.out.println("Exiting search function.");
                    break;
                default:
                    System.out.println("Invalid option Please choose a valid search type.");
            }
        }
    }

    /**
     * helper function for search method searches by symbol (case insensitive)
     * @param symbol symbol to search for
     */
    private void searchSymbol(String symbol) {
        boolean found = false;
        //if the symbol is nothing (its a wildcard get everything)
        if (symbol.equals("")) {
            System.out.println("STOCKS:");
            for (Stock stock : stockList) {
                System.out.println(stock.toString());
            }
            System.out.println("MUTUAL FUNDS:");
            for (MutualFund mutualFund : mutualFundList) {
                System.out.println(mutualFund.toString());
            }
            return; //exit out of function
        }
        else {
            System.out.println("STOCKS:");
            for (Stock stock : stockList) {
                if (stock.getSymbol().equalsIgnoreCase(symbol)) {
                    System.out.println(stock.toString()); //call toString() to display the stock object
                    found = true;
                }
            }
            //search in mutual fund list
            System.out.println("MUTUAL FUNDS:");
            for (MutualFund mutualFund : mutualFundList) {
                if (mutualFund.getSymbol().equalsIgnoreCase(symbol)) {
                    System.out.println(mutualFund.toString()); //call toString() to display the mutual fund object
                    found = true;
                }
            }
            //if not found (found flag == false)
            if (!found) {
                System.out.println("No matching stocks or mutual funds found for the symbol: " + symbol);
            }
        }
    }

    /**
     * helper functiion to search method to search by keywords in the name
     * @param keywords keywords to search for
     * @param caseSense case sensitivity flag
     */
    private void searchByKeywords(String keywords, boolean caseSense) {
        String[] keywordArray = keywords.split(" "); //split input into keywords using whitespace as delimiter
        boolean found = false;
        //if no keywords entered (its a wild card get everything)
        if (keywords.equals("")) {
            System.out.println("STOCKS:");
            for (Stock stock : stockList) {
                System.out.println(stock.toString());
            }
            System.out.println("MUTUAL FUNDS:");
            for (MutualFund mutualFund : mutualFundList) {
                System.out.println(mutualFund.toString());
            }
            return; //exit out of function
        }
        else {
            //search in stock list
            for (Stock stock : stockList) {
                boolean allKeywordsMatched = true;
                for (String keyword : keywordArray) {
                    if (caseSense && stock.getName().contains(keyword)) {
                        continue; //keyword matches --> continue to the next keyword
                    }
                    if (!caseSense && stock.getName().toLowerCase().contains(keyword.toLowerCase())) {
                        continue; //insensitive case match found --> continue to the next keyword
                    }
                    //If no match found set allKeywordsMatched to false (since none foudn) and break out of thisloop
                    allKeywordsMatched = false;
                    break;
                }
                //if all matched call toString to out stock obj info
                if (allKeywordsMatched) {
                    System.out.println("STOCKS:");
                    System.out.println(stock.toString());
                    found = true;
                }
            }
            //search in mutual fund list.
            for (MutualFund mutualFund : mutualFundList) {
                boolean allKeywordsMatched = true;
                for (String keyword : keywordArray) {
                    if (caseSense && mutualFund.getName().contains(keyword)) {
                        continue; //keyword matches then continue to the next keyword.
                    }
                    if (!caseSense && mutualFund.getName().toLowerCase().contains(keyword.toLowerCase())) {
                        continue; //c ase-insensitive match found continue to check for next keyword
                    }
                    //if no match found set allKeywordsMatched flag to false and break out of this dam loop
                    allKeywordsMatched = false;
                    break;
                }
                //if alll matches print out the mutual fund info by calling toString
                if (allKeywordsMatched) {
                    System.out.println("MUTUAL FUNDS:");
                    System.out.println(mutualFund.toString());
                    found = true;
                }
            }
        }
        //if foundd flag is true..
        if (!found) {
            System.out.println("No matching stock or mutual funds found for the keywords: " + keywords);
        }
    }

    /**
     * helper functiosn for search method to search by price range
     * @param priceRange price range to search within
     */
    private void priceRangeSearch(String priceRange) {
        Double minPrice = null; //default to null.
        Double maxPrice = null; //default ot null
        boolean found = false;

        //wildcard if nothign entered so search everywhere!
        if (priceRange.equals("")) {
            System.out.println("STOCKS:");
            for (Stock stock : stockList) {
                System.out.println(stock.toString());
            }
            System.out.println("MUTUAL FUNDS:");
            for (MutualFund mutualFund : mutualFundList) {
                System.out.println(mutualFund.toString());
            }
            return; //exit out of function
        }

        //parse price ranges..
        if (priceRange.contains("-")) {
            String[] prices = priceRange.split("-");
            if (prices.length == 2) {
                if (!prices[0].isEmpty()) {
                    minPrice = Double.parseDouble(prices[0]);
                }
                if (!prices[1].isEmpty()) {
                    maxPrice = Double.parseDouble(prices[1]);
                }
            }
            else {
                System.out.println("Incorrect usage (invalid price range format). Use 'min-max' '-max' or 'min-' or 'value'");
                return;
            }
        }
        //If no hyphen then exact price matching
        else {
            minPrice = Double.parseDouble(priceRange);
            maxPrice = minPrice;
        }

        //Sfearch in stock list
        for (Stock stock : stockList) {
            double stockPrice = stock.getPrice();
            if ((minPrice == null || stockPrice >= minPrice) && (maxPrice == null || stockPrice <= maxPrice)) {
                System.out.println(stock.toString());
                found = true;
            }
        }

        //search in mutual fund dlist
        for (MutualFund mutualFund : mutualFundList) {
            double mutualFundPrice = mutualFund.getPrice();
            if ((minPrice == null || mutualFundPrice >= minPrice) && (maxPrice == null || mutualFundPrice <= maxPrice)) {
                System.out.println(mutualFund.toString());
                found = true;
            }
        }
        //if none found..
        if (!found) {
            System.out.println("No matching mutual funds or stocks found for the given price range.");
        }
    }

    /**
     *combined search methodologyy
     *@param symbol symbol to search
     *@param keywords keywords to search
     *@param priceRange price range to search
     */
    private void combinedSearch(String symbol, String keywords, String priceRange) {
        boolean found = false;
        //IF ALL FIELDS EMPTY PRINT IT ALL
        if (symbol.isEmpty() && keywords.isEmpty() && priceRange.isEmpty()) {
            System.out.println("STOCKS:");
            for (Stock stock : stockList) {
                System.out.println(stock.toString());
            }
            System.out.println("MUTUAL FUNDS:");
            for (MutualFund mutualFund : mutualFundList) {
                System.out.println(mutualFund.toString());
            }
            return; //Exit prematurely.t
        }

        //otherwise.. begin the combined search,
        Double minPrice = null;
        Double maxPrice = null;
        if (!priceRange.isEmpty()) {
            if (priceRange.contains("-")) {
                String[] prices = priceRange.split("-");
                if (!prices[0].isEmpty()) {
                    minPrice = Double.parseDouble(prices[0]);
                }
                if (prices.length > 1 && !prices[1].isEmpty()) {
                    maxPrice = Double.parseDouble(prices[1]);
                }
            }
            else {
                minPrice = Double.parseDouble(priceRange);
                maxPrice = minPrice;
            }
        }

        //convert the keywords to lweowrcase to make it case insenjtive.
        String[] keywordArray = new String[0];
        if (!keywords.isEmpty()) {
            keywordArray = keywords.toLowerCase().split(" ");
        }

        //search within stocks COMBINED SEARCH
        System.out.println("STOCKS:");
        for (Stock stock : stockList) {
            boolean matchedSymbol = symbol.isEmpty() || stock.getSymbol().equalsIgnoreCase(symbol);
            boolean matchedKeywords = true;
            if (keywordArray.length > 0) {
                String stockName = stock.getName().toLowerCase();
                for (String keyword : keywordArray) {
                    if (!stockName.contains(keyword)) {
                        matchedKeywords = false;
                        break;
                    }
                }
            }
            boolean matchedPrice = true;
            if (minPrice != null && stock.getPrice() < minPrice) {
                matchedPrice = false;
            }
            if (maxPrice != null && stock.getPrice() > maxPrice) {
                matchedPrice = false;
            }
            if (matchedSymbol && matchedKeywords && matchedPrice) {
                System.out.println(stock.toString());
                found = true;
            }
        }

        //SEARCH WITHIIN MUTUAL FUNDS COMBINED SEARCH
        System.out.println("MUTUAL FUNDS:");
        for (MutualFund mutualFund : mutualFundList) {
            boolean matchedSymbol = symbol.isEmpty() || mutualFund.getSymbol().equalsIgnoreCase(symbol);
            boolean matchedKeywords = true;
            if (keywordArray.length > 0) {
                String mutualFundName = mutualFund.getName().toLowerCase();
                for (String keyword : keywordArray) {
                    if (!mutualFundName.contains(keyword)) {
                        matchedKeywords = false;
                        break;
                    }
                }
            }
            boolean matchedPrice = true;
            if (minPrice != null && mutualFund.getPrice() < minPrice) {
                matchedPrice = false;
            }
            if (maxPrice != null && mutualFund.getPrice() > maxPrice) {
                matchedPrice = false;
            }
            if (matchedSymbol && matchedKeywords && matchedPrice) {
                System.out.println(mutualFund.toString());
                found = true;
            }
        }

        //IF NOTHING FOUND. print nothing found message.
        if (!found) {
            System.out.println("No matching stocks or mutual funds found during the combined search.");
        }
    }
}