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
import main.java.sample.covidportal.niti.DohvatiSveZupanijeNit;
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

    private static ExecutorService executorServiceDohvatZupanija;
    private static Timeline clock = null;

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
                observableListaZupanija
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
        observableListaZupanija = FXCollections.observableArrayList();

        nazivStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
        stanovniciStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));
        zarazeniStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));
        idStupac.setCellValueFactory(new PropertyValueFactory<Long, String>("id"));

        executorServiceDohvatZupanija = Executors.newSingleThreadExecutor();
        executorServiceDohvatZupanija.execute(new DohvatiSveZupanijeNit(observableListaZupanija, tablicaZupanija));

        startAnimation();

    }

    public static ObservableList<Zupanija> getObservableListaZupanija() {
        return observableListaZupanija;
    }

    public static void setObservableListaZupanija(ObservableList<Zupanija> observableListaZupanija) {
        PretragaZupanijaController.observableListaZupanija = observableListaZupanija;
    }

    private static void startAnimation() {
        clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            String naziv = NajviseZarazenihNit.getImeZupanije();
            Main.getMainStage().setTitle("Županija s najvećim postotkom zaraženih je: " + naziv);
        }), new KeyFrame(Duration.seconds(Main.MAX_VRIJEME_CEKANJA)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public static void stopAnimation() {
        if(clock != null) {
            clock.stop();
            clock.getKeyFrames().clear();
            clock = null;
            System.out.println("Stopping animation");
        }
    }

    public static Timeline getClock() {
        return clock;
    }


}
