package tst.commands;

import src.commands.CommandCutter;
import src.commands.CommandFactory;
import src.commands.Load;
import src.commands.Mix;
import src.commands.Save;
import src.commands.Write;

public class TestCommandFactory {

    public static void testCommandFactory() {
        testIsCommand();
        testCreateCommand();
    }

    public static void testIsCommand() {
        try {
            // Test avec des commandes valides
            CommandCutter watchCommand = new CommandCutter("watch");
            CommandCutter saveCommand = new CommandCutter("save");
            CommandCutter loadCommand = new CommandCutter("load");
            CommandCutter writeCommand = new CommandCutter("write");
            
            assert CommandFactory.isCommand(watchCommand) : "FAILURE: 'watch' should be recognized as a valid command.";
            assert CommandFactory.isCommand(saveCommand) : "FAILURE: 'save' should be recognized as a valid command.";
            assert CommandFactory.isCommand(loadCommand) : "FAILURE: 'load' should be recognized as a valid command.";
            assert CommandFactory.isCommand(writeCommand) : "FAILURE: 'write' should be recognized as a valid command.";

            // Test avec des commandes invalides
            CommandCutter invalidCommand = new CommandCutter("invalid");
            assert !CommandFactory.isCommand(invalidCommand) : "FAILURE: 'invalid' should not be recognized as a valid command.";

            System.out.println("CommandFactory isCommand test passed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testCreateCommand() {
        try {
            CommandCutter watchCommand = new CommandCutter("watch all");
            CommandCutter saveCommand = new CommandCutter("save all");
            CommandCutter loadCommand = new CommandCutter("load all");
            CommandCutter writeCommand = new CommandCutter("write listed all");
            CommandCutter mixCommand = new CommandCutter("mix listed");
            
            // Remplacer les tests par des assertions lorsque les classes de commande sont définies
            assert CommandFactory.createCommand(loadCommand) instanceof Load : "FAILURE: 'load' command should create an instance of Load.";
            
            // Testez les autres commandes lorsque les classes respectives sont implémentées
            // assert CommandFactory.createCommand(watchCommand) instanceof Watch : "FAILURE: 'watch' command should create an instance of Watch.";
            assert CommandFactory.createCommand(saveCommand) instanceof Save : "FAILURE: 'save' command should create an instance of Save.";
            assert CommandFactory.createCommand(writeCommand) instanceof Write : "FAILURE: 'write' command should create an instance of Write.";
            assert CommandFactory.createCommand(mixCommand) instanceof Mix : "FAILURE: 'mix' command should create an instance of Mix.";

            // Test avec une commande invalide
            try {
                CommandFactory.createCommand(new CommandCutter("invalid"));
                assert false : "FAILURE: Creating a command with 'invalid' should throw an exception.";
            } catch (Exception e) {
                assert e.getMessage().equals("command not recognized") : "FAILURE: Exception message should be 'command not recognized'.";
            }

            System.out.println("CommandFactory createCommand test passed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





