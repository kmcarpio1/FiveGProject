package src.commands;

import java.util.ArrayList;
import java.util.List;


/**
 * ICommand is an interface
 * It's used as an interface for all the command name's classes.
 * thanks to it, we make our code more generic and adaptable to the addition or modification of commands
 */
public abstract class ICommand {

    private List<String> _createdNames = null; //null or the name of the loaded files (that are going to be stored in HashList)

    /**
     * This method will execute the command that has been setup previously
     * @throws Exception if there is a problem to the execution of the command
     */
    public abstract  void execute() throws Exception;

    /**
     * This method will return a list of the different names of the created objects (saved logHashTable, loadedLogHashTable, new mexed logHashTable, written LogHashTable, or null if nothing hash been)
     * It's mostly to store the name of the object either saved in a file, or listed in HashList/hashWatched
     * @throws Exception if there is a problem to the execution of the command
     */
    public List<String> getCreatedNames(){
        return _createdNames;
    }

    protected void addCreatedName(String name){
        if(_createdNames==null) _createdNames = new ArrayList<>(); //we check if the list has been initialised
        _createdNames.add(name); //we add the name
    }


    /**
     * this method will often return the last word of the command (like the fileName, or "all")
     */
    public abstract String getArgument();
}
