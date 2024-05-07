package edu.touro.mco152.bm.Command;

import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.DiskMark;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;

import java.io.File;

import static edu.touro.mco152.bm.App.*;
import static edu.touro.mco152.bm.DiskMark.MarkType.READ;


public abstract class ReadWriteCommands implements Runnable, Command{

    DiskRun run;

    /*
          init local vars that keep track of benchmarks, and a large read/write buffer
         */
    int wUnitsComplete = 0, rUnitsComplete = 0, unitsComplete;
    int wUnitsTotal = App.writeTest ? numOfBlocks * numOfMarks : 0;
    int rUnitsTotal = App.readTest ? numOfBlocks * numOfMarks : 0;
    int unitsTotal = wUnitsTotal + rUnitsTotal;
    float percentComplete;

    int blockSize = blockSizeKb * KILOBYTE;
    byte[] blockArr = new byte[blockSize];
    for (int b = 0; b < blockArr.length; b++) {
        if (b % 2 == 0) {
            blockArr[b] = (byte) 0xFF;
        }
    }

    DiskMark wMark, rMark;  // declare vars that will point to objects used to pass progress to UI

    public ReadWriteCommands(DiskRun.IOMode mode, DiskRun.BlockSequence sequence, int numOfMarks, int numOfBlocks, int blockSizeKb, long targetTxSizeKb, String dirLocation) {
        run = new DiskRun(mode, sequence);
        run.setNumMarks(numOfMarks);
        run.setNumBlocks(numOfBlocks);
        run.setBlockSize(blockSizeKb);
        run.setTxSize(targetTxSizeKb);
        run.setDiskInfo(dirLocation);
    }

    @Override
    public void run(){// Tell logger and GUI to display what we know so far about the Run
        msg("disk info: ("+run.getDiskInfo() +")");

        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());
    }

    private void markLoop(){
        for (int m = startFileNum; m < startFileNum + App.numOfMarks && !isCancelled(); m++) {

            if (App.multiFile) {
                testFile = new File(dataDir.getAbsolutePath() + File.separator + "testdata" + m + ".jdm");
            }
            rMark = new DiskMark(READ);  // starting to keep track of a new benchmark
            rMark.setMarkNum(m);
            long startTime = System.nanoTime();
            long totalBytesReadInMark = 0;
        }
    }
}
