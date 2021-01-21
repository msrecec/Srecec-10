package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.iznimke.ZupanijaIstogNaziva;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.niti.SpremiNovuOsobuNit;
import main.java.sample.covidportal.niti.SpremiNovuZupanijuNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DodavanjeNoveZupanijeController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveZupanijeController.class);
    private static ExecutorService executorServiceDodavanjeNoveZupanije;

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

            executorServiceDodavanjeNoveZupanije = Executors.newSingleThreadExecutor();
            executorServiceDodavanjeNoveZupanije.execute(new SpremiNovuZupanijuNit(novaZupanija));

        } catch (NumberFormatException | ZupanijaIstogNaziva | PraznoPolje ex) {
            logger.error(ex.getMessage());
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        }
    }

}
