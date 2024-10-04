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
import src.hashfunctions.SaveLogHashTable;

public class TestLoad {

    private static final String FILES_SAVE_DIR = PathToFile.getPathSave();

    public static void testLoad() {
        testConstructor();
        testExecuteLoadAll();
        testExecuteLoadFile();
        testExecuteLoadNonExistingFile();
    }

    public static void testConstructor() {
        File savedFile = null;
        try {
            LogHashTable logHashTable = new LogHashTable();
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "path");
            logHashTable.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "path");

            String logHashTableName = logHashTable.getLogHashTableName();

            // Save the log hash table
            SaveLogHashTable.save(logHashTable);
            savedFile = new File(FILES_SAVE_DIR + "save"+logHashTableName);

            // Test CommandCutter and CommandFactory
            try {
                CommandCutter command1 = new CommandCutter("load all");
                ICommand load1 = CommandFactory.createCommand(command1);
                assert load1.getArgument().equals("all") : "FAILURE: Command 'load all' should set _name to 'all'.";
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                CommandCutter command2 = new CommandCutter("load save" + logHashTableName);
                ICommand load2 = CommandFactory.createCommand(command2);
                assert load2.getArgument().equals("save"+logHashTableName) : "FAILURE: Command 'load' + logHashTableName should set _name to logHashTableName.";
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                CommandCutter command3 = new CommandCutter("load fakefile");
                ICommand load3 = CommandFactory.createCommand(command3);
                assert false : "FAILURE: Command 'load fakefile' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }

            try {
                CommandCutter command4 = new CommandCutter("load");
                ICommand load4 = CommandFactory.createCommand(command4);
                assert false : "FAILURE: Command 'load' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            try {
                CommandCutter command5 = new CommandCutter("load all stuff");
                ICommand load5 = CommandFactory.createCommand(command5);
                assert false : "FAILURE: Command 'load all stuff' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }


            System.out.println("Load constructor test passed");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
        }
    }


    public static void testExecuteLoadAll() {
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

            // Check that the save file has been created
            SaveLogHashTable.save(logHashTable1);

            File savedFile1 = new File(FILES_SAVE_DIR + "save"+logHashTableName1);
            filesToDelete.add(savedFile1);

            LogHashTable logHashTable2 = new LogHashTable();
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();

            // Check that the save file has been created
            SaveLogHashTable.save(logHashTable2);

            File savedFile2 = new File(FILES_SAVE_DIR + "save"+logHashTableName2);
            filesToDelete.add(savedFile2);

            CommandCutter command = new CommandCutter("load all");
            ICommand load = CommandFactory.createCommand(command);

            load.execute();

            Hashtable<String, LogHashTable> hashTables = HashList.getHashList();
            

            assert hashTables.size() == 2 : "FAILURE: HasList Size incorrect";
            assert hashTables.containsKey(logHashTable1.getLogHashTableName()): "FAILURE: HasList does not contains one or more of the hashtables";
            assert hashTables.containsKey(logHashTable2.getLogHashTableName()): "FAILURE: HasList does not contains one or more of the hashtables";
            System.out.println("Load Execute Load All test passed");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
        }
    }




    public static void testExecuteLoadFile() {
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

            // Check that the save file has been created
            SaveLogHashTable.save(logHashTable1);

            File savedFile1 = new File(FILES_SAVE_DIR + "save"+ logHashTableName1);
            savedFile=savedFile1;

            CommandCutter command = new CommandCutter("load save" +logHashTableName1);
            ICommand load = CommandFactory.createCommand(command);

            load.execute();

            Hashtable<String, LogHashTable> hashTables = HashList.getHashList();
            

            assert hashTables.size() == 1 : "FAILURE: HasList Size incorrect";
            assert hashTables.containsKey(logHashTable1.getLogHashTableName()): "FAILURE: HasList does not contains one or more of the hashtables";

            System.out.println("Load Execute Load OneFile test passed");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();

        }
    }


    public static void testExecuteLoadNonExistingFile() {
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

            // Check that the save file has been created
            SaveLogHashTable.save(logHashTable1);



            CommandCutter command = new CommandCutter("load "+"save"+logHashTableName1);
            ICommand load = CommandFactory.createCommand(command);

            savedFile = new File(FILES_SAVE_DIR + "save"+logHashTableName1);
            savedFile.delete();

            try {
                load.execute();
                System.out.println("FAILURE: File not existing, we should get an Exception");
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: File doesn't exist anymore") : "FAILURE: Exception message should be 'Wrong command: File doesn't exist anymore'.";
                System.out.println("Load execute non-existent file test passed");
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

}