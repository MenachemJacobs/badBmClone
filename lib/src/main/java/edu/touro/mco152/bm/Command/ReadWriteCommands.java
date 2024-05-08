package edu.touro.mco152.bm.Command;


import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.DiskMark;
import edu.touro.mco152.bm.UIWorker;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;


import static edu.touro.mco152.bm.App.*;


public abstract class ReadWriteCommands<T> implements Runnable, Command {

    DiskRun run;

    /*
    init local vars that keep track of benchmarks, and a large read/write buffer
    */
    int wUnitsComplete = 0, rUnitsComplete = 0, unitsComplete;
    int wUnitsTotal;
    int rUnitsTotal;
    int unitsTotal;
    float percentComplete;

    int blockSize;
    byte[] blockArr;

    DiskMark genericMark;  // declare vars that will point to objects used to pass progress to UI
    DiskMark.MarkType OP;

    UIWorker<T> myWorker;
    //TODO REMOVE REFERENCE TO APP
    int startFileNum = App.nextMarkNumber;

    public ReadWriteCommands(DiskRun.IOMode mode, DiskRun.BlockSequence sequence, int numOfMarks, int numOfBlocks, int blockSizeKb, long targetTxSizeKb, String dirLocation, UIWorker<T> myWorker) {
        run = new DiskRun(mode, sequence);
        run.setNumMarks(numOfMarks);
        run.setNumBlocks(numOfBlocks);
        run.setBlockSize(blockSizeKb);
        run.setTxSize(targetTxSizeKb);
        run.setDiskInfo(dirLocation);

        wUnitsTotal = (mode == DiskRun.IOMode.WRITE) ? numOfBlocks * numOfMarks : 0;
        rUnitsTotal = (mode == DiskRun.IOMode.READ) ? numOfBlocks * numOfMarks : 0;
        unitsTotal = wUnitsTotal + rUnitsTotal;

        blockSize = blockSizeKb * KILOBYTE;

        blockArr = new byte[blockSize];
        for (int b = 0; b < blockArr.length; b++) {
            if (b % 2 == 0) {
                blockArr[b] = (byte) 0xFF;
            }
        }

        this.myWorker = myWorker;
    }

    @Override
    public void run() {
        // Tell logger and GUI to display what we know so far about the Run
        msg("disk info: (" + run.getDiskInfo() + ")");

        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());

        execute();
    }

    @Override
    abstract public void execute();
}
