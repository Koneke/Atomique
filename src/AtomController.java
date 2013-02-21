#include "../head"
package lh.koneke.games.Atomique;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Controller.Type;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;
import net.java.games.input.Version;

public class AtomController {
	static int uidc = 0;

	HashMap<String, Float> components;
	HashMap<String, Float> lastpoll;
	int uid;
	String name;
	Controller controller;

	public AtomController(String name) {
		components = new HashMap<String, Float>();
		lastpoll = new HashMap<String, Float>(components);
		this.uid = uidc;
		uidc = uidc + 1;
		this.name = name;
	}
	
	public int getuid() { return uid; }
	public String getName() { return name; } 

	public float getValue(String comp) {
		if(!components.keySet().contains(comp)) {
			components.put(comp, 0f); }
		return components.get(comp); }
	public void setValue(String comp, float value) { components.put(comp, value); }
	
	public void setController(Controller c) { this.controller = c; }
	public Controller getController() { return this.controller; }
	public void update() {
		controller.poll();
		EventQueue q = controller.getEventQueue();
		Event event = new Event();
		while(q.getNextEvent(event)) {
			setValue(event.getComponent().getIdentifier().toString(), event.getValue());
		}
	}

	public void postupdate() {
		lastpoll = new HashMap<String, Float>(components); }

	public float getLast(String comp) {
		if(!lastpoll.keySet().contains(comp)) {
			lastpoll.put(comp, 0f); }
		return lastpoll.get(comp); }
}
