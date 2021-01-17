package main.java.sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import main.java.sample.covidportal.niti.NajviseZarazenihNit;
import main.java.sample.events.SwitchWindow.SwitchWindowEvent;
import main.java.sample.events.SwitchWindow.SwitchWindowEventHandler;

public class Main extends Application {

    public static final int MAX_VRIJEME_CEKANJA = 10;

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Početni ekran");
        primaryStage.setScene(new Scene(root, 800, 500));
        mainStage = primaryStage;

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });

        mainStage.addEventHandler(SwitchWindowEvent.SWITCH_WINDOW, new SwitchWindowEventHandler());

        primaryStage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }


    public static void main(String[] args) {

        launch(args);

    }

}
