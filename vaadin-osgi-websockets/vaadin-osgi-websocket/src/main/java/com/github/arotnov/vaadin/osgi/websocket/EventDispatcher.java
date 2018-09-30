package com.github.arotnov.vaadin.osgi.websocket;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher {
	private final List<Listener> listeners = new ArrayList<>();

	public EventDispatcher() {
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	public void dispatch(String value) {
		listeners.forEach((listener)-> listener.onEvent(value));
	}
}
