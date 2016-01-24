package com.cgp.saral.observer;

import java.util.Observable;

/**
 * Created by WeeSync on 09/10/15.
 */
public class FragmentObserver extends Observable{

    public boolean isComingFromReset() {
        return isComingFromReset;
    }

    public FragmentObserver setIsComingFromReset(boolean isComingFromReset) {
        this.isComingFromReset = isComingFromReset;
        setChanged();
        notifyObservers();
        return this;
    }

    boolean isComingFromReset;
}
