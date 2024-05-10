package edu.touro.mco152.bm.ObserverElements;

import edu.touro.mco152.bm.SwingUIWorker;

public class SwingObserverWrapper implements SubjectObservers {
    SwingUIWorker<Boolean> mySwingWorker = new SwingUIWorker<>();
    ObserverSubject myObserver;

    public SwingObserverWrapper(SwingUIWorker<Boolean> passedWorker) {
        mySwingWorker = passedWorker;
    }

    public SwingUIWorker<Boolean> getSwingWorker() {
        return mySwingWorker;
    }

    public void setMyObserver(ObserverSubject myObserver) {
        this.myObserver = myObserver;
    }

    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {

    }
}
