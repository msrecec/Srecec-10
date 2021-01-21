package main.java.sample.covidportal.niti;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import main.java.sample.controllers.*;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.NepostojecaBolest;
import main.java.sample.covidportal.iznimke.NepostojecaZupanija;
import main.java.sample.covidportal.model.Osoba;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Zupanija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DohvatiSveOsobeNit implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DohvatiSveOsobeNit.class);
    private ObservableList<Osoba> observableListaOsoba;
    private TableView tablicaOsoba ;

    public DohvatiSveOsobeNit(ObservableList<Osoba> observableListaOsoba, TableView tablicaOsoba) {
        this.observableListaOsoba = observableListaOsoba;
        this.tablicaOsoba = tablicaOsoba;
    }

    private synchronized void dohvatiSveOsobeSync() throws IOException, SQLException, NepostojecaBolest, NepostojecaZupanija {
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("Waiting for thread...");
                wait();
            } catch(InterruptedException e) {
                logger.error(e.getMessage());
                PocetniEkranController.neuspjesanUnos(e.getMessage());
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        List<Osoba> osobe = BazaPodataka.dohvatiSveOsobe();
        BazaPodataka.aktivnaVezaSBazomPodataka = false;

        observableListaOsoba.addAll(osobe);
        tablicaOsoba.setItems(observableListaOsoba);

        notifyAll();
    }

    @Override
    public void run() {
        try {
            dohvatiSveOsobeSync();
        } catch (IOException | SQLException | NepostojecaZupanija | NepostojecaBolest e) {
            logger.error(e.getMessage());
            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
            PocetniEkranController.neuspjesanUnos(e.getMessage());
        }
        System.out.println("Destroying thread...");
    }
}
