
package tst.hashing;


import java.time.LocalDateTime;
import src.hashfunctions.HashTableElement;
import src.hashfunctions.Hashing;

public class TestHashTableElement {

    public static void testHashTableElement() {
        testConstructorAndGetters();
        testToString();
    }

    public static void testConstructorAndGetters() {
        try {
            // Test case: Create a HashTableElement and verify its properties
            String rawData = "Test log data";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            String expectedHash = Hashing.getMd5Hash(rawData);

            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "path");

            // Check the rawData
            assert element.getRawData().equals(rawData) : "FAILURE: Raw data mismatch";

            // Check the hash
            assert element.getHash().equals(expectedHash) : "FAILURE: Hash value mismatch";

            // Check the timeReceived
            assert element.getTimeReceived().equals(timeReceived) : "FAILURE: Time received mismatch";

            // Check the IP
            assert element.getIP().equals(IP) : "FAILURE: IP address mismatch";

            System.out.println("HashTableElement constructor and getters test passed");

        } catch (Exception e) {
            System.err.println("Exception during test: " + e.getMessage());
            //e.printStackTrace();
        }
    }

    public static void testToString() {
        try {
            // Test case: Check the toString method
            String rawData = "Test log data";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "path");
            String logPath = "path";
            String expectedString = "HashTableElement{" +
                    "rawData='" + rawData + '\'' +
                    ", hash='" + Hashing.getMd5Hash(rawData) + '\'' +
                    ", date=" + timeReceived +
                    ", ipAddress='" + IP + '\'' +
                    ", logPath='" + logPath + '\'' +
                    '}';

            assert element.toString().equals(expectedString) : "FAILURE: toString method output mismatch";

            System.out.println("HashTableElement toString test passed");

        } catch (Exception e) {
            System.err.println("Exception during test: " + e.getMessage());
            //e.printStackTrace();
        }
    }


}