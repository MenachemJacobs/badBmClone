package edu.touro.mco152.bm.ObserverElements;

import java.util.ArrayList;

public class ObserverSubject {
    ArrayList<SubjectObservers> observerList = new ArrayList<>();

    //TODO it is possible this should take a list of codes and run them all.
    public void updateAllObservers(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        for (SubjectObservers observer : observerList) {
            observer.update(alertCode, alertContent);
        }
    }

    public void addObserver(SubjectObservers observer) {
        observerList.add(observer);
    }

    public void addListOfObservers(ArrayList<SubjectObservers> observers) {
        observerList.addAll(observers);
    }

    public void removeAllObservers() {
        observerList = new ArrayList<>();
    }
}
