package src.hashfunctions;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * this class is the HashTableElement
 * Each of its instances is supposed to be an element of the LogHashTable, and represents a log of the logHashTable(s) that the logHashTable has observed or is observing
 */
public class HashTableElement implements Serializable { //implement serializable to be able to save the object

    private static final long serialVersionUID = 1L; //imperative to implement serializable

    private String rawData; //the log rawData
    private String hash; //the hashing key of the rawData
    private LocalDateTime timeReceived; //the time the log was received
    private String IP; //the IP adress from where the log comes from 
    private String logPath; //the logPath to the logFile where the log is from


    
    /**
     * Constructor of the HashTableElement
     * 
     * @param rawData : the rawData of the log
     * @param timeReceived : the time the log was received
     * @param IPthe : IP adress from where the log comes from
     * @param logPath : the logPath to the logFile where the log is from
     */
    public HashTableElement(String rawData, LocalDateTime timeReceived, String IP, String logPath){
        this.rawData = rawData;
        this.timeReceived = timeReceived;
        this.IP = IP;
        this.hash = Hashing.getMd5Hash(rawData); //to hash the rawData
        this.logPath = logPath;
    }



    /**
     * getRawData method
     * @return rawData
     */
    public String getRawData(){
        return rawData;
    }

    /**
     * getHash method
     * @return hash key
     */
    public String getHash(){
        return hash;
    }

    /**
     * getTimeReceived method
     * @return the time the log was received
     */
    public LocalDateTime getTimeReceived(){
        return timeReceived;
    }

    /**
     * getTimeReceived method
     * @return the time the log was received
     */
    public String getTimeReceivedString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSS");
        return timeReceived.format(formatter);
    }

    /**
     * getIp method
     * @return IP
     */
    public String getIP(){
        return IP;
    }

    /**
     * getLogPath method
     * @return logPath to the logFile where the rawData comes from
     */
    public String getLogPath(){
        return logPath;
    }

    /**
     * method to convert all the information into a String
     */
    @Override
    public String toString() {
        return "HashTableElement{" +
                "rawData='" + rawData + '\'' +
                ", hash='" + hash + '\'' +
                ", date=" + timeReceived +
                ", ipAddress='" + IP + '\'' +
                ", logPath='" + logPath + '\'' +
                '}';
    }


}