package edu.touro.mco152.bm;

import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface UIWorker<T> {
    boolean isCancelled = true;

    boolean getIsCancelled();

    boolean setIsCancelled(boolean mayInterruptIfRunning);

    void setTheProgress(int progress);

    void publishChunks(DiskMark chunks);

    T getResult() throws InterruptedException, ExecutionException;

    T getResult(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException;

    void addPropChangeListen(PropertyChangeListener listener);

    void executeTask();

    void assignDoInBackground(Runnable backgroundTask);

    void assignHardWare(DiskWorker diskWorker);

    void onTaskCompleted(boolean result);
}
