package src.commands;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import src.config.PathToFile;
import src.hashfunctions.HashList;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;

/**
 * The "write" command name class
 * Write is supposed to convert a logHashTable into CSV and write it into a file to be able to read the information.
 * The logHashTable can either be saved, or be listed into the HasList.
 */
public class Write extends  ICommand {
        /*
     * In the command we will have a string
     * It's either listed or saved (were either working with listed logHashTable or saved one)
     * then : 
     * if listed :
     * it's either the path to the  LogFile, in that case we write the loghashtable watching it (if it exists an he is currently watched (in hashwatched))
     * Or either the name of a logHashTable, in that case we write it
     * or "all" and it's writting all the logHashtable listed
     * if saved :
     * either "all", and it's writting all the logHashTable saved
     * or the name of a saved file, in that case we load it and write it

     * @param command
    */

    private static final String FILES_SAVE_DIR = PathToFile.getPathSave();//path to the save file directory
    private static final String FILES_WRITE_DIR = PathToFile.getPathWrite();//path to the write file directory
    private String _name; //the third word of the command (either "all", or the path to a logFile, or the name of the LogHashTable or to a saved LogHashTable)
    private String _type; //the second word of the command (it should either be saved or listed)
    private LogHashTable _logHashTable = null; //the stored logHashTable, if the second argument is saved, or if the third is all or if it's an incorrect command it stays null

    /**
     * Constructor of the write command instance.
     * It checks if there are the right amount of element, and if they are valid (if the second is either "listed" or "saved", and if the third is accepted)
     * You can find the acceptance at the beginning of the class
     * @param command
     * @throws Exception
     */
    Write(CommandCutter command) throws Exception{
        if(command.size() != 3) throw new Exception("Wrong command: wrong number of arguments");

        _name = command.getArg(2);
        _type = command.getArg(1);
        String path = FILES_SAVE_DIR + _name;

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
            if(!_name.equals("all")){ //if it's saved and not "all" we just need to check if there is a saved file name with _name
                File file = new File(path);
                if(!file.exists()){
                    throw new Exception("Wrong command: wrong second argument");
                }
                _name = file.getName();
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
     * Execute the command by loading the saved logHashTable(s), and convert it/them to CSV and writting it/them in the hashList.
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
                        //File logHashTableFile = new File(filePath);
                        if (file.exists()) {
                            try { //we do that so every file is checked, even if it's not a logHashTable
                                File writtenFile = SaveLogHashTable.writeToCSV(SaveLogHashTable.load(name));
                                addCreatedName(writtenFile.getName());
                            } catch (Exception e) {
                                System.err.println(filePath + " is not a saved LogHashTable: " + e.getMessage());
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
                File writtenFile = SaveLogHashTable.writeToCSV(SaveLogHashTable.load(_name)); //we load and write to CSV the corresponding logHashTable
                addCreatedName(writtenFile.getName());
            } else {
                throw new Exception("Wrong command: File doesn't exist anymore");
            }
        }
    }

    



    /**
     * Execute the command by converting and writting to CSV the listed logHashTable in the hashList.
     */
    private void executeListed() throws Exception{
        if(_name.equals("all")){ //if it's all we just have to go threw all the hashList elements
            File directory = new File(FILES_WRITE_DIR);
            if(directory.exists()){
                Hashtable<String,LogHashTable> hashList = HashList.getHashList();
                Enumeration<LogHashTable> values = hashList.elements();
                while (values.hasMoreElements()){ //while there are still elements in HashList
                    LogHashTable logHashTable =  values.nextElement(); 
                    File writtenFile = SaveLogHashTable.writeToCSV(logHashTable);      //write it to CSV in the write directory
                    addCreatedName(writtenFile.getName());
                }
             }
        }
        
        else{ //if it's not "all", it's the name of a logHashTable
            if (_logHashTable!=null) { 
                File writtenFile = SaveLogHashTable.writeToCSV(_logHashTable); //we write to CSV the logHashTable that we got in the constructor
                addCreatedName(writtenFile.getName());
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
            default:
                throw new Exception("Wrong command: wrong first argument"); //it shouldn't happen
        }
    }
}



