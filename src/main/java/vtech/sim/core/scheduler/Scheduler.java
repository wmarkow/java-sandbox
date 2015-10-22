package vtech.sim.core.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vtech.sim.core.Event;
import vtech.sim.core.Process;

public class Scheduler implements EventScheduler {
  private List<Event> events = new ArrayList<Event>();
  private Clock clock = new Clock();

  private EventsComparator comparator = new EventsComparator();

  @Override
  public void addEvent(Process process, double deltaMillis) {
    events.add(new Event(process, clock.getMillisTime() + deltaMillis));

    Collections.sort(events, comparator);
  }
  
  @Override
  public void addEvent(Process process, double deltaMillis, int eventType) {
    events.add(new Event(process, clock.getMillisTime() + deltaMillis, eventType));

    Collections.sort(events, comparator);
  }
  
  @Override
  public void addEvent(Process process, double deltaMillis, int eventType, Object param) {
    events.add(new Event(process, clock.getMillisTime() + deltaMillis, eventType, param));

    Collections.sort(events, comparator);
  }

  public Event getNextEvent() {
    if (events.size() == 0) {
      throw new IllegalStateException("No events to execute!");
    }

    Event event = events.remove(0);
    clock.setCurrentMillisTime(event.getEventMillisTime());
    return event;
  }

  @Override
  public double getCurrentMillisTime() {
    return clock.getMillisTime();
  }
}
