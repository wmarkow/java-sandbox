package vtech.sim.core;

import java.util.ArrayList;
import java.util.List;

import vtech.sim.core.Event;

public class EventScheduler {

  private List<Event> events = new ArrayList<Event>();

  public void addEvent(Event event) {
    events.add(event);
  }
}
