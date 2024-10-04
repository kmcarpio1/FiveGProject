package src.commands;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import src.config.PathToFile;
import src.hashfunctions.HashList;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;

/**
 * The "mix" command name class
 * Mix is supposed to mix either all the saved files or all the actual logHashTable listed in HashList into a new LogHashTable.
 */
public class Mix extends  ICommand {
    /*
     * In the command we will have a string
     * It's either "listed", meaning we want to mix all the logHashTable files in the hashList
     * Or either "saved", meaning we want to mix all the logHashTable files saved
    */


    private static final String FILES_SAVE_DIR = PathToFile.getPathSave(); //path to the save file directory
    private String _name; //either "listed" or "saved"


    /**
     * Constructor of the mix command instance.
     * It checks if there are the right amount of element, and if they are valid (if it's either "lsited" or "saved")
     * You can find the acceptance at the beginning of the class
     * @param command
     * @throws Exception
     */
    Mix(CommandCutter command) throws Exception{
        if(command.size() != 2) throw new Exception("Wrong command: wrong number of arguments");

        _name = command.getArg(1);
        if(!_name.equals("listed")){ 
            if(!_name.equals("saved")){
                throw new Exception("Wrong command: wrong argument");
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


    /**
     * Execute the command by mixing the saved logHashTable in a new one, and listing it in the hashList.
     * If it's not possible (for example if the saved file has been deleted in the meantime), it throws an error
     * 
     * it will create a new LogHashTable, look all the saved LogHashTable in the right directory, load them and add them to the new LogHashTable. Then it will lsit it in the HashList
     */
    private void executeSaved() throws Exception{
        File directory = new File(FILES_SAVE_DIR);
        File[] fileList = directory.listFiles();

        if(fileList != null){ //if the directory exists

            LogHashTable mixLogHashTable = new LogHashTable(); //we are creating an empty logHashTable
            for(File file : fileList){ //for each file 
                if(file.isFile()){
                    String name = file.getName();
                    String filePath = FILES_SAVE_DIR + name;
                    if (file.exists()) {
                        try{ //we do that so every file is checked, even if it's not a logHashTable
                            LogHashTable oneLogHashTable = SaveLogHashTable.load(name); //we are loading the file
                            mixLogHashTable.addLogHashTable(oneLogHashTable);// and add it to the new logHashTable
                        }catch (Exception e) {
                            System.err.println(filePath + " is not a saved LogHashTable: " + e.getMessage());
                        }
                    } 
                    else {
                        System.err.println("File does not exist: " + filePath);
                        //throw new Exception("File does not exist: " + filePath);
                    }
                }
            }
            HashList.addLogHashTable(mixLogHashTable.getLogHashTableName(), mixLogHashTable); //at the end we are listing the new logHashTable
            addCreatedName(mixLogHashTable.getLogHashTableName());//we add the name
            //}
        }
    }

    


    /**
     * Execute the command by mixing the listed logHashTable in a new one, and listing it in the hashList.
     * It takes every listed LogHashTable and add them into an ampty logHashTable, then list it in hashList
     */
    private void executeListed() throws Exception{
        LogHashTable mixLogHashTable = new LogHashTable(); //we are creating an empty logHashTable
        Hashtable<String,LogHashTable> hashList = HashList.getHashList(); //we are getting the HashList
        Enumeration<LogHashTable> values = hashList.elements();
        while (values.hasMoreElements()){ //for each LogHashTable in the HashList
            LogHashTable logHashTable =  values.nextElement();
            mixLogHashTable.addLogHashTable(logHashTable); //we add it 
        }
        HashList.addLogHashTable(mixLogHashTable.getLogHashTableName(), mixLogHashTable); //we list the new mixed logHashTable
        addCreatedName(mixLogHashTable.getLogHashTableName());//we add the name


    }
    
    /**
     * Execute the command by mixing the saved or listed logHashTable in a new one, and listing it in the hashList.
     * If it's not possible (for example if the saved file has been deleted in the meantime), it throws an error
     */
    @Override
    public void execute() throws Exception{
        switch (_name) { //there are two cases
            case "listed":
                executeListed(); //either all listed logHashTable
                break;
            case "saved":
                executeSaved(); //either all saved logHashTable
                break;
            default:
                throw new Exception("Wrong command: wrong argument"); //it shouldn't happen
        }
    }
}



