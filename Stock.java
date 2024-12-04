package Simutrade;

/**
 * Class for representing stocks, extending the Investment class.
 */
//Class for representing stocks, extending the Investment class
public class Stock extends Investment {
    private static final double COMMISSION = 9.99; //commission for stocks is 9.99

    /**
     * Constructor to initialize a stock.
     * @param symbol the stock's symbol
     * @param name the stock's name
     * @param quantity the number of shares
     * @param price the price per share
     * @throws Exception if any parameter is invalid
     */
    //CONSTRUCTORS to initialize a stock.
    public Stock(String symbol, String name, int quantity, double price) throws Exception {
        super(symbol, name, quantity, price);
        this.bookValue = calculateBookValue(quantity, price);
    }

    /**
     * Copy constructor to create a duplicate Stock object.
     * @param other the stock to copy
     */
    //Copy constructor to create a duplicate Stock object
    public Stock(Stock other) {
        super(other);
    }

     /**
     * Calculates the book value of the stock, including a commission fee.
     * @param quantity the number of shares
     * @param price the price per share
     * @return the book value including commission
     */
    //Calculates the book value of the stock, including a commission fee. OVERRIDES
    protected double calculateBookValue(int quantity, double price) {
        return (quantity * price) + COMMISSION; //9.99
    }

    /**
     * Calculates the gain for the stock.
     * @return the gain calculated as (quantity * price) - bookValue
     */
    //calc gain OVERRIDES
    public double calculateGain() {
        return (quantity * price) - bookValue; 
    }
}

