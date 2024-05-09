package edu.touro.mco152.bm.Command;


import edu.touro.mco152.bm.App;
import edu.touro.mco152.bm.DiskMark;
import edu.touro.mco152.bm.UIWorker;
import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.persist.EM;
import edu.touro.mco152.bm.ui.Gui;
import jakarta.persistence.EntityManager;


import static edu.touro.mco152.bm.App.*;

/**
 * The ReadWriteCommands abstract class provides a framework for implementing read and write
 * benchmarking commands in the jDiskMark benchmarking tool.
 *
 * It imports several classes and interfaces from the jDiskMark library.
 * These include classes for managing disk runs, UI elements, disk marks, and utility methods.
 *
 * This class declares and initializes local variables to keep track of benchmarks and a large
 * read/write buffer. It also provides methods for executing benchmark operations and updating
 * progress to the UI.
 *
 * @param <T> The type of UI worker used for updating UI progress.
 */
public abstract class ReadWriteCommands<T> implements Command {

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
    int startFileNum = App.nextMarkNumber;

    /**
     * Constructs a new ReadWriteCommands object with the specified parameters.
     *
     * @param mode The I/O mode for the disk run (READ or WRITE).
     * @param sequence The block sequence for the disk run.
     * @param numOfMarks The number of marks for the disk run.
     * @param numOfBlocks The number of blocks per mark.
     * @param blockSize The size of each block.
     * @param targetTxSizeKb The target transaction size in kilobytes.
     * @param dirLocation The directory location for the disk run.
     * @param myWorker The UI worker for updating UI progress.
     */
    public ReadWriteCommands(DiskRun.IOMode mode, DiskRun.BlockSequence sequence, int numOfMarks, int numOfBlocks, int blockSize, long targetTxSizeKb, String dirLocation, UIWorker<T> myWorker) {
        run = new DiskRun(mode, sequence);
        run.setNumMarks(numOfMarks);
        run.setNumBlocks(numOfBlocks);
        run.setBlockSize(blockSize);
        run.setTxSize(targetTxSizeKb);
        run.setDiskInfo(dirLocation);

        wUnitsTotal = (mode == DiskRun.IOMode.WRITE) ? numOfBlocks * numOfMarks : 0;
        rUnitsTotal = (mode == DiskRun.IOMode.READ) ? numOfBlocks * numOfMarks : 0;
        unitsTotal = wUnitsTotal + rUnitsTotal;

        blockArr = new byte[blockSize];
        for (int b = 0; b < blockArr.length; b++) {
            if (b % 2 == 0) {
                blockArr[b] = (byte) 0xFF;
            }
        }

        this.myWorker = myWorker;
    }

    /**
     * Executes the benchmarking operation and updates UI progress.
     */
    @Override
    public void execute() {
        // Tell logger and GUI to display what we know so far about the Run
        msg("disk info: (" + run.getDiskInfo() + ")");

        Gui.chartPanel.getChart().getTitle().setVisible(true);
        Gui.chartPanel.getChart().getTitle().setText(run.getDiskInfo());

        particularOp();

        /*
        Persist info about the Write BM Run (e.g. into Derby Database) and add it to a GUI panel
        */
        EntityManager em = EM.getEntityManager();
        em.getTransaction().begin();
        em.persist(run);
        em.getTransaction().commit();

        Gui.runPanel.addRun(run);
    }

    /**
     * Executes the benchmarking operation.
     */
    abstract public void particularOp();
}
