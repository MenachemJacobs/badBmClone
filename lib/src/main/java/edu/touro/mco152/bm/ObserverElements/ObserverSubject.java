package edu.touro.mco152.bm.ObserverElements;

import java.util.ArrayList;

public class ObserverSubject {
    ArrayList<SubjectObservers> observerList = new ArrayList<>();

    public void alertAllObservers(){
        System.out.println("notify reached");
    }

    public void addObserver(SubjectObservers observer){observerList.add(observer);}
}
