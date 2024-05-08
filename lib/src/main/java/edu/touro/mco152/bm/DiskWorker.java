package edu.touro.mco152.bm;

import edu.touro.mco152.bm.Command.Invoker;
import edu.touro.mco152.bm.Command.ReadingMark;
import edu.touro.mco152.bm.Command.WritingMark;
import edu.touro.mco152.bm.persist.DiskRun;

import edu.touro.mco152.bm.ui.Gui;



import javax.swing.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;


/**
 * Run the disk benchmarking exclusively as a Swing-compliant thread (only one of these threads can run at
 * once.) Must cooperate with Swing to provide and make use of interim and final progress and
 * information, which is also recorded as needed to the persistence store, and log.
 * <p>
 * Depends on static values that describe the benchmark to be done having been set in App and Gui classes.
 * The DiskRun class is used to keep track of and persist info about each benchmark at a higher level (a run),
 * while the DiskMark class described each iteration's result, which is displayed by the UI as the benchmark run
 * progresses.
 * <p>
 * This class only knows how to do 'read' or 'write' disk benchmarks, all of which is done in doInBackground(). It is instantiated by the
 * startBenchmark() method.
 * <p>
 * To be Swing compliant this class extends SwingWorker and is dependant on it. It declares that its final return (when
 * doInBackground() is finished) is of type Boolean, and declares that intermediate results are communicated to
 * Swing using an instance of the DiskMark class.
 */

public class DiskWorker {
    public final UIWorker<Boolean> currentUI;

    public DiskWorker(UIWorker<Boolean> passedUI) {
        currentUI = passedUI;
        boolean[] result = new boolean[1];

        Runnable backgroundTask = () -> {
            try {
                result[0] = doInBackground();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                currentUI.onTaskCompleted(result[0]);
            }
        };

        currentUI.assignDoInBackground(backgroundTask);
        currentUI.assignHardWare(this);
    }

    protected Boolean doInBackground(){

        /*
          We 'got here' because: 1: End-user clicked 'Start' on the benchmark UI,
          which triggered the start-benchmark event associated with the App::startBenchmark()
          method.  2: startBenchmark() then instantiated a DiskWorker, and called
          its (super class's) execute() method, causing Swing to eventually
          call this doInBackground() method.
         */
        Logger.getLogger(App.class.getName()).log(Level.INFO, "*** New worker thread started ***");
        msg("Running readTest " + App.readTest + "   writeTest " + App.writeTest);
        msg("num files: " + App.numOfMarks + ", num blks: " + App.numOfBlocks + ", blk size (kb): " + App.blockSizeKb + ", blockSequence: " + App.blockSequence);

        Gui.updateLegend();  // init chart legend info

        if (App.autoReset) {
            App.resetTestData();
            Gui.resetTestData();
        }

        /*
          The GUI allows a Write, Read, or both types of BMs to be started. They are done serially.
         */
        if (App.writeTest){
            WritingMark<Boolean> writeBenchmark = new WritingMark<>(DiskRun.IOMode.WRITE,
                    App.blockSequence,
                    App.numOfMarks,
                    App.numOfBlocks,
                    (App.blockSizeKb * KILOBYTE),
                    App.targetTxSizeKb(),
                    Util.getDiskInfo(dataDir),
                    currentUI
            );

            new Invoker(writeBenchmark).invoke();
        }

        /*
          Most benchmarking systems will try to do some cleanup in between 2 benchmark operations to
          make it more 'fair'. For example a networking benchmark might close and re-open sockets,
          a memory benchmark might clear or invalidate the Op Systems TLB or other caches, etc.
         */

        // try renaming all files to clear catch
        if (App.readTest && App.writeTest && !currentUI.getIsCancelled()) {
            JOptionPane.showMessageDialog(Gui.mainFrame,
                    """
                            For valid READ measurements please clear the disk cache by
                            using the included RAMMap.exe or flushmem.exe utilities.
                            Removable drives can be disconnected and reconnected.
                            For system drives use the WRITE and READ operations\s
                            independantly by doing a cold reboot after the WRITE""",
                    "Clear Disk Cache Now", JOptionPane.PLAIN_MESSAGE);
        }

        // Same as above, just for Read operations instead of Writes.
        if (App.readTest) {
            ReadingMark<Boolean> readBenchmark = new ReadingMark<>(
                    DiskRun.IOMode.READ,
                    App.blockSequence,
                    App.numOfMarks,
                    App.numOfBlocks,
                    (App.blockSizeKb * KILOBYTE),
                    App.targetTxSizeKb(),
                    Util.getDiskInfo(dataDir),
                    currentUI
            );

            new Invoker(readBenchmark).invoke();

            if(!readBenchmark.isSuccess())  return false;
        }

        App.nextMarkNumber += App.numOfMarks;

        return true;
    }
}
