package src.commands;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import src.config.PathToFile;
import src.hashfunctions.HashList;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;

/**
 * The "save" command name class
 * save is supposed to save a LogHashTable
 */
public class Save extends  ICommand {
        /*
     * In the command we will have a string of 2 or three words
     * It's either "all", meaning we want to save all the logHashTable files in the hashList
     * Or either the path to the  LogFile, in that case we save the loghashtable watching it (if it exists an he is currently watched (in hashwatched))
     * Or even the name of a logHashTable, in that case we save it
     * 
     * If there is a third word, it should be "clean_on_save" and it will clean the logHashTables when they are saved
    */

    private static final String FILES_SAVE_DIR = PathToFile.getPathSave(); //path to the saved logHashTable directory
    private String _name; //second word of the command
    private LogHashTable _logHashTable = null; //the logHashTable to save (if there is only one, stay null if it's "all", or if it's not correct)
    private boolean _clean_on_save = false;
    /**
     * Constructor of the save command instance.
     * It checks if there are the right amount of element, and if they are valid
     * You can find the acceptance at the beginning of the class
     * @param command
     * @throws Exception
     */   
    Save(CommandCutter command) throws Exception{

        
        if(command.size() != 2) {
            if(command.size() == 3){
                if(command.getArg(2).equals("clean_on_save")) _clean_on_save = true;
                else throw new Exception("Wrong command: wrong second argument");
            }
            else throw new Exception("Wrong command: wrong number of arguments");
        }

        _name = command.getArg(1);

        if(!_name.equals("all")){ //if it's not "all"
            Hashtable<String,LogHashTable> hashList = HashList.getHashList(); //we get hashList to check if the logHashTable's name is inside
            if(!hashList.containsKey(_name)){ //if hashList does not contain _name, the only other correct possibility is that _name is a logPath
                String name = HashList.findNameWithLogPath(_name); //we search if there is a logHashTable in HashList that is only watching _name (it would mean that _name is a logPath)
                if(name == null){ //if not, throw exception
                    throw new Exception("Wrong command: wrong argument");
                }
                else{ //if it is, then we callect the name of the logHashTable and store it in _name, and we get the corresponding logHashTable
                    _name = name;
                    _logHashTable = hashList.get(_name);
                }
            }
            else { // if _name was indeed a logHashTable names listed in HashList, we just have to collect and store the logHashTable
                _logHashTable = hashList.get(_name); 
            }
        }
    }

    /**
     * @return the name of the second word of the command
     */
    @Override
    public String getArgument() {
        return _name;
    }


    private void saveAFile(LogHashTable logHashTable) throws Exception{
        if(!logHashTable.isEmpty()){
            File savedFile = SaveLogHashTable.save(logHashTable);        // we save it in the directory

            if(_clean_on_save) logHashTable.clean(); //we clean the loghashtable if needed

            addCreatedName(savedFile.getName());//we add the name
        }
    }

    /**
     * Execute the command by loading the saved file(s), and listing them in the hashList.
     * If it's not possible (for example if the saved file has been deleted in the meantime), it throws an error
     */    
    @Override
    public void execute() throws Exception{
        if(_name.equals("all")){ // if it's save all
            File directory = new File(FILES_SAVE_DIR);
            if(directory.exists()){ 
                Hashtable<String,LogHashTable> hashList = HashList.getHashList(); //we get the hashList
                Enumeration<LogHashTable> values = hashList.elements();
                while (values.hasMoreElements()){ // and for each logHashTable of the HashList
                    LogHashTable logHashTable =  values.nextElement();
                    saveAFile(logHashTable);
                }
             }
        }
        
        else{ // if it's not "all"
            if (_logHashTable!=null) { //if the constructor found the LogHashTable
                saveAFile(_logHashTable);

            } else { //else we throw an exception
                throw new Exception("Wrong command: logHashTable doesn't exist");
            }
        }
    }

}


