package Simutrade;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * General investment class.
 */
public class Investment {
    protected String symbol;
    protected String name;
    protected int quantity;
    protected double price;
    protected double bookValue;

    /**
     * Constructor
     * 
     * @param symbol investment symbol
     * @param name   name
     * @param quantity shares
     * @param price  price per share
     */
    public Investment(String symbol, String name, int quantity, double price) {
        this.symbol = symbol;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.bookValue = calculateBookValue(quantity, price);
    }

    /**
     *calc book value for an investment can be overridden.
     */
    protected double calculateBookValue(int quantity, double price) {
        return quantity * price; // Default calculation
    }

    /**
     *calc gain for an investment can be overridden.
     */
    public double calculateGain() {
        return (quantity * price) - bookValue; // Default calculation
    }

    /**
     *takes keywords from the name of the investment via strtok
     * @return a list of keywords taken from the name.
     */
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

    //return formatted string for file saving
    public String toFileFormat() {
        return this.getClass().getSimpleName().toLowerCase() + "," + symbol + "," + name + "," + quantity + "," + price;
    }

    //getters and Setters
    public String getSymbol() { 
        return symbol; 
    }
    public String getName() { 
        return name; 
    }
    public int getQuantity() { 
        return quantity; 
    }
    public double getPrice() { 
        return price; 
    }
    public double getBookValue() { 
        return bookValue; 
    }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
    public void setBookValue(double bookValue) { this.bookValue = bookValue; }

    //override tostring
    public String toString() {
        return name + " (" + symbol + ", " + quantity + " @ " + String.format("%.2f", price) + ")";
    }
    //ovverride equals
    public boolean equals(Object otherObject) {
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }
        Investment otherInvestment = (Investment)otherObject;
        return this.symbol.equalsIgnoreCase(otherInvestment.symbol);
    }
}
