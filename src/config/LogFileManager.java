package src.config;

import java.util.Scanner;
import src.commands.CommandCutter;
import src.commands.CommandFactory;
import src.commands.ICommand;
import src.hashfunctions.HashList;
import src.logger.MyLogger;


public class LogFileManager {
    


    public static void watchFile(String path) throws Exception{
        CommandCutter commandCutted = new CommandCutter("watch logFile "+path); //we are watching the differents logfiles in differents loghashtables
        ICommand command = CommandFactory.createCommand(commandCutted); //it gaves us the good type of command, we just need to execute it
        command.execute(); //now the logFile is watched
        //System.out.println("Here are the created object names: "+command.getCreatedNames());
        MyLogger.writeLog("Here are the created object names: "+command.getCreatedNames());
        for(String createdName : command.getCreatedNames()){
            System.out.println(HashList.getLogHashTable(createdName).getLogPaths() + " is now watched.");
            MyLogger.writeLog(createdName + " is now watching "+HashList.getLogHashTable(createdName).getLogPaths());
        }
        System.out.println("\n");
    }



    public static void askLogPaths(){
        Scanner scanInput = ScanInput.getScanInput();
        boolean continueLoop = true;
        boolean atLeastOne = false;

        while(continueLoop){
            try{
                System.out.println("Enter a logFile path, a logDirectory path, or done.");

                String request = scanInput.nextLine();


                switch(request){
                    case "exit" :
                    case "quit":
                        continueLoop = false;
                        ScanInput.closeScanner();
                        System.out.println("Exit the program.");
                        System.exit(0);
                        break;//stopping the switch
                
                    case "done":
                        if(!atLeastOne) {
                            System.out.println("You didn't entered one valid path.");
                            System.out.println("Please try again.");
                        }
                        else continueLoop = false;
                        break;//stopping the switch

                    default:
                        watchFile(request);
                        atLeastOne = true;
                        break;//stopping the switch

                }
            }catch (Exception e){
                System.out.println("There was a problem with the path you entered: "+e.getMessage()+".");
                System.out.println("Try again.");
                System.out.println("\n.");
                
            }
        }
                   
    }

}

/*
 *          File directory = new File(FILES_SAVE_DIR);
            File[] fileList = directory.listFiles();
            if(fileList != null){
                for(File file : fileList){ //for every save files
                    if(file.isFile()){
                        String name = file.getName();
                        String filePath = FILES_SAVE_DIR + name;
                        if (file.exists()) {
                            try { //we do that so every file is checked, even if it's not a logHashTable
                                watchAFileForSaved(name);
                                
                            } catch (Exception e) {
                                System.err.println("A problem occured for "+filePath);
                            }
                        } else {
                            System.err.println("File does not exist: " + filePath);
                        }
                    }


                        //private static List<String> _logPaths  = new ArrayList<>(); 
//
//
    //public static void addLogPath(String logPath){
    //    _logPaths.add(logPath);
    //}

    //public static List<String> getLogPaths(){
    //    return _logPaths;
    //}

    public static boolean checkIfFile(File file){
        return file.isFile();
    }

    public static boolean checkIfDirectory(File file){
        return file.isDirectory();
    }
     
 *///    public static void directoryManager(File directory){
//        File[] fileList = directory.listFiles();
//        if(fileList != null){
//            for(File file : fileList){ //for every files
//                if(file.isFile()){
//                    String filePath = file.getAbsolutePath();
//                    if (file.exists()) {
//                        try{
//                            //addLogPath(filePath);
//                            watchFile(filePath);
//                            //System.out.println(filePath +" added to the watched logPaths.");
//                        }
//                    }
//                }
//            }
//        }
//        else System.out.println("Directory empty.");
//    }

 //   public static void findAName(String path){
 //   
 //       File file = new File(path);
 //       if(checkIfDirectory(file)){
 //           directoryManager(file);
 //       }
 //       else if(checkIfFile(file)){
//
 //       }
 //       else System.out.println("File not found.");
//
 //   }
//
//