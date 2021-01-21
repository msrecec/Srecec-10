package main.java.sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Virus;
import main.java.sample.covidportal.niti.DohvatiSveBolestiNit;
import main.java.sample.covidportal.niti.DohvatiSveSimptomeNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PretragaBolestiController implements Initializable {
    private static ObservableList<Bolest> observableListaBolesti;

    private static ExecutorService executorServiceDohvatBolesti;

    private static final Logger logger = LoggerFactory.getLogger(PretragaBolestiController.class);

    @FXML
    private TableView tablicaBolesti ;
    @FXML
    private TableColumn<Bolest, String> nazivStupac;
    @FXML
    private TableColumn<Set<Simptom>, String> simptomiStupac;
    @FXML
    private TableColumn<Long, String> idStupac;
    @FXML
    private TextField unosNazivaBolesti;

    public void pretraga() throws PraznoPolje {
        if(unosNazivaBolesti.getText().isBlank()) {
            throw new PraznoPolje();
        }

        String uneseniNazivBolesti = unosNazivaBolesti.getText().toLowerCase();

        Optional<List<Bolest>> filtriranaBolest = Optional.ofNullable(
                observableListaBolesti
                .stream()
                .filter(z -> (!(z instanceof Virus)) && z.getNaziv().toLowerCase().contains(uneseniNazivBolesti))
                .collect(Collectors.toList())
        );

        if(filtriranaBolest.isPresent()) {

            tablicaBolesti.getItems().setAll(filtriranaBolest.get());

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListaBolesti = FXCollections.observableArrayList();

        System.out.println("Dodajem bolesti");

        nazivStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        simptomiStupac.setCellValueFactory(new PropertyValueFactory<Set<Simptom>, String>("simptomi"));
        idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

        executorServiceDohvatBolesti = Executors.newSingleThreadExecutor();
        executorServiceDohvatBolesti.execute(new DohvatiSveBolestiNit(observableListaBolesti, tablicaBolesti, false));
    }

}
