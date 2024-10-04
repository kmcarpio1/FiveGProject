package tst;

import tst.commands.TestCommandCutter;
import tst.commands.TestCommandFactory;
import tst.commands.TestLoad;
import tst.commands.TestMix;
import tst.commands.TestSave;
import tst.commands.TestSaveLogHashTable;
import tst.commands.TestSend;
import tst.commands.TestWatch;
import tst.commands.TestWrite;
import tst.hashing.TestHashList;
import tst.hashing.TestHashTableElement;
import tst.hashing.TestHashWatched;
import tst.hashing.TestHashing;
import tst.hashing.TestLogHashTable;
import tst.sqlfunctions.TestSqlTable;

public class Test{

    public static void main(String[] arg){

        System.out.println("");
        System.out.println("Main test launched ! ");
        System.out.println("");
        System.out.println("____________________________________________________________");
        System.out.println("");

        try {
            TestHashing.testHashing();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestHashTableElement.testHashTableElement();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestLogHashTable.testLogHashTable();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestSaveLogHashTable.testSaveLogHashTable();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestHashList.testHashList();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestHashWatched.testHashWatched();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestCommandCutter.testCommandCutter();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestCommandFactory.testCommandFactory();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestLoad.testLoad();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestSave.testSave();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestWrite.testWrite();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestMix.testMix();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestWatch.testWatch();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestSqlTable.testSqlTable();
            System.out.println("____________________________________________________________");
            System.out.println("");
            TestSend.testSend();
            System.out.println("____________________________________________________________");
            System.out.println("");            
            System.out.println("All tests passed successfully");
        } catch (Exception e) {
            System.out.println("Not all tests passed successfully");
            e.printStackTrace();
        }


    }


}