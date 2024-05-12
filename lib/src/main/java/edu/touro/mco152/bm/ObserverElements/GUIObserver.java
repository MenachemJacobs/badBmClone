package edu.touro.mco152.bm.ObserverElements;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;

/**
 * Observer implementation that updates the GUI based on specific alerts.
 */
public class GUIObserver implements SubjectObserver {

    /**
     * Updates the GUI based on the provided alert code and content.
     * If the alert code indicates a new run, the provided content (a DiskRun object)
     * is added to the GUI's run panel.
     * @param alertCode The operation code associated with the update.
     * @param alertContent The content associated with the update.
     */
    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        if (alertCode == ObserverOperationCodes.OperationCode.NEW_RUN)
            Gui.runPanel.addRun((DiskRun) alertContent);
    }
}
