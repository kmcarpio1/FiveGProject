package src.commands;

import java.io.File;
import src.config.PathToFile;
import src.server.FileSender;

//import java.net.InetAddress;
//import java.net.UnknownHostException;
/**
 * The "pass" command name class
 * pass is supposed to send a savedLogHashTable to the center machine and delete them
 */
public class Pass extends  ICommand {
    /*
     * In the command we will have a 3 word string
     * The firt word will be pass
     * the second the IP adress to the center machine
     * Andç the third is either "all", meaning we want to pass all the savedLogHashTable files in the save file
     * Or even the name of a savedLogHashTable, in that case we send it
    */

    private static final String FILES_SAVE_DIR = PathToFile.getPathSave(); //path to the saved logHashTable directory
    private String _name; //second word of the command
    private String _IP;

    /**
     * Constructor of the save command instance.
     * It checks if there are the right amount of element, and if they are valid
     * You can find the acceptance at the beginning of the class
     * @param command
     * @throws Exception
     */   
    Pass(CommandCutter command) throws Exception{

        
        if(command.size() != 3) throw new Exception("Wrong command: wrong number of arguments");

        _name = command.getArg(2);
        _IP = command.getArg(1);

        String path = FILES_SAVE_DIR + _name;

        if(_IP.equals("") || _IP.equals("null")) throw new Exception("Wrong command: IP address hasn't been initialized");

        if(!_name.equals("all")){ //if it's not "all", it should be the name of a saved file
            File file = new File(path);
            if(!file.exists()){
                throw new Exception("Wrong command: wrong second argument");
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


    private void passAFile(String name, File file) throws Exception{
        FileSender.connect(name,_IP); //we send the savedFile
        addCreatedName(name); //we add the name
        file.delete(); //we delete the file that has been sent
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
                        //File logHashTableFile = new File(filePath);
                        if (file.exists()) { //we check that it's still exist
                            try{ //we do that so every file is checked, even if it's not a logHashTable  
                                passAFile(name, file);  
                            } catch (Exception e) {
                                System.err.println(filePath + " is not a saved LogHashTable: ");
                            }
                        } else {
                            System.err.println("File does not exist: " + filePath);
                        } 
                    }
                }
            }
        }
        else{ //else, it should be saved file name
            String path = FILES_SAVE_DIR + _name;
            File file = new File(path);
            if (file.exists()) { // if it still exist
                passAFile(_name, file);
            } else {
                throw new Exception("Wrong command: File doesn't exist anymore");
            }
        }
    }

    

}
