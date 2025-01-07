# WELCOME TO ELECTRONIC PORTFOLIO

**(1) General Problem and Software Functionalities**  
    This program simulates an investment portfolio management system that handles simple buy, sell, update, and search functionalities for stocks and mutual funds. Practice trading strategies risk-free. 

**Software Functionalities:**  
    Buy Investments: Users can purchase stocks or mutual funds by specifying the asset type, symbol, name, quantity, and price.  
    Sell Investments: Users can sell a certain quantity of their investments at a specified price.  
    Update Investment Prices: Users can update the prices of all investments in their portfolio.  
    Calculate Total Gain: The software calculates the total gain or loss of the portfolio based on its current price.  
    Search Investments: Users can search for investments based on symbol, keywords in the name, and price ranges, or all of them combined. Uses hashmaps for 
    fastness.  
    Save to database and load from database.  
    This software makes portfolio management better, making a cumbersome and tedious process manageable through a few simple operations.
    
**(2) User Guide**  
    Make the Program:  
    Environment: YOU NEED JDK version 8 or higher is installed. AND ALSO java 17.  
    The jar file should already exist. If not:

**file structure**
~~~plaintext
ePortfolio/
├── lib/
│   └── sqlite-jdbc-3.47.1.0.jar
├── src/
│   └── ePortfolio/
│       ├── GUI.java
│       ├── Portfolio.java
│       ├── Investment.java
│       ├── Stock.java
│       └── MutualFund.java
├── build/
│   └── ePortfolio/
│       ├── GUI.class
│       ├── Portfolio.class
│       ├── Investment.class
│       ├── Stock.class
│       └── MutualFund.class
├── resources/
│   └── images/
│       ├── logo.png
│       └── icon.png
├── docs/
│   ├── README.md
│   └── screenshots/  
├── manifest.txt
└── ePortfolio.jar
~~~

**A. Compilation and Execution within terminal:**  
    Compilation: javac -cp lib/sqlite-jdbc-3.47.1.0.jar src/*.java -d bin/  
    Execution: java -cp lib/sqlite-jdbc-3.47.1.0.jar:bin/ ePortfolio.GUI

**B. Creation of a new JAR file.**  
    First compile: javac -cp lib/sqlite-jdbc-3.47.1.0.jar src/*.java -d bin/  
    create jar file: jar cfm ePortfolio.jar manifest.txt -C bin/ .  
    execute jar file: java -jar ePortfolio.jar OR simply double click the jar file.  
    Note: for OPTION B make sure manifests.txt includes Main-Class: ePortfolio.GUI and has a newline after that. 

**Manifest.txt:**
~~~plaintext
Main-Class: ePortfolio.GUI
Class-Path: lib/sqlite-jdbc-3.47.1.0.jar

~~~
(there is an empty line above)

**Screenshots**
![Welcome Panel](docs/screenshots/home.jpg)  
![Menu](docs/screenshots/command.jpg)  
![Sell Panel](docs/screenshots/sell.jpg)  
![Update Panel](docs/screenshots/update.jpg)  
![Gain Panel](docs/screenshots/gain.jpg)  
![Search Panel](docs/screenshots/search.jpg)

## File Contents

**File Contents:**

- **`src`** contains all the `.java` files:
  - **`Portfolio.java`**: Contains the `Portfolio` class with methods to manage investments.
  - **`Main.java`**: Contains the main method for running the application.
  - **`Stock.java`**: Defines the `Stock` class representing stock investments.
  - **`MutualFund.java`**: Defines the `MutualFund` class representing mutual fund investments.
  - **`Investment.java`**: A superclass for all types of investments, including stocks and mutual funds, to simplify code by reducing redundancy.
  
- **`lib`** contains the database `portfolio.db` for saving and retrieving user game information.

- **`bin`** contains all the `.class` files.

- **`resources`** contains images.

- **`docs`** contains screenshots.

## (3) Assumptions and Limitations

### **Assumptions:**
- **Unique and Correct Symbols:**  
  Every stock and mutual fund is uniquely identified by its symbol.

- **Commission and Fees:**  
  - Stocks have a commission of $9.99 for both buying and selling, while mutual funds don’t have a commission but have a $45 redemption fee when sold.  
- **Case-Insensitive Symbols:**  
  The software assumes symbols are case-insensitive.

### **Limitations:**
- **Hardcoded Fees:**  
  The program hardcodes the $9.99 commission for stocks and the $45 fee for mutual funds, which may not match different brokerage fees.

- **No Real-time Price Updates:**  
  Users must manually update stock or mutual fund prices.

- **No Check on Whether the Investment Exists:**  
  The program does not verify the existence of an investment and would require an API for such checks.

- **Limited Investment Types:**  
  The program only handles stocks and mutual funds. Other types of investments (like bonds, ETFs, etc.) are not supported.

- **Outdated GUI (Java Swing):**  
  The graphical user interface is built using Java Swing, which may not offer the most modern user experience.

## (4) Test Plan

### A. Welcome Page
- **Objective:**
  - Verify that the welcome message and instructions are displayed correctly.
- **Test Steps:**
  1. Open the program.
  2. Check if the welcome message and instructions appear.
  3. Ensure the menu has these options:
     - Buy Investment
     - Sell Investment
     - Update Investments
     - Get Total Gain
     - Search Investments
     - Quit
  4. Verify that the current balance is displayed.
- **Functionality Tests:**
  - **Set New Balance Button:**
    - Enter a number between 0 and 1,000,000.
      - **Expected Result:** Balance updates accordingly.
    - Enter a wrong value (like numbers outside the range or letters).
      - **Expected Result:** An error message should show.
  - **Reset Game Button:**
    - It should reset all data and set the balance to zero.
- **Database Interaction:**
  - Make sure the program loads data from the database (`lib/portfolio.db`) when it starts and saves data to the database when the balance changes.
  - **Expected Messages:**  
    - Created new database or loaded from database upon startup.

### B. Buy Investment
- **Objective:**
  - Validate buying of stocks or mutual funds.
- **Test Steps:**
  1. Choose "Buy Investment" from the menu.
  2. Check if these fields are there: Type (Stock or Mutual Fund), Symbol, Name, Quantity, and Price.
- **Functionality Tests:**
  - **Buy Button:**
    - Enter correct values (positive quantity and price).  
      **Expected Result:** Investment is added or updated.
    - Enter wrong values (negative, zero, or blank).  
      **Expected Result:** An error message should show.
    - Make sure:  
      - Buying stocks adds a $9.99 fee.  
      - Buying mutual funds does not add a fee.  
      - The same symbol cannot be used for both a stock and a mutual fund.
  - **Reset Button:**
    - It should clear all fields.
- **Database Interaction:**
  - Make sure the program saves the portfolio to the database after buying.

### C. Sell Investment
- **Objective:**
  - Ensure that selling investments works properly.
- **Test Steps:**
  1. Choose "Sell Investment" from the menu.
  2. Check if these fields are there: Symbol, Quantity, and Price.
- **Functionality Tests:**
  - **Sell Button:**
    - Enter correct values.  
      **Expected Result:** Reduces the quantity or removes the investment.
    - Enter wrong values (more than you own, zero, or negative).  
      **Expected Result:** An error message should show.
    - Make sure:  
      - Selling stocks subtracts a $9.99 fee.  
      - Selling mutual funds subtracts a $45 fee.
  - **Reset Button:**
    - Clears all fields.
- **Database Interaction:**
  - Saves the portfolio to the database after selling.

### D. Update Investments
- **Objective:**
  - Validate updating of investment prices.
- **Test Steps:**
  1. Choose "Update Investments" from the menu.
  2. Check if Symbol and Name are shown (cannot edit) and Price is editable.
  3. "Prev" and "Next" buttons should navigate through investments.
     - Disabled if there is no previous or next investment.
- **Functionality Tests:**
  - **Save Button:**
    - Enter correct prices.  
      **Expected Result:** Updates the price.
    - Enter wrong prices (zero or negative).  
      **Expected Result:** Error message.
- **Database Interaction:**
  - Updates saved to the database.

### E. Get Total Gain
- **Objective:**
  - Check that total gain is calculated correctly.
- **Test Steps:**
  1. Choose "Get Total Gain" from the menu.
  2. Total gain should appear in a box that cannot be edited.
  3. Details for each investment displayed in a scrollable box.
- **Functionality Tests:**
  - Gains include $9.99 fees for stocks and $45 fees for mutual funds.
  - Total gain updates with the latest prices.

### F. Search Investments
- **Objective:**
  - Validate the search functionality.
- **Test Steps:**
  1. Choose "Search Investments" from the menu.
  2. Check these fields: Symbol, Name Keywords, Low Price, High Price.
- **Functionality Tests:**
  - **Search Button:**
    - Leave all fields empty (wildcard search).  
      **Expected Result:** Shows all investments.
    - Enter one field (e.g., symbol or keyword).  
      **Expected Result:** Results for that field.
    - More than one field.  
      **Expected Result:** Narrowed search.
    - Wrong price ranges (negative or low > high).  
      **Expected Result:** Error message.
  - **Reset Button:**
    - Clears all fields and results.

**(5) Possible Improvements**
    a. Develop a more modern GUI using JavaFX or javascript or web-based interface to improve accessibility and user experience. 
    b. Dynamic Fees and Commissions: Because every brokerage has different rates we should not hardcode fees and have an option to set them. 
    c. Real-time Data: use an api to fetch live stock/mutual fund prices, saving users from manually entering prices which is a hassle.
    d. Include other investments like bonds, ETFs, and cryptocurrencies. THere are many kind of investments
    e. Make a web app with Java Spring Boot where users can create accounts, log in, and manage their own investment portfolios.
