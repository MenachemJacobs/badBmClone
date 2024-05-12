package edu.touro.mco152.bm.ObserverElements;

public class TestObserver implements SubjectObserver{
    boolean testFlag = false;

    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        testFlag = true;
    }

    public boolean getTestFlag() {return testFlag;}
}
