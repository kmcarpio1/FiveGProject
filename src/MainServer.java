package src;

import src.server.FileServer;
import src.server.TestServer;
import src.threadfunctions.TerminalListener;
import src.threadfunctions.threadManager;

public class MainServer{
    
    public static void main(String[] args){


        //DO THE LOOP HERE
        System.out.println("");
        System.out.println("");
        System.out.println("Welcome into the Main function for the server.");
        System.out.println("");

        try{
            threadManager.initialize();

            // the server is open for the test connection
            threadManager.executeTask(() -> TestServer.start()); 

            // the server is oppen to receive the packages
            threadManager.executeTask(() -> FileServer.start()); 


            //we open the terminal listening, to exit the programm or enter commands 
            threadManager.executeTask(() -> TerminalListener.ListenTerminal()); //to be able to exit properly


        }catch(Exception e){
            System.out.println(e);
        }

    

    }



}
