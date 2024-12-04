package ePortfolio;
import java.util.InputMismatchException;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import javax.swing.BoxLayout; 
import javax.swing.ImageIcon; //didnt use but maybe later.
import java.awt.Color; //figure out how to add more colors later.
import java.awt.Font; //didnt use but maybe later
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The GUI class manages the graphical user interface for the ePortfolio application.
 */
public class GUI {
    private Portfolio portfolio;
    private int currentIndex = 0; //for the update panel like when i want to go next or prev
    //updatables
    private JTextField updateSymbolField;
    private JTextField updateNameField;
    private JTextField updatePriceField;
    private JButton prevButton;
    private JButton nextButton;
    private JButton saveButton;
    private JTextArea updateMessageArea;
    //all panels
    private JPanel welcomePanel;
    private JPanel buyPanel;
    private JPanel sellPanel;
    private JPanel updatePanel;
    private JPanel totalGainPanel;
    private JPanel searchPanel;

    //main panel cont
    private JPanel mainPanel;
    private JFrame frame;

    //height width constants.
    public static final int WIDTH = 670; //setting this const
    public static final int HEIGHT = 350;

    //main
    /**
     * Main method to launch the application.
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.createAndShowGUI();
    }
    //gui constructor
    /**
     * Constructor for the GUI class. Initializes the portfolio and loads data from a file.
     */
    public GUI() {
        portfolio = new Portfolio(); //new prtfolio

        //TRY TO load investments from file
        try {
            portfolio.loadFromFile("investments.txt"); //investment file from A2, reading a file. 
        } 
        catch (Exception e) { //for some reason cant use filenotfound exception, but this works. 
             //exception handling
            System.out.println("Error initializing portfolio: " + e.getMessage());
            System.exit(0); 
        }
    }
    //DISPLAY
    /**
     * Creates and displays the main GUI interface.
     */
    private void createAndShowGUI() {
        //main app frame
        frame = new JFrame("ePortfolio");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        //menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu commandsMenu = new JMenu("Commands");
        JMenuItem welcomeMenuItem = new JMenuItem("Welcome");
        JMenuItem buyMenuItem = new JMenuItem("Buy Investment");
        JMenuItem sellMenuItem = new JMenuItem("Sell Investment");
        JMenuItem updateMenuItem = new JMenuItem("Update Investments");
        JMenuItem totalGainMenuItem = new JMenuItem("Get Total Gain");
        JMenuItem searchMenuItem = new JMenuItem("Search Investments");
        JMenuItem quitMenuItem = new JMenuItem("Quit");

        commandsMenu.add(welcomeMenuItem);
        commandsMenu.add(buyMenuItem);
        commandsMenu.add(sellMenuItem);
        commandsMenu.add(updateMenuItem);
        commandsMenu.add(totalGainMenuItem);
        commandsMenu.add(searchMenuItem);
        commandsMenu.addSeparator();
        commandsMenu.add(quitMenuItem);
        menuBar.add(commandsMenu);
        frame.setJMenuBar(menuBar);

        //mainpanel with borderlayout
        mainPanel = new JPanel(new BorderLayout());

        //the indiv panels (make them)
        welcomePanel = createWelcomePanel();
        buyPanel = createBuyPanel();
        sellPanel = createSellPanel();
        updatePanel = createUpdatePanel();
        totalGainPanel = createTotalGainPanel();
        searchPanel = createSearchPanel();
        //welcome panel
        mainPanel.add(welcomePanel, BorderLayout.CENTER);

        //action listeners for menu items
        welcomeMenuItem.addActionListener(new WelcomeListener());
        buyMenuItem.addActionListener(new BuyListener());
        sellMenuItem.addActionListener(new SellListener());
        updateMenuItem.addActionListener(new UpdateListener());
        totalGainMenuItem.addActionListener(new TotalGainListener());
        searchMenuItem.addActionListener(new SearchListener());
        quitMenuItem.addActionListener(new QuitListener());

        //add mainpanel to frame
        frame.add(mainPanel);
        frame.setVisible(true); //setvisible
    }
/**
     * Swaps the current panel displayed in the main panel with a new panel.
     * @param newPanel the new panel to display.
     */
    private void swapPanel(JPanel newPanel) { //change.
        mainPanel.removeAll();
        mainPanel.add(newPanel, BorderLayout.CENTER);
        mainPanel.validate(); //validate panel.
        mainPanel.repaint();
    }

    //HOME PANEL (WELCOME PANEL WITH WELCOME MESSAGES)
    /**
     * Creates the welcome panel with welcome messages and instructions.
     * @return the welcome panel.
     */
    private static JPanel createWelcomePanel() {
        //make main panel with borderlayout..
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN); //background color blue :)
        //create+add title label..
        JLabel titleLabel = new JLabel("Welcome to ePortfolio");
        titleLabel.setHorizontalAlignment(JLabel.CENTER); // Center align title
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font for the title
        panel.add(titleLabel, BorderLayout.NORTH);
        //subpanel with descriptions using flowlayout.
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.setBackground(Color.CYAN); // Match the main panel's background
        //descript labels.
        JLabel descriptionLabel1 = new JLabel("Choose a command from the \"Commands\" menu to buy or sell an investment,");
        JLabel descriptionLabel2 = new JLabel("update prices for all investments, get gain for the portfolio,");
        JLabel descriptionLabel3 = new JLabel("search for relevant investments, or quit the program.");
        descriptionPanel.add(descriptionLabel1);
        descriptionPanel.add(descriptionLabel2);
        descriptionPanel.add(descriptionLabel3);
        panel.add(descriptionPanel, BorderLayout.CENTER);
        return panel;
    }
    
    //CREATE BUY PANEL.
    /**
     * Creates the buy panel for buying investments.
     * @return the buy panel.
     */
    private JPanel createBuyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Buying an Investment");
        titleLabel.setHorizontalAlignment(JLabel.LEFT); // Align title to the left
        panel.add(titleLabel, BorderLayout.NORTH);
        //GridLayout (5 rows, 1 column)
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 10, 10)); // 5 rows, 1 column

        //TYPE OF INVESTMENT
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeDropdown = new JComboBox<>(new String[]{"Stock", "Mutual Fund"});
        typePanel.add(typeLabel);
        typePanel.add(typeDropdown);
        formPanel.add(typePanel);

        //SYMBOL
        JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField(15); // Limit width
        symbolPanel.add(symbolLabel);
        symbolPanel.add(symbolField);
        formPanel.add(symbolPanel);

        //NAME
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15); // Limit width
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        formPanel.add(namePanel);

        // Quantity
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(15); // Limit width
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);
        formPanel.add(quantityPanel);

        //PRICE
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(15); // Limit width
        pricePanel.add(priceLabel);
        pricePanel.add(priceField);
        formPanel.add(pricePanel);
        panel.add(formPanel, BorderLayout.CENTER);

        //BUTTONS PANEL
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton resetButton = new JButton("Reset");
        JButton buyButton = new JButton("Buy");
        buttonsPanel.add(resetButton);
        buttonsPanel.add(buyButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        //MESSAGE PANEL (use border layout )
        JPanel messagePanel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("Messages:");
        JTextArea messageArea = new JTextArea(5, 30); // Consistent size
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.EAST);
        //we have to attach listeners to buttons
        BuyPanelListener buyListener = new BuyPanelListener(typeDropdown, symbolField, nameField, quantityField, priceField, messageArea);
        buyButton.addActionListener(buyListener);
        resetButton.addActionListener(buyListener);
        return panel;
    }
    /**
     * Creates the sell panel for selling investments.
     * @return the sell panel.
     */
    //sell panel! use similar thing above 
    private JPanel createSellPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Selling an Investment");
        titleLabel.setHorizontalAlignment(JLabel.LEFT); // Align title to the left
        panel.add(titleLabel, BorderLayout.NORTH);

        //panel w/ GridLayout (3 rows, 1 column)
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10)); // 3 rows, 1 column

        //SYMBOL
        JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField(15); // Limit width
        symbolPanel.add(symbolLabel);
        symbolPanel.add(symbolField);
        formPanel.add(symbolPanel);

        //QUANT
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(15); // Limit width
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);
        formPanel.add(quantityPanel);

        //PRICE
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(15); // Limit width
        pricePanel.add(priceLabel);
        pricePanel.add(priceField);
        formPanel.add(pricePanel);

        panel.add(formPanel, BorderLayout.CENTER);

        // BUTTONS PANEL WTH FLOWLAYOUT
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton resetButton = new JButton("Reset");
        JButton sellButton = new JButton("Sell");
        buttonsPanel.add(resetButton);
        buttonsPanel.add(sellButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        // MSG PANEL WITH BORDER LAYOUT
        JPanel messagePanel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("Messages:");
        JTextArea messageArea = new JTextArea(5, 30); //keep it consistent ugh
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.EAST);

        //attaching listen to buttons
        SellPanelListener sellListener = new SellPanelListener(symbolField, quantityField, priceField, messageArea);
        sellButton.addActionListener(sellListener);
        resetButton.addActionListener(sellListener);

        return panel;
    }

    /**
     * Creates the update panel for updating investments.
     * @return the update panel.
     */
    //update panel. remember nav.
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN); // blue color :) check if it works because it didnt work for search panel. 
        JLabel titleLabel = new JLabel("Updating Investments");
        titleLabel.setHorizontalAlignment(JLabel.LEFT); //align title toward leftside.
        panel.add(titleLabel, BorderLayout.NORTH);
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10)); //3 rows, 1 column w/ gridlayout

        //SYMBOL
        JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel symbolLabel = new JLabel("Symbol:");
        updateSymbolField = new JTextField(15);
        updateSymbolField.setEditable(false);
        symbolPanel.add(symbolLabel);
        symbolPanel.add(updateSymbolField);
        formPanel.add(symbolPanel);

        //NAME
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Name:");
        updateNameField = new JTextField(15);
        updateNameField.setEditable(false);
        namePanel.add(nameLabel);
        namePanel.add(updateNameField);
        formPanel.add(namePanel);

        //PRICce
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel priceLabel = new JLabel("Price:");
        updatePriceField = new JTextField(15);
        pricePanel.add(priceLabel);
        pricePanel.add(updatePriceField);
        formPanel.add(pricePanel);

        panel.add(formPanel, BorderLayout.CENTER);

        // Buttons Panel with FlowLayout
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        prevButton = new JButton("Prev");
        nextButton = new JButton("Next");
        saveButton = new JButton("Save");
        buttonsPanel.add(prevButton);
        buttonsPanel.add(nextButton);
        buttonsPanel.add(saveButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        //MSG PANEL W/ BORDERLAYOUT
        JPanel messagePanel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("Messages:");
        updateMessageArea = new JTextArea(5, 30); // Consistent size
        updateMessageArea.setEditable(false);
        updateMessageArea.setLineWrap(true);
        updateMessageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(updateMessageArea);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.EAST);

        //ATTACH LISTENER TO BUTTONS
        UpdatePanelListener updateListener = new UpdatePanelListener();
        prevButton.addActionListener(updateListener);
        nextButton.addActionListener(updateListener);
        saveButton.addActionListener(updateListener);
        return panel;
    }
    
    /**
     * Creates the total gain panel for displaying portfolio gains.
     * @return the total gain panel.
     */

   //total gain panel. OK HERE the blue isnt working why? ask ta
    private JPanel createTotalGainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN); //for some reason this doesnt work.

        JLabel titleLabel = new JLabel("Total Gain");

        titleLabel.setHorizontalAlignment(JLabel.LEFT); //align title towars left
        panel.add(titleLabel, BorderLayout.NORTH);

        //TOP panel for total GAINS
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        JLabel totalGainLabel = new JLabel("Total gain:");
        JTextField totalGainField = new JTextField(10);
        totalGainField.setEditable(false);
        JButton calculateButton = new JButton("Calculate");
        topPanel.add(totalGainLabel);
        topPanel.add(totalGainField);
        topPanel.add(calculateButton);
        panel.add(topPanel, BorderLayout.NORTH);

        //bottom panel for total gains
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel individualGainsLabel = new JLabel("Individual Gains:");
        JTextArea individualGainsArea = new JTextArea(5, 30); // Consistent size
        individualGainsArea.setEditable(false);
        individualGainsArea.setLineWrap(true);
        individualGainsArea.setWrapStyleWord(true);
        JScrollPane gainsScrollPane = new JScrollPane(individualGainsArea);
        bottomPanel.add(individualGainsLabel, BorderLayout.NORTH);
        bottomPanel.add(gainsScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.CENTER);

        //listernr to buttons.
        TotalGainListenerButton gainButtonListener = new TotalGainListenerButton(totalGainField, individualGainsArea);
        calculateButton.addActionListener(gainButtonListener);
        return panel;
    }
    /**
     * Creates the search panel for searching investments.
     * @return the search panel.
     */
    //search panel. 
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Searching Investments");
        titleLabel.setHorizontalAlignment(JLabel.LEFT); //align leftwords. 
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10)); //4 rows 1 column

        //SYMBOL
        JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField(15); 
        symbolPanel.add(symbolLabel);
        symbolPanel.add(symbolField);
        formPanel.add(symbolPanel);

        //KEYWORD (test this )
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Name Keywords:");
        JTextField nameField = new JTextField(15); // Limit width
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        formPanel.add(namePanel);

        //high Price
        JPanel lowPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lowPriceLabel = new JLabel("Low Price:");
        JTextField lowPriceField = new JTextField(15); // Limit width
        lowPricePanel.add(lowPriceLabel);
        lowPricePanel.add(lowPriceField);
        formPanel.add(lowPricePanel);

        //lowPrice
        JPanel highPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel highPriceLabel = new JLabel("High Price:");
        JTextField highPriceField = new JTextField(15); // Limit width
        highPricePanel.add(highPriceLabel);
        highPricePanel.add(highPriceField);
        formPanel.add(highPricePanel);

        panel.add(formPanel, BorderLayout.CENTER);

        //buttons panel (flowlayout)
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton resetButton = new JButton("Reset");
        JButton searchButton = new JButton("Search");
        buttonsPanel.add(resetButton);
        buttonsPanel.add(searchButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        //msg Panel () BorderLayout
        JPanel messagePanel = new JPanel(new BorderLayout());
        JLabel resultLabel = new JLabel("Search Results:");
        JTextArea resultArea = new JTextArea(5, 30); // Consistent size
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        messagePanel.add(resultLabel, BorderLayout.NORTH);
        messagePanel.add(resultScrollPane, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.EAST);

        // listeners to buttons
        SearchPanelListener searchListener = new SearchPanelListener(symbolField, nameField, lowPriceField, highPriceField, resultArea);
        searchButton.addActionListener(searchListener);
        resetButton.addActionListener(searchListener);

        return panel;
    }
    /**
     * Displays the investment details in the update panel.
     * @param symbolField the text field for the investment symbol.
     * @param nameField the text field for the investment name.
     * @param priceField the text field for the investment price.
     * @param index the index of the investment to display.
     * @throws Exception if the investment cannot be displayed.
     */
    private void displayInvestment(JTextField symbolField, JTextField nameField, JTextField priceField, int index) throws Exception {
        Investment investment = portfolio.getInvestments().get(index);
        symbolField.setText(investment.getSymbol());
        nameField.setText(investment.getName());
        priceField.setText(String.valueOf(investment.getPrice()));
    }

    /**
     * Updates the navigation buttons in the update panel based on the current index.
     */
    //for uopdating navigation buttons based on the cur index.
    private void updateNavigationButtons() {
        if (portfolio.getInvestments().isEmpty()) {
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
            saveButton.setEnabled(false);
        } 
        else {
            prevButton.setEnabled(currentIndex > 0);
            nextButton.setEnabled(currentIndex < portfolio.getInvestments().size() - 1);
            saveButton.setEnabled(true);
        }
    }

     /**
     * Listener for the welcome menu option.
     * Switches the current panel to the welcome panel.
     */
    //listener Classes
    //welcome menu listener
    private class WelcomeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(welcomePanel); //change
        }
    }
    /**
     * Listener for the buy menu option.
     * Switches the current panel to the buy panel.
     */
    //buy menu listener
    private class BuyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(buyPanel);
        }
    }

   
    //Sell Menu listener.
    private class SellListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(sellPanel);
        }
    }

    //updatemenu listener
    private class UpdateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!portfolio.getInvestments().isEmpty()) {
                currentIndex = 0;
                try {
                    displayInvestment(updateSymbolField, updateNameField, updatePriceField, currentIndex);
                    updateNavigationButtons();
                    updateMessageArea.setText("");
                    saveButton.setEnabled(true);
                } catch (Exception ex) {
                    updateMessageArea.setText("Error displaying investment: " + ex.getMessage());
                    saveButton.setEnabled(false);
                }
            } 
            else {
                updateMessageArea.setText("No investments to update.");
                prevButton.setEnabled(false);
                nextButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
            swapPanel(updatePanel);
        }
    }

   //total gain in menu listener
    private class TotalGainListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(totalGainPanel);
        }
    }

    //search menu listenre.
    private class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(searchPanel);
        }
    }

    //quit listener
    private class QuitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //save portfolio before quiting.... needs further development. 
            try {
                portfolio.saveToFile("investments.txt");
            } 
            catch (Exception ex) { 
                System.out.println("Error saving portfolio: " + ex.getMessage());
                System.exit(0); //exit if cant be saved
            }
            System.exit(1); //exit success if ti can be saved.
        }
    }

    //Listener Classes for Buy, Sell, Update Panels

    //buy panel listeners
    private class BuyPanelListener implements ActionListener {
        private JComboBox<String> typeDropdown;
        private JTextField symbolField;
        private JTextField nameField;
        private JTextField quantityField;
        private JTextField priceField;
        private JTextArea messageArea;

        public BuyPanelListener(JComboBox<String> typeDropdown, JTextField symbolField, JTextField nameField, JTextField quantityField, JTextField priceField, JTextArea messageArea) {
            this.typeDropdown = typeDropdown;
            this.symbolField = symbolField;
            this.nameField = nameField;
            this.quantityField = quantityField;
            this.priceField = priceField;
            this.messageArea = messageArea;
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Buy")) {
                String type = (String) typeDropdown.getSelectedItem();
                String symbol = symbolField.getText().trim();
                String name = nameField.getText().trim();
                String quantityText = quantityField.getText().trim();
                String priceText = priceField.getText().trim();
                //check if fields empty (must be filled in)
                if (symbol.isEmpty() || name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                    messageArea.setText("All fields must be filled out!");
                    return;
                }
                int quantity;
                double price;

                //validate quantity and price
                try {
                    quantity = Integer.parseInt(quantityText);
                    price = Double.parseDouble(priceText);
                    if (quantity <= 0 || price <= 0) {
                        throw new InputMismatchException();
                    }
                } 
                catch (InputMismatchException ex) { //from exceptions slides. Wrong entry
                    messageArea.setText("Quantity and Price must be positive numbers!");
                    return;
                }

                try {
                    String result = portfolio.buy(type, symbol, name, quantity, price);
                    messageArea.setText(result);
                } 
                catch (Exception ex) {
                    messageArea.setText(ex.getMessage());
                }
            } 
            else if (command.equals("Reset")) {
                typeDropdown.setSelectedIndex(0);
                symbolField.setText("");
                nameField.setText("");
                quantityField.setText("");
                priceField.setText("");
                messageArea.setText("");
            }
        }
    }

    //listener for sell panel buttons

    private class SellPanelListener implements ActionListener {
        private JTextField symbolField;
        private JTextField quantityField;
        private JTextField priceField;
        private JTextArea messageArea;

        public SellPanelListener(JTextField symbolField, JTextField quantityField,
                                 JTextField priceField, JTextArea messageArea) {
            this.symbolField = symbolField;
            this.quantityField = quantityField;
            this.priceField = priceField;
            this.messageArea = messageArea;
        }
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Sell")) {
                String symbol = symbolField.getText().trim();
                String quantityText = quantityField.getText().trim();
                String priceText = priceField.getText().trim();

                //like ABOVE last panel (Buy) check for empty fields.
                if (symbol.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                    messageArea.setText("All fields must be filled out.");
                    return;
                }
                int quantity;
                double price;
                //validate quant and price
                try {
                    quantity = Integer.parseInt(quantityText);
                    price = Double.parseDouble(priceText);

                    if (quantity <= 0 || price <= 0) {
                        throw new InputMismatchException();
                    }
                } 
                catch (InputMismatchException ex) {
                    messageArea.setText("Quantity and Price must be positive numbers.");
                    return;
                }

                try {
                    String result = portfolio.sell(symbol, quantity, price);
                    messageArea.setText(result);
                } 
                catch (Exception ex) {
                    messageArea.setText(ex.getMessage());
                }
            } 
            else if (command.equals("Reset")) {
                symbolField.setText("");
                quantityField.setText("");
                priceField.setText("");
                messageArea.setText("");
            }
        }
    }

    /**
     * Listener for the Update panel navigation and actions.
     * Handles "Prev", "Next", and "Save" commands for navigating and updating investments.
     */

    //update panel listeners. HARD
    private class UpdatePanelListener implements ActionListener {
        //Navigation
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            //prev
            if (command.equals("Prev")) {
                if (!portfolio.getInvestments().isEmpty() && currentIndex > 0) {
                    currentIndex--;
                    try {
                        displayInvestment(updateSymbolField, updateNameField, updatePriceField, currentIndex);
                        updateNavigationButtons();
                        updateMessageArea.setText("");
                    } 
                    catch (Exception ex) {
                        updateMessageArea.setText("Error displaying investment: " + ex.getMessage());
                    }
                }
            } 
            //next
            else if (command.equals("Next")) {
                if (!portfolio.getInvestments().isEmpty() && currentIndex < portfolio.getInvestments().size() - 1) {
                    currentIndex++;
                    try {
                        displayInvestment(updateSymbolField, updateNameField, updatePriceField, currentIndex);
                        updateNavigationButtons();
                        updateMessageArea.setText("");
                    } 
                    catch (Exception ex) {
                        updateMessageArea.setText("Error displaying investment: " + ex.getMessage());
                    }
                }
            } 
            else if (command.equals("Save")) {
                if (portfolio.getInvestments().isEmpty()) {
                    updateMessageArea.setText("No investments to update.");
                    return;
                }
                double newPrice;
                try {
                    newPrice = Double.parseDouble(updatePriceField.getText().trim());
                    if (newPrice <= 0) {
                        throw new InputMismatchException();
                    }
                } 
                //wrong entry
                catch (InputMismatchException ex) {
                    updateMessageArea.setText("Price must be a positive number.");
                    return;
                }

                try {
                    String result = portfolio.updatePrice(currentIndex, newPrice);
                    updateMessageArea.setText(result);
                    displayInvestment(updateSymbolField, updateNameField, updatePriceField, currentIndex);
                } 
                catch (Exception ex) {
                    updateMessageArea.setText(ex.getMessage());
                }
            }
        }
    }
     /**
     * Listener for the Total Gain screen button.
     * Calculates and displays the total gain and individual gains of the portfolio.
     */

    //calc total gain screen listeners
    private class TotalGainListenerButton implements ActionListener {
        private JTextField totalGainField;
        private JTextArea individualGainsArea;

        public TotalGainListenerButton(JTextField totalGainField, JTextArea individualGainsArea) {
            this.totalGainField = totalGainField;
            this.individualGainsArea = individualGainsArea;
        }
        public void actionPerformed(ActionEvent e) {
            String result = portfolio.getGain();
            String[] lines = result.split("\n");
            StringBuilder individualGains = new StringBuilder();
            for (String line : lines) {
                if (line.startsWith("Total gain")) {
                    int dollarIndex = line.indexOf("$");
                    if (dollarIndex != -1 && dollarIndex + 1 < line.length()) {
                        totalGainField.setText(line.substring(dollarIndex + 1));
                    } 
                    else {
                        totalGainField.setText("");
                    }
                } 
                else {
                    individualGains.append(line).append("\n");
                }
            }
            individualGainsArea.setText(individualGains.toString());
        }
    }

    //search panel listenres

 /**
     * Listener for the Search panel.
     * Handles search and reset operations for investments based on user input.
     */
    private class SearchPanelListener implements ActionListener {
        private JTextField symbolField;
        private JTextField nameField;
        private JTextField lowPriceField;
        private JTextField highPriceField;
        private JTextArea resultArea;

        public SearchPanelListener(JTextField symbolField, JTextField nameField, JTextField lowPriceField, JTextField highPriceField,JTextArea resultArea) {
            this.symbolField = symbolField;
            this.nameField = nameField;
            this.lowPriceField = lowPriceField;
            this.highPriceField = highPriceField;
            this.resultArea = resultArea;
        }
        //override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Search")) {
                String symbol = symbolField.getText().trim();
                String keywords = nameField.getText().trim();
                String lowPriceText = lowPriceField.getText().trim();
                String highPriceText = highPriceField.getText().trim();
                double lowPrice = -1;
                double highPrice = -1;

                //price range parce.
                try {
                    if (!lowPriceText.isEmpty()) {
                        lowPrice = Double.parseDouble(lowPriceText);
                        if (lowPrice < 0) throw new InputMismatchException(); //wrong input checker.
                    }
                    if (!highPriceText.isEmpty()) {
                        highPrice = Double.parseDouble(highPriceText);
                        if (highPrice < 0) throw new InputMismatchException();
                    }
                } 
                catch (InputMismatchException ex) {
                    resultArea.setText("Low Price and High Price must be positive numbers.");
                    return;
                }

                String result = portfolio.search(symbol, keywords, lowPrice, highPrice);
                resultArea.setText(result);
            } 
            else if (command.equals("Reset")) {
                symbolField.setText("");
                nameField.setText("");
                lowPriceField.setText("");
                highPriceField.setText("");
                resultArea.setText("");
            }
        }
    }
}
