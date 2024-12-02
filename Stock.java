package Simutrade;

/**
 * Class for representing stocks, extending the Investment class.
 */
public class Stock extends Investment {

    /**
     *CONSTRUCTORS to initialize a stock.
     *@param symbol   symbol
     *@param name  name
     *@param quantity quant
     *@param price  price/share
     */
    public Stock(String symbol, String name, int quantity, double price) {
        super(symbol, name, quantity, price);
    }

    /**
     *Calculates the book value of the stock, including a commission fee. OVERRIDES
     *@param quantity The quantity of shares
     *@param price    The price per share
     *@return The calculated book value including commission
     */
    protected double calculateBookValue(int quantity, double price) {
        double total;
        total = quantity *price + 9.99;
        return total; //commission for stocks 9.99
    }

    /**
     *calc gain oVERRIDES
     *@return The calculated gain based on current price and book value
     */
    public double calculateGain() {
        double total;
        total = quantity * price - bookValue;
        return total;
    }
    /**
     *Returns a string representation of the stock, including its symbol, name, quantity, and price. OVERRIDES
     *@return A string describing the stock
     */
    public String toString() {
        return "Stock: " + super.toString();
    }
}