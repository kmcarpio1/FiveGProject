package src.threadfunctions;

import java.io.RandomAccessFile;
import java.util.List;
import src.config.Configurations;
import src.hashfunctions.HashWatched;
import src.hashfunctions.LogHashTable;
import src.logger.MyLogger;

public class logFileListener {

    private static final long TIMEOUT_MS = 10000;  // max time before getting out of the loop (10sec)


    

    public static void listen(String logFilePath){
        try (RandomAccessFile logFile = new RandomAccessFile(logFilePath, "r")){
            long lastModified = System.currentTimeMillis();
            long currentPointer = logFile.length();

            while(HashWatched.containsKey(logFilePath)){ //if it's not watched anymore, we stop watching it
                long fileLenght = logFile.length();
                if(currentPointer < fileLenght){ //if something new as been written
                    //System.out.println("a log has been read");
                    logFile.seek(currentPointer); //we put the pointer at the last readed position

                    byte[] buffer = new byte[(int)(fileLenght - currentPointer)]; //we create a buffer of the size of what have just been written in the logFile
                    logFile.readFully(buffer); //and we read from the currentPointer to the end (the lenghth of the buffer)

                    String newLog = new String(buffer);
                    
                    String[] logEntries = newLog.split(System.lineSeparator()); //we separate every line in case multiple logs have been merged together
                    //System.out.println(newLog);
                    MyLogger.writeLog("a log has been read in "+ logFilePath);
                    for (String entry : logEntries){
                        if(!entry.trim().isEmpty()){
                            List<LogHashTable> logHashTables = HashWatched.getLogHashTable(logFilePath);
                            if(logHashTables != null && !logHashTables.isEmpty()){
                                for(LogHashTable logHashTable : logHashTables){
                                    logHashTable.addLog(entry.trim(), Configurations.getMachineIP(), logFilePath);
                                }
                            }
                        }
                    }
                    lastModified = System.currentTimeMillis();  //Reset the timeout 
                    currentPointer = logFile.getFilePointer();  //new pointer


                }

                //else{
                //    if(System.currentTimeMillis() - lastModified > TIMEOUT_MS){
                //        System.out.println("No new logLine, we stop looking at the logfile:" + logFilePath);
                //        break;
                //    }
                //    //we can add a waiting time here maybe
                //}
                //Thread.sleep(100);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
}
