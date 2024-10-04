package src;

import src.commands.CommandCutter;
import src.commands.CommandFactory;
import src.commands.ICommand;  // Import the Scanner class
import src.config.Configurations;
import src.config.LogFileManager;
import src.config.ScanInput;
import src.logger.MyLogger;
import src.threadfunctions.TerminalListener;
import src.threadfunctions.threadManager;

public class MainMachineAutomatic{
    
    public static void main(String[] args){


        //DO THE LOOP HERE
        System.out.println("");
        System.out.println("");
        System.out.println("Welcome into the Main function for the watching machines, in the automatic mode.");
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

            
            while(true){
                Thread.sleep(10000);

                if(Configurations.checkingConnection()){//the connection has been a succeed
                    //we save and pass the informations collected

                    if(!Configurations.getIfTheConnectionIsUp()){
                        Configurations.setIfTheConnectionIsUp(true);
                        System.out.println("Initializing regular mode");
                    
                    
                    }

                    // We are going to save the logHashTable and clean them, since those information are now obselete. Indeed, the connection is up, so it's no use to keep them, since they are going to be sent on the center machine.
                    CommandCutter commandCutted3 = new CommandCutter("save all clean_on_save"); //we are cutting the command in differents parts to analyse it better
                    ICommand command3 = CommandFactory.createCommand(commandCutted3); //it gaves us the good type of command, we just need to execute it
                    
                    CommandCutter commandCutted4 = new CommandCutter("pass " + Configurations.getServerIP() +" all"); //we are passing the saved logHashTables to the server
                    ICommand command4 = CommandFactory.createCommand(commandCutted4); //it gaves us the good type of command, we just need to execute it
                    

                    command3.execute(); //now the logFile is watched
                    MyLogger.writeLog("Here are the created object names: "+command3.getCreatedNames());

                    //System.out.println("Here are the created object names: "+command3.getCreatedNames());
                    //System.out.println("\n");

                    command4.execute(); //now the logFile is watched
                    MyLogger.writeLog("Here are the created object names: "+command4.getCreatedNames());
                    
                    //System.out.println("Here are the created object names: "+command4.getCreatedNames());
                    //System.out.println("\n");
                }
                else{ //the connection is impossible, we are stocking the informations in save 
                    
                    if(Configurations.getIfTheConnectionIsUp()){
                        Configurations.setIfTheConnectionIsUp(false);
                        System.out.println("Initializing backup mode");
                    }

                    // here, on another hand, we need to save the logHashTables WITHOUT cleaning them. Indeed, we would lose informations if we do so, since we are not sending them on the center machine (bacause the connection is down)
                    CommandCutter commandCutted3 = new CommandCutter("save all"); //we are cutting the command in differents parts to analyse it better
                    ICommand command3 = CommandFactory.createCommand(commandCutted3); //it gaves us the good type of command, we just need to execute it
                    

                    command3.execute(); //now the logFile is watched
                    MyLogger.writeLog("Here are the created object names: "+command3.getCreatedNames());
                    
                    //System.out.println("Here are the created object names: "+command3.getCreatedNames());
                    //System.out.println("\n");

                    //Thread.sleep(50000); //we sleep longer is the connection is impossible
                }
            }



        }catch(Exception e){
            System.out.println(e);
        }

        


    }



}