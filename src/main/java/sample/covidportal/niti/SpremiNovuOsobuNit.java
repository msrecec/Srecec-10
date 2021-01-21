package main.java.sample.covidportal.niti;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import main.java.sample.controllers.*;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.NepostojecaBolest;
import main.java.sample.covidportal.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SpremiNovuOsobuNit implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SpremiNovuOsobuNit.class);
    private Osoba osoba;

    public SpremiNovuOsobuNit(Osoba osoba) {
        this.osoba = osoba;
    }

    private synchronized void spremiNovuOsobuSync() throws IOException, SQLException, NepostojecaBolest {
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("Waiting for thread...");
                wait();
            } catch(InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        BazaPodataka.spremiNovuOsobu(osoba);
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();

    }

    @Override
    public void run() {
        System.out.println("Creating thread...");
        try {
            spremiNovuOsobuSync();
            System.out.println("Saved osoba...");
        } catch (IOException | SQLException | NepostojecaBolest e) {
            logger.error(e.getMessage());
            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
        }
        System.out.println("Destroying thread...");
    }
}
