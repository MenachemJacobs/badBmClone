package edu.touro.mco152.bm.ObserverElements;

import edu.touro.mco152.bm.persist.EM;
import jakarta.persistence.EntityManager;

public class EntityManagerObserver implements SubjectObservers {
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
