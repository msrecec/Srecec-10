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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PretragaVirusiController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(PretragaVirusiController.class);

    private static ObservableList<Bolest> observableListaVirusa;
    private static ExecutorService executorServiceDohvatBolesti;

    @FXML
    private TableView tablicaVirusa ;
    @FXML
    private TableColumn<Bolest, String> nazivStupac;
    @FXML
    private TableColumn<Set<Simptom>, String> simptomiStupac;
    @FXML
    private TableColumn<Long, String> idStupac;
    @FXML
    private TextField unosNazivaVirusa;

    public void pretraga() throws PraznoPolje {
        if(unosNazivaVirusa.getText().isBlank()) {
            throw new PraznoPolje();
        }

        String uneseniNazivVirusa = unosNazivaVirusa.getText().toLowerCase();

        Optional<List<Bolest>> filtriranaBolest = Optional.ofNullable(
                observableListaVirusa
                        .stream()
                        .filter(z -> ((z instanceof Virus)) && z.getNaziv().toLowerCase().contains(uneseniNazivVirusa))
                        .collect(Collectors.toList())
        );

        if(filtriranaBolest.isPresent()) {

            tablicaVirusa.getItems().setAll(filtriranaBolest.get());

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListaVirusa = FXCollections.observableArrayList();

        nazivStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        simptomiStupac.setCellValueFactory(new PropertyValueFactory<Set<Simptom>, String>("simptomi"));
        idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

        executorServiceDohvatBolesti = Executors.newSingleThreadExecutor();
        executorServiceDohvatBolesti.execute(new DohvatiSveBolestiNit(observableListaVirusa, tablicaVirusa, true));

    }

    public static ObservableList<Bolest> getObservableListaVirusa() {
        return observableListaVirusa;
    }

    public static void setObservableListaVirusa(ObservableList<Bolest> observableListaVirusa) {
        PretragaVirusiController.observableListaVirusa = observableListaVirusa;
    }

}
