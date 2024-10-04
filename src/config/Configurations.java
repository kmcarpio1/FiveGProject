package src.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import src.server.TestSender;
/**
 * A class meant to stock some configurations
 */
public class Configurations {


    private static final String CONFIG_FILE = "src/config/config.ini";
    private static final String IP_KEY = "centerMachineAddress";

    // Main interface to check for the IP adress of the openStack machines
    private static String primaryInterface = "ens3";
    // Secondary interface to check if primary not found
    private static String secondaryInterface = "ens4";


    private static final int TIMEOUT = 5000; 
    private static final int RECEPTION_TIMEOUT = 30000; 
    private static final int TEST_PORT = 6000; 

    private static String firstTestingMessage = "are you there ?"; //the message for the first connection, or when the connexion has been cut
    private static String testingMessage = "are you still there ?"; //the message for the other connction
    private static String confirmingConnection = "yes, i'm here."; //the return message of the server
    private static String confirmingReception = "I received a logHashTable from you."; //the reception confirmation message

    //this is the listening port of the central machine, where it will receive the informations
    static private int _listeningPort = 5000;

    //thi is the IP adress of the central machine
    static private String _serverIP;
    static private String _machineIP;

    static private Boolean _isConnectionUp = false; //this boolean indicate if the connection with the server is up or not
    //static private Boolean _wasConnectionUp = false; //this boolean indicate if the connection with the server was up or not

    /**
     * method to know if the connection with the server is up (it's supposed to be always false on the central machine, since it is the server)
     * @return
     */
    public static boolean getIfTheConnectionIsUp(){
        return _isConnectionUp;
    }
    
    /**
     * Method to set if the connection with the central machine is up
     * @param isConnectionUp
     */
    public static void setIfTheConnectionIsUp(boolean isConnectionUp){
        _isConnectionUp = isConnectionUp;
    }

    //    /**
    // * method to know if the connection with the server was up (it's supposed to be always false on the central machine, since it is the server)
    // * @return
    // */
    //public static boolean getIfTheConnectionWasUp(){
    //    return _wasConnectionUp;
    //}
    //
    ///**
    // * Method to set if the connection with the central machine was up
    // * @param isConnectionUp
    // */
    //public static void setIfTheConnectionWasUp(boolean wasConnectionUp){
    //    _wasConnectionUp = wasConnectionUp;
    //}



        
    /**
     * Method to get the reception confirmation message
     * @return
     */
    public static String getReceptionConfirmationMessage(){
        return confirmingReception;
    }




    /**
     * Method to get the connection confirmation message
     * @return
     */
    public static String getConnectionConfirmationMessage(){
        return confirmingConnection;
    }

    /**
     * Method to get the test message
     * @return
     */
    public static String getTestMessage(){
        return testingMessage;
    }

    /**
     * Method to get the first test message
     * @return
     */
    public static String getFirstTestMessage(){
        return firstTestingMessage;
    }

    /**
     * Method to get _listeningPort
     * @return
     */
    public static int getListeningPort(){
        return _listeningPort;
    }

    /**
     * Method to get _listeningPort
     * @return
     */
    public static int getTestPort(){
        return TEST_PORT;
    }


    /**
     * Method to get TIMEOUT
     * @return
     */
    public static int getTimeOut(){
        return TIMEOUT;
    }

    /**
     * Method to get RECEPTION_TIMEOUT
     * @return
     */
    public static int getReceptionTimeOut(){
        return RECEPTION_TIMEOUT;
    }

    /**
     * Method to get _serverIP
     * @return
     */
    public static String getServerIP(){
        return _serverIP;
    }

    /**
     * Method to get _machineIP
     * @return
     */
    public static String getMachineIP(){
        return _machineIP;
    }


    
    /**
     * This method checks if the serverIP provided is correct (and if it's reachable)
     * @param ip
     * @return
     */
    private static boolean checkServerIP(String ip){
        //we need to check if it's a correct one
        if (ip.isEmpty()) { //if it's empty, it's not good
            System.out.println("");
            System.out.println("You provided an empty server IP."); 
        } else if (!isValidIP(ip)) { //if it's not a valid IP, we ask again
            System.out.println("");
            System.out.println("The server IP address format is incorrect.");
        } else if (!TestSender.testConnection(ip, TEST_PORT, firstTestingMessage)) {   //we use the port 6000 to test the connection and to not toggle the code of the server
            System.out.println("");
            System.out.println("Cannot connect to the server at the provided IP.");
        } else { //in another case it should be a good adress IP
            setIfTheConnectionIsUp(true);
            return true;
        }
        return false;
    }

    /**
     * This method is meant to be called to check the connexion during the loop
     * If the connection is up, we are sending the testing message, and the server will not diplay anything.
     * If the connection is down tho, it's like a new connection. Indeed, the server will then diplay that there is a new connection.
     * It's only checking if the connection is up, it soesn't change the value.
     * @return
     */
    public static boolean checkingConnection(){
        String messageToSend;
        if(_isConnectionUp) messageToSend = testingMessage; //i'm doing that for the central machine to be abble to print if the other machine are reconnecting.
        else messageToSend = firstTestingMessage;
        if (_serverIP.isEmpty() || _serverIP == null || !TestSender.testConnection(_serverIP, TEST_PORT, messageToSend)){
            System.out.println("The connection to the server has failed.");
            //setIfTheConnectionIsUp(false);
            return false;
        }
        else{
            //setIfTheConnectionIsUp(true);
            return true;
        }
    }
    


    public static void findServerIp(){
        Properties properties = new Properties();
        File configFile = new File(CONFIG_FILE);

        // loading config.ini
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error while reading config.ini: " + e.getMessage());
            return;
        }

        // we read the IP address
        String ipAddress = properties.getProperty(IP_KEY);

        // we check if the ip adress is correct
        if (ipAddress == null || ipAddress.trim().isEmpty() || !checkServerIP(ipAddress)) {
            properties.setProperty(IP_KEY, askServerIP());
            try (FileOutputStream fos = new FileOutputStream(configFile)) {
                properties.store(fos, "The new center machine IP adress is: "+_serverIP);
            } catch (IOException e) {
                System.err.println("Error while writing in config.ini: " + e.getMessage());
            }
        } else {
            _serverIP = ipAddress;
            System.out.println("Successfully connected to the server at IP: " + _serverIP);
            System.out.println("");
        }
    }

    /**
     * method that will ask to the user the central machine adress IP, and store it
     */
    public static String askServerIP() {
        Scanner scanInput = ScanInput.getScanInput();
        boolean correctIP = false;

        while (!correctIP) {
            System.out.println("");
            System.out.println("Please enter the server IP:"); //we are asking the adress to the user

            String serverIP = scanInput.nextLine(); //it collect the response
            if(checkServerIP(serverIP)){
                correctIP = true;
                _serverIP = serverIP;
                System.out.println("Successfully connected to the server at IP: " + _serverIP);
                System.out.println("");
            }
            else System.out.println("Please try again");
        }
        return _serverIP;
    }


    /**
     * method that checks if an adress IP is valid
     * @param ip
     * @return
     */
    private static boolean isValidIP(String ip) {
        try {
            InetAddress.getByName(ip);
            return true;
        } catch (Exception e) {
            return false;
        }
    }








    /**
     * This method is meant to launch the searches for the differents possible interfaces
     * @param primaryInterface
     * @param secondaryInterface
     * @return
     */
    private static String findIpAddress(String primaryInterface, String secondaryInterface) {
        // Find the IP address for the main interface
        String ip = getIpAddress(primaryInterface);
        if (ip != null) {
            return ip;
        }

        // Find the IP address for the secondary interface if the primary interface was not found
        return getIpAddress(secondaryInterface);
    }

    /**
     * This method searches for a correct IPv4 adress corresponding to the interfaceName provided
     * @param interfaceName
     * @return
     */
    private static String getIpAddress(String interfaceName) {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
            if (networkInterface != null) {
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // checking if the adress is an IPv4 adress
                    if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error while trying to get the IP adress : " + e.getMessage());
        }
        return null;
    }

    /**
     * This method checks if the provided IP is part of the differents IP adress of the machine
     * @param ipToCheck
     * @return
     */
    private static boolean isIpInLocalPool(String ipToCheck) {
        try {
            InetAddress addressToCheck = InetAddress.getByName(ipToCheck);
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress.equals(addressToCheck)) {
                        return true; // IP adress found
                    }
                }
            }
        } catch (SocketException | java.net.UnknownHostException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
        
        return false; // IP adress not found
    }



    
    /**
     * This method checks if the provided IP could be the one of the machine
     * @param ip
     * @return
     */
    private static boolean checkOwnIP(String ip){
        if (ip.isEmpty()) { //if it's empty, it's not good
            System.out.println("");
            System.out.println("You provided an empty server IP."); 
        } else if (!isValidIP(ip)) { //if it's not a valid IP, we ask again
            System.out.println("");
            System.out.println("The machine IP address format is incorrect.");
        } else if(!isIpInLocalPool(ip)){
            System.out.println("");
            System.out.println("The machine IP address provided isn't in the pool of IP address of the machine.");
        }else return true;
        return false;
    }


    /**
     * this method is meant to ask to the user to enter the machine IP address and check if it's a correct one
     */
    private static void askOwnIP() {

        Scanner scanInput = ScanInput.getScanInput();
        boolean correctIP = false;

        while (!correctIP) {
            System.out.println("");
            System.out.println("Please enter the Machine IP:"); //we are asking the adress to the user

            String OwnIP = scanInput.nextLine(); //it collect the response
            if(checkOwnIP(OwnIP)){
                correctIP = true;
                _machineIP = OwnIP;
                System.out.println("The Ip address has been stored: " + _machineIP);
                System.out.println("");
            }
            else System.out.println("Please try again");
        }
    }







    /**
     * This method is meant to search for the machine IP adress.
     * It's starting by searching it automatically with the openStack interface.
     * If it's not found, it will have to be entered manually by the user
     */
    public static void findOwnIp(){
        // Find the IP address
        String ip = findIpAddress(primaryInterface, secondaryInterface);
        if (ip != null) {
            System.out.println("Own IP adress has been found : " + ip);
        } else {
            System.out.println("The IP address could not be found automatically.");
            askOwnIP();
        }
    }


}

