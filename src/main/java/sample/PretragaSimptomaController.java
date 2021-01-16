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
import main.java.sample.covidportal.enumeracija.VrijednostSimptoma;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.VrijednostEnumeracije;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PretragaSimptomaController implements Initializable {
    private static ObservableList<Simptom> observableListaSimptoma;
    private static List<Simptom> simptomi;

    private static final Logger logger = LoggerFactory.getLogger(PretragaSimptomaController.class);

    @FXML
    private TableView tablicaSimptoma ;
    @FXML
    private TableColumn<Simptom, String> nazivStupac;
    @FXML
    private TableColumn<Simptom, VrijednostSimptoma> vrijednostStupac;
    @FXML
    private TextField unosNazivaSimptoma;
    @FXML
    private TableColumn<Long, String> idStupac;

    public void pretraga() {
        String uneseniNazivSimptoma = unosNazivaSimptoma.getText().toLowerCase();

        Optional<List<Simptom>> filtriraniSimptom = Optional.ofNullable(simptomi.stream().filter(z -> z.getNaziv().toLowerCase().contains(uneseniNazivSimptoma)).collect(Collectors.toList()));

        if(filtriraniSimptom.isPresent()) {

            tablicaSimptoma.getItems().setAll(filtriraniSimptom.get());

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        simptomi = new ArrayList<>();
        observableListaSimptoma = FXCollections.observableArrayList();
        try {
            simptomi = BazaPodataka.dohvatiSveSimptome();
            observableListaSimptoma.addAll(simptomi);

            nazivStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
            vrijednostStupac.setCellValueFactory(new PropertyValueFactory<Simptom, VrijednostSimptoma>("vrijednost"));
            idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

            tablicaSimptoma.setItems(observableListaSimptoma);

        } catch (SQLException | IOException throwables) {
            logger.error(throwables.getMessage());
            PocetniEkranController.neuspjesanUnos(throwables.getMessage());
        }
    }

    public static ObservableList<Simptom> getObservableListaSimptoma() {
        return observableListaSimptoma;
    }

    public static void setObservableListaSimptoma(ObservableList<Simptom> observableListaSimptoma) {
        PretragaSimptomaController.observableListaSimptoma = observableListaSimptoma;
    }

    public static List<Simptom> getSimptomi() {
        return simptomi;
    }

    public static void setSimptomi(List<Simptom> simptomi) {
        PretragaSimptomaController.simptomi = simptomi;
    }
}
