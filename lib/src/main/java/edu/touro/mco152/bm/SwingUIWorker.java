package edu.touro.mco152.bm;

import com.sun.codemodel.JForEach;
import edu.touro.mco152.bm.ui.Gui;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static edu.touro.mco152.bm.App.dataDir;

public class SwingUIWorker<T> extends SwingWorker<Boolean, T> implements UIWorker<T> {
    private Runnable backgroundTask;
    private DiskWorker currentHardWare;

    public void assignDoInBackground(Runnable backgroundTask) {
        this.backgroundTask = backgroundTask;
    }
    public void assignHardWare(DiskWorker passedHardWare){currentHardWare = passedHardWare;}

    @Override
    public boolean getIsCancelled() {
        return isCancelled();
    }

    @Override
    public boolean setIsCancelled(boolean cancelCommand) {
        return cancel(cancelCommand);
    }

    @Override
    public void setTheProgress(int progress) {
        setProgress(progress);
    }

    //@SafeVarargs
    @Override
    public final void publishChunks(DiskMark chunks) {
        publish((T) chunks);
    }

    @Override
    public T getResult() throws InterruptedException, ExecutionException {
        return (T) get();
    }

    @Override
    public T getResult(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (T) get(timeout, unit);
    }

    @Override
    public void addPropChangeListen(PropertyChangeListener listener) {
        addPropertyChangeListener(listener);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    protected Boolean doInBackground() throws Exception {
        try {
            backgroundTask.run();
            return true;
        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public final void executeTask() {
        execute();
    }

    /**
     * Process a list of 'chunks' that have been processed, ie that our thread has previously
     * published to Swing. For my info, watch Professor Cohen's video -
     * Module_6_RefactorBadBM Swing_DiskWorker_Tutorial.mp4
     *
     * @param markList a list of DiskMark objects reflecting some completed benchmarks
     */
    @Override
    protected void process(List<T> markList) {
        for(T dm : markList) {
            if (((DiskMark)dm).type == DiskMark.MarkType.WRITE) {
                Gui.addWriteMark((DiskMark) dm);
            } else {
                Gui.addReadMark((DiskMark)dm);
            }
            System.out.println("");
        }
    }

    /**
     * Called when doInBackGround method of SwingWorker successfully or unsuccessfully finishes or is aborted.
     * This method is called by Swing and has access to the get method within it's scope, which returns the computed
     * result of the doInBackground method.
     */
    @Override
    protected void done() {
        // Obtain final status, might from doInBackground ret value, or SwingWorker error
        try {
            currentHardWare.setLastStatus((boolean) getResult());   // record for future access
        } catch (Exception e) {
            Logger.getLogger(App.class.getName()).warning("Problem obtaining final status: " + e.getMessage());
        }

        if (App.autoRemoveData) {
            Util.deleteDirectory(dataDir);
        }
        App.state = App.State.IDLE_STATE;
        Gui.mainFrame.adjustSensitivity();
    }
}
