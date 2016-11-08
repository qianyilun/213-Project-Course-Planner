package ca.cmpt213.courseplanner.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Simple observable class for maintaining and notifying a set of observers.
 */
public class ModelObservableImpl implements ModelObserbable {
	private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
	
	@Override
	public void addListener(ChangeListener listener){
		listeners.add(listener);
	}
	
	@Override
	public void notifyListeners() {
		ChangeEvent event = new ChangeEvent(this);
		for (ChangeListener listener : listeners) {
			listener.stateChanged(event);
		}
	}
}
