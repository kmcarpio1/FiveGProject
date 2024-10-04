package src.hashfunctions;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * The class LogHashTable is the center of the project
 * Its instances will each be able to contain lohHashTableElements, each representing a log
 * Indeed, it's in a logHashTable that will be stocked the differents logs of one or multiple logFile watched.
 * It will also be saved, loaded, or converted and written in CSV
 */
public class LogHashTable implements Serializable { //implement serializable to be able to save the object
    private static final long serialVersionUID = 1L; 
    
    private Hashtable<String,HashTableElement> logTable; //the hashKey and the HashTableElement
    private String hashTableName; //the name of the logHashTable
    private List<String> logPaths; //the list of paths of logFiles whose logs are in the loghashTable

    /**
     * Constructor of logHashTable with no arguments
     */
    public LogHashTable(){
        logTable = new Hashtable<>();
        hashTableName = "logHashTable_" + LocalDateTime.now(); //give a generic name to the logHashTable
        this.logPaths = new ArrayList<>(); 
        //System.out.println("Creation of a new LogHashTable names "+hashTableName);
    }

    /**
     * Constructor of logHashTable
     * @param copyLogTable : a hashTable to put in the logHashTable
     */
    public LogHashTable(Hashtable<String,HashTableElement> copyLogTable){
        this.logTable = copyLogTable;
        hashTableName = "logHashTable_" + LocalDateTime.now(); //give a generic name to the logHashTable
        this.logPaths = new ArrayList<>(); //TODO : we could check all the logs in copylogtable and retrieve all the logPaths
        //System.out.println("Creation of a new LogHashTable names "+hashTableName);
    }

    /**
     * Constructor of logHashTable
     * @param logPath : one logPath
     */
    public LogHashTable(String logPath){
        logTable = new Hashtable<>();
        hashTableName = "logHashTable_" + LocalDateTime.now(); //give a generic name to the logHashTable
        this.logPaths = new ArrayList<>(); 
        this.logPaths.add(logPath);
        //System.out.println("Creation of a new LogHashTable names "+hashTableName+ " watching "+logPath);
    }

    /**
     * Constructor of logHashTable
     * @param logTable : a hashTable to put in the logHashTable
     * @param logPath : one logPath
     */
    public LogHashTable(Hashtable<String,HashTableElement> logTable,String logPath){
        this.logTable = logTable;
        hashTableName = "logHashTable_" + LocalDateTime.now(); //give a generic name to the logHashTable
        this.logPaths = new ArrayList<>();
        this.logPaths.add(logPath);
        //System.out.println("Creation of a new LogHashTable names "+hashTableName+ " watching "+logPath);
    }

    /**
     * Constructor of logHashTable
     * @param logPaths : a list of logPath
     */
    public LogHashTable(List<String> logPaths){
        logTable = new Hashtable<>(); 
        hashTableName = "logHashTable_" + LocalDateTime.now(); //give a generic name to the logHashTable
        this.logPaths = logPaths;

        //String announce = "Creation of a new LogHashTable names "+hashTableName+ " watching";
        //for(String logP : logPaths){
        //    announce += " " +logP;
        //}    
        //System.out.println(announce);
    }

    /**
     * Constructor of logHashTable
     * @param logTable : a hashTable to put in the logHashTable
     * @param logPaths : a list of logPath
     */
    public LogHashTable(Hashtable<String,HashTableElement> logTable,List<String> logPaths){
        this.logTable = logTable; 
        hashTableName = "logHashTable_" + LocalDateTime.now(); //give a generic name to the logHashTable
        this.logPaths = logPaths;

        //String announce = "Creation of a new LogHashTable names "+hashTableName+ " watching";
        //for(String logP : logPaths){
        //    announce += " " +logP;
        //}    
        //System.out.println(announce);
    }

    /**
     * To set the logPaths list with one logPath 
     * @param logPath : the only logPath that LogPaths should contain 
     */
    public void setLogPath(String logPath){
        this.logPaths = new ArrayList<>();
        this.logPaths.add(logPath); 
    }

    /**
     * To set the logPaths list with a list of logPath 
     * @param logPaths : the list of logPath that LogPaths should contain 
     */
    public void setLogPaths(List<String> logPaths){
        this.logPaths = logPaths;
    }

    /**
     * A method to add a log (in the form of a HashTableElement) in the logHashTable
     * @param el : a HashTableElement 
     */
    public void addLog(HashTableElement el){
        logTable.put(el.getHash(), el); //we add it with his hash key for the hashTable key
    }

    /**
     * A method to add a log (with all the raw informations) in the logHashTable
     * @param rawData : the rawData of the log
     * @param IP : the IP from the user it comes from
     * @param logPath : the path to the logFile
     */
    public void addLog(String rawData, String IP, String logPath){
        HashTableElement el = new HashTableElement(rawData, LocalDateTime.now(), IP,logPath); //we create a HashTableElement with the current time
        logTable.put(el.getHash(), el); //and add it
    }

    /**
     * A method to add a log (with all the raw informations) in the logHashTable
     * @param rawData : the rawData of the log
     * @param t : the time the log has been received  
     * @param IP : the IP from the user it comes from
     * @param logPath : the path to the logFile
     */
    public void addLog(String rawData, LocalDateTime t, String IP, String logPath){
        HashTableElement el = new HashTableElement(rawData, t, IP, logPath);//we create a HashTableElement
        logTable.put(el.getHash(), el); //and add it
    }

    /**
     * The method mix another logHashTabel with the actual instance. 
     * It's adding all the elements in the logTable, and the new logPathNames in logPaths
     * @param logHashTable : a logHashTable to mix with the actual instance
     */
    public void addLogHashTable(LogHashTable logHashTable){

        List<String> otherLogPaths = logHashTable.getLogPaths();
        if(otherLogPaths != null){//in the case that the other logHashTable has multiple logPaths
            for(String instanceOtherLogPaths : otherLogPaths){ //for each logPath we have to make sure it's not already in the List, and add it 
                if(!logPaths.contains(instanceOtherLogPaths)){ // we make sure it's not already in the List
                    logPaths.add(instanceOtherLogPaths); // and add it if it's not
                }
            }
        }
        Hashtable<String,HashTableElement> otherLogTable = logHashTable.getLogTable();
        for (String key : otherLogTable.keySet()) {
            logTable.put(key, otherLogTable.get(key)); // we add all the values and replace them if they already exists
        }
    }


    /**
     * get logTable method
     * @return the logTable
     */
    public Hashtable<String, HashTableElement> getLogTable(){
        return logTable;
    }

    /**
     * get LogTable name method
     * @return the LogTable name
     */
    public String getLogHashTableName(){
        return hashTableName;
    }

    /**
     * get LogTable logPaths method
     * @return the LogTable list of logPath
     */
    public List<String> getLogPaths(){
        return logPaths;
    } 

    public boolean isEmpty(){
        return (logTable.equals(new Hashtable<>()));
    }

    public void clean(){
        logTable = new Hashtable<>();
    }

}