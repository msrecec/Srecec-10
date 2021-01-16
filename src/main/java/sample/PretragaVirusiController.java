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
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Virus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PretragaVirusiController implements Initializable {

    private static List<Bolest> virusi;

    private static final Logger logger = LoggerFactory.getLogger(PretragaVirusiController.class);

    private static ObservableList<Bolest> observableListaVirusa;

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
                virusi
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
        virusi = new ArrayList<>();
        observableListaVirusa = FXCollections.observableArrayList();

        try {
            virusi = BazaPodataka.dohvatiSveBolesti();

            observableListaVirusa.addAll(virusi.stream().filter(z -> ((z instanceof Virus))).collect(Collectors.toList()));

            nazivStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
            simptomiStupac.setCellValueFactory(new PropertyValueFactory<Set<Simptom>, String>("simptomi"));
            idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

            tablicaVirusa.setItems(observableListaVirusa);
        } catch (SQLException | IOException throwables) {
            logger.error(throwables.getMessage());
            PocetniEkranController.neuspjesanUnos(throwables.getMessage());
        }

    }

    public static ObservableList<Bolest> getObservableListaVirusa() {
        return observableListaVirusa;
    }

    public static void setObservableListaVirusa(ObservableList<Bolest> observableListaVirusa) {
        PretragaVirusiController.observableListaVirusa = observableListaVirusa;
    }

    public static List<Bolest> getVirusi() {
        return virusi;
    }

    public static void setVirusi(List<Bolest> virusi) {
        PretragaVirusiController.virusi = virusi;
    }
}
