package main.java.sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.NepostojecaBolest;
import main.java.sample.covidportal.iznimke.NepostojecaZupanija;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Osoba;
import main.java.sample.covidportal.model.Zupanija;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {
    private static ObservableList<Osoba> observableListaOsoba;

    private static final Logger logger = LoggerFactory.getLogger(PretragaOsobaController.class);

    private static List<Osoba> osobe;

    @FXML
    private TableView tablicaOsoba ;
    @FXML
    private TableColumn<Osoba, String> imeStupac;
    @FXML
    private TableColumn<Osoba, String> prezimeStupac;
    @FXML
    private TableColumn<Osoba, Date> datumRodjenjaStupac;
    @FXML
    private TableColumn<Osoba, Zupanija> zupanijaStupac;
    @FXML
    private TableColumn<Osoba, Bolest> bolestStupac;
    @FXML
    private TableColumn<Osoba, List<Osoba>> kontaktiraneOsobeStupac;
    @FXML
    private TextField unosNazivaOsobe;

    public void pretraga() {
        String uneseniNazivOsobe = unosNazivaOsobe.getText().toLowerCase();

        Optional<List<Osoba>> filtriraneOsobePoImenu = Optional.ofNullable(osobe.stream()
                .filter(z -> (z.getIme().toLowerCase().contains(uneseniNazivOsobe)))
                .collect(Collectors.toList()));
        Optional<List<Osoba>> filtriraneOsobePoPrezimenu = Optional.ofNullable(osobe.stream()
                .filter(z -> (z.getPrezime().toLowerCase().contains(uneseniNazivOsobe)))
                .collect(Collectors.toList()));

        if(filtriraneOsobePoImenu.isPresent() || filtriraneOsobePoPrezimenu.isPresent()) {
            List<Osoba> filtriraneOsobe = new ArrayList<>();
            if(filtriraneOsobePoImenu.isPresent() && filtriraneOsobePoPrezimenu.isPresent()) {
                filtriraneOsobe.addAll(filtriraneOsobePoImenu.get());
                filtriraneOsobe.addAll(filtriraneOsobePoPrezimenu.get());
            } else if(filtriraneOsobePoImenu.isPresent() && filtriraneOsobePoPrezimenu.isEmpty()) {
                filtriraneOsobe.addAll(filtriraneOsobePoImenu.get());
            } else {
                filtriraneOsobe.addAll(filtriraneOsobePoPrezimenu.get());
            }
            Set<Osoba> setFiltriranihOsoba = new HashSet(filtriraneOsobe);
            filtriraneOsobe = new ArrayList<>();
            filtriraneOsobe.addAll(setFiltriranihOsoba);

            tablicaOsoba.getItems().setAll(filtriraneOsobe);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            osobe = new ArrayList<>();
            observableListaOsoba = FXCollections.observableArrayList();
            osobe = BazaPodataka.dohvatiSveOsobe();
            observableListaOsoba.addAll(osobe);

            imeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
            prezimeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
            datumRodjenjaStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Date>("datumRodjenja"));
            zupanijaStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Zupanija>("zupanija"));
            bolestStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Bolest>("zarazenBolescu"));
            kontaktiraneOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, List<Osoba>>("kontaktiraneOsobe"));

            tablicaOsoba.setItems(observableListaOsoba);
        } catch (SQLException | IOException | NepostojecaBolest | NepostojecaZupanija throwables) {
            logger.error(throwables.getMessage());
            PocetniEkranController.neuspjesanUnos(throwables.getMessage());
        }
    }

    public static ObservableList<Osoba> getObservableListaOsoba() {
        return observableListaOsoba;
    }

    public static void setObservableListaOsoba(ObservableList<Osoba> observableListaOsoba) {
        PretragaOsobaController.observableListaOsoba = observableListaOsoba;
    }

    public static List<Osoba> getOsobe() {
        return osobe;
    }

    public static void setOsobe(List<Osoba> osobe) {
        PretragaOsobaController.osobe = osobe;
    }
}
