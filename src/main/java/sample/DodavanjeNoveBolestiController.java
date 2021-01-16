package main.java.sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.BolestIstihSimptoma;
import main.java.sample.covidportal.iznimke.NepostojecaBolest;
import main.java.sample.covidportal.iznimke.NepostojeciSimptom;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.model.Bolest;
import main.java.sample.covidportal.model.Simptom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DodavanjeNoveBolestiController {

    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNoveBolestiController.class);

    @FXML
    private TextField nazivBolesti;

    @FXML
    private TextField simptomi;

    public void dodajNovuBolest() {
        try {
            Simptom dohvaceniSimptom;

            String nazivBolestiText = nazivBolesti.getText();
            String simptomiText = simptomi.getText();

            if(nazivBolestiText.isBlank() || simptomiText.isBlank()) {
                throw new PraznoPolje();
            }

            Set<Simptom> simptomi = new HashSet<>();

            List<Long> indexList =  Arrays.stream(simptomiText.split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toSet()).stream().collect(Collectors.toList());

            for(Long i : indexList) {
                dohvaceniSimptom = BazaPodataka.dohvatiSimptom(i);
                System.out.println("Index od simptoma: " + i);
                simptomi.add(dohvaceniSimptom);
            }

            Bolest novaBolest = new Bolest((long)1, nazivBolestiText, simptomi);

            BazaPodataka.spremiNovuBolest(novaBolest);

            logger.info("Unesena je bolest: " + novaBolest.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException | PraznoPolje | NumberFormatException | BolestIstihSimptoma | SQLException | NepostojeciSimptom | NepostojecaBolest e) {
            logger.error(e.getMessage());
            PocetniEkranController.neuspjesanUnos(e.getMessage());
        }
    }
}
