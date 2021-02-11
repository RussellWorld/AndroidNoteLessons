package com.example.androidnotelessons.observe;

import com.example.androidnotelessons.data.Note;
import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    public void unsubscribeAll() {
        observers.clear();
    }

    public void notifySingle(Note note) {
//        for (Observer observer : observers) {
//            observer.updateNotes(note);
//            unsubscribe(observer);
//        }
        int observersQuantity = observers.size() - 1;
        for (int i = observersQuantity; i >= 0; i--) {
            Observer observer = observers.get(i);
            observer.updateNotes(note);
            unsubscribe(observer);
        }
    }

}
