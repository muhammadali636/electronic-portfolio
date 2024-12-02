WELCOME TO EPORTFOLIO!

(1)General Problem
I want to make it easier to manage stocks, so I made this program. This program simulates an investment portfolio management system that handles simple buy, sell, update, and search functionalities for stocks and mutual funds.

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

Limitations:
Limited Input Validation: While there are some checks  not all inputs are validated, potentially leading to runtime errors.
No Graphical User Interface (GUI): This is console-based, which may limit user accessibility and human-computer interaction.
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

Compiling the Program:
Open a terminal.
Create a folder named ePortfolio and place all Java files in it.
Compile: javac ePortfolio/*.java.
Running the Program: java ePortfolio.Main <filename>.
if a  file exists, it will load investments like this:
text
Copy code
type = "stock"
symbol = "AAPL"
name = "Apple Inc."
quantity = "500"
price = "142.23"
bookValue = "55049.99"
If the file does not exist, it will be created at the end of the session with the updated investment list.
Using the Program:

Available commands:
buy/b, sell, update/u, getGain/g, search, quit/q
Command Shortcuts:
buy or b: Buy an investment.
update or u: Update the prices of all investments.
getGain or g: Calculate and display the total gain of the portfolio.
search: Search for investments by symbol, price range, keywords in name, or combined fields.

(4)Test Plan
Buying
Test Case 1: Buy a new stock with right inputs.
Test Case 2: Buy an existing stock and ensure the quantity and book value update rightly.
Test Case 3: Attempt to buy with negative quantity or price and verify the program rejects wrong input.
Selling
Test Case 4: Sell a portion of stock and verify correct quantity and book value updates.
Test Case 5: Sell all units of a mutual fund, ensuring removal from the portfolio.
Test Case 6: Attempt to sell more units than owned and check for correct error handling.
Updating Prices
Test Case 7: Update ALL investment prices and confirm updates.
Test Case 8: Input invalid prices and observe program response.
Calculating Total Gain
Test Case 9: After various transactions, calculate the total gain and check the result.
Searching Function
Test Case 10: Search by symbol and confirm correct results.
Test Case 11: Search by name keywords (both case-sensitive and insensitive).
Test Case 12: Search by price range and validate outputs.
Test Case 13: Perform a combined search using symbol, keywords, and price range.
Test Case 14: Use wildcards (empty inputs) and verify results.
Test Case 15: Test program behavior with an empty portfolio.

It will also be worthwhile to test the shorthand operations. Like q for quit.


(5)Possible Improvements
Stronger Input Validation:
Use try-catch blocks around parsing methods to handle invalid inputs.

User Interface Enhancements: Develop a GUI using swing or javascript or web-based interface to improve accessibility and user experience. Right now its just a console.

Dynamic Fees and Commissions:  Because every brokerage has different rates we should not hardcode fees.

Real Data: use an api to fetch live stock/mutual fund prices, saving users from manually entering prices which is a hassle.

Expand Investment Types: maybe also  include other investments like bonds, ETFs, and cryptocurrencies. THere are many kind of investments

Better Error Handling: use better error handling to prevent unexpected program crashes and test further.