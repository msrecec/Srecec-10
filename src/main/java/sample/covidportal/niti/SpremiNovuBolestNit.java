package main.java.sample.covidportal.niti;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import main.java.sample.controllers.*;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.NepostojecaBolest;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Virus;
import main.java.sample.covidportal.model.Zupanija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SpremiNovuBolestNit implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SpremiNovuBolestNit.class);
    private Bolest bolest;

    public SpremiNovuBolestNit(Bolest bolest) {
        this.bolest = bolest;
    }

    private synchronized void spremiNovuBolestSync() throws IOException, SQLException, NepostojecaBolest {
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("Waiting for thread...");
                wait();
            } catch(InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        BazaPodataka.spremiNovuBolest(bolest);
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();

    }

    @Override
    public void run() {
        System.out.println("Creating thread...");
        try {
            spremiNovuBolestSync();
            System.out.println("Saved bolesti...");
        } catch (IOException | SQLException | NepostojecaBolest e) {
            logger.error(e.getMessage());
            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
        }
        System.out.println("Destroying thread...");
    }
}
