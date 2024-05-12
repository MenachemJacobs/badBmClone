package edu.touro.mco152.bm.ObserverElements;

import edu.touro.mco152.bm.persist.EM;
import jakarta.persistence.EntityManager;

/**
 * Observer implementation that updates an entity manager based on specific alerts.
 */
public class EntityManagerObserver implements SubjectObserver {

    /**
     * Updates the entity manager based on the provided alert code and content.
     * If the alert code indicates a new run, the provided content is persisted
     * into the entity manager (e.g., Derby Database) and added to a GUI panel.
     * @param alertCode The operation code associated with the update.
     * @param alertContent The content associated with the update.
     */
    @Override
    public void update(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        if (alertCode == ObserverOperationCodes.OperationCode.NEW_RUN) {

        /*
        Persist info about the Write BM Run (e.g. into Derby Database) and add it to a GUI panel
        */
            EntityManager em = EM.getEntityManager();
            em.getTransaction().begin();
            em.persist(alertContent);
            em.getTransaction().commit();
        }
    }
}
