package edu.touro.mco152.bm.ObserverElements;

/**
 * Defines operation codes for observer updates.
 */
public class ObserverOperationCodes {
    public enum OperationCode {
        UPDATE_GUI,         // Indicates an update to the GUI.
        SEND_E_COMMUNIQUE,  // Indicates sending an e-communication.
        READ,               // Indicates a read operation.
        WRITE,              // Indicates a write operation.
        NEW_RUN             // Indicates a new run.
    }
}
