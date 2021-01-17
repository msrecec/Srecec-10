package main.java.sample.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.java.sample.Main;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.model.Zupanija;
import main.java.sample.covidportal.niti.NajviseZarazenihNit;
import main.java.sample.covidportal.sort.CovidSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class PretragaZupanijaController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(PretragaZupanijaController.class);

    private static ObservableList<Zupanija> observableListaZupanija;

    private static SortedSet<Zupanija> zupanije;
    private static ExecutorService executorServiceIspisZupanije;

    @FXML
    private TableView tablicaZupanija ;
    @FXML
    private TableColumn<Zupanija, String> nazivStupac;
    @FXML
    private TableColumn<Zupanija, Integer> stanovniciStupac;
    @FXML
    private TableColumn<Zupanija, Integer> zarazeniStupac;
    @FXML
    private TextField unosNazivaZupanije;
    @FXML
    private TableColumn<Long, String> idStupac;

    public void pretraga() throws IOException {
        String uneseniNazivZupanije = unosNazivaZupanije.getText().toLowerCase();

        Optional<List<Zupanija>> filtriraneZupanije = Optional.ofNullable(
                zupanije
                .stream()
                .filter(z -> z.getNaziv().toLowerCase().contains(uneseniNazivZupanije))
                .collect(Collectors.toList())
        );

        if(filtriraneZupanije.isPresent()) {

            tablicaZupanija.getItems().setAll(filtriraneZupanije.get());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zupanije = new TreeSet<>(new CovidSorter());
        observableListaZupanija = FXCollections.observableArrayList();
        try {

            Main.getMainStage().getScene().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::destroyNajviseZarazenihNitThread);

            if(!NajviseZarazenihNit.exists()) {
                executorServiceIspisZupanije = Executors.newSingleThreadExecutor();
                executorServiceIspisZupanije.execute(new NajviseZarazenihNit());
            }

            Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                String naziv = NajviseZarazenihNit.getImeZupanije();
                Main.getMainStage().setTitle("Županija s najvećim postotkom zaraženih je: " + naziv);
            }), new KeyFrame(Duration.seconds(Main.MAX_VRIJEME_CEKANJA)));

            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();

            zupanije = BazaPodataka.dohvatiSveZupanije();
            observableListaZupanija.addAll(zupanije);

            nazivStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
            stanovniciStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));
            zarazeniStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));
            idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

            tablicaZupanija.setItems(observableListaZupanija);


        } catch (SQLException | IOException throwables) {
            logger.error(throwables.getMessage());
            PocetniEkranController.neuspjesanUnos(throwables.getMessage());
        }
    }

    public static ObservableList<Zupanija> getObservableListaZupanija() {
        return observableListaZupanija;
    }

    public static void setObservableListaZupanija(ObservableList<Zupanija> observableListaZupanija) {
        PretragaZupanijaController.observableListaZupanija = observableListaZupanija;
    }

    public static SortedSet<Zupanija> getZupanije() {
        return zupanije;
    }

    private void destroyNajviseZarazenihNitThread(WindowEvent event) {
        NajviseZarazenihNit.destroy();
    }

    public static void setZupanije(SortedSet<Zupanija> zupanije) {
        PretragaZupanijaController.zupanije = zupanije;
    }
}
