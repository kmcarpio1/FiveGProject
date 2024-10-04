package src;

import src.config.Configurations;
import src.config.LogFileManager;
import src.config.ScanInput;  // Import the Scanner class
import src.threadfunctions.TerminalListener;
import src.threadfunctions.threadManager;

public class MainMachineManual{
    
    public static void main(String[] args){


        //DO THE LOOP HERE
        System.out.println("");
        System.out.println("");
        System.out.println("Welcome into the Main function for the watching machines, in the manual mode.");
        System.out.println("");

        
        try{
            threadManager.initialize();

            ScanInput.setScanInput();

            //we are trying to find the IP adress of the machine. If it doesn't work, we ask the user
            Configurations.findOwnIp(); //to be abble to get the adressIP of the machine

            //we are asking the user to give us the center machine IP 
            Configurations.findServerIp(); //to be abble to get the adressIP of the server

            //we ask the user for the differents path that will be watched
            LogFileManager.askLogPaths();
            

            //we open the terminal listening, to exit the programm
            threadManager.executeTask(() -> TerminalListener.ListenTerminal()); //to be able to exit properly

            


        }catch(Exception e){
            System.out.println(e);
        }

        


    }



}