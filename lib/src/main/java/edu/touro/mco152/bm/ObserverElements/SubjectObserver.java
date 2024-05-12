package edu.touro.mco152.bm.ObserverElements;

public interface SubjectObserver {

    void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent);
}
