import edu.touro.mco152.bm.*;
import edu.touro.mco152.bm.Command.Invoker;
import edu.touro.mco152.bm.Command.ReadingMark;
import edu.touro.mco152.bm.Command.WritingMark;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Properties;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.persist.DiskRun.BlockSequence.SEQUENTIAL;

/**
 * The CommandTests class contains JUnit test methods for testing the command pattern
 * implementations in the jDiskMark benchmarking tool.
 *
 * It imports several classes and interfaces from the jDiskMark library and JUnit framework.
 * These include classes for benchmarking commands, UI elements, disk run information,
 * file handling, and properties configuration.
 *
 * The setupDefaultAsPerProperties() method initializes static classes and fields to
 * allow DiskWorker to run properly.
 *
 */
public class CommandTests {
    UIWorker<Boolean> myUiWorker;

    /**
     * Sets up the test environment before each test method execution by initializing
     * default properties and creating a UIWorker instance.
     */
    @BeforeEach
    void setUp() {
        setupDefaultAsPerProperties();
        myUiWorker = new TestingUIWorker<>();
    }

    /**
     * Tests the reading benchmark by creating a ReadingMark instance and invoking
     * its benchmarking operation using an Invoker.
     */
    @Test
    void read(){
        ReadingMark<Boolean> readBenchmark = new ReadingMark<>(DiskRun.IOMode.READ, SEQUENTIAL, 25, 128, (2048 * KILOBYTE), App.targetTxSizeKb(),
                Util.getDiskInfo(dataDir), myUiWorker);

        new Invoker(readBenchmark).invoke();
    }

    /**
     * Tests the writing benchmark by creating a WritingMark instance and invoking
     * its benchmarking operation using an Invoker.
     */
    @Test
    void write(){
        WritingMark<Boolean> writeBenchmark = new WritingMark<>(DiskRun.IOMode.READ, SEQUENTIAL, 25, 128, (2048 * KILOBYTE), App.targetTxSizeKb(),
                Util.getDiskInfo(dataDir), myUiWorker);

        new Invoker(writeBenchmark).invoke();
    }

    /**
     * Bruteforce setup of static classes/fields to allow DiskWorker to run.
     *
     * @author lcmcohen
     */
    public static void setupDefaultAsPerProperties() {
        /// Do the minimum of what  App.init() would do to allow to run.
        Gui.mainFrame = new MainFrame();
        App.p = new Properties();
        App.loadConfig();

        Gui.progressBar = Gui.mainFrame.getProgressBar(); //must be set or get Nullptr

        // configure the embedded DB in .jDiskMark
        System.setProperty("derby.system.home", App.APP_CACHE_DIR);

        // code from startBenchmark
        //4. create data dir reference

        // may be null when tests not run in original proj dir, so use a default area
        if (App.locationDir == null) {
            App.locationDir = new File(System.getProperty("user.home"));
        }

        App.dataDir = new File(App.locationDir.getAbsolutePath() + File.separator + App.DATADIRNAME);

        //5. remove existing test data if exist
        if (App.dataDir.exists()) {
            if (App.dataDir.delete()) {
                App.msg("removed existing data dir");
            } else {
                App.msg("unable to remove existing data dir");
            }
        } else {
            App.dataDir.mkdirs(); // create data dir if not already present
        }
    }
}