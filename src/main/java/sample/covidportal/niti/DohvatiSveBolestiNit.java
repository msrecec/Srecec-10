package main.java.sample.covidportal.niti;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import main.java.sample.controllers.DodavanjeNoveBolestiController;
import main.java.sample.controllers.PocetniEkranController;
import main.java.sample.controllers.PretragaBolestiController;
import main.java.sample.controllers.PretragaSimptomaController;
import main.java.sample.covidportal.baza.BazaPodataka;
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

public class DohvatiSveBolestiNit implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DohvatiSveBolestiNit.class);
    private ObservableList<Bolest> observableListaBolesti;
    private boolean isVirus;
    private TableView tablicaBolesti;

    public DohvatiSveBolestiNit(ObservableList<Bolest> observableListaBolesti, TableView tablicaBolesti, boolean isVirus) {
        this.observableListaBolesti = observableListaBolesti;
        this.tablicaBolesti = tablicaBolesti;
        this.isVirus = isVirus;
    }

    private synchronized void dohvatiSveBolestiSync() throws IOException, SQLException {
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
        List<Bolest> bolesti = BazaPodataka.dohvatiSveBolesti();
        BazaPodataka.aktivnaVezaSBazomPodataka = false;

        observableListaBolesti.addAll(bolesti.stream().filter(z -> (this.isVirus == (z instanceof Virus)) ).collect(Collectors.toList()));

        tablicaBolesti.setItems(observableListaBolesti);

        notifyAll();
    }

    @Override
    public void run() {
        System.out.println("Creating thread...");
        try {
            dohvatiSveBolestiSync();
            System.out.println("Fetched bolesti...");
        } catch (IOException | SQLException e) {
            logger.error(e.getMessage());
            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
            DodavanjeNoveBolestiController.uspio = false;
        }
        System.out.println("Destroying thread...");
    }
}
