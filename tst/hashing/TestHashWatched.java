package tst.hashing;

import java.util.ArrayList;
import java.util.List;
import src.hashfunctions.HashWatched;
import src.hashfunctions.LogHashTable;

public class TestHashWatched {

    public static void testHashWatched() {
        testInitialization();
        testAddLogHashTable();
        testRemoveLogHashTable();
        testGetLogHashTable();
    }



    public static void testInitialization() {
        // Vérification initiale de l'état de _hashList
        boolean initialized = HashWatched.isHashWatchedInitializedAndNotEmpty();
        assert !initialized : "FAILURE: HashWatched should not be initialized and not empty initially.";

        // Ajouter un élément pour vérifier l'état après ajout
        LogHashTable logTable = new LogHashTable();
        HashWatched.addLogHashTable("path/to/log1", logTable);

        // Vérification si _hashList est initialisé et non vide après ajout
        initialized = HashWatched.isHashWatchedInitializedAndNotEmpty();
        assert initialized : "FAILURE: HashWatched should be initialized and not empty after adding an element.";

        System.out.println("HashWatched Initialization test passed");

        HashWatched.clean();
    }


    public static void testAddLogHashTable() {
        LogHashTable logTable1 = new LogHashTable();
        LogHashTable logTable2 = new LogHashTable();

        List<LogHashTable> logTableList1 = new ArrayList<>();
        logTableList1.add(logTable1);


        List<LogHashTable> logTableList2 = new ArrayList<>();
        logTableList2.add(logTable2);

        // Ajout de log tables
        HashWatched.addLogHashTable("path/to/log1", logTable1);
        HashWatched.addLogHashTable("path/to/log2", logTable2);

        // Vérification si les log tables ont été ajoutées correctement
        assert HashWatched.getLogHashTable("path/to/log1").equals(logTableList1) : "FAILURE: logTable1 was not added correctly.";
        assert HashWatched.getLogHashTable("path/to/log2").equals(logTableList2) : "FAILURE: logTable2 was not added correctly.";

        System.out.println("HashWatched AddLogHashTable test passed");
        HashWatched.clean();

    }

    public static void testRemoveLogHashTable() {
        LogHashTable logTable3 = new LogHashTable();
        HashWatched.addLogHashTable("path/to/log3", logTable3);

        // Suppression de logTable3
        HashWatched.removeLogHashTable("path/to/log3");

        // Vérification si la log table a été supprimée correctement
        assert HashWatched.getLogHashTable("path/to/log3") == null : "FAILURE: logTable3 was not removed correctly.";

        System.out.println("HashWatched RemoveLogHashTable test passed");
        HashWatched.clean();

    }

    public static void testGetLogHashTable() {
        LogHashTable logTable4 = new LogHashTable();
        HashWatched.addLogHashTable("path/to/log4", logTable4);


        List<LogHashTable> logTableList4 = new ArrayList<>();
        logTableList4.add(logTable4);

        // Vérification si la récupération fonctionne correctement
        assert HashWatched.getLogHashTable("path/to/log4").equals(logTableList4) : "FAILURE: logTable4 was not retrieved correctly.";

        // Vérification pour une clé inexistante
        assert HashWatched.getLogHashTable("path/to/nonexistent") == null : "FAILURE: Nonexistent key did not return null.";

        System.out.println("HashWatched GetLogHashTable test passed");
        HashWatched.clean();

    }
}