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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    public static final int MAX_VRIJEME_CEKANJA = 10;

    private static Stage mainStage;
    private static ExecutorService executorServiceIspisZupanije;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Početni ekran");
        primaryStage.setScene(new Scene(root, 800, 500));
        mainStage = primaryStage;

        registerEventHandlers();

        executorServiceIspisZupanije = Executors.newSingleThreadExecutor();
        executorServiceIspisZupanije.execute(new NajviseZarazenihNit());

        primaryStage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * Registrira sve custom event handler-e
     */

    private static void registerEventHandlers() {
        mainStage.addEventHandler(SwitchWindowEvent.SWITCH_WINDOW, new SwitchWindowEventHandler());
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
