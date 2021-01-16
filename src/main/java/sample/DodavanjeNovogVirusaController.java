package main.java.sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.sample.covidportal.baza.BazaPodataka;
import main.java.sample.covidportal.iznimke.BolestIstihSimptoma;
import main.java.sample.covidportal.iznimke.NepostojecaBolest;
import main.java.sample.covidportal.iznimke.NepostojeciSimptom;
import main.java.sample.covidportal.iznimke.PraznoPolje;
import main.java.sample.covidportal.model.Simptom;
import main.java.sample.covidportal.model.Virus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DodavanjeNovogVirusaController {


    private static final Logger logger = LoggerFactory.getLogger(DodavanjeNovogVirusaController.class);

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TextField simptomi;

    public void dodajNoviVirus() {
        try {
            Simptom dohvaceniSimptom;

            String nazivVirusaText = nazivVirusa.getText();
            String simptomiText = simptomi.getText();

            if(nazivVirusaText.isBlank() || simptomiText.isBlank()) {
                throw new PraznoPolje();
            }

            Set<Simptom> simptomi = new HashSet<>();

            List<Long> indexList =  Arrays.stream(simptomiText.split(",")).map(e -> Long.parseLong(e)).collect(Collectors.toList());

            for(Long i : indexList) {
                dohvaceniSimptom = BazaPodataka.dohvatiSimptom(i);
                System.out.println("Index od simptoma: " + i);
                simptomi.add(dohvaceniSimptom);
            }

            Virus noviVirus = new Virus((long)1, nazivVirusaText, simptomi);

            BazaPodataka.spremiNovuBolest(noviVirus);

            logger.info("Unesen je virus: " + noviVirus.getNaziv());

            PocetniEkranController.uspjesanUnos();

        } catch (IOException | PraznoPolje | NumberFormatException | BolestIstihSimptoma | SQLException | NepostojeciSimptom | NepostojecaBolest e) {
            logger.error(e.getMessage());
            PocetniEkranController.neuspjesanUnos(e.getMessage());
        }
    }
}
