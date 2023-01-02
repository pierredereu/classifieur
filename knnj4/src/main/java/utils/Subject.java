package utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    protected List<Observer> listeners;
    protected List<FunctionalObserver> functionalListeners;

    protected Subject() {
        listeners = new ArrayList<>();
        functionalListeners = new ArrayList<>();
    }

    public void addListener(Observer obs) {
        if (!listeners.contains(obs)) {
            listeners.add(obs);
        }
    }

    public void addListener(FunctionalObserver obs){
        if (!functionalListeners.contains(obs)) {
            functionalListeners.add(obs);
        }
    }



    public void removeListener(Observer obs) {
        if (listeners.contains(obs)) {
            listeners.remove(obs);
        }
    }

    public void removeListener(FunctionalObserver obs) {
        if (functionalListeners.contains(obs)) {
            functionalListeners.remove(obs);
        }
    }

    public void notifyObservers() {
        for (Observer o : listeners) {
            o.update(this);
        }

        for(FunctionalObserver fo : functionalListeners){
            fo.update(this,null);
        }
    }

    public void notifyObservers(Object data) {
        for (Observer o : listeners) {
            o.update(this, data);
        }

        for(FunctionalObserver fo : functionalListeners){
            fo.update(this,data);
        }
    }


}
