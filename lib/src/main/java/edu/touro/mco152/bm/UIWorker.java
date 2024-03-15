package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface UIWorker<T> {
    boolean isCancelled = true;

    public boolean getIsCancelled();

    public boolean setIsCancelled(boolean mayInterruptIfRunning);

    void setTheProgress(int progress);

    void publishChunks(DiskMark chunks);

    public T getResult() throws InterruptedException, ExecutionException;

    public T getResult(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException;

    public void addPropChangeListen(PropertyChangeListener listener);

    public void executeTask();

    public void assignDoInBackground(Runnable backgroundTask);

    void assignHardWare(DiskWorker diskWorker);

    void onTaskCompleted(boolean result);
}
