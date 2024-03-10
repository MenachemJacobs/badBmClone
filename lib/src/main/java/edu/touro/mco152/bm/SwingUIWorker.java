package edu.touro.mco152.bm;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SwingUIWorker<T> extends SwingWorker<Boolean, T> implements UIWorker<T>{
    private final Runnable backgroundTask;

    public SwingUIWorker(Runnable backgroundTask) {
        this.backgroundTask = backgroundTask;
    }

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
    public final void publishChunks(T... chunks) {
        publish(chunks);
    }

    @Override
    public T getResult() throws InterruptedException, ExecutionException {
        return (T)get();
    }

    @Override
    public T getResult(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return (T)get(timeout,unit);
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
        }
        catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return false;
        }
    }
}
