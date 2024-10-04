package tst.commands;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import src.commands.CommandCutter;
import src.commands.CommandFactory;
import src.commands.ICommand;
import src.config.DatabaseConnection;
import src.hashfunctions.HashList;
import src.hashfunctions.HashTableElement;
import src.hashfunctions.LogHashTable;
import src.sqlfunctions.SqlTable;

public class TestSend {


    public static void testSend() {
        testConstructor();
        testExecuteSendAll();
        testExecuteSendOneWithName();
        testExecuteSendOneWithPath();
        testExecuteSendNonExistingLogHashTable();
        try{
            SqlTable.cleanDatabase();
        }catch(SQLException e){
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
        }            

    }

    public static void testConstructor() {
        boolean stateOfTest = true;
        File savedFile = null;
        try {
            LogHashTable logHashTable = new LogHashTable("important/path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "important/path");
            logHashTable.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "important/path");

            String logHashTableName = logHashTable.getLogHashTableName();

            List<String> listPaths = new ArrayList<>();
            listPaths.add("fake/path");
            listPaths.add("important/path");
            LogHashTable logHashTable2 = new LogHashTable(listPaths);
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "fake/path");
            
            String logHashTableName2 = logHashTable.getLogHashTableName();


            HashList.addLogHashTable(logHashTableName,logHashTable);

        
            // Test of the constructor with the "all" argument
            try {
                CommandCutter command1 = new CommandCutter("send all");
                ICommand send1 = CommandFactory.createCommand(command1);
                assert send1.getArgument().equals("all") : "FAILURE: Command 'send all' should set _name to 'all'.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the name of the logHashTable 
            try {
                CommandCutter command2 = new CommandCutter("send " + logHashTableName);//send + the name of the logHashTable
                ICommand send2 = CommandFactory.createCommand(command2);
                assert send2.getArgument().equals(logHashTableName) : "FAILURE: Command 'send' + logHashTableName should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();
            }

            // Test of the constructor with a normal logHashTable stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command3 = new CommandCutter("send important/path");
                ICommand send3 = CommandFactory.createCommand(command3);
                assert send3.getArgument().equals(logHashTableName) : "FAILURE: Command 'send' + logFile path should set _name to logHashTableName.";
            } catch (Exception e) {
                stateOfTest = false;
                e.printStackTrace();            }

            // Test of the constructor with a fake logHashTable not stored in the hashList, with the path of the logFile 
            try {
                CommandCutter command4 = new CommandCutter("send fakeLogHashTableName");
                ICommand send4 = CommandFactory.createCommand(command4);
                stateOfTest = false;
                assert false : "FAILURE: Command 'send fakeLogHashTableName' should throw an exception for non-existing file.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong argument") : "FAILURE: Exception message should be 'Wrong command: wrong argument'.";
            }

            try {
                CommandCutter command5 = new CommandCutter("send");
                ICommand send5 = CommandFactory.createCommand(command5);
                stateOfTest = false;
                assert false : "FAILURE: Command 'send' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }

            try {
                CommandCutter command6 = new CommandCutter("send all stuff");
                ICommand send6 = CommandFactory.createCommand(command6);
                stateOfTest = false;
                assert false : "FAILURE: Command 'send all stuff' should throw an exception for wrong number of arguments.";
            } catch (Exception e) {
                assert e.getMessage().equals("Wrong command: wrong number of arguments") : "FAILURE: Exception message should be 'Wrong command: wrong number of arguments'.";
            }


            if(stateOfTest) System.out.println("Send constructor test passed");
            else System.out.println("TEST FAILED : Send constructor test failed");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HashList.clean();

        }
    }


    public static void testExecuteSendAll() {
        boolean stateOfTest = true;
        try {

            LogHashTable logHashTable1 = new LogHashTable();
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();

            HashList.addLogHashTable(logHashTableName1,logHashTable1);

            LogHashTable logHashTable2 = new LogHashTable();
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("send all");
            ICommand send = CommandFactory.createCommand(command);

            send.execute();

            boolean loghashTable1Found =false;
            boolean loghashTable2Found =false;
            
            try(Connection conn = DatabaseConnection.getTestConnection()){

                List<String> names = SqlTable.listTables(conn);
                assert names.contains(SqlTable.getSqlLogHashTableName(logHashTable1.getLogHashTableName())): "FAILURE: There is not the good name in the list";
                assert names.contains(SqlTable.getSqlLogHashTableName(logHashTable2.getLogHashTableName())): "FAILURE: There is not the good name in the list";
                
            }catch (SQLException e) {
                System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
                throw new Exception(e.getMessage());
            }catch (Exception e) {
                throw e;
            }

        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Save ExecuteSendAll test passed");
            else System.out.println("TEST FAILED : Save ExecuteSendAll test failed");
            HashList.clean();
        }
    }



    public static void testExecuteSendOneWithName() {
        boolean stateOfTest = true;
        try {

            LogHashTable logHashTable1 = new LogHashTable();
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();

            HashList.addLogHashTable(logHashTableName1,logHashTable1);

            LogHashTable logHashTable2 = new LogHashTable();
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("send "+ logHashTableName1);
            ICommand send = CommandFactory.createCommand(command);

            System.out.println("the name of the sql table created is: " + SqlTable.getSqlLogHashTableName(logHashTableName1));

            send.execute();
            send.execute();

            assert logHashTable1.getLogTable().isEmpty() : "FAILURE: After the use of send, the logHashTable should not have HashTableElement inside.";
            
            try(Connection conn = DatabaseConnection.getTestConnection()){

                List<String> names = SqlTable.listTables(conn);
                assert names.contains(SqlTable.getSqlLogHashTableName(logHashTable1.getLogHashTableName())): "FAILURE: There is not the good name in the list";
                
            }catch (SQLException e) {
                System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
                throw new Exception(e.getMessage());
            }catch (Exception e) {
                throw e;
            }

        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Save ExecuteSendOneWithName test passed");
            else System.out.println("TEST FAILED : Save ExecuteSendOneWithName test failed");
            HashList.clean();
        }
    }

    public static void testExecuteSendOneWithPath() {
        boolean stateOfTest = true;
        try {

            LogHashTable logHashTable1 = new LogHashTable("path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();

            HashList.addLogHashTable(logHashTableName1,logHashTable1);

            LogHashTable logHashTable2 = new LogHashTable();
            logHashTable2.addLog(element);
            logHashTable2.addLog(rawData2, IP, "path");

            String logHashTableName2 = logHashTable2.getLogHashTableName();
            HashList.addLogHashTable(logHashTableName2,logHashTable2);

            CommandCutter command = new CommandCutter("send path");
            ICommand send = CommandFactory.createCommand(command);

            send.execute();

            boolean loghashTable1Found =false;
            boolean loghashTable2Found =false;
            
            try(Connection conn = DatabaseConnection.getTestConnection()){

                List<String> names = SqlTable.listTables(conn);
                assert names.contains(SqlTable.getSqlLogHashTableName(logHashTable1.getLogHashTableName())): "FAILURE: There is not the good name in the list";
                
            }catch (SQLException e) {
                System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
                throw new Exception(e.getMessage());
            }catch (Exception e) {
                throw e;
            }

        } catch (Exception e) {
            stateOfTest = false;
            e.printStackTrace();
        }finally {
            if(stateOfTest) System.out.println("Save ExecuteSendOneWithPath test passed");
            else System.out.println("TEST FAILED : Save ExecuteSendOneWithPath test failed");
            HashList.clean();
        }
    }


    public static void testExecuteSendNonExistingLogHashTable() {
        boolean stateOfTest = true;
        try {

            LogHashTable logHashTable1 = new LogHashTable();
            String rawData = "Log entry 1";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            //InetAddress IP = InetAddress.getByName("192.168.1.1");
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP,"path");
            logHashTable1.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable1.addLog(rawData2, IP, "path");

            String logHashTableName1 = logHashTable1.getLogHashTableName();



            //we do not put it inside hashList

            CommandCutter command = new CommandCutter("send path");
            ICommand send = CommandFactory.createCommand(command); //it should make an Exception

            send.execute();//we will never get there because creating a save instance with the wrong argument create an exception

            stateOfTest = false;
    
        } catch (Exception e) {
            try{
                assert e.getMessage().equals("Wrong command: wrong argument") : "FAILURE: Exception message should be 'Wrong command: File doesn't exist anymore'.";
            }catch (Exception a){
                a.printStackTrace();
                stateOfTest = false;
            }
        } finally {
            if(stateOfTest) System.out.println("Save ExecuteSendNonExistingLogHashTable test passed");
            else System.out.println("TEST FAILED : Save ExecuteSendNonExistingLogHashTable test failed");

            HashList.clean();
        }
    }

}