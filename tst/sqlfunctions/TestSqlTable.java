package tst.sqlfunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import src.config.DatabaseConnection;
import src.hashfunctions.HashTableElement;
import src.hashfunctions.Hashing;
import src.hashfunctions.LogHashTable;
import src.sqlfunctions.SqlTable;


public class TestSqlTable {


    public static void testSqlTable() {
        testPrepareInsertStatement();
        testAddElementToThePstmt();
        testAddElementToThePstmtWithNull();
        testCreateSqlCommand();
        testConvertAndSendLogHashTableIntoSql();
        testCreateSqlCommand();
        testExecuteBatch();
        testSetupDatabase();
        testListTables();
        testCleanDatabase(); //comment this method to be able to go watch the values in the database
    }



    public static void testPrepareInsertStatement(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        
        try{
            conn = DatabaseConnection.getTestConnection();
            String tableName = "TestTable";


            pstmt = SqlTable.prepareInsertStatement(conn, tableName);

            assert pstmt != null : "FAILURE : the preparedStatement returned by the prepareInsertStatement is null";
            
            String expectedSql = "INSERT INTO " + SqlTable.getSqlLogHashTableName(tableName) + " "
                               + "(hashKey, rawData, ipAddress, datetime, logFilePath) "
                               + "VALUES (** NOT SPECIFIED **, ** NOT SPECIFIED **, ** NOT SPECIFIED **, ** NOT SPECIFIED **, ** NOT SPECIFIED **)";



            assert pstmt.toString().contains(expectedSql) : "FAILURE : the preparedStatement do not contain the expected informations"; //we use contain because usually the preparedStatement add stuff to the string
            System.out.println("SqlTable PrepareInsertStatement test passed");

        }catch (SQLException e) {
            System.out.println("SqlTable PrepareInsertStatement test failed");

            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            assert(false) : "Error while trying to connect to the DataBase: " + e.getMessage();
        }finally {
            // Fermeture des ressources pour éviter les fuites
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing ressourses: " + e.getMessage());
            }
        }
    }


    public static void testAddElementToThePstmt() {
        Connection conn = null;
        PreparedStatement pstmt = null;
    
             
        try{
            conn = DatabaseConnection.getTestConnection();
            String tableName = "TestTable";
        
            String rawData = "Test log data";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            String expectedHash = Hashing.getMd5Hash(rawData);

            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "path");

            String timeReceivedString = element.getTimeReceivedString();
            // Simuler la préparation de la requête SQL
            
            String expectedSql = "INSERT INTO " + SqlTable.getSqlLogHashTableName(tableName) + " "
                               + "(hashKey, rawData, ipAddress, datetime, logFilePath) "
                               + "VALUES ('"+expectedHash+"', '"+rawData+"', '"+IP+"', '"+timeReceivedString+"', 'path')";

            pstmt = SqlTable.prepareInsertStatement(conn, tableName);

            SqlTable.addElementToThePstmt(element, pstmt);
            
            assert pstmt != null : "FAILURE : the preparedStatement returned by the prepareInsertStatement is null";

            assert pstmt.toString().contains(expectedSql) : "FAILURE : the preparedStatement do not contain the expected informations"; //we use contain because usually the preparedStatement add stuff to the string
            System.out.println("SqlTable AddElementToThePstmt test passed");

        } catch (SQLException e) {
            System.out.println("SqlTable AddElementToThePstmt test failed");
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            assert false : "Error while trying to connect to the DataBase: " + e.getMessage();
        } finally {
            // Fermeture des ressources pour éviter les fuites
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
    }

    public static void testAddElementToThePstmtWithNull() {
        Connection conn = null;
        PreparedStatement pstmt = null;
    
             
        try{
            conn = DatabaseConnection.getTestConnection();
            String tableName = "TestTable";
        
            String rawData = "Test log data";
            LocalDateTime timeReceived =  LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            String expectedHash = Hashing.getMd5Hash(rawData);

            HashTableElement element = new HashTableElement(rawData, timeReceived, null, "path");

            String timeReceivedString = element.getTimeReceivedString();
            // Simuler la préparation de la requête SQL
            
            String expectedSql = "INSERT INTO " + SqlTable.getSqlLogHashTableName(tableName) + " "
                               + "(hashKey, rawData, ipAddress, datetime, logFilePath) "
                               + "VALUES ('"+expectedHash+"', '"+rawData+"', NULL, '"+timeReceivedString+"', 'path')";

            pstmt = SqlTable.prepareInsertStatement(conn, tableName);

            SqlTable.addElementToThePstmt(element, pstmt);
            
            assert pstmt != null : "FAILURE : the preparedStatement returned by the prepareInsertStatement is null";


            assert pstmt.toString().contains(expectedSql) : "FAILURE : the preparedStatement do not contain the expected informations"; //we use contain because usually the preparedStatement add stuff to the string
            System.out.println("SqlTable AddElementToThePstmtWithNull test passed");

        } catch (SQLException e) {
            System.out.println("SqlTable AddElementToThePstmtWithNull test failed");
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            assert false : "Error while trying to connect to the DataBase: " + e.getMessage();
        } finally {
            // Fermeture des ressources pour éviter les fuites
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
    }





    public static void testCreateSqlCommand() {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getTestConnection();



            LogHashTable logHashTable = new LogHashTable("important/path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "important/path");
            logHashTable.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "important/path");



            pstmt = SqlTable.createSqlCommand(logHashTable, conn);

            assert pstmt != null : "FAILURE: PreparedStatement returned by createSqlCommand is null";

            // we can't check what had been written in the databse with test. We are going to check manually

            System.out.println("SqlTable CreateSqlCommand test passed");

        } catch (SQLException e) {
            System.out.println("SqlTable CreateSqlCommand test failed");
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            assert false : "Error while trying to connect to the DataBase: " + e.getMessage();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
    }   

    public static void testExecuteBatch() {
        Connection conn = null;
        PreparedStatement pstmt = null;
    
        try {
            conn = DatabaseConnection.getTestConnection();
            String tableName = "TestTable";

            SqlTable.setupDatabase(conn, tableName);

            pstmt = SqlTable.prepareInsertStatement(conn, tableName);
    
            HashTableElement element = new HashTableElement("data", LocalDateTime.now(), "192.168.1.1", "path");
            SqlTable.addElementToTheBatch(element, pstmt);
    
            SqlTable.executeBatch(pstmt);
    
            // we can't check what had been written in the databse with test. We are going to check manually

            System.out.println("SqlTable ExecuteBatch test passed");
    
        } catch (SQLException e) {
            System.out.println("SqlTable ExecuteBatch test failed");
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            assert false : "Error while trying to connect to the DataBase: " + e.getMessage();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
    }

    public static void testSetupDatabase() {
        Connection conn = null;
    
        try {
            conn = DatabaseConnection.getTestConnection();
            SqlTable.setupDatabase(conn,"testTable");
    
            // we can't check what had been written in the databse with test. We are going to check manually

            System.out.println("SqlTable SetupDatabase test passed");
    
        } catch (SQLException e) {
            System.out.println("SqlTable SetupDatabase test failed");
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            assert false : "Error while trying to connect to the DataBase: " + e.getMessage();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
    }

    public static void testListTables() {
        Connection conn = null;
    
        try {
            conn = DatabaseConnection.getTestConnection();
            SqlTable.setupDatabase(conn,"testTable");
    

            LogHashTable logHashTable = new LogHashTable("important/path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "important/path");
            logHashTable.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "important/path");

            SqlTable.ConvertAndSendLogHashTableIntoSql(logHashTable);
    

            List<String> names = SqlTable.listTables(conn);

            assert names.contains(SqlTable.getSqlLogHashTableName("testTable")): "FAILURE: There is not the sql table name in the lsit";
            assert names.contains(SqlTable.getSqlLogHashTableName(logHashTable.getLogHashTableName())): "FAILURE: There is not the sql table name in the lsit";

            System.out.println("SqlTable SetupDatabase test passed");
    
        } catch (SQLException e) {
            System.out.println("SqlTable SetupDatabase test failed");
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            assert false : "Error while trying to connect to the DataBase: " + e.getMessage();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
    }


    public static void testConvertAndSendLogHashTableIntoSql() {
        try {
            LogHashTable logHashTable = new LogHashTable("important/path");
            String rawData = "Log entry 1";
            LocalDateTime timeReceived = LocalDateTime.of(2024, 7, 22, 14, 30);
            String IP = "192.168.1.1";
            HashTableElement element = new HashTableElement(rawData, timeReceived, IP, "important/path");
            logHashTable.addLog(element);
            String rawData2 = "Log entry 2";
            logHashTable.addLog(rawData2, IP, "important/path");

            SqlTable.ConvertAndSendLogHashTableIntoSql(logHashTable);
            SqlTable.ConvertAndSendLogHashTableIntoSql(logHashTable);
    
            // we can't check what had been written in the databse with test. We are going to check manually
            System.out.println("SqlTable ConvertAndSendLogHashTableIntoSql test passed (the name of the sqlTable is: "+SqlTable.getSqlLogHashTableName(logHashTable.getLogHashTableName())+")");
    
        } catch (SQLException e) {
            System.out.println("SqlTable ConvertAndSendLogHashTableIntoSql test failed");
            assert false;
        }
    }

    public static void testCleanDatabase() {
        Connection conn = null;
    
        try {
            conn = DatabaseConnection.getTestConnection();
            SqlTable.setupDatabase(conn, "testTable");

            SqlTable.cleanDatabase(conn);
    
            // we can't check what had been deletes in the databse with test. We are going to check manually
            System.out.println("SqlTable CleanDatabase test passed");
    
        } catch (SQLException e) {    
            System.out.println("SqlTable CleanDatabase test failed");
            System.err.println("Error while trying to connect to the DataBase: " + e.getMessage());
            assert false;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
    }

}
