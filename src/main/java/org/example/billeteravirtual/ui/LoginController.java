package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtIdentificador;

    @FXML
    protected void onBotonIngresarClick() {
        String dato = txtIdentificador.getText();
        Usuario u = RepositorioUsuarios.buscarPorCedula(dato);
        if (u == null) {
            u = RepositorioUsuarios.buscarPorAlias(dato);
        }

        if (u != null) {
            cambiarPantallaUsuario(u);
        } else {
            mostrarAlerta("Error", "Usuario no encontrado", Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onBotonAdminClick() {
        cambiarPantallaAdmin();
    }

    @FXML
    protected void onBotonRegistrarClick() {
        // Navegación simple al registro
        cambiarEscena("/org/example/billeteravirtual/registro-view.fxml");
    }

    private void cambiarPantallaUsuario(Usuario usuarioLogueado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/panel-usuario.fxml"));
            Parent root = loader.load();

            PanelUsuarioController controller = loader.getController();
            controller.setUsuarioActivo(usuarioLogueado);

            // Cambiar la raíz de la escena actual
            txtIdentificador.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista de usuario.", Alert.AlertType.ERROR);
        }
    }

    private void cambiarPantallaAdmin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/panel-admin.fxml"));
            Parent root = loader.load();

            // Cambiar la raíz de la escena actual
            txtIdentificador.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar panel-admin.fxml", Alert.AlertType.ERROR);
        }
    }

    // Método auxiliar para no repetir código de carga
    private void cambiarEscena(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            txtIdentificador.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de Navegación", "No se pudo cargar: " + fxmlPath, Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}