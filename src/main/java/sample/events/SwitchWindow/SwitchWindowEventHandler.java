package main.java.sample.events.SwitchWindow;

import javafx.event.EventHandler;
import main.java.sample.covidportal.niti.NajviseZarazenihNit;

public class SwitchWindowEventHandler implements EventHandler<SwitchWindowEvent> {
    @Override
    public void handle(SwitchWindowEvent switchWindowEvent) {
        if(NajviseZarazenihNit.exists()) {
            NajviseZarazenihNit.destroy();
        }
    }
}
