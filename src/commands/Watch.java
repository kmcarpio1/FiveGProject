package src.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import src.config.PathToFile;
import src.hashfunctions.HashList;
import src.hashfunctions.HashWatched;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;
import src.threadfunctions.logFileListener;
import src.threadfunctions.threadManager;

/**
 * 
*/
public class Watch extends  ICommand {
    
     /*
     * In the command we will have a 3 or 4 arguments string
     *
     * - "listed", then either "all" or the name of a listed logHashTable, or the path to the only logFile that the logHashTable is linked and add it/them into HashWatched (meaning we watch to watch this/these logHashTable(s)) 
     * - "saved", then either "all" or the name of a save logHashTable file, meaning we want to laod it/them, add them into hashList and HashWtached (meaning we watch to watch this/these logHashTable(s)) 
     * - "logFile"then either a path to a directory or a logFile, meaning we want te create a/multiple logHashTable each linked to one of the logFile, add them to hashList and to HashWatched (meaning we watch to watch this/these logFile(s)) 
     *      if it's a directory, it will create as much logHashTable as the number of file in the directory, except if we add "mixed" after
    */


    /**
     * Watch will also do like load in addition if it's specified in the command. If we want to watch a saved LogHashFile, it will load it, add it in HashList, and then proceed to do the regular Watch behavior
    */

    private static final String FILES_SAVE_DIR = PathToFile.getPathSave();//path to the save file directory
    private String _name; //the third word of the command (either "all", or the path to a logFile, or the name of the LogHashTable or to a saved LogHashTable)
    private String _type; //the second word of the command (it should either be saved or listed)
    private boolean _mixed = false;
    private LogHashTable _logHashTable = null; //the stored logHashTable, if the second argument is saved, or if the third is all or if it's an incorrect command it stays null

    /**
     * Constructor of the watch command instance.
     * It checks if there are the right amount of element, and if they are valid (if the second is either "listed", "saved" or "logFile", and if the third is accepted)
     * You can find the acceptance at the beginning of the class
     * @param command
     * @throws Exception
     */
    Watch(CommandCutter command) throws Exception{
        if(command.size() != 3){
            if(command.size() != 4 || !command.getArg(3).equals("mixed") || !command.getArg(1).equals("logFile") ){
                throw new Exception("Wrong command: wrong number of arguments");
            }
            else _mixed = true;
        }

        _name = command.getArg(2);
        _type = command.getArg(1);

        if(_type.equals("listed")){ //the command is about listed logHashTable
            if(!_name.equals("all")){ //if it's listed and not "all", the name is either a logHashTable name listed in the hashTableList or a logFile path
                Hashtable<String,LogHashTable> hashList = HashList.getHashList();
                if(!hashList.containsKey(_name)){ //we are looking if it's a logHashTable name listed in hashList, if not it could be a logFile path
                    String name = HashList.findNameWithLogPath(_name); //we check if there is a logHashTable with the coresponding logFile path
                    if(name == null){ //if not, wrong argument
                        throw new Exception("Wrong command: wrong second argument");
                    }
                    else{ //else, good argument, we change _name with the logHashTable name, and store the logHashTable
                        _name = name;
                        _logHashTable = hashList.get(_name);
                    }
                }
                else{ //else, good argument, we change _name with the logHashTable name, and store the logHashTable
                    _logHashTable = hashList.get(_name);
                }
            }
        }


        else if(_type.equals("saved")){ //the command is about saved logHashTable
            String path = FILES_SAVE_DIR + _name;
            if(!_name.equals("all")){ //if it's saved and not "all" we just need to check if there is a saved file name with _name
                File file = new File(path);
                if(!file.exists()){
                    throw new Exception("Wrong command: wrong second argument");
                }
                _name = file.getName();
            }
        }

        else if(_type.equals("logFile")){ //the command is about a logFile or a logDirectory
            File file = new File(_name);//here the _name should be the path to the directory or the logfile
            if (file.exists()) {
                if (!file.isDirectory() && !file.isFile()) { //we check if it is either not a file or a directory
                    throw new Exception("Wrong command: wrong second argument");
                }
            } else {
                throw new Exception("Wrong command: wrong second argument");
            }
        }


        else{
            throw new Exception("Wrong command: wrong first argument");
        }


    }






    /**
     * @return the name of the second word of the command
     */    
    @Override
    public String getArgument() {
        return _name;
    }


    /**
     * Method that launch the loop and give it to a thread to watch a logFile
     * @param logPath
     * @param logHashTable
     * @throws Exception
     */
    private void launchLoop(String logPath, LogHashTable logHashTable) throws Exception{
        if (!HashWatched.containsKey(logPath)) { //if it's not in hashWatch yet, it means that there is no thread executing the logFile listening
            HashWatched.addLogHashTable(logPath, logHashTable); //for each logPath linked to logHashTable we add logHashTable in the HashWatched
            threadManager.executeTask(()->logFileListener.listen(logPath));
        } else {
            // if it's already inside, it means that logFile is already watched. We just have to update the logHashTable list
            HashWatched.addLogHashTable(logPath, logHashTable); //for each logPath linked to logHashTable we add logHashTable in the HashWatched
        }
    }

    /**
     * method to avoid a repetition that prepare the call of launchLoop for the execution of the watch command with saved logHashTable files
     * @param name
     * @throws Exception
     */
    private void watchAFileForSaved(String name) throws Exception{
        LogHashTable logHashTable = SaveLogHashTable.load(name);
        List<String> logPaths = logHashTable.getLogPaths();
        if(logPaths!=null){
            for(String logPath : logPaths){
                launchLoop(logPath,logHashTable);//for each logPath linked to logHashTable we add logHashTable in the HashWatched
            }
            addCreatedName(logHashTable.getLogHashTableName());
        }
        else{
            throw new Exception("Wrong command: logHashTable linked to no logPath");
        }
    }


    /**
     * Execute the command by loading the saved logHashTable(s),  and adding it/them to hashList and hashWatched.
     * If it's not possible (for example if the saved file has been deleted in the meantime), it throws an error
     */
    private void executeSaved() throws Exception{
        if(_name.equals("all")){ //if it's all, we have to look every saved files, load them, and write them to CSV
            File directory = new File(FILES_SAVE_DIR);
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
                }
            }
        }
        else{ //if it's not "all", it's supposed to be a specific saved file
            String path = FILES_SAVE_DIR + _name;
            File file = new File(path);
            if (file.exists()) { // we check if the saved logHashTable still exists
                watchAFileForSaved(_name);
            } else {
                throw new Exception("Wrong command: File doesn't exist anymore");
            }
        }
    }

    /**
     * This method is meant to be called if _name is the path to a directory. In that case, it'll check if it ends with a /, and will add it if not
     */
    private void checkAndAddSlash(){
        if(_name.charAt(_name.length() - 1)!='/') _name += "/";
    }


    /**
     * method to avoid a repetition that prepare the call of launchLoop for the execution of the watch command with logFiles (not if mixed)
     * @param logPath
     * @throws Exception
     */
    private void watchAFileForLogFile(String logPath) throws Exception{
        LogHashTable logHashTable = new LogHashTable(logPath); //a new logHashTable for this logPath
        HashList.addLogHashTable(logHashTable.getLogHashTableName(), logHashTable); //we add this new logHashTable to hashList
        launchLoop(logPath,logHashTable);
        addCreatedName(logHashTable.getLogHashTableName());
    }





    /**
     * Execute the command by creating logHashTable for the LogFile(s), and adding it/them to hashList and hashWatched.
     * If it's not possible (for example if the saved file has been deleted in the meantime), it throws an error
     */
    private void executeLogFile() throws Exception{

        File file = new File(_name);//here the _name should be the path to the directory or the logfile
        if (file.exists()) {
            
            
            if (file.isDirectory()) { //if it's a directory, it's kinda a "all", we will create a logHashTable for every file inside

                checkAndAddSlash();

                File[] fileList = file.listFiles(); 
                if(fileList != null){

                    if(_mixed){//if we want all the logFile to be linked to one logHashTable
                        List<String> logPaths = new ArrayList<>();
                        for(File logFile : fileList){ //for every files
                            if(logFile.isFile()){
                                String logFileName = logFile.getName();
                                if (logFile.exists()) {
                                    logPaths.add(_name+logFileName);
                                }
                            }    
                        }
                        LogHashTable logHashTable = new LogHashTable(logPaths);
                        HashList.addLogHashTable(logHashTable.getLogHashTableName(), logHashTable); //we add this new logHashTable to hashList
                        addCreatedName(logHashTable.getLogHashTableName());

                        for(String logPath : logPaths){ //now for each logPath we have to add the logHashTable into the HashWatched list
                            launchLoop(logPath,logHashTable);
                        }
                    }

                    else{ //each logFile is linked to a logHashTable
                        for(File logFile : fileList){ //for every files
                            if(logFile.isFile()){
                                String logFileName = logFile.getName();
                                if (logFile.exists()) {
                                    String logPath =_name+logFileName;
                                    watchAFileForLogFile(logPath);
                                }
                            }    
                        }
                    }
                } 
            }
            else if (file.isFile()) {//_name is the path to the logFile
                watchAFileForLogFile(_name);            
            }  
            else{
                throw new Exception("Wrong command: path " + _name+" isn't either a file or a directory");
            }
        } else {
            throw new Exception("Wrong command: path " + _name +" soes not exist anymore");
        }        

    
    }
    


    
    /**
     * method to avoid a repetition that prepare the call of launchLoop for the execution of the watch command with listed logHashTable 
     * @param logHashTable
     * @throws Exception
     */
    private void watchAFileForListed(LogHashTable logHashTable) throws Exception{
        List<String> logPaths = logHashTable.getLogPaths();
        if(logPaths!=null){
            for(String logPath : logPaths){
                launchLoop(logPath,logHashTable);//for each logPath linked to logHashTable we add logHashTable in the HashWatched

            }
            addCreatedName(logHashTable.getLogHashTableName());
        }
        else{
            throw new Exception("Wrong command: logHashTable linked to no logPath");
        }
    }



    /**
     * Execute the command by adding to hashWatched the listed logHashTable(s) in the hashList.
     */
    private void executeListed() throws Exception{
        if(_name.equals("all")){ //if it's all we just have to go threw all the hashList elements
            Hashtable<String,LogHashTable> hashList = HashList.getHashList();
            Enumeration<LogHashTable> values = hashList.elements();
            while (values.hasMoreElements()){ //while there are still elements in HashList
                LogHashTable logHashTable =  values.nextElement(); 
                try {
                    watchAFileForListed(logHashTable);
                } catch (Exception e) {
                    System.err.println("A problem occured for "+logHashTable.getLogHashTableName());
                }
            }
        
        }
        
        else{ //if it's not "all", it's the name of a logHashTable
            if (_logHashTable!=null) { 
                watchAFileForListed(_logHashTable);
            } else {
                throw new Exception("Wrong command: logHashTable doesn't exist");
            }
        }
    }
    
    /**
     * Execute the command by converting and writting ti CSV the saved or listed logHashTable.
     * If it's not possible (for example if the saved file has been deleted in the meantime), it throws an error
     */
    @Override
    public void execute() throws Exception{
        switch (_type) {
            case "listed":
                executeListed();
                break;
            case "saved":
                executeSaved();
                break;
            case "logFile":
                executeLogFile();
                break;
            default:
                throw new Exception("Wrong command: wrong first argument"); //it shouldn't happen
        }
    }







}
