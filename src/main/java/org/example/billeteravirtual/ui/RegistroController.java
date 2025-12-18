package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAlias;
    @FXML private TextField txtCiudad; // Declarar variable nueva


    @FXML
    protected void onBotonGuardarClick() {
        try {
            // Recolectar datos
            String nombre = txtNombre.getText();
            String cedula = txtCedula.getText();
            String email = txtEmail.getText();
            String alias = txtAlias.getText();

            // Validar campos vacíos antes de llamar al Usuario
            if(nombre.isEmpty() || cedula.isEmpty() || email.isEmpty() || alias.isEmpty()) {
                mostrarAlerta("Error", "Por favor llene todos los campos", Alert.AlertType.WARNING);
                return;
            }

            // Crear Usuario (Esto lanzará excepción si el email o cédula están mal)
            Usuario nuevoUsuario = new Usuario(cedula, nombre, "Sin Ciudad", alias, email);

            // Guardar en repositorio
            RepositorioUsuarios.guardarUsuario(nuevoUsuario);

            mostrarAlerta("Éxito", "Cuenta creada. Tu Alias es: " + alias, Alert.AlertType.INFORMATION);
            onBotonVolverClick(); // Volver al login

        } catch (Exception e) {
            // AQUÍ ATRAPAMOS LOS ERRORES DE VALIDACIÓN (Ej: "Correo no válido")
            mostrarAlerta("Error de Registro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    protected void onBotonVolverClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            Scene sceneActual = txtNombre.getScene(); // O el control que tengas a mano
            stage.setScene(new Scene(root, sceneActual.getWidth(), sceneActual.getHeight()));

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