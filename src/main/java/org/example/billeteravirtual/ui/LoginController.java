package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.repositorios.RepositorioTransacciones;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;

/**
 * Controlador encargado de gestionar la vista de inicio de sesión.
 * Maneja la autenticación de usuarios mediante cédula o alias y la navegación
 * hacia el registro o el panel de administración.
 */
public class LoginController {


    @FXML
    private TextField txtIdentificador;

    /**
     * Maneja el evento de clic en el botón "Ingresar".
     * Valida si el texto ingresado corresponde a una cédula o un alias existente.
     * Si es correcto, redirige al panel de usuario; de lo contrario, muestra una alerta de error.
     */
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


    /**
     * Redirige la aplicación a la vista de administración.
     * Se ejecuta al hacer clic en el botón correspondiente.
     */
    @FXML
    protected void onBotonAdminClick() {
        cambiarPantallaAdmin();
    }

    /**
     * Redirige la aplicación a la vista de registro de nuevos usuarios.
     */
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

            txtIdentificador.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar panel-admin.fxml", Alert.AlertType.ERROR);
        }
    }

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

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;

    /**
     * Inicializa el controlador.
     * Se ejecuta automáticamente al cargar la vista. Intenta cargar los datos persistidos
     * de usuarios y transacciones desde los archivos locales.
     */    @FXML
    public void initialize() {
        try {
            // Intentamos cargar los usuarios y transacciones guardados
            RepositorioUsuarios.cargarDesdeArchivo("usuarios.dat");
            RepositorioTransacciones.cargarDesdeArchivo("transacciones.dat");
            System.out.println("Datos cargados correctamente al iniciar.");
        } catch (Exception e) {
            // Si es la primera vez o no hay archivos, no pasa nada
            System.out.println("No se encontraron datos previos o error al cargar: " + e.getMessage());
        }
}}