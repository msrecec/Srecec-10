package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.DuplikatSimptoma;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.VrijednostEnumeracije;
import main.java.sample.covidportal.niti.DohvatiSveSimptomeNit;
import main.java.sample.covidportal.niti.SpremiNoviSimptomNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DodavanjeNovogSimptomaController implements VrijednostEnumeracije {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNovogSimptomaController.class);
    private static ExecutorService executorServiceDodavanjeSimptoma;
    @FXML
    private TextField nazivSimptoma;

    @FXML
    private TextField vrijednostSimptoma;




    public void dodajNoviSimptom() {
        try {

            String nazivSimptomaText = nazivSimptoma.getText();
            String vrijednostSimptomaText = vrijednostSimptoma.getText();

            if(nazivSimptomaText.isBlank() || vrijednostSimptomaText.isBlank()) {
                throw new PraznoPolje();
            }

            Simptom noviSimptom = new Simptom((long) 1, nazivSimptomaText, VrijednostEnumeracije.vrijednostZarazno(vrijednostSimptomaText));

            executorServiceDodavanjeSimptoma = Executors.newSingleThreadExecutor();
            executorServiceDodavanjeSimptoma.execute(new SpremiNoviSimptomNit(noviSimptom));

        } catch (NumberFormatException | PraznoPolje | DuplikatSimptoma exc) {
            logger.error(exc.getMessage());
            PocetniEkranController.neuspjesanUnos(exc.getMessage());
        }
    }
}
