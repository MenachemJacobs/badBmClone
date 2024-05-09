package edu.touro.mco152.bm.Command;

import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.DiskMark;
import edu.touro.mco152.bm.UIWorker;
import edu.touro.mco152.bm.Util;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.*;

import static edu.touro.mco152.bm.DiskMark.MarkType.READ;

/**
 * The ReadingMark class represents a command for performing read benchmarking operations
 * in the jDiskMark benchmarking tool.
 *
 * It extends the ReadWriteCommands abstract class and implements the Command interface.
 * This class is responsible for executing read benchmarking operations and updating UI
 * progress accordingly.
 *
 * @param <T> The type of UI worker used for updating UI progress.
 */
public class ReadingMark<T> extends ReadWriteCommands<T> implements Command {

    boolean isSuccess = true;

    /**
     * Constructs a new ReadingMark object with the specified parameters.
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
    public ReadingMark(DiskRun.IOMode mode, DiskRun.BlockSequence sequence, int numOfMarks, int numOfBlocks, int blockSizeKb, long targetTxSizeKb, String dirLocation, UIWorker<T> myWorker) {
        super(mode, sequence, numOfMarks, numOfBlocks, blockSizeKb, targetTxSizeKb, dirLocation, myWorker);
        OP = READ;
    }

    /**
     * Indicates whether the read operation was successful.
     *
     * @return true if the read operation was successful, false otherwise.
     */
    public boolean isSuccess(){return isSuccess;}

    /**
     * Executes the read benchmarking operation.
     */
    @Override
    public void particularOp()  {
        for (int m = super.startFileNum; m < startFileNum + run.getNumMarks() && !myWorker.getIsCancelled(); m++) {

            if (multiFile) testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata" + m + ".jdm");

            genericMark = new DiskMark(OP);  // starting to keep track of a new benchmark
            genericMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesReadInMark = 0;

            try {
                try (RandomAccessFile rAccFile = new RandomAccessFile(testFile, "r")) {
                    for (int b = 0; b < run.getNumBlocks(); b++) {
                        if (run.getBlockOrder() == DiskRun.BlockSequence.RANDOM) {
                            int rLoc = Util.randInt(0, run.getNumBlocks() - 1);
                            rAccFile.seek((long) rLoc * run.getBlockSize());
                        } else {
                            rAccFile.seek((long) b * run.getBlockSize());
                        }

                        rAccFile.readFully(blockArr, 0, run.getBlockSize());
                        totalBytesReadInMark += run.getBlockSize();
                        rUnitsComplete++;
                        unitsComplete = rUnitsComplete + wUnitsComplete;
                        percentComplete = (float) unitsComplete / (float) unitsTotal * 100f;
                        myWorker.setTheProgress((int) percentComplete);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                String emsg = "May not have done Write Benchmarks, so no data available to read." + ex.getMessage();
                JOptionPane.showMessageDialog(Gui.mainFrame, emsg, "Unable to READ", JOptionPane.ERROR_MESSAGE);
                msg(emsg);

                isSuccess = false;
            }

            long endTime = System.nanoTime();
            long elapsedTimeNs = endTime - startTime;
            double sec = (double) elapsedTimeNs / (double) 1000000000;
            double mbRead = (double) totalBytesReadInMark / (double) MEGABYTE;
            genericMark.setBwMbSec(mbRead / sec);
            msg("m:" + m + " READ IO is " + genericMark.getBwMbSec() + " MB/s    " + "(MBread " + mbRead + " in " + sec + " sec)");
            App.updateMetrics(genericMark);
            myWorker.publishChunks(genericMark);

            run.setRunMax(genericMark.getCumMax());
            run.setRunMin(genericMark.getCumMin());
            run.setRunAvg(genericMark.getCumAvg());
            run.setEndTime(new Date());
        }
    }
}