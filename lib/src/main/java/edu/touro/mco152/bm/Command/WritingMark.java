package edu.touro.mco152.bm.Command;

import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.DiskMark;
import edu.touro.mco152.bm.UIWorker;
import edu.touro.mco152.bm.Util;
import edu.touro.mco152.bm.persist.DiskRun;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.App.msg;
import static edu.touro.mco152.bm.DiskMark.MarkType.WRITE;

/**
 * The WritingMark class represents a command for performing write benchmarking operations
 * in the jDiskMark benchmarking tool.
 *
 * It extends the ReadWriteCommands abstract class and is responsible for executing write
 * benchmarking operations and updating UI progress accordingly.
 *
 * @param <T> The type of UI worker used for updating UI progress.
 */
public class WritingMark<T> extends ReadWriteCommands<T>{

    /**
     * Constructs a new WritingMark object with the specified parameters.
     *
     * @param mode The I/O mode for the disk run (READ or WRITE).
     * @param sequence The block sequence for the disk run.
     * @param numOfMarks The number of marks for the disk run.
     * @param numOfBlocks The number of blocks per mark.
     * @param blockSizeKb The size of each block in kilobytes.
     * @param targetTxSizeKb The target transaction size in kilobytes.
     * @param dirLocation The directory location for the disk run.
     * @param myWorker The UI worker for updating UI progress.
     */
    public WritingMark(DiskRun.IOMode mode, DiskRun.BlockSequence sequence, int numOfMarks, int numOfBlocks, int blockSizeKb, long targetTxSizeKb, String dirLocation, UIWorker<T> myWorker) {
        super(mode, sequence, numOfMarks, numOfBlocks, blockSizeKb, targetTxSizeKb, dirLocation, myWorker);
        OP = WRITE;
    }

    /**
     * Executes the write benchmarking operation.
     */
    @Override
    public void particularOp() {
        System.out.println("block size equals " + run.getBlockSize());

        // Create a test data file using the default file system and config-specified location
        if (!App.multiFile) {
            testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata.jdm");
        }

        /*
        Begin an outer loop for specified duration (number of 'marks') of benchmark,
        that keeps writing data (in its own loop - for specified # of blocks). Each 'Mark' is timed
        and is reported to the GUI for display as each Mark completes.
        */
        for (int m = startFileNum; m < startFileNum + run.getNumMarks() && !myWorker.getIsCancelled(); m++) {

            if (multiFile)  testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata" + m + ".jdm");


            genericMark = new DiskMark(OP);    // starting to keep track of a new benchmark
            genericMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesWrittenInMark = 0;

            String mode = "rw";
            if (App.writeSyncEnable) {
                mode = "rwd";
            }

            try {
                try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, mode)) {
                    for (int b = 0; b < run.getNumBlocks(); b++) {
                        if (run.getBlockOrder() == DiskRun.BlockSequence.RANDOM) {
                            int rLoc = Util.randInt(0, run.getNumBlocks() - 1);
                            rAccFile.seek((long) rLoc * run.getBlockSize());
                        } else {
                            rAccFile.seek((long) b * run.getBlockSize());
                        }
                        rAccFile.write(blockArr, 0, run.getBlockSize());
                        totalBytesWrittenInMark += run.getBlockSize();
                        wUnitsComplete++;
                        unitsComplete = rUnitsComplete + wUnitsComplete;
                        percentComplete = (float) unitsComplete / (float) unitsTotal * 100f;

                            /*
                              Report to GUI what percentage level of Entire BM (#Marks * #Blocks) is done.
                             */
                        myWorker.setTheProgress((int) percentComplete);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }

            /*
            Compute duration, throughput of this Mark's step of BM
            */
            long endTime = System.nanoTime();
            long elapsedTimeNs = endTime - startTime;
            double sec = (double) elapsedTimeNs / (double) 1000000000;
            double mbWritten = (double) totalBytesWrittenInMark / (double) MEGABYTE;
            genericMark.setBwMbSec(mbWritten / sec);
            msg("m:" + m + " write IO is " + genericMark.getBwMbSecAsString() + " MB/s     " + "(" + Util.displayString(mbWritten) + "MB written in "
                    + Util.displayString(sec) + " sec)");
            updateMetrics(genericMark);

            /*
            Let the GUI know the interim result described by the current Mark
            */
            myWorker.publishChunks(genericMark);

            // Keep track of statistics to be displayed and persisted after all Marks are done.
            run.setRunMax(genericMark.getCumMax());
            run.setRunMin(genericMark.getCumMin());
            run.setRunAvg(genericMark.getCumAvg());
            run.setEndTime(new Date());
        } // END outer loop for specified duration (number of 'marks') for WRITE benchmark
    }
}