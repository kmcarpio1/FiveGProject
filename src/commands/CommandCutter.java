package src.commands;


/**
 * This class is used to cut and store the command in a generic way (tu cut and isolate every words)
 */
public class CommandCutter {
    private String[] _t; //the command cut

    /**
     * Constructor, cutting the command at every space, and storing it
     * @param command 
     * @throws Exception
    */
    public CommandCutter(String command) throws Exception{
        if(command == null) throw new Exception("Command was null");
        _t = command.split(" ");
    }

    /**
     * to get the name of the command
     * @return the first word of the command, used to determine which command it is
     */
    public String getCommandName(){
        return _t[0];
    }

    /**
     * To know the size of the command (the number of words)
     * @return the number of words in the command
     */
    public int size(){
        return _t.length;
    }

    /**
     * to get a certain word in the command
     * @param n : the index of the searched word
     * @return either "" if n is higher than the number of word in the command, or the word at the n index
     */
    public String getArg(int n){
        if(_t.length<=n) return "";
        else return _t[n];
    }


}
