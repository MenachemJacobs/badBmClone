package edu.touro.mco152.bm.ObserverElements;

import java.util.ArrayList;

public class ObserverSubject {
    ArrayList<SubjectObservers> observerList = new ArrayList<>();

    public void updateAllObservers(ObserverOperationCodes.OperationCode alertCode, Object alertContent){
        for(SubjectObservers observer : observerList){
            observer.update(alertCode, alertContent);
        }
    }

    public void addObserver(SubjectObservers observer){observerList.add(observer);}
}
