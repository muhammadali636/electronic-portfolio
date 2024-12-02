package Simutrade;

/**
 * Represents a mutual fund investment, a type of investment with no commission on the book value calculation.
 */
public class MutualFund extends Investment {

    /**
     *CONSTRUCTOR MutualFund object 
     * @param symbol  symbol
     * @param name   name
     * @param quantity shares
     * @param price    price per share
     */
    public MutualFund(String symbol, String name, int quantity, double price) {
        super(symbol, name, quantity, price);
    }
    /**
     *calc the book value of the mutual fund without commission. OVERRIDES
     * @param quantity number of share
     * @param price   price per share
     * @return the calc book val
     */
    protected double calculateBookValue(int quantity, double price) {
        return quantity * price; //NO COMMISSION FOR MUTUAL FUNDS
    }

    /**
     *calc the gain for the mutual fund. OVERRIDES
     * @return the calculated gain based on current price and book value
     */
    public double calculateGain() {
        double total;
        total = quantity * price - bookValue;
        return total;
    }

    /**
     * Returns a string representation of the mutual fund, including its details. OVERRIDE
     * @return a string describing the mutual fund
     */
    public String toString() {
        return "MutualFund: " + super.toString();
    }
}