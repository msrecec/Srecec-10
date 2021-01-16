package main.java.sample;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PocetniEkranController implements Initializable {

    public void prikaziEkranZaPretraguZupanija () throws IOException {
        Parent pretragaZupanijaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene pretragaZupanijaScene = new Scene(pretragaZupanijaFrame, 800, 500);
        Main.getMainStage().setScene(pretragaZupanijaScene);
    }
    public void prikaziEkranZaPretraguSimptoma () throws IOException {
        Parent pretragaSimptomaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene pretragaSimptomaScene = new Scene(pretragaSimptomaFrame, 800, 500);
        Main.getMainStage().setScene(pretragaSimptomaScene);
    }
    public void prikaziEkranZaPretraguBolesti () throws IOException {
        Parent pretragaBolestiFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene pretragaBolestiScene = new Scene(pretragaBolestiFrame, 800, 500);
        Main.getMainStage().setScene(pretragaBolestiScene);
    }
    public void prikaziEkranZaPretraguVirusa () throws IOException {
        Parent pretragaVirusaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusi.fxml"));
        Scene pretragaVirusaScene = new Scene(pretragaVirusaFrame, 800, 500);
        Main.getMainStage().setScene(pretragaVirusaScene);
    }
    public void prikaziEkranZaPretraguOsoba () throws IOException {
        Parent pretragaOsobaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene pretragaOsobaScene = new Scene(pretragaOsobaFrame, 800, 500);
        Main.getMainStage().setScene(pretragaOsobaScene);
    }

    public void prikaziEkranZaDodavanjeNoveZupanije() throws IOException {
        Parent dodavanjeZupanijeFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeZupanije.fxml"));
        Scene dodavanjeZupanijeScene = new Scene(dodavanjeZupanijeFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeZupanijeScene);
    }

    public void prikaziEkranZaDodavanjeNovogSimptoma() throws IOException {
        Parent dodavanjeSimptomaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeSimptoma.fxml"));
        Scene dodavanjeSimptomaScene = new Scene(dodavanjeSimptomaFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeSimptomaScene);
    }

    public void prikaziEkranZaDodavanjeNoveBolesti() throws IOException {
        Parent dodavanjeBolestiFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeBolesti.fxml"));
        Scene dodavanjeBolestiScene = new Scene(dodavanjeBolestiFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeBolestiScene);
    }

    public void prikaziEkranZaDodavanjeNovogVirusa() throws IOException {
        Parent dodavanjeVirusaFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeVirusa.fxml"));
        Scene dodavanjeVirusaScene = new Scene(dodavanjeVirusaFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeVirusaScene);
    }

    public void prikaziEkranZaDodavanjeNoveOsobe() throws IOException {
        Parent dodavanjeOsobeFrame = FXMLLoader.load(getClass().getClassLoader().getResource("dodavanjeOsobe.fxml"));
        Scene dodavanjeOsobeScene = new Scene(dodavanjeOsobeFrame, 800, 500);
        Main.getMainStage().setScene(dodavanjeOsobeScene);
    }

    public static void uspjesanUnos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspjesan unos");
        alert.setHeaderText("Uspješan unos");
        alert.setContentText("Uspješan unos");
        alert.showAndWait();
    }


    public static void neuspjesanUnos(String err) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Greška");
        alert.setHeaderText("Greška");
        alert.setContentText(err);
        alert.showAndWait();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
