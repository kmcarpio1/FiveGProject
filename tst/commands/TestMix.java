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
import src.hashfunctions.Hashing;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;

public class TestMix {
    
    private static final String FILES_SAVE_DIR = PathToFile.getPathSave();
    private static final String FILES_WRITE_DIR = PathToFile.getPathWrite();

    public static void testMix() {
        testConstructorListed();
        testConstructorSaved();
        testExecuteMixListed();
        testExecuteMixSaved();
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

            // Test of the constructor with the "listed" argument
            try {
                CommandCutter command1 = new CommandCutter("mix listed");
                ICommand mix1 = CommandFactory.createCommand(command1);
                assert mix1.getArgument().equals("listed") : "FAILURE: Command 'mix listed' should set _name to 'listed'.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

 
            try {
                CommandCutter command2= new CommandCutter("mix");
                ICommand mix2 = CommandFactory.createCommand(command2);
                stateOfTest = false;
                assert false : "FAILURE: Command 'mix' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command3 = new CommandCutter("mix listed all");
                ICommand mix3 = CommandFactory.createCommand(command3);
                stateOfTest = false;
                assert false : "FAILURE: Command 'mix listed all' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

           
            try {
                CommandCutter command4 = new CommandCutter("mix thing");
                ICommand mix4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'mix thing' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong argument") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            if(stateOfTest) System.out.println("Mix constructorListed test passed");
            else System.out.println("TEST FAILED : Mix constructorListed test failed");


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

      
            HashList.addLogHashTable(logHashTableName,logHashTable);



            // Test of the constructor with the "listed" argument
            try {
                CommandCutter command1 = new CommandCutter("mix saved");
                ICommand mix1 = CommandFactory.createCommand(command1);
                assert mix1.getArgument().equals("saved") : "FAILURE: Command 'mix saved' should set _name to 'listed'.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

 
            try {
                CommandCutter command2= new CommandCutter("mix");
                ICommand mix2 = CommandFactory.createCommand(command2);
                stateOfTest = false;
                assert false : "FAILURE: Command 'mix' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }
            try {
                CommandCutter command3 = new CommandCutter("mix saved all");
                ICommand mix3 = CommandFactory.createCommand(command3);
                stateOfTest = false;
                assert false : "FAILURE: Command 'mix saved all' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

           
            try {
                CommandCutter command4 = new CommandCutter("mix thing");
                ICommand mix4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'mix thing' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong argument") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            if(stateOfTest) System.out.println("Mix constructorSaved test passed");
            else System.out.println("TEST FAILED : Mix constructorSaved test failed");


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



    public static void testExecuteMixListed() {
        boolean stateOfTest = true;
        try {

            LogHashTable logHashTable1 = new LogHashTable("important/path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"important/path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            HashTableElement element2 = new HashTableElement(rawData2, timeReceived, IP,"important/path");
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();

            HashList.addLogHashTable(logHashTableName1,logHashTable1);

            LogHashTable logHashTable2 = new LogHashTable("lame/path");

            String rawData3 = "Log entry 3";
            HashTableElement element3 = new HashTableElement(rawData3, timeReceived, IP,"lame/path");
            
            String rawData4 = "Log entry 4";
            HashTableElement element4 = new HashTableElement(rawData4, timeReceived, IP,"lame/path");
            
            logHashTable2.addLog(element3);
            logHashTable2.addLog(rawData4, IP, "lame/path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("mix listed");
            ICommand mix = CommandFactory.createCommand(command);

            mix.execute(); //a new logHashTable should be in hashList
            Hashtable<String,LogHashTable> hashlist = HashList.getHashList();

            assert hashlist.size()==3 :"FAILURE: The HashList should now contain 3 logHashTable";


            LogHashTable mixLogHashTable = null;
            for(String key : hashlist.keySet()){
                LogHashTable loghashTable = hashlist.get(key);
                if(loghashTable != logHashTable1 && loghashTable != logHashTable2){
                    mixLogHashTable = loghashTable;
                }
            }

            assert mixLogHashTable!=null : "FAILURE : the mixed logHashTable hasn't been found";

            Hashtable<String,HashTableElement> mixTable = mixLogHashTable.getLogTable();

            
            assert mix.getCreatedNames().size()==1 : "FAILURE : the number of created logHashTable isn't the right one. It should be 1 and it is " +mix.getCreatedNames().size();
            String mixName = mix.getCreatedNames().get(0);
            assert mixName==mixLogHashTable.getLogHashTableName() : "FAILURE : the created name returned by getCreatedNames isn't the right one";

            //now we are going to check if every logHashElement are in the mixLogHashTable

            assert mixTable.containsKey(Hashing.getMd5Hash(rawData)) : "FAILURE : the mixLogHashTable do not contain an element of one of the logHashTables";
            assert mixTable.containsKey(Hashing.getMd5Hash(rawData2)) : "FAILURE : the mixLogHashTable do not contain an element of one of the logHashTables";
            assert mixTable.containsKey(Hashing.getMd5Hash(rawData3)) : "FAILURE : the mixLogHashTable do not contain an element of one of the logHashTables";
            assert mixTable.containsKey(Hashing.getMd5Hash(rawData4)) : "FAILURE : the mixLogHashTable do not contain an element of one of the logHashTables";


            //we'll finally check the  logFile paths

            List<String> logPaths = mixLogHashTable.getLogPaths();

            assert logPaths.contains("important/path") : "FAILURE : the mixed logHashTable logPath list does not contain one of the paths";
            assert logPaths.contains("lame/path") : "FAILURE : the mixed logHashTable logPath list does not contain one of the paths";

        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Mix ExecuteMixListed test passed");
            else System.out.println("TEST FAILED : Mix ExecuteMixListed test failed");
            HashList.clean();
        }
    }


    public static void testExecuteMixSaved() {
        boolean stateOfTest = true;
        List<File> filesToDelete = new ArrayList<>();

        try {

            LogHashTable logHashTable1 = new LogHashTable("important/path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"important/path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            HashTableElement element2 = new HashTableElement(rawData2, timeReceived, IP,"important/path");
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();

            File savedFile1 = SaveLogHashTable.save(logHashTable1);
            filesToDelete.add(savedFile1);

            LogHashTable logHashTable2 = new LogHashTable("lame/path");

            String rawData3 = "Log entry 3";
            HashTableElement element3 = new HashTableElement(rawData3, timeReceived, IP,"lame/path");
            
            String rawData4 = "Log entry 4";
            HashTableElement element4 = new HashTableElement(rawData4, timeReceived, IP,"lame/path");
            
            logHashTable2.addLog(element3);
            logHashTable2.addLog(rawData4, IP, "lame/path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            File savedFile2 = SaveLogHashTable.save(logHashTable2);
            filesToDelete.add(savedFile2);

            CommandCutter command = new CommandCutter("mix saved");
            ICommand mix = CommandFactory.createCommand(command);

            mix.execute(); //a new logHashTable should be in hashList
            Hashtable<String,LogHashTable> hashlist = HashList.getHashList();

            assert hashlist.size()==1 :"FAILURE: The HashList should now contain 3 logHashTable";


            LogHashTable mixLogHashTable = null;
            for(String key : hashlist.keySet()){
                LogHashTable loghashTable = hashlist.get(key);
                if(loghashTable != logHashTable1 && loghashTable != logHashTable2){
                    mixLogHashTable = loghashTable;
                }
            }

            assert mixLogHashTable!=null : "FAILURE : the mixed logHashTable hasn't been found";

            Hashtable<String,HashTableElement> mixTable = mixLogHashTable.getLogTable();

            
            assert mix.getCreatedNames().size()==1 : "FAILURE : the number of created logHashTable isn't the right one. It should be 1 and it is " +mix.getCreatedNames().size();
            String mixName = mix.getCreatedNames().get(0);
            assert mixName==mixLogHashTable.getLogHashTableName() : "FAILURE : the created name returned by getCreatedNames isn't the right one";

            //now we are going to check if every logHashElement are in the mixLogHashTable

            assert mixTable.containsKey(Hashing.getMd5Hash(rawData)) : "FAILURE : the mixLogHashTable do not contain an element of one of the logHashTables";
            assert mixTable.containsKey(Hashing.getMd5Hash(rawData2)) : "FAILURE : the mixLogHashTable do not contain an element of one of the logHashTables";
            assert mixTable.containsKey(Hashing.getMd5Hash(rawData3)) : "FAILURE : the mixLogHashTable do not contain an element of one of the logHashTables";
            assert mixTable.containsKey(Hashing.getMd5Hash(rawData4)) : "FAILURE : the mixLogHashTable do not contain an element of one of the logHashTables";


            //we'll finally check the  logFile paths

            List<String> logPaths = mixLogHashTable.getLogPaths();

            assert logPaths.contains("important/path") : "FAILURE : the mixed logHashTable logPath list does not contain one of the paths";
            assert logPaths.contains("lame/path") : "FAILURE : the mixed logHashTable logPath list does not contain one of the paths";

        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Mix ExecuteMixSaved test passed");
            else System.out.println("TEST FAILED : Mix ExecuteMixSaved test failed");
            for (File file : filesToDelete) {
                if (file.exists()) {
                    file.delete();
                }
            }
            HashList.clean();
        }
    }

}

