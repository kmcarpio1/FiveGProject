package src.commands;


/**
 * this class is a static class used to sort the command, and launch the construction of the corresponding instance depending on the name
 * of the command.
 * 
 * It's throwing an error if the first name isn't recognize
 */
public class CommandFactory {
    
    private static final String[] _Commands = {"watch","save","load","write","mix","send"}; //a list of the supported command names.

    /**
     * the constructor does nothing. It isn't meant to be used
     */
    private CommandFactory(){}

    /**
     * This static method is checking is the given command is supported
     * @param command: the cutted command (so we can easily have access to the name of it)
     * @return true if it's a supported command, false else.
     */
    public static boolean isCommand(CommandCutter command){
        String commandName = command.getCommandName();
        for(String cmd : _Commands){
            if(commandName.equals(cmd)){
                return true;
            }
        }
        return false;
    }


    /**
     * this static method is meant to be used to generalize the process.
     * We take the cutted command, and it will return an instance corresponding of the name of the command. We will then be able to work with it.
     * @param command : the cutted command (so we can easily have access to the name of it)
     * @return an instance of ICommand, corresponding on the right command name
     * @throws Exception if the command name isn't supported
     */
    public static ICommand createCommand(CommandCutter command) throws Exception{
        switch(command.getCommandName()){
            case "watch":
                return (new Watch(command));
            case "save":
                return (new Save(command));
            case "load":
                return (new Load(command));
            case "write":
                return (new Write(command));
            case "mix":
                return (new Mix(command));
            case "send":
                return (new Send(command));
            case "pass":
                return (new Pass(command));
            default:
                throw new Exception("command not recognized");
        }



    }

}
