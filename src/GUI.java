package ePortfolio;

//SWING
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
import javax.swing.ImageIcon;
import javax.swing.Box;
//LAYOUT
import java.awt.Color; //for more colors- -> https://www.javatpoint.com/java-color-codes
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image; //for images in /resources folder.
import java.awt.Dimension;
import java.awt.Component;
//EVENT
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//EXCEPTION
import java.util.InputMismatchException;
import javax.swing.JOptionPane;

//in the future we can break this file down further --> create panels, listeners, etc.

//(Added) SQL imports for handling SQLite
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//(ADDED) For Window Listener
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *The GUI class manages the graphical user interface for the ePortfolio application.
 */
public class GUI {
    private Portfolio portfolio;
    private int currentIndex = 0; //for the update panel like when I want to go next or prev

    //updatables 
    private JTextField updateSymbolField;
    private JTextField updateNameField;
    private JTextField updatePriceField;
    private JButton prevButton; //for the update panel
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

    //height width constants for GUI
    public static final int WIDTH = 675;  //change this to adj width
    public static final int HEIGHT = 475; //change this to adj height.

    private JTextField currentBalField;             //instance var for Current Balance Field

    /**
     *Main method to launch app
     *@param args 
     */
    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.createAndShowGUI(); //sets visible
    }

    /**
     *constructor for the GUI class. Initializes the portfolio and loads data from a file.
     *(Note: Now adapted to load from a database instead of a file.)
     */
    public GUI() {
        portfolio = new Portfolio(); //new portfolio

        //TRY TO load investments from the SQLite database instead of file
        try {
            //portfolio.loadFromFile("lib/investments.txt"); //old approach commented out

            //New approach: Load from SQLite database
            portfolio.loadFromDatabase("lib/portfolio.db"); 
        } 
        catch (Exception e) {
            System.out.println("Error initializing portfolio from database: " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     *Creates and displays the main GUI interface.
     */
    private void createAndShowGUI() {
        // Main app frame
        frame = new JFrame("ePortfolio");
        //override default close operation
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Window Listener to save upon window [X]
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Attempt to save before closing
                try {
                    portfolio.saveToDatabase("lib/portfolio.db");
                    System.out.println("Portfolio saved on window close.");
                } catch (Exception ex) {
                    System.out.println("Error saving portfolio on window close: " + ex.getMessage());
                }
                // Then exit
                System.exit(0);
            }
        });

        frame.setSize(WIDTH, HEIGHT);

        //menu bar OPTIONS.. user can click any of these and it will redirect them to that panel.
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

        mainPanel = new JPanel(new BorderLayout()); //main panel with BorderLayout

        //init panels
        welcomePanel = createWelcomePanel();
        buyPanel = createBuyPanel();
        sellPanel = createSellPanel();
        updatePanel = createUpdatePanel();
        totalGainPanel = createTotalGainPanel();
        searchPanel = createSearchPanel();

        //default to welcome panel
        mainPanel.add(welcomePanel, BorderLayout.CENTER);

        //action listeners for menu items
        welcomeMenuItem.addActionListener(new WelcomeListener());
        buyMenuItem.addActionListener(new BuyListener());
        sellMenuItem.addActionListener(new SellListener());
        updateMenuItem.addActionListener(new UpdateListener());
        totalGainMenuItem.addActionListener(new TotalGainListener());
        searchMenuItem.addActionListener(new SearchListener());
        quitMenuItem.addActionListener(new QuitListener());

        //add mainPanel to frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /**
     *Swaps the current panel displayed in the main panel with a new panel.
     *@param newPanel the new panel to display.
     */
    private void swapPanel(JPanel newPanel) {
        mainPanel.removeAll();
        mainPanel.add(newPanel, BorderLayout.CENTER);
        mainPanel.validate();
        mainPanel.repaint();
    }

    /**
     *Creates the welcome panel with an H1 title at the top,
     *@return the welcome panel.
     */
    private JPanel createWelcomePanel() {
        //MAIN panel has BorderLayout.
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN); //i Like this color -----------------_> CYAN. This is used for title.

        //1) H1 Title at the very top
        JLabel h1Title = new JLabel("Welcome to ePortfolio", JLabel.CENTER);
        h1Title.setFont(new Font("Arial", Font.BOLD, 24));  //font for H1 should be big
        panel.add(h1Title, BorderLayout.NORTH);

        //   2) Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        //2.1) Welcome Instructions (to the left of the panel)
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        instructionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT); //alin to left in BoxLayout

        JLabel instruction1 = new JLabel("Set \"new starting balance\" to get started");
        JLabel instruction2 = new JLabel("Choose from \"Commands\" to buy, sell, update or search investments");
        JLabel instruction3 = new JLabel("$9.99 commission fee for stocks and $45 redemption for mutual funds");
        JLabel instruction4 = new JLabel("Press reset to RESTART FULLY");

        for (JLabel instruction : new JLabel[]{instruction1, instruction2, instruction3, instruction4}) {
            instruction.setAlignmentX(Component.LEFT_ALIGNMENT);
            instruction.setHorizontalAlignment(JLabel.LEFT);
            instruction.setMaximumSize(new Dimension(Integer.MAX_VALUE, instruction.getPreferredSize().height));
            instructionsPanel.add(instruction);
        }
        centerPanel.add(instructionsPanel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 2.2) Current Balance (align left)
        JPanel currentBalancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel currentBalLabel = new JLabel("Current Balance:");
        currentBalField = new JTextField(10); //use the instance varieble.
        currentBalField.setEditable(false);
        //starting balance PALCEHOLDER for current balance..
        currentBalField.setText(String.format("%.2f", portfolio.getStartingBalance()));
        currentBalancePanel.add(currentBalLabel);
        currentBalancePanel.add(currentBalField);
        centerPanel.add(currentBalancePanel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); 

        // 2.3) Image center 
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        JLabel bullishLabel = new JLabel();
        try {
            ImageIcon bullishIcon = new ImageIcon("resources/bullish.png");
            if (bullishIcon.getIconWidth() == -1) {
                throw new Exception("Image not found or unable to load.");
            }
            Image image = bullishIcon.getImage();
            Image scaledImage = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            bullishIcon = new ImageIcon(scaledImage);
            bullishLabel.setIcon(bullishIcon);
        } 
        catch (Exception ex) {
            System.out.println("Unable to load the image: " + ex.getMessage());
        }
        imagePanel.add(bullishLabel);
        centerPanel.add(imagePanel);
        panel.add(centerPanel, BorderLayout.CENTER);

        //3) bottom panel for setting starting balance and resetting game/.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel balanceLabel = new JLabel("Set new starting balance:");
        JTextField balanceField = new JTextField(10);
        JButton setBalanceButton = new JButton("Set");
        JButton resetGameButton = new JButton("Reset Game");

        bottomPanel.add(balanceLabel);
        bottomPanel.add(balanceField);
        bottomPanel.add(setBalanceButton);
        bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        bottomPanel.add(resetGameButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // ACTION LISTENERS FOR SET BALANCE AND RESET GAME
        setBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = balanceField.getText().trim();
                try {
                    double newBalance = Double.parseDouble(text);
                    if (newBalance < 0 || newBalance > 1_000_000) {
                        throw new NumberFormatException("Out of range");
                    }
                    portfolio.setStartingBalance(newBalance);
                    currentBalField.setText(String.format("%.2f", newBalance));

                    // Save the updated balance to DB
                    try {
                        portfolio.saveToDatabase("lib/portfolio.db");
                    } 
                    catch (Exception ex) {
                        System.out.println("Error saving updated balance: " + ex.getMessage());
                    }

                    JOptionPane.showMessageDialog(panel,  
                        "Starting balance set to: " + newBalance,  
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } 
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Please enter a valid number between 0 and 1,000,000.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        resetGameButton.addActionListener(new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                int confirmation = JOptionPane.showConfirmDialog(
                        panel, "Are you sure you want to reset everything?", "Confirm Reset", JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    portfolio.setStartingBalance(0);
                    portfolio.clearAllInvestments();
                    try {
                        portfolio.saveToDatabase("lib/portfolio.db");
                        currentBalField.setText("0.00");
                        JOptionPane.showMessageDialog(panel, "Game reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } 
                    catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel, "Error resetting game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        return panel;
    }

    /**
     *Creates the buy panel for buying investments.
     *@return the buy panel.
     */
    private JPanel createBuyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Buying an Investment");
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 1, 10, 10)); 
        //TYPE
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeDropdown = new JComboBox<>(new String[]{"Stock", "Mutual Fund"});
        typePanel.add(typeLabel);
        typePanel.add(typeDropdown);
        formPanel.add(typePanel);

        //sYMBOL
        JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField(15);
        symbolPanel.add(symbolLabel);
        symbolPanel.add(symbolField);
        formPanel.add(symbolPanel);

        //NAME
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        formPanel.add(namePanel);

        //Quantity
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(15);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);
        formPanel.add(quantityPanel);

        //PRICE
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(15);
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

        //MESSAGE PANEL
        JPanel messagePanel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("Messages:");
        JTextArea messageArea = new JTextArea(5, 30);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.EAST);

        //Listeners
        BuyPanelListener buyListener = new BuyPanelListener(
                typeDropdown, symbolField, nameField, quantityField, priceField, messageArea);
        buyButton.addActionListener(buyListener);
        resetButton.addActionListener(buyListener);

        return panel;
    }

    /**
     *Creates the sell panel for selling investments.
     *@return the sell panel.
     */
    private JPanel createSellPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN); //I like this color ----------------------- CYAN
        JLabel titleLabel = new JLabel("Selling an Investment");
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        panel.add(titleLabel, BorderLayout.NORTH);

        //form
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        //SYMBOL
        JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField(15);
        symbolPanel.add(symbolLabel);
        symbolPanel.add(symbolField);
        formPanel.add(symbolPanel);

        //QUANT
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(15);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);
        formPanel.add(quantityPanel);

        //PRICE
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(15);
        pricePanel.add(priceLabel);
        pricePanel.add(priceField);
        formPanel.add(pricePanel);

        panel.add(formPanel, BorderLayout.CENTER);

            //   BUTTONS PANEL
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton resetButton = new JButton("Reset");
        JButton sellButton = new JButton("Sell");
        buttonsPanel.add(resetButton);
        buttonsPanel.add(sellButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        //MESSAGE PANEL
        JPanel messagePanel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("Messages:");
        JTextArea messageArea = new JTextArea(5, 30);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.EAST);

        // Listeners
        SellPanelListener sellListener = new SellPanelListener(symbolField, quantityField, priceField, messageArea);
        sellButton.addActionListener(sellListener);
        resetButton.addActionListener(sellListener);

        return panel;
    }

    /**
     *Creates the update panel for updating investments.
     *@return the update panel.
     */
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN); 
        JLabel titleLabel = new JLabel("Updating Investments");
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        //  SYMBOL
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

        //PRICE
        JPanel pricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel priceLabel = new JLabel("Price:");
        updatePriceField = new JTextField(15);
        pricePanel.add(priceLabel);
        pricePanel.add(updatePriceField);
        formPanel.add(pricePanel);

        panel.add(formPanel, BorderLayout.CENTER);

        //Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        prevButton = new JButton("Prev");
        nextButton = new JButton("Next");
        saveButton = new JButton("Save");
        buttonsPanel.add(prevButton);
        buttonsPanel.add(nextButton);
        buttonsPanel.add(saveButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        //MSG PANEL
        JPanel messagePanel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("Messages:");
        updateMessageArea = new JTextArea(5, 30);
        updateMessageArea.setEditable(false);
        updateMessageArea.setLineWrap(true);
        updateMessageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(updateMessageArea);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(messageScrollPane, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.EAST);

        //Listener
        UpdatePanelListener updateListener = new UpdatePanelListener();
        prevButton.addActionListener(updateListener);
        nextButton.addActionListener(updateListener);
        saveButton.addActionListener(updateListener);

        return panel;
    }

    /**
     *Creates the total gain panel for displaying portfolio gains.
     *@return the total gain panel.
     */
    private JPanel createTotalGainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Total Gain");
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        panel.add(titleLabel, BorderLayout.NORTH);

        //TOP panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        JLabel totalGainLabel = new JLabel("Total gain:");
        JTextField totalGainField = new JTextField(10);
        totalGainField.setEditable(false);
        JButton calculateButton = new JButton("Calculate");
        topPanel.add(totalGainLabel);
        topPanel.add(totalGainField);
        topPanel.add(calculateButton);
        panel.add(topPanel, BorderLayout.NORTH);

        //Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel individualGainsLabel = new JLabel("Individual Gains:");
        JTextArea individualGainsArea = new JTextArea(5, 30);
        individualGainsArea.setEditable(false);
        individualGainsArea.setLineWrap(true);
        individualGainsArea.setWrapStyleWord(true);
        JScrollPane gainsScrollPane = new JScrollPane(individualGainsArea);
        bottomPanel.add(individualGainsLabel, BorderLayout.NORTH);
        bottomPanel.add(gainsScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.CENTER);

        //Listener
        TotalGainListenerButton gainButtonListener = new TotalGainListenerButton(totalGainField, individualGainsArea);
        calculateButton.addActionListener(gainButtonListener);

        return panel;
    }

    /**
     *Creates the search panel for searching investments.
     *@return the search panel.
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.CYAN);
        JLabel titleLabel = new JLabel("Searching Investments");
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        //SYMBOL
        JPanel symbolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel symbolLabel = new JLabel("Symbol:");
        JTextField symbolField = new JTextField(15);
        symbolPanel.add(symbolLabel);
        symbolPanel.add(symbolField);
        formPanel.add(symbolPanel);

        //KEYWORDS..
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nameLabel = new JLabel("Name Keywords:");
        JTextField nameField = new JTextField(15);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        formPanel.add(namePanel);

        //Low Price
        JPanel lowPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lowPriceLabel = new JLabel("Low Price:");
        JTextField lowPriceField = new JTextField(15);
        lowPricePanel.add(lowPriceLabel);
        lowPricePanel.add(lowPriceField);
        formPanel.add(lowPricePanel);

        //high Price
        JPanel highPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel highPriceLabel = new JLabel("High Price:");
        JTextField highPriceField = new JTextField(15);
        highPricePanel.add(highPriceLabel);
        highPricePanel.add(highPriceField);
        formPanel.add(highPricePanel);
        panel.add(formPanel, BorderLayout.CENTER);

        //Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton resetButton = new JButton("Reset");
        JButton searchButton = new JButton("Search");
        buttonsPanel.add(resetButton);
        buttonsPanel.add(searchButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        //Msgg Panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        JLabel resultLabel = new JLabel("Search Results:");
        JTextArea resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        messagePanel.add(resultLabel, BorderLayout.NORTH);
        messagePanel.add(resultScrollPane, BorderLayout.CENTER);
        panel.add(messagePanel, BorderLayout.EAST);

        // Listeners
        SearchPanelListener searchListener = new SearchPanelListener(
                symbolField, nameField, lowPriceField, highPriceField, resultArea);
        searchButton.addActionListener(searchListener);
        resetButton.addActionListener(searchListener);

        return panel;
    }

    /**
     *displays the investment details in the update panel.
     *@param symbolField the text field for the investment symbol.
     *@param nameField the text field for the investment name.
     *@param priceField the text field for the investment price.
     *@param index the index of the investment to display.
     *@throws Exception if the investment cannot be displayed.
     */
    private void displayInvestment(JTextField symbolField, JTextField nameField, JTextField priceField, int index) throws Exception {
        Investment investment = portfolio.getInvestments().get(index);
        symbolField.setText(investment.getSymbol());
        nameField.setText(investment.getName());
        priceField.setText(String.valueOf(investment.getPrice()));
    }

    /**
     *updates the nav buttons in the update panel based on the current index.
     */
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

    //LISTENERS -------------

    /**
     *Listener for the welcome menu option
     *Switches the current panel to the welcome panel and updates the current balance.
     */
    private class WelcomeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //update the current balance field before showing the panel**
            currentBalField.setText(String.format("%.2f", portfolio.getStartingBalance()));
            swapPanel(welcomePanel);
        }
    }

    /**
     *Listener for the buy menu option
     *Switches the current panel to the buy panel.
     */
    private class BuyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(buyPanel);
        }
    }

    /**
     *Listener for the sell menu option
     *Switches the current panel to the sell panel.
     */
    private class SellListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(sellPanel);
        }
    }

    /**
     *Listener for the update menu option
     *Switches the current panel to the update panel and initializes the first investment.
     */
    private class UpdateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!portfolio.getInvestments().isEmpty()) {
                currentIndex = 0;
                try {
                    displayInvestment(updateSymbolField, updateNameField, updatePriceField, currentIndex);
                    updateNavigationButtons();
                    updateMessageArea.setText("");
                    saveButton.setEnabled(true);
                } 
                catch (Exception ex) {
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

    /**
     *Listener for the total gain menu option.
     *Switches the current panel to the total gain pane.
     */
    private class TotalGainListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(totalGainPanel);
        }
    }

    /**
     *Listener for the search menu option.
     *Switches the current panel to the search panel.
     */
    private class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            swapPanel(searchPanel);
        }
    }

    /**
     *Listener for the quit menu option.
     *Saves the portfolio to a database and then exits the application.
     */
    private class QuitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //SAVE PORTFOLIO BEFORE QUITING
            try {
                portfolio.saveToDatabase("lib/portfolio.db");
                System.out.println("Portfolio saved on Quit menu item.");
            } 
            catch (Exception ex) {
                System.out.println("Error saving portfolio: " + ex.getMessage());
            }
            // Now exit
            System.exit(0);
        }
    }

    // LISTENER CLASSES FOR BUY, SELL, UPDATE, TOTAL GAIN, SEARCH PANELS =====

    /**
     *Listener for the Buy panel buttons.
     */
    private class BuyPanelListener implements ActionListener {
        private JComboBox<String> typeDropdown;
        private JTextField symbolField;
        private JTextField nameField;
        private JTextField quantityField;
        private JTextField priceField;
        private JTextArea messageArea;

        public BuyPanelListener(JComboBox<String> typeDropdown, JTextField symbolField,
                                JTextField nameField, JTextField quantityField,
                                JTextField priceField, JTextArea messageArea) {
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

                //checks if fields are empty
                if (symbol.isEmpty() || name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                    messageArea.setText("All fields must be filled out!");
                    return;
                }
                int quantity;
                double price;

                //check and verify quantity and price
                try {
                    quantity = Integer.parseInt(quantityText);
                    price = Double.parseDouble(priceText);
                    if (quantity <= 0 || price <= 0) {
                        throw new InputMismatchException();
                    }
                } 
                catch (Exception ex) {
                    messageArea.setText("Quantity and Price must be positive numbers!");
                    return;
                }

                try {
                    String result = portfolio.buy(type, symbol, name, quantity, price);
                    messageArea.setText(result);

                    // **Update the current balance field if the welcome panel is active**
                    if (mainPanel.getComponent(0) == welcomePanel) {
                        currentBalField.setText(String.format("%.2f", portfolio.getStartingBalance()));
                    }

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

    /**
     *Listener for the Sell panel buttons.
     */
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

                //checks if fields are empty
                if (symbol.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                    messageArea.setText("All fields must be filled out.");
                    return;
                }
                int quantity;
                double price;

                //validate thequantity and price
                try {
                    quantity = Integer.parseInt(quantityText);
                    price = Double.parseDouble(priceText);
                    if (quantity <= 0 || price<= 0) {
                        throw new InputMismatchException();
                    }
                } 
                catch (Exception ex) {
                    messageArea.setText("Quantity and Price must be positive numbers.");
                    return;
                }

                try {
                    String result = portfolio.sell(symbol, quantity, price);
                    messageArea.setText(result);

                    //update the cur balance field if the welcome panel is active
                    if (mainPanel.getComponent(0) == welcomePanel) {
                        currentBalField.setText(String.format("%.2f", portfolio.getStartingBalance()));
                    }

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
     *Listener for the Update panel navigation and actions.
     *Handles "Prev", "Next", and "Save" commands for navigating and updating investments.
     */
    private class UpdatePanelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
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
            else if (command.equals("Next")) {
                if (!portfolio.getInvestments().isEmpty() && currentIndex < portfolio.getInvestments().size()-1) {
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
                catch (Exception ex) {
                    updateMessageArea.setText("Price must be a positive number.");
                    return;
                }

                try {
                    String result = portfolio.updatePrice(currentIndex, newPrice);
                    updateMessageArea.setText(result);
                    displayInvestment(updateSymbolField, updateNameField, updatePriceField, currentIndex);

                    portfolio.saveToDatabase("lib/portfolio.db");

                    //update the cur balance field if the welcome panel is active
                    if (mainPanel.getComponent(0) == welcomePanel) {
                        currentBalField.setText(String.format("%.2f", portfolio.getStartingBalance()));
                    }

                } 
                catch (Exception ex) {
                    updateMessageArea.setText(ex.getMessage());
                }
            }
        }
    }

    /**
     *Listener for the Total Gain screen button.
     *Calculates and displays the total gain and individual gains of the portfolio.
     */
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

            //SAVES the updated gains to DB
            try {
                portfolio.saveToDatabase("lib/portfolio.db");
            } 
            catch (Exception ex) {
                System.out.println("Error saving portfolio after calculating gains: " + ex.getMessage());
            }
        }
    }

    /**
     *Listener for the Search panel.
     *Handles search and reset operations for investments based on user input.
     */
    private class SearchPanelListener implements ActionListener {
        private JTextField symbolField;
        private JTextField nameField;
        private JTextField lowPriceField;
        private JTextField highPriceField;
        private JTextArea resultArea;

        public SearchPanelListener(JTextField symbolField, JTextField nameField,
                                   JTextField lowPriceField, JTextField highPriceField,
                                   JTextArea resultArea) {
            this.symbolField = symbolField;
            this.nameField = nameField;
            this.lowPriceField = lowPriceField;
            this.highPriceField = highPriceField;
            this.resultArea = resultArea;
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Search")) {
                String symbol = symbolField.getText().trim();
                String keywords = nameField.getText().trim();
                String lowPriceText = lowPriceField.getText().trim();
                String highPriceText = highPriceField.getText().trim();
                double lowPrice = -1;
                double highPrice = -1;

                //parsin price riange.
                try {
                    if (!lowPriceText.isEmpty()) {
                        lowPrice = Double.parseDouble(lowPriceText);
                        if (lowPrice < 0) throw new InputMismatchException();
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

                //optionally save to DB
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
