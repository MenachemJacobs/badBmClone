package edu.touro.mco152.bm.ObserverElements;

import edu.touro.mco152.bm.SlackManager;
import edu.touro.mco152.bm.persist.DiskRun;

/**
 * Observer implementation that notifies Slack based on specific alerts.
 */
public class SlackObserver implements SubjectObserver {

    /**
     * Updates Slack based on the provided alert code and content.
     * If the alert code indicates a new run, and the maximum run time exceeds
     * 130% of the average run time, a message is posted to Slack.
     * @param alertCode The operation code associated with the update.
     * @param alertContent The content associated with the update.
     */
    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        DiskRun run = (DiskRun) alertContent;

        if (alertCode == ObserverOperationCodes.OperationCode.NEW_RUN)
            if (run.getRunMax() > run.getRunAvg() * 1.3)
                new SlackManager("BadBM")
                        .postMsg2OurChannel("A run iteration has resulted in a max run more than 3% greater than the average");
    }
}
