package tst.commands;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import src.commands.CommandCutter;
import src.commands.CommandFactory;
import src.commands.ICommand;
import src.config.PathToFile;
import src.hashfunctions.HashList;
import src.hashfunctions.HashTableElement;
import src.hashfunctions.LogHashTable;

public class TestSave {

    private static final String FILES_SAVE_DIR = PathToFile.getPathSave();

    public static void testSave() {
        testConstructor();
        testExecuteSaveAll();
        testExecuteSaveOneWithName();
        testExecuteSaveOneWithPath();
        testExecuteSaveOneWithPathClean();
        testExecuteSaveNonExistingLogHashTable();
    }

    public static void testConstructor() {
        boolean stateOfTest = true;
        File savedFile = null;
        try {
            LogHashTable logHashTable = new LogHashTable("important/path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "important/path");
            logHashTable.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "important/path");

            String logHashTableName = logHashTable.getLogHashTableName();

            List<String> listPaths = new ArrayList<>();
            listPaths.add("fake/path");
            listPaths.add("important/path");
            LogHashTable logHashTable2 = new LogHashTable(listPaths);
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "fake/path");
            
            String logHashTableName2 = logHashTable.getLogHashTableName();



            HashList.addLogHashTable(logHashTableName,logHashTable);

            // Save the log hash table
            //SaveLogHashTable.save(logHashTable);
            //savedFile = new File(FILES_SAVE_DIR + "save"+logHashTableName);

            // Test of the constructor with the "all" argument
            try {
                CommandCutter command1 = new CommandCutter("save all");
                ICommand save1 = CommandFactory.createCommand(command1);
                assert save1.getArgument().equals("all") : "FAILURE: Command 'save all' should set _name to 'all'.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the name of the logHashTable 
            try {
                CommandCutter command2 = new CommandCutter("save " + logHashTableName);//load + the name of the logHashTable
                ICommand save2 = CommandFactory.createCommand(command2);
                assert save2.getArgument().equals(logHashTableName) : "FAILURE: Command 'save' + logHashTableName should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command3 = new CommandCutter("save important/path");
                ICommand save3 = CommandFactory.createCommand(command3);
                assert save3.getArgument().equals(logHashTableName) : "FAILURE: Command 'save' + logFile path should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();            
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the path of the logFile and clean_on_save
            try {
                CommandCutter command3 = new CommandCutter("save important/path clean_on_save");
                ICommand save3 = CommandFactory.createCommand(command3);
                assert save3.getArgument().equals(logHashTableName) : "FAILURE: Command 'save' + logFile path should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();            
            }

            // Test of the constructor with a fake logHashTable not stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command4 = new CommandCutter("save fakeLogHashTableName");
                ICommand save4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'save fakeLogHashTableName' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }

            try {
                CommandCutter command5 = new CommandCutter("save");
                ICommand save5 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'save' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            try {
                CommandCutter command6 = new CommandCutter("save all stuff");
                ICommand save6 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'save all stuff' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong second argument'.";
            }


            if(stateOfTest) System.out.println("Save constructor test passed");
            else System.out.println("TEST FAILED : Save constructor test failed");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();

        }
    }


    public static void testExecuteSaveAll() {
        boolean stateOfTest = true;
        List<File> filesToDelete = new ArrayList<>();
        try {

            LogHashTable logHashTable1 = new LogHashTable();
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();
            String savedLogHashTablePath1 = FILES_SAVE_DIR + "save"+logHashTableName1;

            HashList.addLogHashTable(logHashTableName1,logHashTable1);

            LogHashTable logHashTable2 = new LogHashTable();
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            String savedLogHashTablePath2 = FILES_SAVE_DIR + "save"+logHashTableName2;
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("save all");
            ICommand save = CommandFactory.createCommand(command);

            save.execute();

            File directory = new File(FILES_SAVE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    assert fileList.length == 2 : "FAILURE: fileList Size incorrect";
                    File savedFile1 = new File(savedLogHashTablePath1); //does not create a file
                    filesToDelete.add(savedFile1);
                    File savedFile2 = new File(savedLogHashTablePath2); //does not create a file
                    filesToDelete.add(savedFile2);
                    assert savedFile1.exists() :"FAILURE: The saved file does not exist";
                    assert savedFile2.exists() :"FAILURE: The saved file does not exist";
                }
            }
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Save ExecuteSaveAll test passed");
            else System.out.println("TEST FAILED : Save ExecuteSaveAll test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
        }
    }



    public static void testExecuteSaveOneWithName() {
        boolean stateOfTest = true;
        File savedFile = null;

        try {

            LogHashTable logHashTable1 = new LogHashTable();
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();
            String savedLogHashTablePath1 = FILES_SAVE_DIR + "save"+logHashTableName1;

            HashList.addLogHashTable(logHashTableName1,logHashTable1);


            CommandCutter command = new CommandCutter("save "+logHashTableName1);
            ICommand save = CommandFactory.createCommand(command);

            save.execute();

            File directory = new File(FILES_SAVE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    assert fileList.length == 1 : "FAILURE: fileList Size incorrect";
                    savedFile = new File(savedLogHashTablePath1); //does not create a file
                    assert savedFile.exists() :"FAILURE: The saved file does not exist";
                }
            }
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Save ExecuteSaveOneWithName test passed");
            else System.out.println("TEST FAILED : Save ExecuteSaveOneWithName test failed");
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();
        }
    }

    public static void testExecuteSaveOneWithPath() {
        boolean stateOfTest = true;
        File savedFile = null;
        try {

            LogHashTable logHashTable1 = new LogHashTable("path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();
            String savedLogHashTablePath1 = FILES_SAVE_DIR + "save"+logHashTableName1;

            HashList.addLogHashTable(logHashTableName1,logHashTable1);


            CommandCutter command = new CommandCutter("save path");
            ICommand save = CommandFactory.createCommand(command);

            save.execute();

            File directory = new File(FILES_SAVE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    savedFile = new File(savedLogHashTablePath1); //does not create a file
                    assert fileList.length == 1 : "FAILURE: fileList Size incorrect";
                    assert savedFile.exists() :"FAILURE: The saved file does not exist";
                }
                else stateOfTest = false;
            }
            else stateOfTest = false;
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Save ExecuteSaveOneWithPath test passed");
            else System.out.println("TEST FAILED : Save ExecuteSaveOneWithPath test failed");
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();
        }
    }


    public static void testExecuteSaveOneWithPathClean() {
        boolean stateOfTest = true;
        File savedFile = null;
        try {

            LogHashTable logHashTable1 = new LogHashTable("path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();
            String savedLogHashTablePath1 = FILES_SAVE_DIR + "save"+logHashTableName1;

            HashList.addLogHashTable(logHashTableName1,logHashTable1);


            CommandCutter command = new CommandCutter("save path clean_on_save");
            ICommand save = CommandFactory.createCommand(command);

            save.execute();

            File directory = new File(FILES_SAVE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    savedFile = new File(savedLogHashTablePath1); //does not create a file
                    assert fileList.length == 1 : "FAILURE: fileList Size incorrect";
                    assert savedFile.exists() :"FAILURE: The saved file does not exist";
                }
                else stateOfTest = false;
            }
            else stateOfTest = false;

            assert logHashTable1.getLogTable().equals( new Hashtable<>());

        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Save ExecuteSaveOneWithPathClean test passed");
            else System.out.println("TEST FAILED : Save ExecuteSaveOneWithPathClean test failed");
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();
        }
    }



    public static void testExecuteSaveNonExistingLogHashTable() {
        File savedFile = null;
        boolean stateOfTest = true;
        try {

            LogHashTable logHashTable1 = new LogHashTable();
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();


            String savedLogHashTablePath1 = FILES_SAVE_DIR + "save"+logHashTableName1;

            //we do not put it inside hashList

            CommandCutter command = new CommandCutter("save path");
            ICommand save = CommandFactory.createCommand(command); //it should make an Exception

            savedFile = new File(savedLogHashTablePath1);

            save.execute();//we will never get there because creating a save instance with the wrong argument create an exception

            stateOfTest = false;
    
        } catch (Exception e) {
            try{
                assert e.getMessage().equals("Wrong command: wrong argument") : "FAILURE: Exception message should be 'Wrong command: File doesn't exist anymore'.";
            }catch (Exception a){
                a.printStackTrace();
                stateOfTest = false;
            }
        } finally {
            if(stateOfTest) System.out.println("Save ExecuteSaveNonExistingLogHashTable test passed");
            else System.out.println("TEST FAILED : Save ExecuteSaveNonExistingLogHashTable test failed");
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();
        }
    }

}