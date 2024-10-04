package tst.commands;

import src.commands.CommandCutter;

public class TestCommandCutter {
    
    public static void testCommandCutter() {
        testConstructor();
        testgetCommandName();
        testSize();
        testGetArg();
    }
 
    public static void testConstructor() {

        try {
            CommandCutter cutter = new CommandCutter(null);
            // This line should not be reached, as an exception is expected
            assert false : "FAILURE: An exception should have been thrown for null command.";
        } catch (Exception e) {
            assert e.getMessage().equals("Command was null") : "FAILURE: Exception message should be 'Command was null'.";
        }


        try {
            // Test with a non-null command
            CommandCutter cutter = new CommandCutter("test");
            assert cutter != null : "FAILURE: CommandCutter should be initialized properly with a valid command.";
            System.out.println("CommandCutter constructor with non-null command test passed");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("CommandCutter constructor test passed");
    }

    public static void testgetCommandName() {
        try{
            CommandCutter commandCutter1 = new CommandCutter(""); 
            assert commandCutter1.getCommandName().equals("") : "FAILURE command \"\" first argument should be empty";
     
            
            
            CommandCutter commandCutter2 = new CommandCutter("test function but way longer, because i am curious");

            assert commandCutter2.getCommandName().equals("test") : "FAILURE command \"test function but way longer, because i am curious\" first argument should be test";

            System.out.println("CommandCutter getCommandName test passed");
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }


    public static void testSize() {
        try{
            CommandCutter commandCutter1 = new CommandCutter(""); //size 0
            CommandCutter commandCutter2 = new CommandCutter("test function"); //size 2
            CommandCutter commandCutter3 = new CommandCutter("test function but way longer, because i am curious"); //size 9

            assert commandCutter1.size() == 1 : "FAILURE command \"\" and its CommandCutter size is different";
            assert commandCutter2.size() == 2 : "FAILURE command \"test function\" and its CommandCutter size is different";
            assert commandCutter3.size() == 9 : "FAILURE command \"test function but way longer, because i am curious\" and its CommandCutter size is different";

            System.out.println("CommandCutter size test passed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void testGetArg() {
        try{
        
            CommandCutter commandCutter1 = new CommandCutter(""); 
            assert commandCutter1.getArg(0).equals("") : "FAILURE command \"\" first argument should be empty";
            assert commandCutter1.getArg(5).equals("") : "FAILURE command \"\" don't have 6 arguments, so it should return an empty string";
     
            
            
            CommandCutter commandCutter2 = new CommandCutter("test function but way longer, because i am curious");

            assert commandCutter2.getArg(0).equals("test") : "FAILURE command \"test function but way longer, because i am curious\" first argument should be test";
            assert commandCutter2.getArg(5).equals("because") : "FAILURE command \"test function but way longer, because i am curious\" 6th argument should be because";
            assert commandCutter2.getArg(10).equals("") : "FAILURE command \"test function but way longer, because i am curious\" don't have 11 arguments, so it should return an empty string";
            
            System.out.println("CommandCutter getArg test passed");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
