package edu.touro.mco152.bm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;

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
            cleanUpFileStruct(directory);
    }

    private static void createFile(String dirPath, String fN) {
        File file = new File(dirPath, fN);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cleanUpFileStruct(File dirName) {
        File[] files = dirName.listFiles();

        assert files != null;
        for (File fN : files) {
            if (fN.isDirectory()) {
                cleanUpFileStruct(fN);
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

    @ParameterizedTest
    @CsvSource({
            "1, 10",
            "-100, 100",
            "-5, 5",
            "0, 0",
            "10, 1"
    })
    void randInt(int min, int max) {
        int randReturn = Util.randInt(min, max);

        assertTrue(randReturn >= min && randReturn <= max, "out of bounds values are being returned by Util.randInt()");
    }

    @Test
    void displayString() {
    }
}
