package Simutrade;

/**
 * Represents a mutual fund investment, a type of investment without a commission fee for book value calculations.
 */
//rep a mutual fund investment, a type of investment with no commission on the book value calculation
public class MutualFund extends Investment {
    private static final double REDEMPTION_FEE = 45.00;

    /**
     * Constructor to create a MutualFund object.
     *
     * @param symbol the mutual fund's symbol
     * @param name the mutual fund's name
     * @param quantity the number of units
     * @param price the price per unit
     * @throws Exception if any parameter is invalid
     */
    //CONSTRUCTOR MutualFund object 
    public MutualFund(String symbol, String name, int quantity, double price) throws Exception {
        super(symbol, name, quantity, price);
        this.bookValue = calculateBookValue(quantity, price);
    }

    /**
     * Copy constructor to create a duplicate MutualFund object.
     *
     * @param other the mutual fund to copy
     */
    //Copy constructor to create a duplicate MutualFund object
    public MutualFund(MutualFund other) {
        super(other);
    }

    /**
     * Calculates the book value of the mutual fund without commission.
     *
     * @param quantity the number of units
     * @param price the price per unit
     * @return the book value (quantity * price)
     */
    //calc the book value of the mutual fund without commission. OVERRIDES
    //NO COMMISSION FOR MUTUAL FUNDS
    protected double calculateBookValue(int quantity, double price) {
        return quantity * price;
    }

    /**
     * Calculates the gain for the mutual fund.
     *
     * @return the gain calculated as (quantity * price) - redemption fee - bookValue
     */
    //calc the gain for the mutual fund. OVERRIDES
    public double calculateGain() {
        return (quantity * price) - REDEMPTION_FEE - bookValue; //redemption fee == 45!! in future remove hardcoded.
    }
}
