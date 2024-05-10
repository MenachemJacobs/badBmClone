package edu.touro.mco152.bm.ObserverElements;

import edu.touro.mco152.bm.SlackManager;


public class SlackObserver implements SubjectObservers {
    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        if (alertCode == ObserverOperationCodes.OperationCode.NEW_RUN) {
            new SlackManager("BadBM").postMsg2OurChannel(":smile: Benchmark completed");
        }
    }
}
