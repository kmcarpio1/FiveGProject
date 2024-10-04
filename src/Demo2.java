package src;

import src.commands.CommandCutter;
import src.commands.CommandFactory;  // Import the Scanner class
import src.commands.ICommand;
import src.hashfunctions.LogHashTable;
import src.threadfunctions.TerminalListener;
import src.threadfunctions.logFileFillerDemo;
import src.threadfunctions.threadManager;

public class Demo2{
    
    public static void main(String[] args){

        LogHashTable logHashTable = new LogHashTable();

        //DO THE LOOP HERE

        System.out.println("Welcome into the DEMO1 function.");
        try{
            threadManager.initialize();
            String logDir = "logfile";
            String logFilePath1 = "logfile/logfile.log";
            String logFilePath2 = "logfile/logfile_other.log";

            threadManager.executeTask(() -> TerminalListener.ListenTerminal()); //to be able to exit properly

            
            CommandCutter commandCutted = new CommandCutter("watch logFile "+logDir+" mixed"); //we are cutting the command in differents parts to analyse it better
            ICommand command = CommandFactory.createCommand(commandCutted); //it gaves us the good type of command, we just need to execute it
            command.execute(); //now the logFile is watched
            System.out.println("Here are the created object names: "+command.getCreatedNames());
            System.out.println("\n");

            threadManager.executeTask(() -> logFileFillerDemo.filler(logFilePath1)); //now stuff are going to be written in the logfile
            threadManager.executeTask(() -> logFileFillerDemo.filler_other(logFilePath2)); //now stuff are going to be written in the logfile


            
            while(true){
                Thread.sleep(10000);

                CommandCutter commandCutted2 = new CommandCutter("write listed all"); //we are cutting the command in differents parts to analyse it better
                ICommand command2 = CommandFactory.createCommand(commandCutted2); //it gaves us the good type of command, we just need to execute it
    
                CommandCutter commandCutted3 = new CommandCutter("send all"); //we are cutting the command in differents parts to analyse it better
                ICommand command3 = CommandFactory.createCommand(commandCutted3); //it gaves us the good type of command, we just need to execute it
    

                command2.execute(); //now the logFile is watched
                System.out.println("Here are the created object names: "+command2.getCreatedNames());
                System.out.println("\n");

                command3.execute(); //now the logFile is watched
                System.out.println("Here are the created object names: "+command3.getCreatedNames());
                System.out.println("\n");

            }



        }catch(Exception e){
            System.out.println(e);
        }

        


    }



}