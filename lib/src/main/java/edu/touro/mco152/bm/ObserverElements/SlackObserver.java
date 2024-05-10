package edu.touro.mco152.bm.ObserverElements;

import edu.touro.mco152.bm.SlackManager;
import edu.touro.mco152.bm.persist.DiskRun;


public class SlackObserver implements SubjectObserver {
    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        DiskRun run = (DiskRun) alertContent;

        if (alertCode == ObserverOperationCodes.OperationCode.NEW_RUN)
            if (run.getRunMax() > run.getRunAvg() * 1.3)
                new SlackManager("BadBM")
                        .postMsg2OurChannel("A run iteration has resulted in a max run more than 3% greater than the average");
    }
}
