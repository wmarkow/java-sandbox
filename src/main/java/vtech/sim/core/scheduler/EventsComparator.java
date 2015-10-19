package vtech.sim.core.scheduler;

import java.util.Comparator;

import vtech.sim.core.Event;

class EventsComparator implements Comparator<Event> {

  @Override
  public int compare(Event o1, Event o2) {
    if (o1.getEventMillisTime() < o2.getEventMillisTime()) {
      return -1;
    }

    if (o1.getEventMillisTime() > o2.getEventMillisTime()) {
      return 1;
    }

    return 0;
  }
}
