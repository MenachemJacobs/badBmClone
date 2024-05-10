package edu.touro.mco152.bm.ObserverElements;

public interface SubjectObserver {

    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent);
}
