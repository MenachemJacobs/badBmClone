import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.DiskWorker;
import edu.touro.mco152.bm.ObserverElements.TestObserver;
import edu.touro.mco152.bm.TestingUIWorker;
import edu.touro.mco152.bm.UIWorker;
import edu.touro.mco152.bm.ui.Gui;
import edu.touro.mco152.bm.ui.MainFrame;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObserverTest {
    static UIWorker<Boolean> myTestWorker;
    static TestObserver myTestObserver;

    @BeforeEach
    void setUpMyWorker(){
        myTestWorker = new TestingUIWorker<>();
        myTestObserver = new TestObserver();
        setupDefaultAsPerProperties();

        DiskWorker myWorker = new DiskWorker(myTestWorker);
        myWorker.mySubject.addObserver(myTestObserver);
    }

    @Test
    void testObserver(){
        assertFalse(myTestObserver.getTestFlag());
        myTestWorker.executeTask();
    }

    @AfterAll
    static void testMyObserver(){
        assertTrue(myTestObserver.getTestFlag());
    }


    /**
     * Bruteforce setup of static classes/fields to allow DiskWorker to run.
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
