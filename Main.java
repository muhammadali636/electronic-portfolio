package Simutrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main class for running ePortfolio. Main operations: to buy, sell, update investments, calculate gains, search, and quit.
 */
public class Main {

    /**
     * The main method of the ePortfolio. Initializes portfolio and enters a command loop, where the user can pick which options to pick like 
     * buying, selling, updating, calculating gains, and searching.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide a filename as a command-line argument.");
            return;
        }

        String filename = args[0]; //commandline argument for filename
        Portfolio portfolio = new Portfolio(); //create portfolio object

        // Load investments from the specified file
        portfolio.loadFromFile(filename);

        Scanner keyboard = new Scanner(System.in);
        String operator = "";
        
        //command loop 
        while (true) {
            //options.
            System.out.println("Type an operator:");
            System.out.println("buy/b"); 
            System.out.println("sell (no shorthand)");
            System.out.println("update/u");
            System.out.println("getGain/g");
            System.out.println("search (no shorthand)");
            System.out.println("quit/q"); 
            operator = keyboard.nextLine();
            
            //choose the operation
            switch (operator.toLowerCase()) {
                //buying.
                case "buy":
                case "b":
                    portfolio.buy(keyboard);
                    break;

                //sell.
                case "sell":
                    portfolio.sell(keyboard);
                    break;

                //update..
                case "update":
                case "u":
                    portfolio.update(keyboard);
                    break;

                //getgain
                case "getgain":
                case "g":
                    portfolio.getGain();
                    break;

                //search
                case "search":
                    portfolio.search(keyboard);
                    break;

                //sell
                case "s":
                    System.out.println("Please do not use shorthand for search or sell. Enter either search or sell");
                    break;

                case "q":
                case "quit":
                    keyboard.close(); //close scanner
                    //save updated portfilo to file. 
                    portfolio.saveToFile(filename);
                    System.out.println("Portfolio saved to " + filename + ". Exiting program.");
                    return;

                default:
                    System.out.println("Invalid operator try again with a valid operator!!!");
            }
        }
    }
}