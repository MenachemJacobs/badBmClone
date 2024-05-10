package edu.touro.mco152.bm.ObserverElements;

import java.util.ArrayList;
import java.util.Arrays;

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

    public void addListOfObservers(SubjectObserver[] observers) {
        observerList.addAll(Arrays.asList(observers));
    }

    public void removeAllObservers() {
        observerList = new ArrayList<>();
    }
}
