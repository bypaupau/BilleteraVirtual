package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtIdentificador; // El id del TextField en tu FXML

    @FXML
    protected void onBotonIngresarClick() {
        String dato = txtIdentificador.getText();

        // 1. Reutilizamos tu lógica de validación existente
        Usuario u = RepositorioUsuarios.buscarPorCedula(dato);
        if (u == null) {
            u = RepositorioUsuarios.buscarPorAlias(dato);
        }

        if (u != null) {
            // ¡Usuario encontrado! -> Cambiar a la escena de Usuario
            cambiarPantallaUsuario(u);
        } else {
            mostrarAlerta("Error", "Usuario no encontrado", Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onBotonAdminClick() {
        System.out.println("Intentando abrir admin..."); // Agrega esto para depurar
        cambiarPantallaAdmin();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void cambiarPantallaAdmin() {
        try {
            // CORRECCIÓN: Agregamos la ruta completa "/org/example/billeteravirtual/..."
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/panel-admin.fxml"));

            Parent root = loader.load();

            // Usar la escena actual para mantener el tamaño
            Stage stage = (Stage) txtIdentificador.getScene().getWindow();
            Scene sceneActual = stage.getScene();
            stage.setScene(new Scene(root, sceneActual.getWidth(), sceneActual.getHeight()));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarNotificacion("Error Crítico", "No se encuentra el archivo 'panel-admin.fxml'.");
        }
    }

    private void cambiarPantallaUsuario(Usuario usuarioLogueado) {
        try {
            // CORRECCIÓN: También aquí corregimos la ruta
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/panel-usuario.fxml"));

            Parent root = loader.load();

            PanelUsuarioController controller = loader.getController();
            controller.setUsuarioActivo(usuarioLogueado);

            Stage stage = (Stage) txtIdentificador.getScene().getWindow();
            Scene sceneActual = stage.getScene();
            stage.setScene(new Scene(root, sceneActual.getWidth(), sceneActual.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarNotificacion(String titulo, String contenido) {
        // Decidimos el icono (Error o Información) según el título
        Alert.AlertType tipo = titulo.equalsIgnoreCase("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null); // Para que se vea más limpio sin cabecera
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @FXML
    protected void onBotonRegistrarClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/registro-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtIdentificador.getScene().getWindow();
            Scene sceneActual = txtIdentificador.getScene(); // O el control que tengas a mano
            stage.setScene(new Scene(root, sceneActual.getWidth(), sceneActual.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


