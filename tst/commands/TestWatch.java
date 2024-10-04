package tst.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import src.hashfunctions.HashWatched;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;

public class TestWatch {
    
    private static final String FILES_SAVE_DIR = PathToFile.getPathSave();
    private static final String FILES_WRITE_DIR = PathToFile.getPathWrite();

    public static void testWatch() {
        testConstructorListed();
        testConstructorSaved();
        testConstructorLogFile();
        testExecuteWatchAllListed();
        testExecuteWatchAllSaved();
        testExecuteLogFileDirectory();
        testExecuteLogFileDirectoryMixed();
        testExecuteLogFileOne();
        testExecuteWatchOneSaved();
        testExecuteWatchOneListedWithPath();
        testExecuteWatchOneListedWithName();
        testExecutewatchNonExistingSavedFile();
    }

    public static void testConstructorListed() {
        boolean stateOfTest = true;
        File savedFile = null;
        try {
            LogHashTable logHashTable = new LogHashTable("logfile/logfile.log");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "logfile/logfile.log");
            logHashTable.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "logfile/logfile.log");

            String logHashTableName = logHashTable.getLogHashTableName();

            HashList.addLogHashTable(logHashTableName,logHashTable);


            // Test of the constructor with the "all" argument
            try {
                CommandCutter command1 = new CommandCutter("watch listed all");
                ICommand watch1 = CommandFactory.createCommand(command1);
                assert watch1.getArgument().equals("all") : "FAILURE: Command 'watch listed all' should set _name to 'all'.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the name of the logHashTable 
            try {
                CommandCutter command2 = new CommandCutter("watch listed " + logHashTableName);//watch + the name of the logHashTable
                ICommand watch2 = CommandFactory.createCommand(command2);
                assert watch2.getArgument().equals(logHashTableName) : "FAILURE: Command 'watch listed' + logHashTableName should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command3 = new CommandCutter("watch listed logfile/logfile.log");
                ICommand watch3 = CommandFactory.createCommand(command3);
                assert watch3.getArgument().equals(logHashTableName) : "FAILURE: Command 'watch listed' + logFile path should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();            }

            // Test of the constructor with a fake logHashTable not stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command4 = new CommandCutter("watch listed fakeLogHashTableName");
                ICommand watch4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch listed fakeLogHashTableName' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }

            try {
                CommandCutter command5 = new CommandCutter("watch");
                ICommand watch5 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command5 = new CommandCutter("watch listed");
                ICommand watch52 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch listed' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            try {
                CommandCutter command6 = new CommandCutter("watch listed all stuff");
                ICommand watch6 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch listed all stuff' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command6 = new CommandCutter("watch thing all");
                ICommand watch62 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch thing all' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong first argument") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            if(stateOfTest) System.out.println("Watch constructorListed test passed");
            else System.out.println("TEST FAILED : Watch constructorListed test failed");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();
            HashWatched.clean();


        }
    }

    public static void testConstructorSaved() {
        boolean stateOfTest = true;
        File savedFile = null;
        try {
            LogHashTable logHashTable = new LogHashTable("logfile/logfile.log");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "logfile/logfile.log");
            logHashTable.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "logfile/logfile.log");

            String logHashTableName = logHashTable.getLogHashTableName();

      



            // Test of the constructor with a logHashTable not saved, with the path of the logFile 
            try {
                CommandCutter command4 = new CommandCutter("watch saved save" + logHashTableName);
                ICommand watch4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch saved fakeLogHashTableName' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }


            // Save the log hash table
            SaveLogHashTable.save(logHashTable);
            savedFile = new File(FILES_SAVE_DIR + "save"+logHashTableName);
            String savedFileName = "save"+logHashTableName;
    
            // Test of the constructor with the "all" argument
            try {
                CommandCutter command1 = new CommandCutter("watch saved all");
                ICommand watch1 = CommandFactory.createCommand(command1);
                assert watch1.getArgument().equals("all") : "FAILURE: Command 'watch saved all' should set _name to 'all'.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the name of the logHashTable 
            try {
                CommandCutter command2 = new CommandCutter("watch saved " + savedFileName);//watch + the name of the logHashTable
                ICommand watch2 = CommandFactory.createCommand(command2);
                assert watch2.getArgument().equals(savedFileName) : "FAILURE: Command 'watch saved' + savedFileName should set _name to savedFileName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }



            // Test of the constructor with a fake logHashTable not stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command4 = new CommandCutter("watch saved fakeLogHashTableName");
                ICommand watch4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch saved fakeLogHashTableName' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }

            try {
                CommandCutter command5 = new CommandCutter("watch");
                ICommand watch5 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command5 = new CommandCutter("watch saved");
                ICommand watch52 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch saved' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            try {
                CommandCutter command6 = new CommandCutter("watch saved all stuff");
                ICommand watch6 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch saved all stuff' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command6 = new CommandCutter("watch thing all");
                ICommand watch62 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'watch thing all' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong first argument") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            if(stateOfTest) System.out.println("Watch ConstructorSaved test passed");
            else System.out.println("TEST FAILED : Watch ConstructorSaved test failed");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Cleanup the saved file if it exists
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
            HashList.clean();
            HashWatched.clean();


        }
    }



    public static void testConstructorLogFile() {
        try{    
            boolean stateOfTest = true;
            String dirName = "logDir/";
            String fileName = "logFile.log";

            // create a logDirectory and a logFile
            File dir = new File(dirName);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    throw new Exception("FAILURE : impossible to create the directory");
                }
            } 

            File file = new File(dir, fileName);
            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("Log Example");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 

            try {

                try {
                    CommandCutter command1 = new CommandCutter("watch logFile "+dirName);
                    ICommand watch1 = CommandFactory.createCommand(command1);
                    assert watch1.getArgument().equals(dirName) : "FAILURE: Command 'watch saved all' should set _name to 'all'.";
                } catch (Exception e) {
                    stateOfTest = false;
                    e.printStackTrace();
                }

                try {
                    CommandCutter command1 = new CommandCutter("watch logFile "+dirName +" mixed");
                    ICommand watch1 = CommandFactory.createCommand(command1);
                    assert watch1.getArgument().equals(dirName) : "FAILURE: Command 'watch logFile dirName mixed' should set _name to 'all'.";
                } catch (Exception e) {
                    stateOfTest = false;
                    e.printStackTrace();
                }


                try {
                    CommandCutter command2 = new CommandCutter("watch logFile " + dirName+fileName);//watch + the name of the logHashTable
                    ICommand watch2 = CommandFactory.createCommand(command2);
                    assert watch2.getArgument().equals(dirName+fileName) : "FAILURE: Command 'watch logFile' + savedFileName should set _name to savedFileName.";
                } catch (Exception e) {
                    stateOfTest = false;
                    e.printStackTrace();
                }



                // Test of the constructor with a fake path, with the path of the logFile 
                try {
                    CommandCutter command4 = new CommandCutter("watch logFile fakePath");
                    ICommand watch4 = CommandFactory.createCommand(command4);
                    stateOfTest = false;
                    assert false : "FAILURE: Command 'watch saved fakeLogHashTableName' should throw an exception for non-existing file.";
                } catch (Exception e) {
                    assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
                }

                try {
                    CommandCutter command4 = new CommandCutter("watch logFile fakePath mixed");
                    ICommand watch4 = CommandFactory.createCommand(command4);
                    stateOfTest = false;
                    assert false : "FAILURE: Command 'watch saved fakeLogHashTableName' should throw an exception for non-existing file.";
                } catch (Exception e) {
                    assert e.getMessage().equals("Wrong command: wrong second argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
                }


                try {
                    CommandCutter command5 = new CommandCutter("watch");
                    ICommand watch5 = CommandFactory.createCommand(command5);
                    stateOfTest = false;
                    assert false : "FAILURE: Command 'watch' should throw an exception for wrong number of arguments.";
                } catch (Exception e) {
                    assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
                }

                try {
                    CommandCutter command5 = new CommandCutter("watch truc "+dirName+fileName+" mixed");
                    ICommand watch5 = CommandFactory.createCommand(command5);
                    stateOfTest = false;
                    assert false : "FAILURE: Command 'watch' should throw an exception for wrong number of arguments.";
                } catch (Exception e) {
                    assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
                }


                try {
                    CommandCutter command5 = new CommandCutter("watch logFile "+dirName+fileName+" truc");
                    ICommand watch5 = CommandFactory.createCommand(command5);
                    stateOfTest = false;
                    assert false : "FAILURE: Command 'watch' should throw an exception for wrong number of arguments.";
                } catch (Exception e) {
                    assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
                }

                try {
                    CommandCutter command5 = new CommandCutter("watch logFile");
                    ICommand watch52 = CommandFactory.createCommand(command5);
                    stateOfTest = false;
                    assert false : "FAILURE: Command 'watch saved' should throw an exception for wrong number of arguments.";
                } catch (Exception e) {
                    assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
                }

            
                try {
                    CommandCutter command6 = new CommandCutter("watch thing "+dirName+fileName);
                    ICommand watch62 = CommandFactory.createCommand(command6);
                    stateOfTest = false;
                    assert false : "FAILURE: Command 'watch thing all' should throw an exception for wrong number of arguments.";
                } catch (Exception e) {
                    assert e.getMessage().equals("Wrong command: wrong first argument") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
                }

                if(stateOfTest) System.out.println("Watch ConstructorSaved test passed");
                else System.out.println("TEST FAILED : Watch ConstructorSaved test failed");


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Cleanup the saved file if it exists
                if (file.exists()) {
                    file.delete();
                }
            
                // Supprimer le répertoire
                if (dir.exists()) {
                    dir.delete();
                }
                HashList.clean();
                HashWatched.clean();


            }
        }catch(Exception e){
        }
    }





    public static void testExecuteWatchAllListed() {
        boolean stateOfTest = true;
        List<File> filesToDelete = new ArrayList<>();
        try {

            LogHashTable logHashTable1 = new LogHashTable("logfile/logfile.log");
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

            LogHashTable logHashTable2 = new LogHashTable("logfile/logfile_other.log");
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("watch listed all");
            ICommand watch = CommandFactory.createCommand(command);

            watch.execute();

            List<String> newLogHashTableNames = watch.getCreatedNames(); 
            assert newLogHashTableNames.size() == 2 : "FAILURE : it should be 2 names in the created name, even if nothing new has been created, it's to show the logHashTable that now watch a logFile" ;


            Hashtable<String,List<LogHashTable>> hashWatch = HashWatched.getHashWatched();

            assert hashWatch.size()==2 :"FAILURE: The HashWatched should now contain 2 diferent paths";

            assert hashWatch.containsKey("logfile/logfile.log") : "FAILURE : one of the watched path isn't in the hashList";
            assert hashWatch.containsKey("logfile/logfile_other.log") : "FAILURE : one of the watched path isn't in the hashList";


            for(String key : hashWatch.keySet()){
                List<LogHashTable> watchingLogHashTables = hashWatch.get(key);
                if(key.equals("logfile/logfile.log")) assert watchingLogHashTables.contains(logHashTable1) : "FAILURE : the elements in hashWatch are incorrect";
                else if(key.equals("logfile/logfile_other.log")) assert watchingLogHashTables.contains(logHashTable2) : "FAILURE : the elements in hashWatch are incorrect";
                else assert false : "FAILURE : there is another unknow path in the hashWatch";

            }

            
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Watch ExecuteWatchAllListed test passed");
            else System.out.println("TEST FAILED : Watch ExecuteWatchAllListed test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
            HashWatched.clean();
        }
    }
    


    public static void testExecuteWatchAllSaved() {
        boolean stateOfTest = true;
        List<File> filesToDelete = new ArrayList<>();
        try {

            LogHashTable logHashTable1 = new LogHashTable("logfile/logfile.log");
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

            SaveLogHashTable.save(logHashTable1);

            File savedFile1 = new File(savedLogHashTablePath1);
            filesToDelete.add(savedFile1);


            LogHashTable logHashTable2 = new LogHashTable("logfile/logfile_other.log");
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            String savedLogHashTablePath2 = FILES_SAVE_DIR + "save"+logHashTableName2;
            
            
            SaveLogHashTable.save(logHashTable2);

            File savedFile2 = new File(savedLogHashTablePath2);
            filesToDelete.add(savedFile2);
            

            CommandCutter command = new CommandCutter("watch saved all");
            ICommand watch = CommandFactory.createCommand(command);

            watch.execute();

            List<String> newLogHashTableNames = watch.getCreatedNames(); 
            assert newLogHashTableNames.size() == 2 : "FAILURE : it should be 2 names in the created name, even if nothing new has been created, it's to show the logHashTable that now watch a logFile" ;
            assert newLogHashTableNames.contains(logHashTable1.getLogHashTableName()) : "FAILURE : the name of one of the logHashTable loaded isn't in the created name list";
            assert newLogHashTableNames.contains(logHashTable2.getLogHashTableName()) : "FAILURE : the path of one of the logHashTable loaded isn't in the created name list";

            Hashtable<String,List<LogHashTable>> hashWatch = HashWatched.getHashWatched();

            assert hashWatch.size()==2 :"FAILURE: The HashWatched should now contain 2 diferent paths";

            assert hashWatch.containsKey("logfile/logfile.log") : "FAILURE : one of the watched path isn't in the hashList";
            assert hashWatch.containsKey("logfile/logfile_other.log") : "FAILURE : one of the watched path isn't in the hashList";

            for(String key : hashWatch.keySet()){
                List<LogHashTable> watchingLogHashTables = hashWatch.get(key);
                if(key.equals("logfile/logfile.log")){
                    assert watchingLogHashTables.size()==1 : "FAILURE : it should only be one logHashTable watching logfile/logfile.log in this example";
                    LogHashTable watchingLogHashTable = watchingLogHashTables.get(0);
                    assert watchingLogHashTable.getLogHashTableName().equals(logHashTable1.getLogHashTableName()) : "FAILURE : the elements in hashWatch are incorrect";
                    assert watchingLogHashTable.getLogPaths().equals(logHashTable1.getLogPaths()) : "FAILURE : the elements in hashWatch are incorrect";
                } 
                else if(key.equals("logfile/logfile_other.log")) {
                    assert watchingLogHashTables.size()==1 : "FAILURE : it should only be one logHashTable watching logfile/logfile_other.log in this example";
                    LogHashTable watchingLogHashTable = watchingLogHashTables.get(0);
                    assert watchingLogHashTable.getLogHashTableName().equals(logHashTable2.getLogHashTableName()) : "FAILURE : the elements in hashWatch are incorrect";
                    assert watchingLogHashTable.getLogPaths().equals(logHashTable2.getLogPaths()) : "FAILURE : the elements in hashWatch are incorrect";
                }
                else assert false : "FAILURE : there is another unknow path in the hashWatch";
 
            }


        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Watch ExecuteWriteAllSaved test passed");
            else System.out.println("TEST FAILED : Watch ExecuteWriteAllSaved test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
            HashWatched.clean();
        }
    }




    public static void testExecuteLogFileDirectory() {
        try{    
            boolean stateOfTest = true;
            String dirName = "logDir/";
            String fileName1 = "logFile1.log";
            String fileName2 = "logFile2.log";

            // create a logDirectory and a logFile
            File dir = new File(dirName);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    throw new Exception("FAILURE : impossible to create the directory");
                }
            } 

            File file1 = new File(dir, fileName1);
            if (!file1.exists()) {
                try (FileWriter writer = new FileWriter(file1)) {
                    writer.write("Log Example 1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 

            File file2 = new File(dir, fileName2);
            if (!file2.exists()) {
                try (FileWriter writer = new FileWriter(file2)) {
                    writer.write("Log Example 2");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 

            try {


                CommandCutter command = new CommandCutter("watch logFile "+dirName);
                ICommand watch = CommandFactory.createCommand(command);

                
                watch.execute();
    
                List<String> newLogHashTableNames = watch.getCreatedNames(); 
                assert newLogHashTableNames.size() == 2 : "FAILURE : it should be 2 names in the created name, even if nothing new has been created, it's to show the logHashTable that now watch a logFile" ;
                Hashtable<String, LogHashTable> hashList = HashList.getHashList();

                String logHashTableName1 = newLogHashTableNames.get(0);
                String logHashTableName2 = newLogHashTableNames.get(1);

                assert hashList.containsKey(logHashTableName1) : "FAILURE : the created logHashTable name aren't in the hashList";
                assert hashList.containsKey(logHashTableName2) : "FAILURE : the created logHashTable name aren't in the hashList";
                
                LogHashTable logHashTable1 = hashList.get(logHashTableName1);
                LogHashTable logHashTable2 = hashList.get(logHashTableName2);

                Hashtable<String,List<LogHashTable>> hashWatch = HashWatched.getHashWatched();
                assert hashWatch.size()==2 :"FAILURE: The HashWatched should now contain 2 diferent paths";
    
                assert hashWatch.containsKey(dirName+fileName1) : "FAILURE : one of the watched path isn't in the hashList";
                assert hashWatch.containsKey(dirName+fileName2) : "FAILURE : one of the watched path isn't in the hashList";
    
                for(String key : hashWatch.keySet()){
                    List<LogHashTable> watchingLogHashTables = hashWatch.get(key);
                    if(key.equals(dirName+fileName1)){
                        assert watchingLogHashTables.size()==1 : "FAILURE : it should only be one logHashTable watching logfile/logfile.log in this example";
                        LogHashTable watchingLogHashTable = watchingLogHashTables.get(0);
                        assert watchingLogHashTable.getLogHashTableName().equals(logHashTable1.getLogHashTableName()) : "FAILURE : the elements in hashWatch are incorrect";
                        assert watchingLogHashTable.getLogPaths().equals(logHashTable1.getLogPaths()) : "FAILURE : the elements in hashWatch are incorrect";
                    } 
                    else if(key.equals(dirName+fileName2)) {
                        assert watchingLogHashTables.size()==1 : "FAILURE : it should only be one logHashTable watching logfile/logfile_other.log in this example";
                        LogHashTable watchingLogHashTable = watchingLogHashTables.get(0);
                        assert watchingLogHashTable.getLogHashTableName().equals(logHashTable2.getLogHashTableName()) : "FAILURE : the elements in hashWatch are incorrect";
                        assert watchingLogHashTable.getLogPaths().equals(logHashTable2.getLogPaths()) : "FAILURE : the elements in hashWatch are incorrect";
                    } 
                    else assert false : "FAILURE : there is another unknow path in the hashWatch";

                }
    
    

                if(stateOfTest) System.out.println("Watch ExecuteLogFileDirectory test passed");
                else System.out.println("TEST FAILED : Watch ExecuteLogFileDirectory test failed");


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Cleanup the saved file if it exists
                if (file1.exists()) {
                    file1.delete();
                }
                if (file2.exists()) {
                    file2.delete();
                }
            
                // Supprimer le répertoire
                if (dir.exists()) {
                    dir.delete();
                }
                HashList.clean();
                HashWatched.clean();


            }
        }catch(Exception e){
        }
    }



    public static void testExecuteLogFileDirectoryMixed() {
        try{    
            boolean stateOfTest = true;
            String dirName = "logDir/";
            String fileName1 = "logFile1.log";
            String fileName2 = "logFile2.log";

            // create a logDirectory and a logFile
            File dir = new File(dirName);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    throw new Exception("FAILURE : impossible to create the directory");
                }
            } 

            File file1 = new File(dir, fileName1);
            if (!file1.exists()) {
                try (FileWriter writer = new FileWriter(file1)) {
                    writer.write("Log Example 1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 

            File file2 = new File(dir, fileName2);
            if (!file2.exists()) {
                try (FileWriter writer = new FileWriter(file2)) {
                    writer.write("Log Example 2");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 

            try {


                CommandCutter command = new CommandCutter("watch logFile "+dirName+" mixed");
                ICommand watch = CommandFactory.createCommand(command);

                
                watch.execute();
    
                List<String> newLogHashTableNames = watch.getCreatedNames(); 
                assert newLogHashTableNames.size() == 1 : "FAILURE : it should be 1 names in the created name, even if nothing new has been created, it's to show the logHashTable that now watch a logFile" ;
                Hashtable<String, LogHashTable> hashList = HashList.getHashList();

                String logHashTableName1 = newLogHashTableNames.get(0);

                assert hashList.containsKey(logHashTableName1) : "FAILURE : the created logHashTable name aren't in the hashList";
                
                LogHashTable logHashTable1 = hashList.get(logHashTableName1);

                Hashtable<String,List<LogHashTable>> hashWatch = HashWatched.getHashWatched();
                assert hashWatch.size()==2 :"FAILURE: The HashWatched should now contain 2 diferent paths";
    
                assert hashWatch.containsKey(dirName+fileName1) : "FAILURE : one of the watched path isn't in the hashList";
    
                for(String key : hashWatch.keySet()){
                    List<LogHashTable> watchingLogHashTables = hashWatch.get(key);
                    if(key.equals(dirName+fileName1)){
                        assert watchingLogHashTables.size()==1 : "FAILURE : it should only be one logHashTable watching logfile/logfile.log in this example";
                        LogHashTable watchingLogHashTable = watchingLogHashTables.get(0);
                        assert watchingLogHashTable.getLogHashTableName().equals(logHashTable1.getLogHashTableName()) : "FAILURE : the elements in hashWatch are incorrect";
                        assert watchingLogHashTable.getLogPaths().equals(logHashTable1.getLogPaths()) : "FAILURE : the elements in hashWatch are incorrect";
                    } 
                    else if(key.equals(dirName+fileName2)) {
                        assert watchingLogHashTables.size()==1 : "FAILURE : it should only be one logHashTable watching logfile/logfile_other.log in this example";
                        LogHashTable watchingLogHashTable = watchingLogHashTables.get(0);
                        assert watchingLogHashTable.getLogHashTableName().equals(logHashTable1.getLogHashTableName()) : "FAILURE : the elements in hashWatch are incorrect";
                        assert watchingLogHashTable.getLogPaths().equals(logHashTable1.getLogPaths()) : "FAILURE : the elements in hashWatch are incorrect";
                    } 
                    else assert false : "FAILURE : there is another unknow path in the hashWatch";

                }
    
    

                if(stateOfTest) System.out.println("Watch ExecuteLogFileDirectoryMixed test passed");
                else System.out.println("TEST FAILED : Watch ExecuteLogFileDirectoryMixed test failed");


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Cleanup the saved file if it exists
                if (file1.exists()) {
                    file1.delete();
                }
                if (file2.exists()) {
                    file2.delete();
                }
            
                // Supprimer le répertoire
                if (dir.exists()) {
                    dir.delete();
                }
                HashList.clean();
                HashWatched.clean();


            }
        }catch(Exception e){
        }
    }


    public static void testExecuteLogFileOne() {
        try{    
            boolean stateOfTest = true;
            String dirName = "logDir/";
            String fileName1 = "logFile1.log";
            String fileName2 = "logFile2.log";

            // create a logDirectory and a logFile
            File dir = new File(dirName);
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    throw new Exception("FAILURE : impossible to create the directory");
                }
            } 

            File file1 = new File(dir, fileName1);
            if (!file1.exists()) {
                try (FileWriter writer = new FileWriter(file1)) {
                    writer.write("Log Example 1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 

            File file2 = new File(dir, fileName2);
            if (!file2.exists()) {
                try (FileWriter writer = new FileWriter(file2)) {
                    writer.write("Log Example 2");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 

            try {


                CommandCutter command = new CommandCutter("watch logFile "+dirName+fileName1);
                ICommand watch = CommandFactory.createCommand(command);

                
                watch.execute();
    
                List<String> newLogHashTableNames = watch.getCreatedNames(); 
                assert newLogHashTableNames.size() == 1 : "FAILURE : it should be 1 names in the created name, even if nothing new has been created, it's to show the logHashTable that now watch a logFile" ;
                Hashtable<String, LogHashTable> hashList = HashList.getHashList();

                String logHashTableName1 = newLogHashTableNames.get(0);

                assert hashList.containsKey(logHashTableName1) : "FAILURE : the created logHashTable name aren't in the hashList";
                
                LogHashTable logHashTable1 = hashList.get(logHashTableName1);

                Hashtable<String,List<LogHashTable>> hashWatch = HashWatched.getHashWatched();
                assert hashWatch.size()==1 :"FAILURE: The HashWatched should now contain 2 diferent paths";
    
                assert hashWatch.containsKey(dirName+fileName1) : "FAILURE : one of the watched path isn't in the hashList";
    
                for(String key : hashWatch.keySet()){
                    List<LogHashTable> watchingLogHashTables = hashWatch.get(key);
                    if(key.equals(dirName+fileName1)){
                        assert watchingLogHashTables.size()==1 : "FAILURE : it should only be one logHashTable watching logfile/logfile.log in this example";
                        LogHashTable watchingLogHashTable = watchingLogHashTables.get(0);
                        assert watchingLogHashTable.getLogHashTableName().equals(logHashTable1.getLogHashTableName()) : "FAILURE : the elements in hashWatch are incorrect";
                        assert watchingLogHashTable.getLogPaths().equals(logHashTable1.getLogPaths()) : "FAILURE : the elements in hashWatch are incorrect";
                    } 
                    
                    else assert false : "FAILURE : there is another unknow path in the hashWatch";
                }
    
    

                if(stateOfTest) System.out.println("Watch ExecuteLogFileOne test passed");
                else System.out.println("TEST FAILED : Watch ExecuteLogFileOne test failed");


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Cleanup the saved file if it exists
                if (file1.exists()) {
                    file1.delete();
                }
                if (file2.exists()) {
                    file2.delete();
                }
            
                // Supprimer le répertoire
                if (dir.exists()) {
                    dir.delete();
                }
                HashList.clean();
                HashWatched.clean();


            }
        }catch(Exception e){
        }
    }



    public static void testExecuteWatchOneListedWithName() {
        boolean stateOfTest = true;
        List<File> filesToDelete = new ArrayList<>();
        try {

            List<String> logPaths = new ArrayList<>();
            logPaths.add("logfile/logfile.log");
            logPaths.add("logfile/logfile_other.log");
            
            LogHashTable logHashTable1 = new LogHashTable(logPaths);
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

            LogHashTable logHashTable2 = new LogHashTable("logfile/logfile_other.log");
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("watch listed "+logHashTableName1);
            ICommand watch = CommandFactory.createCommand(command);

            watch.execute();

            List<String> newLogHashTableNames = watch.getCreatedNames(); 
            assert newLogHashTableNames.size() == 1 : "FAILURE : it should be 1 names in the created name, even if nothing new has been created, it's to show the logHashTable that now watch a logFile" ;
            assert newLogHashTableNames.get(0).equals(logHashTableName1) : "FAILURE : wrong logHashTable name used";

            Hashtable<String,List<LogHashTable>> hashWatch = HashWatched.getHashWatched();

            assert hashWatch.size()==2 :"FAILURE: The HashWatched should now contain 2 diferent paths";

            assert hashWatch.containsKey("logfile/logfile.log") : "FAILURE : one of the watched path isn't in the hashList";
            assert hashWatch.containsKey("logfile/logfile_other.log") : "FAILURE : one of the watched path isn't in the hashList";


            for(String key : hashWatch.keySet()){
                List<LogHashTable> watchingLogHashTables = hashWatch.get(key);
                if(key.equals("logfile/logfile.log")) assert watchingLogHashTables.contains(logHashTable1) : "FAILURE : the elements in hashWatch are incorrect";
                else if(key.equals("logfile/logfile_other.log")) assert watchingLogHashTables.contains(logHashTable1) : "FAILURE : the elements in hashWatch are incorrect";
                else assert false : "FAILURE : there is another unknow path in the hashWatch";
            }

            
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Watch ExecuteWatchOneListedWithName test passed");
            else System.out.println("TEST FAILED : Watch ExecuteWatchOneListedWithName test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
            HashWatched.clean();
        }
    }


    public static void testExecuteWatchOneListedWithPath() {
        boolean stateOfTest = true;
        List<File> filesToDelete = new ArrayList<>();
        try {

            LogHashTable logHashTable1 = new LogHashTable("logfile/logfile.log");
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

            LogHashTable logHashTable2 = new LogHashTable("logfile/logfile_other.log");
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("watch listed logfile/logfile.log");
            ICommand watch = CommandFactory.createCommand(command);

            watch.execute();

            List<String> newLogHashTableNames = watch.getCreatedNames(); 
            assert newLogHashTableNames.size() == 1 : "FAILURE : it should be 2 names in the created name, even if nothing new has been created, it's to show the logHashTable that now watch a logFile" ;


            Hashtable<String,List<LogHashTable>> hashWatch = HashWatched.getHashWatched();

            assert hashWatch.size()==1 :"FAILURE: The HashWatched should now contain 2 diferent paths";

            assert hashWatch.containsKey("logfile/logfile.log") : "FAILURE : one of the watched path isn't in the hashList";


            for(String key : hashWatch.keySet()){
                List<LogHashTable> watchingLogHashTables = hashWatch.get(key);
                if(key.equals("logfile/logfile.log")) assert watchingLogHashTables.contains(logHashTable1) : "FAILURE : the elements in hashWatch are incorrect";
                else assert false : "FAILURE : there is another unknow path in the hashWatch";
            }

            
        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Watch ExecuteWatchOneListedWithPath test passed");
            else System.out.println("TEST FAILED : Watch ExecuteWatchOneListedWithPath test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
            HashWatched.clean();
        }
    }
    


    public static void testExecuteWatchOneSaved() {
        boolean stateOfTest = true;
        List<File> filesToDelete = new ArrayList<>();
        try {

            LogHashTable logHashTable1 = new LogHashTable("logfile/logfile.log");
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

            SaveLogHashTable.save(logHashTable1);

            File savedFile1 = new File(savedLogHashTablePath1);
            filesToDelete.add(savedFile1);


            LogHashTable logHashTable2 = new LogHashTable("logfile/logfile_other.log");
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            String savedLogHashTablePath2 = FILES_SAVE_DIR + "save"+logHashTableName2;
            
            
            SaveLogHashTable.save(logHashTable2);

            File savedFile2 = new File(savedLogHashTablePath2);
            filesToDelete.add(savedFile2);
            

            CommandCutter command = new CommandCutter("watch saved save"+logHashTableName1);
            ICommand watch = CommandFactory.createCommand(command);

            watch.execute();

            List<String> newLogHashTableNames = watch.getCreatedNames(); 
            assert newLogHashTableNames.size() == 1 : "FAILURE : it should be 1 names in the created name, even if nothing new has been created, it's to show the logHashTable that now watch a logFile" ;
            assert newLogHashTableNames.contains(logHashTable1.getLogHashTableName()) : "FAILURE : the name of one of the logHashTable loaded isn't in the created name list";

            Hashtable<String,List<LogHashTable>> hashWatch = HashWatched.getHashWatched();

            assert hashWatch.size()==1 :"FAILURE: The HashWatched should now contain 2 diferent paths";

            assert hashWatch.containsKey("logfile/logfile.log") : "FAILURE : one of the watched path isn't in the hashList";

            for(String key : hashWatch.keySet()){
                List<LogHashTable> watchingLogHashTables = hashWatch.get(key);
                if(key.equals("logfile/logfile.log")){
                    assert watchingLogHashTables.size()==1 : "FAILURE : it should only be one logHashTable watching logfile/logfile.log in this example";
                    LogHashTable watchingLogHashTable = watchingLogHashTables.get(0);
                    assert watchingLogHashTable.getLogHashTableName().equals(logHashTable1.getLogHashTableName()) : "FAILURE : the elements in hashWatch are incorrect";
                    assert watchingLogHashTable.getLogPaths().equals(logHashTable1.getLogPaths()) : "FAILURE : the elements in hashWatch are incorrect";
                }
                else assert false : "FAILURE : there is another unknow path in the hashWatch";

                
                
            }


        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Watch ExecuteWatchOneSaved test passed");
            else System.out.println("TEST FAILED : Watch ExecuteWatchOneSaved test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
            HashWatched.clean();
        }
    }


    



    public static void testExecutewatchNonExistingSavedFile() {
        boolean stateOfTest = true;
        List<File> filesToDelete = new ArrayList<>();
        try {

            LogHashTable logHashTable1 = new LogHashTable("logfile/logfile.log");
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

            SaveLogHashTable.save(logHashTable1);

            File savedFile1 = new File(savedLogHashTablePath1);
            filesToDelete.add(savedFile1);

            CommandCutter command = new CommandCutter("watch saved save"+logHashTableName1);



            ICommand watch = CommandFactory.createCommand(command);

            savedFile1.delete();

            watch.execute();

            stateOfTest = false;
        } catch (Exception e) {
            try{
                assert e.getMessage().equals("Wrong command: File doesn't exist anymore") : "FAILURE: Exception message should be 'Wrong command: File doesn't exist anymore'.";
            }catch (Exception a){
                a.printStackTrace();
                stateOfTest = false;
            }
        }finally {
            if(stateOfTest) System.out.println("Watch ExecutewatchNonExistingSavedFile test passed");
            else System.out.println("TEST FAILED : Watch ExecutewatchNonExistingSavedFile test failed");

            // Cleanup all saved files
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
            HashWatched.clean();
        }
    }

}