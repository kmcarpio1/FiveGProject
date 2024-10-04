package src.config;

import java.util.Scanner;

/**
 * this class is meant to configure the scanner that will read the terminal
 * it has to be oppened only once and closed only once to avoid problems
 */
public class ScanInput {
    private static Scanner _scanInput;

    /**
     * method to get the scanner input
     * @return
     */
    public static Scanner getScanInput(){
        if(_scanInput == null) setScanInput();
        return _scanInput;
    }

    /**
     * method to set the scanner input
     */
    public static void setScanInput(){
        _scanInput = new Scanner(System.in);
    }

    /**
     * A method to close the scanInput
     */
    public static void closeScanner() {
        if (_scanInput != null) {
            _scanInput.close();
            _scanInput = null;
        }
    }
}
