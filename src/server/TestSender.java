package src.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import src.config.Configurations;

public class TestSender {
    
    /**            
     * method that test the connection to the machine corresponding at the IP adress at the indicated port
     * it's sending a message to the provided IP adress on the provided port, and waits a specific response befer a timeout
     * @param ip
     * @param port
     * @return
     */
    public static boolean testConnection(String ip, int port, String message) {
        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(ip, port), Configurations.getTimeOut());
            try(
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                out.println(message);
                String response = in.readLine();
                //System.out.println(response);
                if(response != null && response.equals(Configurations.getConnectionConfirmationMessage())) {
                    return true;
                }
                else  return false; 
            }
        } catch (Exception e) {
            return false; 
        }
    }
}
