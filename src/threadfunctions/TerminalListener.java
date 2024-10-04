package src.threadfunctions;

import java.sql.SQLException;
import java.util.Scanner;
import src.commands.CommandCutter;
import src.commands.CommandFactory;
import src.commands.ICommand;
import src.config.ScanInput;
import src.logger.MyLogger;
import src.sqlfunctions.SqlTable;


public class TerminalListener {
    
    public static void ListenTerminal(){
        boolean continueLoop = true;
        Scanner scanInput = ScanInput.getScanInput();

        while(continueLoop){

            System.out.println("");
            System.out.println("< ");

            String request = scanInput.nextLine();


            if(request.equals("exit") || request.equals("quit")){
                continueLoop = false;
                ScanInput.closeScanner();
                System.out.println("Exit the program");
                System.exit(0); 
        
            }
            else if(request.equals("clean")){
                try{
                    SqlTable.cleanDatabase();
                }catch(SQLException e){
                    System.err.println(e.getMessage());
                }
            }
            else{
                try{
                    CommandCutter commandCutted = new CommandCutter(request); //we are cutting the command in differents parts to analyse it better

                    ICommand command = CommandFactory.createCommand(commandCutted); //it gaves us the good type of command, we just need to execute it

                    command.execute();

                    //System.out.println("Here are the created object names: "+command.getCreatedNames());
                    MyLogger.writeLog("Here are the created object names: "+command.getCreatedNames());


                } catch (Exception e){
                    System.err.println(e);
                }
            }





        }

    }


}
