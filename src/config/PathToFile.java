package src.config;

public class PathToFile {
    private static final String FILES_DIR = "files/";
    private static final String FILES_WRITE_DIR = "files/write/";
    private static final String FILES_SAVE_DIR = "files/save/";

    public static String getPathFile(){
        return FILES_DIR;
    }

    public static String getPathSave(){
        return FILES_SAVE_DIR;
    }

    public static String getPathWrite(){
        return FILES_WRITE_DIR;
    }
}
