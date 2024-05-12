package edu.touro.mco152.bm.ObserverElements;

/**
 * Observer implementation used for testing purposes.
 */
public class TestObserver implements SubjectObserver{
    /** Flag indicating whether the update method was called. */
    boolean testFlag = false;

    /**
     * Updates the test flag when the update method is called.
     * @param alertCode The operation code associated with the update.
     * @param alertContent The content associated with the update.
     */
    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        testFlag = true;
    }

    /**
     * Gets the value of the test flag.
     * @return The value of the test flag.
     */
    public boolean getTestFlag() {return testFlag;}
}
