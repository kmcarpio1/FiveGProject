package tst.hashing;

import src.hashfunctions.HashList;
import src.hashfunctions.LogHashTable;

public class TestHashList {

    public static void testHashList() {
        testInitialization();
        testAddLogHashTable();
        testGetLogHashTable();
    }


    public static void testInitialization() {
        // Vérification initiale de l'état de _hashList
        boolean initialized = HashList.isHashListInitializedAndNotEmpty();
        assert !initialized : "FAILURE: HashList should not be initialized and not empty initially.";

        // Ajouter un élément pour vérifier l'état après ajout
        LogHashTable logTable = new LogHashTable();
        HashList.addLogHashTable("path/to/log1", logTable);

        // Vérification si _hashList est initialisé et non vide après ajout
        initialized = HashList.isHashListInitializedAndNotEmpty();
        assert initialized : "FAILURE: HashList should be initialized and not empty after adding an element.";

        HashList.clean();
        System.out.println("HashList Initialization test passed");
    }


    public static void testAddLogHashTable() {
        LogHashTable logTable1 = new LogHashTable();
        LogHashTable logTable2 = new LogHashTable();

        // Ajout de log tables
        HashList.addLogHashTable("path/to/log1", logTable1);
        HashList.addLogHashTable("path/to/log2", logTable2);

        // Vérification si les log tables ont été ajoutées correctement
        assert HashList.getLogHashTable("path/to/log1") == logTable1 : "FAILURE: logTable1 was not added correctly.";
        assert HashList.getLogHashTable("path/to/log2") == logTable2 : "FAILURE: logTable2 was not added correctly.";

        HashList.clean();
        System.out.println("HashList AddLogHashTable test passed");
    }

    public static void testGetLogHashTable() {
        LogHashTable logTable3 = new LogHashTable();
        HashList.addLogHashTable("path/to/log3", logTable3);

        // Vérification si la récupération fonctionne correctement
        assert HashList.getLogHashTable("path/to/log3") == logTable3 : "FAILURE: logTable3 was not retrieved correctly.";

        // Vérification pour une clé inexistante
        assert HashList.getLogHashTable("path/to/nonexistent") == null : "FAILURE: Nonexistent key did not return null.";

        HashList.clean();
        System.out.println("HashList GetLogHashTable test passed");
    }
}