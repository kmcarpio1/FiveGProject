package src.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyLogger {

    private static final String LOG_FILE_PATH = "src/logger/log.log";
    private static File logFile = new File(LOG_FILE_PATH); // Initialized once

    // Method to create the log file if it does not exist
    private static void createLogFile() {
        try {
            if (logFile.createNewFile()) {
                writeLog("Log file has been created: " + logFile.getName());
            }
        } catch (IOException e) {
            System.err.println("Error while creating the log file.");
            e.printStackTrace();
        }
    }

    // Method to write a message to the log file with a timestamp
    public static void writeLog(String message) {
        // Automatically create the log file if it doesn't exist
        if (!logFile.exists()) {
            createLogFile();
        }

        try (FileWriter writer = new FileWriter(logFile, true)) {
            // Formatting the current date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Write the log with the date, time, and message
            writer.write("[" + now.format(formatter) + "] " + message + "\n");
        } catch (IOException e) {
            System.err.println("Error while writing to the log file.");
            e.printStackTrace();
        }
    }

}