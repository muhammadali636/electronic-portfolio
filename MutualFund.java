package Simutrade;

/**
 * class for mutual fund
 */
public class MutualFund {
    private String symbol, name;
    private int quantity;
    private double price, bookValue;

    /**
     * constructor for MutualFund
     * @param symbol mutual fund symbol
     * @param name mutual fund name
     * @param quantity number of shares
     * @param price current price per share
     */
    public MutualFund(String symbol, String name, int quantity, double price) {
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = quantity * price; //no commission for mutual funds 
    }

    /**
     *getter method for mutual fund symbol
     *@return mutual fund symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * getter method for mutual fund name
     *@return mutual fund name
     */
    public String getName() {
        return name;
    }

    /**
     *getter method for number of shares
     *@return number of shares
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * getter method for price per share
     * @return price per share
     */
    public double getPrice() {
        return price;
    }

    /**
     * getter method for book value
     * @return book value
     */
    public double getBookValue() {
        return bookValue;
    }

    /**
     * setter method for number of shares
     * @param quantity new number of shares
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * setter method for price per share
     * @param price new price per share
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *setter method for book value
     *@param bookValue new book value
     */
    public void setBookValue(double bookValue) {
        this.bookValue = bookValue;
    }

    /**
     *calculate gain for mutual fund
     * @return gain value
     */
    public double calculateMutualFundGain() {
        return quantity * price - bookValue; //gain calculation
    }

    /**
     * compare two MutualFund objects (equals method from lecture)
     * @param otherObject object to compare
     * @return true if mutual funds have same symbol (ignore case), false otherwise
     */
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        } 
        else if (getClass() != otherObject.getClass()) {
            return false;
        } 
        else {
            MutualFund otherMutualFund = (MutualFund) otherObject;
            return this.symbol.equalsIgnoreCase(otherMutualFund.symbol);
        }
    }

    /**
     * toString method for printing mutual fund details
     * @return string representation of mutual fund
     */
    public String toString() {
        return name + " (" + symbol + ", " + quantity + " @ " + String.format("%.2f", price) + ")";
    }
}