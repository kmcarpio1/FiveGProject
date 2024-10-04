package src.threadfunctions;

import java.io.RandomAccessFile;

public class logFileFillerDemo {
    
    public static void filler(String logFilePath){
        Integer count = 0;
        try (RandomAccessFile logFile = new RandomAccessFile(logFilePath, "rw")){

            while (true) { 
                String log = "this is the log number "+count+". \n";

                logFile.seek(logFile.length()); // positionning at the end of the file

                logFile.writeBytes(log+"\n");



                count++;
                Thread.sleep(4000);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void filler_other(String logFilePath){
        Integer count = 0;
        try (RandomAccessFile logFile = new RandomAccessFile(logFilePath, "rw")){

            while (true) { 
                String log = "this is the log number "+count+" from the other logFile. \n";

                logFile.seek(logFile.length()); // positionning at the end of the file

                logFile.writeBytes(log+"\n");



                count++;
                Thread.sleep(4000);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
