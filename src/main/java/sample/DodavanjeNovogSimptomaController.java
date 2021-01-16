package main.java.sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.enumeracija.VrijednostSimptoma;
import main.java.sample.covidportal.iznimke.DuplikatSimptoma;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.VrijednostEnumeracije;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class DodavanjeNovogSimptomaController implements VrijednostEnumeracije {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNovogSimptomaController.class);

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

            BazaPodataka.spremiNoviSimptom(noviSimptom);

            logger.info("Unesen je simptom: " + noviSimptom.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (NumberFormatException | PraznoPolje | DuplikatSimptoma | IOException | SQLException exc) {
            logger.error(exc.getMessage());
            PocetniEkranController.neuspjesanUnos(exc.getMessage());
        }
    }
}
