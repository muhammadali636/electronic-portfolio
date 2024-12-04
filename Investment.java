package Simutrade;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//gen investment class
/**
 *General  class representing  investment.
 */
public abstract class Investment {
    protected String symbol;
    protected String name;
    protected int quantity;
    protected double price;
    protected double bookValue;

    //Constructor
    /**
     * Constructor to initialize an Investment object.
     *
     * @param symbol the investment's symbol
     * @param name the investment's name
     * @param quantity the number of units
     * @param price the price per unit
     * @throws Exception if any parameter is invalid
     */
    public Investment(String symbol, String name, int quantity, double price) throws Exception {
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
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = calculateBookValue(quantity, price);
    }

    /**
     * Copy constructor to create a dupe Investment object.
     * @param other the Investment object to copy
     */
    //Copy constructor to create a duplicate Investment object
    public Investment(Investment other) {
        this.symbol = other.symbol;
        this.name = other.name;
        this.quantity = other.quantity;
        this.price = other.price;
        this.bookValue = other.bookValue;
    }

    /**
     * Calculates the book value for an investment. Can be overridden by subclasses.
     *
     * @param quantity the number of units
     * @param price the price per unit
     * @return the book value
     */
    //calc book value for an investment can be overridden
    protected abstract double calculateBookValue(int quantity, double price);

    /**
     * Calculates the gain for an investment. Must be implemented by subclasses.
     *
     * @return the calculated gain
     */
    //calc gain for an investment can be overridden
    public abstract double calculateGain();

    /**
     * Extracts keywords from the name of the investment.
     *
     * @return a list of unique keywords in lowercase
     */
    //takes keywords from the name of the investment via strtok
    public List<String> getKeywords() {
        List<String> keywords = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(name.toLowerCase());
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if (!keywords.contains(word)) {
                keywords.add(word);
            }
        }
        return keywords;
    }

    //getters and Setters
    /**
     * Gets the symbol of the investment.
     *
     * @return the symbol
     */
    public String getSymbol() { 
        return symbol; }
    /**
     * Gets the name of the investment.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the quantity of units held.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the price per unit.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the book value of the investment.
     *
     * @return the book value
     */
    public double getBookValue() {
        return bookValue;
    }

    /**
     * Sets the quantity of the investment.
     *
     * @param quantity the new quantity
     * @throws Exception if the quantity is negative
     */
    public void setQuantity(int quantity) throws Exception {
        if (quantity < 0) {
            throw new Exception("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    /**
     * Sets the price of the investment.
     *
     * @param price the new price
     * @throws Exception if the price is not positive
     */
    public void setPrice(double price) throws Exception {
        if (price <= 0) {
            throw new Exception("Price must be positive.");
        }
        this.price = price;
    }

    /**
     * Sets the book value of the investment.
     *
     * @param bookValue the new book value
     */
    public void setBookValue(double bookValue) {
        this.bookValue = bookValue;
    }

    /**
     * Returns a formatted string representation of the investment.
     *
     * @return a string with the name, symbol, quantity, and price
     */
    public String toString() {
        return name + " (" + symbol + ", " + quantity + " @ " +
               String.format("%.2f", price) + ")";
    }

    /**
     * Compares this investment to another for equality based on the symbol.
     *
     * @param otherObject the object to compare to
     * @return true if the symbols match (case insensitive), false otherwise
     */
    public boolean equals(Object otherObject) {
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        Investment otherInvestment = (Investment) otherObject;
        return this.symbol.equalsIgnoreCase(otherInvestment.symbol);
    }
}