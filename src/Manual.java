package src;

import src.threadfunctions.TerminalListener;
import src.threadfunctions.threadManager;

public class Manual{
    
    public static void main(String[] args){


        //DO THE LOOP HERE
        System.out.println("");
        System.out.println("");
        System.out.println("Welcome into the Manual function. It is meant to test the commands.");
        System.out.println("");

        try{
            threadManager.initialize();

            //we open the terminal listening, to exit the programm or enter commands 
            threadManager.executeTask(() -> TerminalListener.ListenTerminal()); //to be able to exit properly


        }catch(Exception e){
            System.out.println(e);
        }

    

    }



}
