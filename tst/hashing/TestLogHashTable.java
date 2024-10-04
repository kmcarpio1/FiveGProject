package tst.hashing;

import java.time.LocalDateTime;
import src.hashfunctions.HashTableElement;
import src.hashfunctions.Hashing;
import src.hashfunctions.LogHashTable;



public class TestLogHashTable {

    public static void testLogHashTable() {
        testAddLog();
        testGetters();
    }

    public static void testAddLog() {
        try {
            LogHashTable logHashTable = new LogHashTable();

            // Test case: add log using HashTableElement
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "path");
            logHashTable.addLog(element);

            assert logHashTable.getLogTable().size() == 1 : "FAILURE: LogTable size should be 1";

            // Test case: add log using raw data and IP
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP,"path");

            assert logHashTable.getLogTable().size() == 2 : "FAILURE: LogTable size should be 2";

            // Test case: add log using raw data, time, and IP
            LocalDateTime timeReceived2 =  LocalDateTime.of(2024, 7, 22, 14, 30);
            logHashTable.addLog(rawData, timeReceived2, IP, "path");

            assert logHashTable.getLogTable().size() == 2 : "FAILURE: LogTable size should still be 2 due to hash collision";

            // Check that the logs have the correct hashes and data
            HashTableElement elementFromTable = logHashTable.getLogTable().get(Hashing.getMd5Hash(rawData));
            assert elementFromTable.getRawData().equals(rawData) : "FAILURE: Raw data mismatch for hash";
            assert elementFromTable.getTimeReceived().equals(timeReceived2) : "FAILURE: Time received mismatch for hash";

            System.out.println("LogHashTable addLog test passed");

        } catch (Exception e) {
            System.err.println("Exception during test: " + e.getMessage());
            //e.printStackTrace();
        }
    }

    public static void testGetters() {
        try {
            LogHashTable logHashTable = new LogHashTable();
            String hashTableName = logHashTable.getLogHashTableName();

            // The name should start with "logHashTable_" and contain a date-time
            assert hashTableName.startsWith("logHashTable_") : "FAILURE: HashTable name should start with \"logHashTable_\"";
            assert hashTableName.length() > 15 : "FAILURE: HashTable name should include date-time";

            System.out.println("LogHashTable getters test passed");

        } catch (Exception e) {
            System.err.println("Exception during test: " + e.getMessage());
            //e.printStackTrace();
        }
    }
}