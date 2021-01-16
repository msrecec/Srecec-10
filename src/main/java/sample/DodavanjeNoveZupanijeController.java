package main.java.sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.iznimke.ZupanijaIstogNaziva;
import main.java.sample.covidportal.model.Zupanija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class DodavanjeNoveZupanijeController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveZupanijeController.class);

    @FXML
    private TextField nazivZupanije;

    @FXML
    private TextField brojStanovnikaZupanije;

    @FXML
    private TextField brojZarazenihStanovnikaZupanije;

    public void dodajNovuZupaniju() {
        try {

            if(
                nazivZupanije.getText().isBlank() ||
                brojStanovnikaZupanije.getText().isBlank() ||
                brojZarazenihStanovnikaZupanije.getText().isBlank()
            ) {
                throw new PraznoPolje();
            }

            String nazivZupanijeText = nazivZupanije.getText();
            Integer brojStanovnikaZupanijeNumber = Integer.parseInt(brojStanovnikaZupanije.getText());
            Integer brojZarazenihStanovnikaZupanijeNumber = Integer.parseInt(brojZarazenihStanovnikaZupanije.getText());

            Zupanija novaZupanija = new Zupanija((long) 1, nazivZupanijeText, brojStanovnikaZupanijeNumber, brojZarazenihStanovnikaZupanijeNumber);

            BazaPodataka.spremiNovuZupaniju(novaZupanija);

            logger.info("Unesena je zupanija: " + novaZupanija.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException | NumberFormatException | ZupanijaIstogNaziva | SQLException | PraznoPolje ex) {
            logger.error(ex.getMessage());
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        }
    }
}
