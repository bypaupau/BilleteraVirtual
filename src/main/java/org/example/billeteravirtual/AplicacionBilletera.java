package org.example.billeteravirtual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AplicacionBilletera extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Cargar directamente la vista de Login
        FXMLLoader fxmlLoader = new FXMLLoader(AplicacionBilletera.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);

        // Cargar el CSS nuevo
        String css = getClass().getResource("/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Billetera Virtual");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}