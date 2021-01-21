package main.java.sample.covidportal.niti;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import main.java.sample.controllers.PocetniEkranController;
import main.java.sample.controllers.PretragaSimptomaController;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Zupanija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DohvatiSveSimptomeNit implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DohvatiSveSimptomeNit.class);
    private ObservableList<Simptom> observableListaSimptoma;
    private TableView tablicaSimptoma ;

    public DohvatiSveSimptomeNit(ObservableList<Simptom> observableListaSimptoma, TableView tablicaSimptoma) {
        this.observableListaSimptoma = observableListaSimptoma;
        this.tablicaSimptoma = tablicaSimptoma;
    }

    private synchronized void dohvatiSveSimptomeSync() throws IOException, SQLException {
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
        List<Simptom> simptomi = BazaPodataka.dohvatiSveSimptome();
        BazaPodataka.aktivnaVezaSBazomPodataka = false;

        observableListaSimptoma.addAll(simptomi);
        tablicaSimptoma.setItems(observableListaSimptoma);

        notifyAll();
    }

    @Override
    public void run() {
        try {
            dohvatiSveSimptomeSync();
        } catch (IOException | SQLException e) {
            logger.error(e.getMessage());
            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
            PocetniEkranController.neuspjesanUnos(e.getMessage());
        }
        System.out.println("Destroying thread...");
    }
}
