package src.commands;

import java.io.File;
import java.util.List;
import src.config.PathToFile;
import src.hashfunctions.HashList;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;


/**
 * The "load" command name class
 * Load is supposed to load a saved LogHashTable
 */
public class Load extends  ICommand {
    /*
     * In the command we will have a string
     * It's either "all", meaning we want to load all the saved logHashTable files
     * Or either the path to the saved LogHashTable, in that case we load it
     * 
     * example : "load" + _name  (with _name being "all" or a path to a saved logGashTable)
    */

    private static final String FILES_SAVE_DIR = PathToFile.getPathSave(); //path to the saving directory
    private String _name; //the name of the file to load (or "all")
    private List<String> _createdNames = null; //null or the name of the loaded files (that are going to be stored in HashList)

    /**
     * Constructor of the load command instance.
     * It checks if there are the right amount of element, and if they are valid (if the file exist for example)
     * You can find the acceptance at the beginning of the class
     * @param command
     * @throws Exception
     */
    Load(CommandCutter command) throws Exception{
        if(command.size() != 2) throw new Exception("Wrong command: wrong number of arguments");

        _name = command.getArg(1);
        String path = FILES_SAVE_DIR + _name;

        if(!_name.equals("all")){ //if it's not "all", it should be the name of a saved file
            File file = new File(path);
            if(!file.exists()){
                throw new Exception("Wrong command: wrong argument");
            }
            _name = file.getName();
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
     * This method is loading the given file
     * @param file
     * @param name
     */
    private void loadAFile(File file, String name)throws Exception{

        String filePath = FILES_SAVE_DIR + name;
        LogHashTable logHashTable = SaveLogHashTable.load(name); //we load it
        HashList.addLogHashTable(logHashTable.getLogHashTableName(), logHashTable); // and we list it in HashList

        addCreatedName(logHashTable.getLogHashTableName()); //we add the name
            
        
        
    }

    /**
     * Execute the command by loading the saved file(s), and listing them in the hashList.
     * If it's not possible (for example if the saved file has been deleted in the meantime), it throws an error
     */
    @Override
    public void execute() throws Exception{
        if(_name.equals("all")){ //in the cas of _name being "all"
            File directory = new File(FILES_SAVE_DIR);
            File[] fileList = directory.listFiles();
            if(fileList != null){ //if there is file in the saved file directory
                for(File file : fileList){ //for each file
                    if(file.isFile()){ //we check if it's a file
                        String name = file.getName();
                        String filePath = FILES_SAVE_DIR + name;
                        if (file.exists()) { //we check that it's still exist
                            try{ //we do that so every file is checked, even if it's not a logHashTable    
                                loadAFile(file, name);
                            } catch (Exception e) {
                                System.err.println(filePath + " is not a saved LogHashTable");
                            }
                        } 
                        else {
                            System.err.println(filePath+" doesn't exist anymore");
                        }
                    }
                }
            }
        }
        else{ //else, it should be saved file name

            String path = FILES_SAVE_DIR + _name;
            File file = new File(path);
            
            if (file.exists()) { //we check that it's still exist
                try{
                    loadAFile(file, _name);
                }catch (Exception e){
                    System.err.println("Wrong command: the file "+path+" isn't a saved logHashTable");
                    throw e;
                }
            } else {
                throw new Exception("Wrong command: File doesn't exist anymore");
            }
        }
    }

    

}

