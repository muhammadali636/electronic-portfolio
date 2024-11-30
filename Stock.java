package Simutrade;

/**
 * class for stocks
 */
public class Stock {
    //instance variables set to pvt
    private String symbol, name;
    private int quantity;
    private double price, bookValue;

    /**
     *constructor for Stock class to initialize instance of stock
     *@param symbol stock symbol
     *@param name stock name
     *@param quantity num of shares
     *@param price current price per share
     */
    public Stock(String symbol, String name, int quantity, double price) {
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = quantity * price + 9.99; //9.99 is assumed commission
    }

    /**
     *getter metjhod for stock symbol
     *@return stock symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *getter method for stock name
     *@return stock name
     */
    public String getName() {
        return name;
    }

    /**
     *getter method for num of shares
     *@return num of shares
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *getter method for price per share
     *@return price per share
     */
    public double getPrice() {
        return price;
    }

    /**
     *getter for book value
     *@return book value
     */
    public double getBookValue() {
        return bookValue;
    }

    /**
     *setter for num of shares
     *@param quantity new num of shares
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     *setter for price per share
     *@param price new price per share
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *setter for book value
     *@param bookValue new book value
     */
    public void setBookValue(double bookValue) {
        this.bookValue = bookValue;
    }

    /**
     *method to calcualte stock gain
     *@return gain value
     */
    public double calculateStockGain() {
        return (quantity * price) - bookValue; 
    }

    /**
     *equals method to compare two stock objects based on symbols
     *@param otherObject object to compare
     *@return true if stocks have same symbol (ignore case), false otherwise
     */
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        } 
        else if (getClass() != otherObject.getClass()) {
            return false;
        } 
        else {
            Stock otherStock = (Stock) otherObject;
            return this.symbol.equalsIgnoreCase(otherStock.symbol);
        }
    }

    /**
     *toString method for printing stock details
     *@return string representation of stock
     */
    public String toString() {
        return name + " (" + symbol + ", " + quantity + " @ " + String.format("%.2f", price) + ")";
    }
}