package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAlias;
    // Si tienes txtCiudad en tu FXML, descomenta esto:
    // @FXML private TextField txtCiudad;

    @FXML
    protected void onBotonGuardarClick() {
        try {
            String nombre = txtNombre.getText();
            String cedula = txtCedula.getText();
            String email = txtEmail.getText();
            String alias = txtAlias.getText();

            if(nombre.isEmpty() || cedula.isEmpty() || email.isEmpty() || alias.isEmpty()) {
                mostrarAlerta("Error", "Por favor llene todos los campos", Alert.AlertType.WARNING);
                return;
            }

            // Crear Usuario
            Usuario nuevoUsuario = new Usuario(cedula, nombre, "Sin Ciudad", alias, email);
            RepositorioUsuarios.guardarUsuario(nuevoUsuario);

            mostrarAlerta("Éxito", "Cuenta creada. Alias: " + alias, Alert.AlertType.INFORMATION);
            onBotonVolverClick();

        } catch (Exception e) {
            mostrarAlerta("Error de Registro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onBotonVolverClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/login-view.fxml"));
            Parent root = loader.load();
            // Simplemente cambiamos la raíz de la escena actual
            txtNombre.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
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