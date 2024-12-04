WELCOME TO EPORTFOLIO!

(1)General Problem
I want to make it easier to manage investments, so I made this program. This program simulates an investment portfolio management system that handles simple buy, sell, update, and search functionalities for stocks and mutual funds.

Software Functionalities:
Buy Investments: Users can purchase stocks or mutual funds by specifying the asset type, symbol, name, quantity, and price.
Sell Investments: Users can sell a certain quantity of their investments at a specified price.
Update Investment Prices: Users can update the prices of all investments in their portfolio.
Calculate Total Gain: The software calculates the total gain or loss of the portfolio based on its current price.
Search Investments: Users can search for investments based on symbol, keywords in the name, and price ranges, or all of them combined. Uses hashmaps for fastness.
Save to file and load to files.
This software make portfolio management better, making a cumbersome and tedious process manageable through a few simple operations.

(2)Assumptions and Limitations
Assumptions:
Unique Symbols: Every stock and mutual fund is uniquely identified by its symbol.
Commission and Fees: Stocks have a commission of $9.99 for both buying and selling, while mutual funds don’t have a commission but have a $45 redemption fee when sold.
Input Validity: The software assumes that the user inputs valid data types. If the user enters invalid data types, Java may produce a runtime error.
Case-Insensitive Symbols: The software assumes symbols are case-insensitive.
No Database Used: The program relies on  text files for data storing and retreiving, which limits scalabnility and other capabilities..
No Real-Time Updates: Since there is no database or API used, users must manually save and reload data at the start and end of each session, and prices must be entered manually which is tedious.

Limitations:
Hardcoded Fees: The program hardcodes the $9.99 commission for stocks and the $45 fee for mutual funds, which may not match different brokerage fees, potentially resulting in inaccurate gain calculations.
Manual Price Updates: Users must manually update stock or mutual fund prices.
Limited Investment Types: The program only handles stocks and mutual funds. Other types of investments (like bonds, ETFs, etc.) are not supported.

(3)User Guide
Make the Program:
Environment: YOU NEED JDK version 8 or higher is installed. AND ALSO java 17.

File Contents:
Portfolio.java: Contains the Portfolio class with methods to manage investments.
Main.java: Contains the main method for running the application.
Stock.java: Defines the Stock class representing stock investments.
MutualFund.java: Defines the MutualFund class representing mutual fund investments.
Investment.java: A superclass for all types of investments, including stocks and mutual funds, to simplify code by reducing redundancy.
hi.txt showing you whats the program does as an exmaple.


Download eportfolio tar file. A folder should be extracted.

Compiling the Program:
Open a terminal.
cd into the location where the ePortfolio folder is stored (for example downloads, or desktop).

Compile: javac ePortfolio/*.java.

Running the Program: java ePortfolio.GUI

A GUI will load and you will be greated by a welcome page. 
Hover over to commands and you will see the menu. It should show buy investment, sell investment, update investments, get total gain, search investments, quit. 



(4)Test Plan
A. Welcome Page
    Start the program.
    Make sure the welcome message and instructions are visible.
    Check that the menu shows the following options:
    Buy Investment
    Sell Investment
    Update Investments
    Get Total Gain
    Search Investments
    Quit
    Click on the Quit option to make sure the program closes.

B.  Buy Investment
    Select the Buy Investment option from the menu.
    Check that the following fields appear:
    Type (with a dropdown for "Stock" and "Mutual Fund").
    Symbol (text field).
    Name (text field).
    Quantity (text field).
    Price (text field).
    Make sure the default type is "Stock."
    Test the Reset button to ensure all fields are cleared.
    Enter valid and invalid values in the fields and press Buy:
    Check that valid inputs add or update the investment.
    Check that invalid inputs are handled.
    Check if you can add to either stock/mutual fund whatevers opposite after adding a symbol as either stock/mutual fund. It should not allow u. For example, if u add mutual fund with name aapl, u cant add stock with same name aapl.

C. Sell Investment
    Choose the Sell Investment option from the menu.
    Verify fields for:
    Symbol (text field).
    Quantity (text field).
    Test the Reset button to clear the fields.
    Enter valid and invalid values and press sell button:
    Valid inputs should reduce the investment quantity.
    Invalid inputs should show an error message in the text area.
    Try entering a higher quantity then what is held in the investment, an error should appear.

D. Update Investments
    Choose the Update Investments option from the menu.
    Check the interface shows:
    Noneditable fields for Symbol and Name 
    An editable field for Price.
    Prev, Next, and Save buttons. (this should be disabled if no investments, next/prev only if only one investment)
    Navigate through investments using Prev and Next:
    Edit the price and press Save:
    Valid prices should update the investment and show details in the text area.
    Invalid prices or inputs should show an error message.

E. Get Total Gain
    Choose the Get Total Gain option from the menu.
    Check that the total gain shows up in a noneditable field.
    Check that detailed gains for each investment are shown in a scrollable text area.
    If price not updated for bought price, then see if the redemmption fee (-45, mutual fund) or commission fee (-9.99 stocks) appear for total gain.

(5)Possible Improvements
*Replace in-memory or file-based storage with a relational database, which can help with info retrieval, and storage, and can help with search functions and other functions. 
*User Interface Enhancements: Develop a GUI using swing or javascript or web-based interface to improve accessibility and user experience. Right now its just a console.
*Dynamic Fees and Commissions:  Because every brokerage has different rates we should not hardcode fees.
*Real Data: use an api to fetch live stock/mutual fund prices, saving users from manually entering prices which is a hassle.
*Expand Investment Types: maybe also  include other investments like bonds, ETFs, and cryptocurrencies. THere are many kind of investments

