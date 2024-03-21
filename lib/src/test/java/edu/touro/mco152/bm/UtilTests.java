package edu.touro.mco152.bm;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.createFile;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilTests {
    @Test
    void deleteDirectory() {
        String directoryPath = "testDirRemove";

        File directory = new File(directoryPath);

        if (!directory.exists())
            directory.mkdir();

        createFile(directoryPath, "file1");
        createFile(directoryPath, "file2");
        createFile(directoryPath, "file3");

        Util.deleteDirectory(directory);

        assertFalse(directory.exists(), "Directory created as part of test has not been removed");
        assertTrue(allFilesDeleted(directory), "All files should be deleted");

        if (directory.exists())
            cleanUp(directory);
    }

    private static void createFile(String dirPath, String fN) {
        File file = new File(dirPath, fN);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cleanUp(File dirName) {
        File[] files = dirName.listFiles();

        assert files != null;
        for (File fN : files) {
            if (fN.isDirectory()) {
                cleanUp(fN);
            } else {
                fN.delete();
            }
        }
        dirName.delete();
    }

    private static boolean allFilesDeleted(File directory){
        File[] files = directory.listFiles();
        return files == null || files.length == 0;
    }

    @Test
    void randInt() {
    }

    @Test
    void displayString() {
    }
}
