package main.java.sample.covidportal.niti;

import main.java.sample.controllers.DodavanjeNoveZupanijeController;
import main.java.sample.controllers.DodavanjeNovogSimptomaController;
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

public class SpremiNoviSimptomNit implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DohvatiSveSimptomeNit.class);
    private Simptom simptom;

    public SpremiNoviSimptomNit(Simptom simptom) {
        this.simptom = simptom;
    }

    private synchronized void spremiNoviSimptomSync() throws IOException, SQLException {
        while(BazaPodataka.aktivnaVezaSBazomPodataka) {
            try {
                System.out.println("Waiting for thread...");
                wait();
            } catch(InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        BazaPodataka.aktivnaVezaSBazomPodataka = true;
        BazaPodataka.spremiNoviSimptom(simptom);
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
    }

    @Override
    public void run() {
        try {
            spremiNoviSimptomSync();
        } catch (IOException | SQLException e) {
            logger.error(e.getMessage());
            BazaPodataka.aktivnaVezaSBazomPodataka = false;
            notifyAll();
        }
        System.out.println("Destroying thread...");
    }
}
