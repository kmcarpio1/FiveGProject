package tst.hashing;

import src.hashfunctions.Hashing;


public class TestHashing {

    public static void testHashing() {
        testSize();
        testGetMd5Hash();
    }

    public static void testSize() {
        // Test case: empty character string
        String input = "";
        assert Hashing.getMd5Hash(input).length() == 32 : "FAILURE input \"\" and its hash length is different";

        // Test case: simple character string
        input = "hello";
        assert Hashing.getMd5Hash(input).length() == 32 : "FAILURE input \"hello\" and its hash length is different";

        // Test case: longer character string
        input = "The quick brown fox jumps over the lazy dog";
        assert Hashing.getMd5Hash(input).length() == 32 : "FAILURE input \"The quick brown fox jumps over the lazy dog\" and its hash length is different";

        // Test case: string with special characters
        input = "!@#$%^&*()_+-=";
        assert Hashing.getMd5Hash(input).length() == 32 : "FAILURE input \"!@#$%^&*()_+-=\" and its hash length is different";

        // Test case: hash starting with zeros
        input = "363";
        assert Hashing.getMd5Hash(input).length() == 32 : "FAILURE input \"363\" and its hash length is different";

        System.out.println("Hashing size test passed");
    }

    public static void testGetMd5Hash() {
        // Test case: empty character string
        String input = "";
        String expectedHash = "d41d8cd98f00b204e9800998ecf8427e"; // Hash MD5 of empty string
        assert Hashing.getMd5Hash(input).equals(expectedHash) : "FAILURE input \"\" and its expected hash value are different";

        // Test case: simple character string
        input = "hello";
        expectedHash = "5d41402abc4b2a76b9719d911017c592"; // Hash MD5 of "hello"
        assert Hashing.getMd5Hash(input).equals(expectedHash) : "FAILURE input \"hello\" and its expected hash value are different";

        // Test case: longer character string
        input = "The quick brown fox jumps over the lazy dog";
        expectedHash = "9e107d9d372bb6826bd81d3542a419d6"; // Hash MD5 of this sentence
        assert Hashing.getMd5Hash(input).equals(expectedHash) : "FAILURE input \"The quick brown fox jumps over the lazy dog\" and its expected hash value are different";

        // Test case: string with special characters
        input = "!@#$%^&*()_+-=";
        expectedHash = "28f37ff2ee48627bb2ec0b14ceab6d37"; // Hash MD5 of "!@#$%^&*()_+-="
        assert Hashing.getMd5Hash(input).equals(expectedHash) : "FAILURE input \"!@#$%^&*()_+-=\" and its expected hash value are different";

        // Test case: hash starting with zeros
        input = "363";
        expectedHash = "00411460f7c92d2124a67ea0f4cb5f85"; // Hash MD5 of "363"
        assert Hashing.getMd5Hash(input).equals(expectedHash) : "FAILURE input \"363\" and its expected hash value are different";

        System.out.println("Hashing GetMd5Hash test passed");
    }
}