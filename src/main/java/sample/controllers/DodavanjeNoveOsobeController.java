package main.java.sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.*;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Osoba;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.niti.SpremiNovuBolestNit;
import main.java.sample.covidportal.niti.SpremiNovuOsobuNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DodavanjeNoveOsobeController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveOsobeController.class);
    private static ExecutorService executorServiceDodavanjeNoveOsobe;
    @FXML
    private TextField imeOsobe;
    @FXML
    private TextField prezimeOsobe;
    @FXML
    private TextField datumRodjenja;
    @FXML
    private TextField zupanijaOsobe;
    @FXML
    private TextField bolestOsobe;
    @FXML
    private TextField kontaktiraneOsobe;

    public void dodajNovuOsobu() {
        try {
            if (
                imeOsobe.getText().isBlank() ||
                prezimeOsobe.getText().isBlank() ||
                datumRodjenja.getText().isBlank() ||
                bolestOsobe.getText().isBlank() ||
                kontaktiraneOsobe.getText().isBlank()
            ) {
                throw new PraznoPolje();
            }

            String imeOsobeText = imeOsobe.getText();
            String prezimeOsobeText = prezimeOsobe.getText();
            Date datumRodjenjaDate = Date.valueOf(datumRodjenja.getText());
            Zupanija zupanijaOsobeZupanija;
            String bolestOsobeText = bolestOsobe.getText();
            String kontaktiraneOsobeText = kontaktiraneOsobe.getText();
            Bolest bolestOsobeBolest;

             // odabir zupanije iz seta zupanija po indeksu

            Long odabranaZupanija = Long.parseLong(zupanijaOsobe.getText());
            zupanijaOsobeZupanija  = BazaPodataka.dohvatiZupaniju(odabranaZupanija);

            Long odabranaBolest = Long.parseLong(bolestOsobeText);
            bolestOsobeBolest  = BazaPodataka.dohvatiBolest(odabranaBolest);

            List<Osoba> finalKontaktiraneOsobe = new ArrayList<>();

            List<Long> listaIdOsobaArrays = Arrays.stream(kontaktiraneOsobeText
                    .split(","))
                    .map(el -> Long.parseLong(el))
                    .collect(Collectors.toSet()).stream()
                    .collect(Collectors.toList());

            Osoba dohvacenaOsoba;

            for(Long i : listaIdOsobaArrays) {
                dohvacenaOsoba = BazaPodataka.dohvatiOsobu(i);
                finalKontaktiraneOsobe.add(dohvacenaOsoba);
            }

            Osoba novaOsoba = new Osoba.Builder((long) 1)
                    .ime(imeOsobeText)
                    .prezime(prezimeOsobeText)
                    .datumRodjenja(datumRodjenjaDate)
                    .zupanija(zupanijaOsobeZupanija)
                    .zarazenBolescu(bolestOsobeBolest)
                    .kontaktiraneOsobe(finalKontaktiraneOsobe)
                    .build();

            executorServiceDodavanjeNoveOsobe = Executors.newSingleThreadExecutor();
            executorServiceDodavanjeNoveOsobe.execute(new SpremiNovuOsobuNit(novaOsoba));


        } catch (IOException | PraznoPolje | NumberFormatException | SQLException | DuplikatKontaktiraneOsobe | NepostojecaOsoba | NepostojecaZupanija | NepostojecaBolest ex) {
            logger.error(ex.getMessage());
            PocetniEkranController.neuspjesanUnos(ex.getMessage());
        }
    }

}
