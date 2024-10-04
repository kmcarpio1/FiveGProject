package src;

import src.server.FileServer;
import src.server.TestServer;
import src.threadfunctions.TerminalListener;
import src.threadfunctions.threadManager;

public class Demo3Server{
    
    public static void main(String[] args){


        //DO THE LOOP HERE
        System.out.println("");
        System.out.println("");
        System.out.println("Welcome into the DEMO3SERVER function.");
        System.out.println("");

        try{
            threadManager.initialize();


            threadManager.executeTask(() -> TerminalListener.ListenTerminal()); //to be able to exit properly

            
            threadManager.executeTask(() -> FileServer.start()); //now stuff are going to be written in the logfile
            threadManager.executeTask(() -> TestServer.start()); //now stuff are going to be written in the logfile



        }catch(Exception e){
            System.out.println(e);
        }

        


    }



}
