package src.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import src.config.Configurations;
import src.config.PathToFile;
import src.logger.MyLogger;


public class FileSender {
    

    public static void connect(String savedFileName, String serverIP) throws Exception{
        String filePath = PathToFile.getPathSave()+savedFileName;
        try {
            File savedFile = new File(filePath);
            if (!savedFile.exists() || !savedFile.canRead()) {
                throw new FileNotFoundException("File does not exist or cannot be read: " + savedFile.getAbsolutePath());
            }
            
            try (Socket socket = new Socket()){
                socket.connect(new InetSocketAddress(serverIP, Configurations.getListeningPort()), Configurations.getTimeOut()); //timeout for connection
                try(
                    FileInputStream fileIn = new FileInputStream(savedFile); 
                    BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);
                    BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream())); //it is meant to read what's going to be sent to this machine through the socket
                    OutputStream socketOut = socket.getOutputStream()) { //all these are going to be automatically closed thanks to the try-with-ressources

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = bufferedIn.read(buffer)) != -1) {
                        socketOut.write(buffer, 0, bytesRead);
                    }
                    socketOut.flush(); //we are sending all the informations 
                    
                    socket.shutdownOutput(); //it indicates to the other machine that no more data will be sent
                    
                    socket.setSoTimeout(Configurations.getReceptionTimeOut()); //timeout to read data


                    try{
                        String response= socketIn.readLine();
                        if(!response.equals(Configurations.getReceptionConfirmationMessage())) {
                            throw new Exception("The message received from the center machine ("+response+") is not the expected one: "+Configurations.getReceptionConfirmationMessage()+".");
                        }

                        //System.out.println("A socket has been sent");
                        MyLogger.writeLog("A socket has been sent");
                    }catch (SocketTimeoutException e) {
                        System.err.println("Error sending file, never received a response, loss of data: " + e.getMessage());
                    }        
                }//the exception of the second try are taken in charge by the first

            } catch (SocketTimeoutException e) {
                System.err.println("Error connecting to the server, never sending a package, loss of data: " + e.getMessage());
            }catch (IOException e) {
                System.err.println("Error sending file: " + e.getMessage());
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }



}
