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

public class SpremiNovuZupanijuNit implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SpremiNovuOsobuNit.class);
    private Zupanija zupanija;

    public SpremiNovuZupanijuNit(Zupanija zupanija) {

        this.zupanija = zupanija;

    }

    private synchronized void spremiNovuZupanijuSync() throws IOException, SQLException {
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("Waiting for thread...");
                wait();
            } catch(InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        BazaPodataka.spremiNovuZupaniju(zupanija);
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }

    @Override
    public void run() {
        System.out.println("Creating thread...");
        try {
            spremiNovuZupanijuSync();
            System.out.println("Saved zupanija...");
            logger.info("Unesena je osoba: " + zupanija.getNaziv());
        } catch (IOException | SQLException e) {
            logger.error(e.getMessage());
            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
        }
        System.out.println("Destroying thread...");
    }
}
