package main.java.sample.covidportal.niti;

import main.java.sample.Main;
import main.java.sample.controllers.PocetniEkranController;
import main.java.sample.controllers.PretragaZupanijaController;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.NepostojecaZupanija;
import main.java.sample.covidportal.model.Zupanija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class NajviseZarazenihNit implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NajviseZarazenihNit.class);
    private static Zupanija zupanija = null;

    public static String getImeZupanije() {
        if (Optional.ofNullable(zupanija).isPresent()) {
            return zupanija.getNaziv();
        } else {
            return "nepostojeca";
        }
    }

    private synchronized Zupanija dohvatiZupanijuSNajvecimBrojemZarazenihSync() throws NepostojecaZupanija, SQLException, IOException {
        Zupanija zupanija;
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
        zupanija = BazaPodataka.dohvatiZupanijuSNajvecimBrojemZarazenih();
        BazaPodataka.aktivnaVezaSBazomPodataka = false;
        notifyAll();
        return zupanija;
    }

    @Override
    public void run() {
        System.out.println("Creating thread...");
        while(true) {
            try {
                zupanija = dohvatiZupanijuSNajvecimBrojemZarazenihSync();

                System.out.println("Zupanija s najvecim postotkom zarazenih je: " + zupanija.getNaziv());

                Thread.sleep(TimeUnit.SECONDS.toMillis(Main.MAX_VRIJEME_CEKANJA));

            } catch (SQLException | IOException | InterruptedException | NepostojecaZupanija throwables) {
                logger.error(throwables.getMessage());
                System.out.println("Exception in thread...");
                BazaPodataka.aktivnaVezaSBazomPodataka = false;
                notifyAll();
                break;
            }
            System.out.println("Destroying thread...");
        }
        System.out.println("Destroying thread...");
    }
}
