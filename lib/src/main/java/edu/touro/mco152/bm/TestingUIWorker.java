package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestingUIWorker<T> implements UIWorker<T> {
    boolean isCancelled = false;
    private Runnable backgroundTask;
    private DiskWorker currentHardWare;
    int progress = 0;

    // Record any success or failure status returned from SwingWorker (might be us or super)
    Boolean lastStatus = null;  // so far unknown

    public void assignDoInBackground(Runnable backgroundTask) {
        this.backgroundTask = backgroundTask;
    }
    public void assignHardWare(DiskWorker passedHardWare){currentHardWare = passedHardWare;}

    @Override
    public void onTaskCompleted(boolean result) {
        setLastStatus(result);
    }

    @Override
    public boolean getIsCancelled() {
        return isCancelled;
    }

    @Override
    public boolean setIsCancelled(boolean mayInterruptIfRunning) {
        return isCancelled = mayInterruptIfRunning;
    }

    /**
     * Sets the {@code progress} bound property.
     * The value should be from 0 to 100.
     *
     * @param progress the progress value to set
     * @throws IllegalArgumentException is value not from 0 to 100
     */
    @Override
    public void setTheProgress(int progress) {
        if (progress < 0 || progress > 100)
            throw new IllegalArgumentException("the value should be from 0 to 100");

        if (this.progress == progress)
            return;

        int oldProgress = this.progress;
        this.progress = progress;
        assertTrue(oldProgress < this.progress);
    }

    @Override
    public void executeTask(){
        try{ doInBackground(); }
        catch (Exception e){ e.printStackTrace(); }
    }

    protected Boolean doInBackground(){
        try { backgroundTask.run(); }
        catch (Exception e) { e.printStackTrace(); }

        return getLastStatus();
    }

    public Boolean getLastStatus() { return lastStatus; }

    public void setLastStatus(Boolean lastStatus) {
        this.lastStatus = lastStatus;
    }

    public int getProgress(){ return progress; }

    @Override
    public T getResult(){ return null; }

    @Override
    public T getResult(long timeout, TimeUnit unit) { return null; }

    @Override
    public void addPropChangeListen(PropertyChangeListener listener) {}

    @Override
    public void publishChunks(DiskMark chunks) {}

}
