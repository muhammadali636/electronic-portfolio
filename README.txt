
(1) the general problem I am trying to solve; 

I want to make it easier to manage stocks, so I made this program. This program simulates an investment portfolio managing system that does simple buy sell update and search functionalities for stocks and mutual funds.

Software Functionalities:
1. Buy Investments: Users can purchase stocks or mutual funds by specifying the asset type, symbol, name, quantity, and price.
2. Sell Investments: Users can sell a certain quantity of their investments at a specified price.
3. Update Investment Prices: Users can update the prices of all investments in their portfolio.
4. Calculate Total Gain: The software calculates the total gain or loss of the portfolio based on its' price.
5. Search Investments: Users can search for investments based on symbol, keywords in the name, and price ranges or all of them (combined search).

An issue that people who want to manage their portfolio can face is the fact that it can be a lot to manage. This software enhances portfolio management turning a cumbersome and tedious process into just a few simple operations.

(2) what are the assumptions and limitations of your solution;

Assumptions:
1. Unique Symbols. The program assumes that every stock and mutual fund is uniquely identified by its symbol.
2. Commission and fees. The program assumes that stocks have a commission of $9.99 when bough and sold, and mutual funds dont have a commision, but instead a $45 redemption fee when sold.
3. Input Validity: The software assumes that the user inputs valid data types. If the user enters wrong then Java may produce a runtimeError.
4. The software assumes symbols are case insensitive. 

Limitations of the Software:
There is no continuous storage. The program is console based and does not store data between sessions. All data is lose when the program terminates.
Input validity erros: some defensive programming checks are present, but not all inputs are held accountable which can result in a runtime error.
No GUI. The program is console based and does not provide a graphical user interface which would make the program more accessible and enhance human computer interaction.
Hardcoding of commissions and redemption fee for mutual funds. Different institutions may have different commissions. For example, questrade charges $5 per trade while ScotiaBank charges $10. THe program hardcodes $9.99, which may give inaccurate gain calculations to some users.
No live-stock/mutual fund prices. This is problematic as the user has to manually enter the actual price of stock or mutual fund. This is cumbersome and tedious.
The program only deals with 2 kinds of investments: stocks and mutual funds. There are so many other kinds of investments like baskets, ETFs etc.

(3) how can a user build and test your program (also called the user guide); 

Building Program:

Environment:
Make sure JDK version 8+ is installed on the system.

Environment Setup:

File Contents:

Portfolio.java: Contains the Portfolio class with methods to manage investments and the main method as well. 
Stock.java: has the Stock class representing stock investments.
MutualFund.java: has the MutualFund class representing mutual fund investments.

Compiling the Program:
Open terminal 
go to directory where ePortfolio package is located. 
Compile the classes using the following command: javac Simutrade/*.java

Running the Program:
Run the program using the following command: java Simutrade.Main

Using the Program: Follow the prompts.

buy/b sell update/u getGain/g search or quit/q
- Buy: Buy an investment.
- Sell: Sell an investment.
- Update: update the prices of all investments.
- GetGain: calcualte and display  total gain of the portfolio.
- Search: Search for investments based on field entries. There is symbol, price range, keyword and combined searches.
  - 1. 1 for Symbol search, 2 for Keyword search in name (case sensitive), 3 for Keyword search in name (case insensitive) 4 for  Price range search, 5 fo Combined search, 0 to Exit search function"
Quit: Exit the program.

Command Shortcuts:
Buy: buy or b
Update: update or u
GetGain: getgain or g
The program will prompt for details based on the command entered
Follow console instructions to input symbols, names, quantities, prices, etc.


(4) testplan

Buying
Test Case 1: Buy new stock with valid inputs.
Test Case 2: Buy already existing stock and check that the quantity and book value are updated properly
Test Case 3: try to buy with a negative quantity or price and verify program rejects the incorrect negative input.

Selling
Test Case 4: Sell quantity of a stock and verify that the quantity and book value are properly updated.
Test Case 5: Sell all units of a mutual fund and ensure it is removed from the portfolio.
Test Case 6: Attempt to sell more units than owned and check for correct error handling.

Updating Prices
Test Case 7: Update prices for all investments and check that the new prices are reflected.
Test Case 8: input invalid price inputs (like negative numbers, nonnumeric values) and see how the program deals with it.

Calculating Total Gain:
Test Case 9: After some buying, selling, and updating, calculate the total gain and check if its correct with a calculater.

Searching Function:
Test Case 10: search by symbol and check if the correct investments are displayed on console
Test Case 11: search by keywords in the name (case-sensitive and case-insensitive) and check if the right result is on console.
Test Case 12: search by price range with various formats (e.g., 10-50, -100, 200-) and validate outputs.
Test Case 13: do a combined search using symbol, keywords, and price range and check if it is correct
Test Case 14: test wildcards (nothing entered) for different search methods, see if you get right results.
Test Case 15: Test program  when no investments are present in the portfolio.

Testing Steps:
For each test case:
Start the program.
Follow the  prompts 
Record program's outputs and compare them with what the expected results are. Write down any errors. 

Expected Results:
The program should properly handle all valid inputs and different operations.
Calculations for book values, payments, and gains should be accurate.
Search functions should return correct and relevant results based on the fields inputted.


(5) Possible Improvements
Better Input Checks:

I'll make the input validation stronger to avoid errors and crashes.
I can use try-catch blocks around methods like Double.parseDouble and Integer.parseInt to handle bad inputs.
Save Data:

I'll add a way to save and load the portfolio data from a file so that the information isn't lost when I close the program.
User Interface (UI) Improvements:

I can create a graphical interface to make the program easier to use.
For web-based solutions, I'll use HTML, CSS, and JavaScript to design the interface. A backend framework like Spring Boot can help connect the Java program with the web interface.

Remove Redundancy:
I'll simplify the code by making reusable helper methods for common tasks, like updating investment values.

I'll add error handling throughout the program to catch unexpected issues without crashing.

Add More Investment Types: I could expand the program to handle other investment types like bonds, options, index funds, or cryptocurrencies.
I'll avoid hardcoding things like commissions and fees and allow them to be set dynamically.
More Testing:

I'll do more testing to make sure the program behaves correctly under different scenarios.
I'll try different cases to see if everything works as expected and doesn't crash.

Use Real Stock Data: To make the program more realistic, I could use real stock data for testing.
I can find historical stock prices on sites like Yahoo Finance and manually enter them or connect to an API for automatic data fetching.
Add Web-Based GUI with JavaScript:

