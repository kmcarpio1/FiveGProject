package tst.commands;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Hashtable;
import src.config.PathToFile;
import src.hashfunctions.HashTableElement;
import src.hashfunctions.Hashing;
import src.hashfunctions.LogHashTable;
import src.hashfunctions.SaveLogHashTable;

//TODO add an test on loading on files that are not logHashTable (and modifying the load code for it to work properly)


public class TestSaveLogHashTable {


    private static final String FILES_DIR = PathToFile.getPathFile();
    private static final String FILES_WRITE_DIR = PathToFile.getPathWrite();
    private static final String FILES_SAVE_DIR = PathToFile.getPathSave();


    public static void main(String[] args) {
        testSaveLogHashTable();
    }

    public static void testSaveLogHashTable() {
        testSave();
        testLoad();
        testWriteToCSV();
    }

    public static void testSave() {
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

            String filename =  "save"+logHashTable.getLogHashTableName();
            String filepath = FILES_SAVE_DIR  + filename;

            // Check that the save file has been created
            File save = SaveLogHashTable.save(logHashTable);

            assert save.getName().equals(filename) : "FAILURE : the name of the returned save file should be \"save\"+logHashTableName(), and here is it" + save ;

            savedFile = new File(filepath); //does not create a file
            if (savedFile.exists()) {
                System.out.println("SaveHashTable testSave test passed");
            } else {
                System.out.println("FAILURE: The saved file does not exist");
            }

        } catch (Exception e) {
            System.err.println("Exception during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
        }
    }

    public static void testLoad() {
        File savedFile = null;
        try {
            LogHashTable logHashTable = new LogHashTable();
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element1 = new HashTableElement(rawData, timeReceived, IP, "path");
            logHashTable.addLog(element1);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "path");
            HashTableElement element2 = logHashTable.getLogTable().get(Hashing.getMd5Hash(rawData2));

            String filename = "save" + logHashTable.getLogHashTableName();
            String filepath = FILES_SAVE_DIR + filename;

            // Check that the save file has been created
            SaveLogHashTable.save(logHashTable);

            savedFile = new File(filepath);



            LogHashTable loadedLogHashTable = SaveLogHashTable.load(filename);
            Hashtable<String, HashTableElement> loadedHashTable = loadedLogHashTable.getLogTable();
            String loadedHashTableName = loadedLogHashTable.getLogHashTableName();

            assert loadedHashTable.size() == 2 : "FAILURE: Loaded table size should be 2";
            assert loadedHashTable.containsKey(element1.getHash()) : "FAILURE: Loaded table should contain element1";
            assert loadedHashTable.containsKey(element2.getHash()) : "FAILURE: Loaded table should contain element2";
            assert loadedHashTableName.equals(logHashTable.getLogHashTableName()) : "FAILURE: Loaded table does not have the right name";

            System.out.println("SaveHashTable testLoad test passed");

        } catch (Exception e) {
            System.err.println("Exception during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
        }
    }

    public static void testWriteToCSV() {
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

            String filename = "tab_" + logHashTable.getLogHashTableName();
            String filepath = FILES_WRITE_DIR + filename;

            // Check that the save file has been created
            File written = SaveLogHashTable.writeToCSV(logHashTable);
            savedFile = new File(filepath); //does not create a file

            assert written.getName().equals(filename) : "FAILURE : the name of the returned written file should be \"tab_\"+logHashTableName(), and here is it" + written ;


            if (savedFile.exists()) {
                System.out.println("SaveHashTable testWriteToCSV test passed");
            } else {
                System.out.println("FAILURE: The CSV file does not exist");
            }

        } catch (Exception e) {
            System.err.println("Exception during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (savedFile != null && savedFile.exists()) {
                savedFile.delete();
            }
        }
    }
}