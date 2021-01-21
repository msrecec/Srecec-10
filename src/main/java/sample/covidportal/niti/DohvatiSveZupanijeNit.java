package main.java.sample.covidportal.niti;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import main.java.sample.controllers.PocetniEkranController;
import main.java.sample.controllers.PretragaBolestiController;
import main.java.sample.controllers.PretragaSimptomaController;
import main.java.sample.controllers.PretragaZupanijaController;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Zupanija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.SortedSet;

public class DohvatiSveZupanijeNit implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DohvatiSveZupanijeNit.class);
    private ObservableList<Zupanija> observableListaZupanija;
    private TableView tablicaZupanija ;

    public DohvatiSveZupanijeNit(ObservableList<Zupanija> observableListaZupanija, TableView tablicaZupanija) {
        this.observableListaZupanija = observableListaZupanija;
        this.tablicaZupanija = tablicaZupanija;
    }

    private synchronized void dohvatiSveZupanijeSync() throws IOException, SQLException {
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
        SortedSet<Zupanija> zupanije = BazaPodataka.dohvatiSveZupanije();
        BazaPodataka.aktivnaVezaSBazomPodataka = false;

        System.out.println(zupanije);

        observableListaZupanija.addAll(zupanije);
        tablicaZupanija.setItems(observableListaZupanija);

        notifyAll();
    }

    @Override
    public void run() {
        try {
            dohvatiSveZupanijeSync();
        } catch (IOException | SQLException e) {
            logger.error(e.getMessage());
            PocetniEkranController.neuspjesanUnos(e.getMessage());
            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
        }
        System.out.println("Destroying thread...");
    }
}
