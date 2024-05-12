package edu.touro.mco152.bm.ObserverElements;

/**
 * Represents an observer that can receive updates from a subject.
 */
public interface SubjectObserver {

    /**
     * Called by a subject to update the observer with a specific alert code and content.
     * @param alertCode The operation code associated with the update.
     * @param alertContent The content associated with the update.
     */
    void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent);
}
