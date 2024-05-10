package edu.touro.mco152.bm.ObserverElements;

import java.util.ArrayList;

public class ObserverSubject {
    ArrayList<SubjectObserver> observerList = new ArrayList<>();

    //TODO it is possible this should take a list of codes and run them all.
    public void updateAllObservers(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        for (SubjectObserver observer : observerList) {
            observer.update(alertCode, alertContent);
        }
    }

    public void addObserver(SubjectObserver observer) {
        observerList.add(observer);
    }

    public void addListOfObservers(ArrayList<SubjectObserver> observers) {
        observerList.addAll(observers);
    }

    public void removeAllObservers() {
        observerList = new ArrayList<>();
    }
}
