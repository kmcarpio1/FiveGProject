package tst.commands;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import src.commands.CommandCutter;
import src.commands.CommandFactory;
import src.commands.ICommand;
import src.config.PathToFile;
import src.hashfunctions.HashList;
import src.hashfunctions.HashTableElement;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;

public class TestWrite {
    
    private static final String FILES_SAVE_DIR = PathToFile.getPathSave();
    private static final String FILES_WRITE_DIR = PathToFile.getPathWrite();//path to the write file directory

    public static void testWrite() {
        testConstructorListed();
        testConstructorSaved();
        testExecuteWriteAllListed();
        testExecuteWriteAllSaved();
        testExecuteWriteOneSaved();
        testExecuteWriteOneListedWithPath();
        testExecuteWriteOneListedWithName();
        testExecuteWriteNonExistingSavedFile();
    }

    public static void testConstructorListed() {
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


            HashList.addLogHashTable(logHashTableName,logHashTable);

            // Test of the constructor with the "all" argument
            try {
                CommandCutter command1 = new CommandCutter("write listed all");
                ICommand write1 = CommandFactory.createCommand(command1);
                assert write1.getArgument().equals("all") : "FAILURE: Command 'write listed all' should set _name to 'all'.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the name of the logHashTable 
            try {
                CommandCutter command2 = new CommandCutter("write listed " + logHashTableName);//write + the name of the logHashTable
                ICommand write2 = CommandFactory.createCommand(command2);
                assert write2.getArgument().equals(logHashTableName) : "FAILURE: Command 'write listed' + logHashTableName should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command3 = new CommandCutter("write listed important/path");
                ICommand write3 = CommandFactory.createCommand(command3);
                assert write3.getArgument().equals(logHashTableName) : "FAILURE: Command 'write listed' + logFile path should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();            }

            // Test of the constructor with a fake logHashTable not stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command4 = new CommandCutter("write listed fakeLogHashTableName");
                ICommand write4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write listed fakeLogHashTableName' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }

            try {
                CommandCutter command5 = new CommandCutter("write");
                ICommand write5 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command5 = new CommandCutter("write listed");
                ICommand write52 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write listed' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            try {
                CommandCutter command6 = new CommandCutter("write listed all stuff");
                ICommand write6 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write listed all stuff' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command6 = new CommandCutter("write thing all");
                ICommand write62 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write thing all' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong first argument") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            if(stateOfTest) System.out.println("Write constructorListed test passed");
            else System.out.println("TEST FAILED : Write constructorListed test failed");


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

    public static void testConstructorSaved() {
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

      
            //HashList.addLogHashTable(logHashTableName,logHashTable);



            // Test of the constructor with a logHashTable not saved, with the path of the logFile 
            try {
                CommandCutter command4 = new CommandCutter("write saved save" + logHashTableName);
                ICommand write4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write saved fakeLogHashTableName' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }


            // Save the log hash table
            SaveLogHashTable.save(logHashTable);
            savedFile = new File(FILES_SAVE_DIR + "save"+logHashTableName);
            String savedFileName = "save"+logHashTableName;
    
            // Test of the constructor with the "all" argument
            try {
                CommandCutter command1 = new CommandCutter("write saved all");
                ICommand write1 = CommandFactory.createCommand(command1);
                assert write1.getArgument().equals("all") : "FAILURE: Command 'write saved all' should set _name to 'all'.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the name of the logHashTable 
            try {
                CommandCutter command2 = new CommandCutter("write saved " + savedFileName);//write + the name of the logHashTable
                ICommand write2 = CommandFactory.createCommand(command2);
                assert write2.getArgument().equals(savedFileName) : "FAILURE: Command 'write saved' + savedFileName should set _name to savedFileName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }



            // Test of the constructor with a fake logHashTable not stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command4 = new CommandCutter("write saved fakeLogHashTableName");
                ICommand write4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write saved fakeLogHashTableName' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }

            try {
                CommandCutter command5 = new CommandCutter("write");
                ICommand write5 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command5 = new CommandCutter("write saved");
                ICommand write52 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write saved' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            try {
                CommandCutter command6 = new CommandCutter("write saved all stuff");
                ICommand write6 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write saved all stuff' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command6 = new CommandCutter("write thing all");
                ICommand write62 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'write thing all' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong first argument") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            if(stateOfTest) System.out.println("Write ConstructorSaved test passed");
            else System.out.println("TEST FAILED : Write ConstructorSaved test failed");


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



    public static void testExecuteWriteAllListed() {
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
            String writtenLogHashTablePath1 = FILES_WRITE_DIR + "tab_"+logHashTableName1;

            HashList.addLogHashTable(logHashTableName1,logHashTable1);

            LogHashTable logHashTable2 = new LogHashTable();
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            String writtenLogHashTablePath2 = FILES_WRITE_DIR + "tab_"+logHashTableName2;
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("write listed all");
            ICommand write = CommandFactory.createCommand(command);

            write.execute();

            File directory = new File(FILES_WRITE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    assert fileList.length == 2 : "FAILURE: fileList Size incorrect";
                    File writtenFile1 = new File(writtenLogHashTablePath1); //does not create a file
                    filesToDelete.add(writtenFile1);
                    File writtenFile2 = new File(writtenLogHashTablePath2); //does not create a file
                    filesToDelete.add(writtenFile2);
                    assert writtenFile1.exists() :"FAILURE: The written file does not exist";
                    assert writtenFile2.exists() :"FAILURE: The written file does not exist";
                }
            }
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Write ExecuteWriteAllListed test passed");
            else System.out.println("TEST FAILED : Write ExecuteWriteAllListed test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
        }
    }


    public static void testExecuteWriteAllSaved() {
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
            String writtenLogHashTablePath1 = FILES_WRITE_DIR + "tab_"+logHashTableName1;
            String savedLogHashTablePath1 = FILES_SAVE_DIR + "save"+logHashTableName1;

            SaveLogHashTable.save(logHashTable1);

            File savedFile1 = new File(savedLogHashTablePath1);
            filesToDelete.add(savedFile1);


            LogHashTable logHashTable2 = new LogHashTable();
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            String writtenLogHashTablePath2 = FILES_WRITE_DIR + "tab_"+logHashTableName2;
            String savedLogHashTablePath2 = FILES_SAVE_DIR + "save"+logHashTableName2;
            
            
            SaveLogHashTable.save(logHashTable2);

            File savedFile2 = new File(savedLogHashTablePath2);
            filesToDelete.add(savedFile2);
            

            CommandCutter command = new CommandCutter("write saved all");
            ICommand write = CommandFactory.createCommand(command);

            write.execute();

            File directory = new File(FILES_WRITE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    assert fileList.length == 2 : "FAILURE: fileList Size incorrect";
                    File writtenFile1 = new File(writtenLogHashTablePath1); //does not create a file
                    filesToDelete.add(writtenFile1);
                    File writtenFile2 = new File(writtenLogHashTablePath2); //does not create a file
                    filesToDelete.add(writtenFile2);
                    assert writtenFile1.exists() :"FAILURE: The written file does not exist";
                    assert writtenFile2.exists() :"FAILURE: The written file does not exist";
                }
            }
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Write ExecuteWriteAllSaved test passed");
            else System.out.println("TEST FAILED : Write ExecuteWriteAllSaved test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
        }
    }



    public static void testExecuteWriteOneSaved() {
        boolean stateOfTest = true;
        File savedFile = null;
        File writenFile = null;

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


            String writtenLogHashTablePath1 = FILES_WRITE_DIR + "tab_"+logHashTableName1;
            String savedLogHashTablePath1 = FILES_SAVE_DIR + "save"+logHashTableName1;

            SaveLogHashTable.save(logHashTable1);

            savedFile = new File(savedLogHashTablePath1);

            CommandCutter command = new CommandCutter("write saved save"+logHashTableName1);
            ICommand write = CommandFactory.createCommand(command);

            write.execute();

            File directory = new File(FILES_WRITE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    assert fileList.length == 1 : "FAILURE: fileList Size incorrect";
                    writenFile = new File(writtenLogHashTablePath1); //does not create a file
                    assert writenFile.exists() :"FAILURE: The saved file does not exist";
                }
            }
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Write ExecuteWriteOneSaved test passed");
            else System.out.println("TEST FAILED : Write ExecuteWriteOneSaved test failed");
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            if (writenFile != null && writenFile.exists()) {
                writenFile.delete();
            }
            HashList.clean();
        }
    }

    public static void testExecuteWriteOneListedWithPath() {
        boolean stateOfTest = true;
        File writenFile = null;

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

            HashList.addLogHashTable(logHashTableName1,logHashTable1);

            String writtenLogHashTablePath1 = FILES_WRITE_DIR + "tab_"+logHashTableName1;

            CommandCutter command = new CommandCutter("write listed path");
            ICommand write = CommandFactory.createCommand(command);

            write.execute();

            File directory = new File(FILES_WRITE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    assert fileList.length == 1 : "FAILURE: fileList Size incorrect";
                    writenFile = new File(writtenLogHashTablePath1); //does not create a file
                    assert writenFile.exists() :"FAILURE: The saved file does not exist";
                }
            }
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Write ExecuteWriteOneListedWithPath test passed");
            else System.out.println("TEST FAILED : Write ExecuteWriteOneListedWithPath test failed");
            // Cleanup the saved file if it exists
            if (writenFile != null && writenFile.exists()) {
                writenFile.delete();
            }
            HashList.clean();
        }
    }


    public static void testExecuteWriteOneListedWithName() {
        boolean stateOfTest = true;
        File writenFile = null;

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

            HashList.addLogHashTable(logHashTableName1,logHashTable1);

            String writtenLogHashTablePath1 = FILES_WRITE_DIR + "tab_"+logHashTableName1;

            CommandCutter command = new CommandCutter("write listed "+logHashTableName1);
            ICommand write = CommandFactory.createCommand(command);

            write.execute();

            File directory = new File(FILES_WRITE_DIR);
            if(directory.exists()){
                File[] fileList = directory.listFiles();
                if(fileList != null){
                    assert fileList.length == 1 : "FAILURE: fileList Size incorrect";
                    writenFile = new File(writtenLogHashTablePath1); //does not create a file
                    assert writenFile.exists() :"FAILURE: The saved file does not exist";
                }
            }
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Write ExecuteWriteOneListedWithName test passed");
            else System.out.println("TEST FAILED : Write ExecuteWriteOneListedWithName test failed");
            // Cleanup the saved file if it exists
            if (writenFile != null && writenFile.exists()) {
                writenFile.delete();
            }
            HashList.clean();
        }
    }




    public static void testExecuteWriteNonExistingSavedFile() {
        File writtenFile = null;
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


            String writtenLogHashTablePath1 = FILES_WRITE_DIR + "tab_"+logHashTableName1;
            String savedLogHashTablePath1 = FILES_SAVE_DIR + "save"+logHashTableName1;

            //we  put it inside hashList
            SaveLogHashTable.save(logHashTable1);
            savedFile = new File(savedLogHashTablePath1);

            CommandCutter command = new CommandCutter("write saved save"+logHashTableName1);
            ICommand write = CommandFactory.createCommand(command); //it should make an Exception


            savedFile.delete();


            write.execute();//we will never get there because creating a save instance with the wrong argument create an exception
            writtenFile = new File(writtenLogHashTablePath1);

            stateOfTest = false;
    
        } catch (Exception e) {
            try{
                assert e.getMessage().equals("Wrong command: File doesn't exist anymore") : "FAILURE: Exception message should be 'Wrong command: File doesn't exist anymore'.";
            }catch (Exception a){
                a.printStackTrace();
                stateOfTest = false;
            }
        } finally {
            if(stateOfTest) System.out.println("Write ExecuteWriteNonExistingSavedFile test passed");
            else System.out.println("TEST FAILED : Write ExecuteWriteNonExistingSavedFile test failed");
            // Cleanup the saved file if it exists
            if (writtenFile != null && writtenFile.exists()) {
                writtenFile.delete();
            }
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();
        }
    }

}

