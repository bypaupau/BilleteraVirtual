package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.agentes.Validador;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAlias;
    // Si tienes txtCiudad en tu FXML, descomenta esto:
    @FXML private TextField txtCiudad;

    @FXML
    protected void onBotonGuardarClick() {
        StringBuilder errores = new StringBuilder(); // Aquí guardaremos los mensajes

        String nombre = txtNombre.getText();
        String cedula = txtCedula.getText();
        String email = txtEmail.getText();
        String alias = txtAlias.getText();
        String ciudad = txtCiudad.getText();

        // 1. Validamos campo por campo "atrapando" el error para que no detenga el flujo
        try {
            Validador.validarNombreCampo(nombre);
        } catch (RuntimeException e) {
            errores.append("- ").append(e.getMessage()).append("\n");
        }

        try {
            Validador.validarCedula(cedula);
        } catch (RuntimeException e) {
            errores.append("- ").append(e.getMessage()).append("\n");
        }

        try {
            Validador.validarCorreo(email);
        } catch (RuntimeException e) {
            errores.append("- ").append(e.getMessage()).append("\n");
        }

        try {
            Validador.validarAlias(alias);
        } catch (RuntimeException e) {
            errores.append("- ").append(e.getMessage()).append("\n");
        }

        try {
            Validador.validarCiudad(ciudad);
        } catch (RuntimeException e) {
            errores.append("- ").append(e.getMessage()).append("\n");
        }

        // 2. Si hay errores acumulados, mostramos la alerta y NO creamos el usuario
        if (errores.length() > 0) {
            mostrarAlerta("Errores de Validación", errores.toString(), Alert.AlertType.WARNING);
            return; // Detenemos aquí
        }

        // 3. Si llegamos aquí, todo está bien. Creamos el usuario sin miedo.
        try {
            // Como ya validamos arriba, esto no debería fallar por formato,
            // pero sí podría fallar por lógica de negocio (ej: alias repetido en la BD)
            Usuario nuevoUsuario = new Usuario(cedula, nombre, ciudad, alias, email);
            RepositorioUsuarios.guardarUsuario(nuevoUsuario);

            mostrarAlerta("Éxito", "Cuenta creada correctamente.", Alert.AlertType.INFORMATION);
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