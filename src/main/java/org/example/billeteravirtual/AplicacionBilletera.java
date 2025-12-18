package org.example.billeteravirtual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AplicacionBilletera extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AplicacionBilletera.class.getResource("login-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 400, 500); // Tamaño un poco más alto
        stage.setTitle("Billetera Virtual");
        stage.setScene(scene);
        stage.show();
    }}