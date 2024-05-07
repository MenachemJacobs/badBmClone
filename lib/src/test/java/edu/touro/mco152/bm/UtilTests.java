package edu.touro.mco152.bm;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTests {

    /**
     * Performance: test assert that removal is done under a sub-linear time barrier.
     */
    @RepeatedTest(5) // This will run the test method 5 times
    void deleteDirectory() {
        String directoryPath = "testDirRemove";
        File directory = new File(directoryPath);
        int numOfFiles = 1000;

        if (!directory.exists()) {
            directory.mkdir();

            for (int i = 0; i < numOfFiles; i++) {
                createFile(directoryPath, "file" + i);
            }
        }

        Instant start = Instant.now();
        Util.deleteDirectory(directory);
        long end = (Instant.now()).toEpochMilli() - start.toEpochMilli();

        assertFalse(directory.exists(), "Directory created as part of test has not been removed");
        assertTrue(allFilesDeleted(directory), "All files should be deleted");

        assertTrue(end * 2 < numOfFiles, "Files are being removed too slow");

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

    private static boolean allFilesDeleted(File directory) {
        File[] files = directory.listFiles();
        return files == null || files.length == 0;
    }

    /**
     * boundaries checked: very large, very negative, single correct results.
     *
     * @param min
     * @param max
     */
    @ParameterizedTest
    @CsvSource({
            "1, 10",
            "1, 2147483647",
            "-5, 5",
            "1, 1",
    })
    void randInt(int min, int max) {
        int randReturn = Util.randInt(min, max);

        assertTrue(randReturn >= min && randReturn <= max, "out of bounds values are being returned by Util.randInt()");
    }

    /**
     * Error Forcing: Passing in a large and then small integer to the intRandom method should force an Illegal Arg Exception
     */
    @Test
    void randIntError(){
        assertThrows(IllegalArgumentException.class, () -> Util.randInt(10,1), "passing (large, small) int randInt generator is not throwing an Exception");
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "10.24",
            "0.2"
    })
    void displayString(float num) {
        if (num - (int)num == 0)
            assertEquals((int) num + "", Util.displayString(num));
        else
            assertEquals(num + "", Util.displayString(num));
    }
}
