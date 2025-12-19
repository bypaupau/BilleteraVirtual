package org.example.billeteravirtual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.billeteravirtual.repositorios.Paths;
import org.example.billeteravirtual.repositorios.RepositorioTransacciones;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;
import java.util.Objects;

public class AplicacionBilletera extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AplicacionBilletera.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 650);

        String css = getClass().getResource("/styles.css").toExternalForm();
        scene.getStylesheets().add(css);

        try {
            Image icono = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/iconoBilletera.png")));
            stage.getIcons().add(icono);
        } catch (NullPointerException e) {
            System.out.println("Advertencia: No se encontró el icono de la aplicación.");
        }

        stage.setTitle("Billetera Virtual");
        stage.setScene(scene);
        stage.setMinWidth(650);
        stage.setMinHeight(600);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        System.out.println("Iniciando aplicación... cargando datos.");
        // Intentamos cargar. Si los archivos no existen (primera vez), no pasa nada gracias al try-catch interno del repositorio
        RepositorioUsuarios.cargarDesdeArchivo(Paths.ARCHIVO_USUARIOS);
        RepositorioTransacciones.cargarDesdeArchivo(Paths.ARCHIVO_TRANSACCIONES);
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Cerrando aplicación... guardando datos.");
        try {
            RepositorioUsuarios.guardarEnArchivo();
            RepositorioTransacciones.guardarEnArchivo();
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}