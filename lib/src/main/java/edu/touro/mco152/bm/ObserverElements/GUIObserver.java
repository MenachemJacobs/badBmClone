package edu.touro.mco152.bm.ObserverElements;

import edu.touro.mco152.bm.persist.DiskRun;
import edu.touro.mco152.bm.ui.Gui;

public class GUIObserver implements SubjectObservers {
    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        if (alertCode == ObserverOperationCodes.OperationCode.NEW_RUN)
            Gui.runPanel.addRun((DiskRun) alertContent);
    }
}
