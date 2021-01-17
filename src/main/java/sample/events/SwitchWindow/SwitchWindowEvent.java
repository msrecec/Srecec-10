package main.java.sample.events.SwitchWindow;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class SwitchWindowEvent extends Event {
    public static final EventType<SwitchWindowEvent> SWITCH_WINDOW = new EventType<>(Event.ANY, "SWITCH_WINDOW");

    public SwitchWindowEvent(EventType<? extends Event> eventType) {
        super(SWITCH_WINDOW);
    }

    public SwitchWindowEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, SWITCH_WINDOW);
    }

    @Override
    public EventType<? extends Event> getEventType() {
        return (EventType<? extends SwitchWindowEvent>)super.getEventType();
    }
}
