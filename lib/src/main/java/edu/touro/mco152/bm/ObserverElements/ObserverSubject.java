package edu.touro.mco152.bm.ObserverElements;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a subject that can be observed by one or more observers.
 */
public class ObserverSubject {

    /** List of observers registered to this subject. */
    ArrayList<SubjectObserver> observerList = new ArrayList<>();

    /**
     * Notifies all registered observers with the provided alert code and content.
     * @param alertCode The operation code to alert observers about.
     * @param alertContent The content associated with the alert.
     */
    //TODO it is possible this should take a list of codes and run them all.
    public void updateAllObservers(ObserverOperationCodes.OperationCode alertCode, Object alertContent) {
        for (SubjectObserver observer : observerList) {
            observer.update(alertCode, alertContent);
        }
    }

    /**
     * Adds an observer to the list of registered observers.
     * @param observer The observer to be added.
     */
    public void addObserver(SubjectObserver observer) {
        observerList.add(observer);
    }

    /**
     * Adds an array of observers to the list of registered observers.
     * @param observers An array of observers to be added.
     */
    public void addListOfObservers(SubjectObserver[] observers) {
        observerList.addAll(Arrays.asList(observers));
    }

    /**
     * Removes all observers from the list of registered observers.
     */
    public void removeAllObservers() {
        observerList = new ArrayList<>();
    }
}
