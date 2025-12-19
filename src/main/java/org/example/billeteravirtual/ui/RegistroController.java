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

/**
 * Controlador para la vista de registro de nuevos usuarios.
 * Recolecta los datos del formulario, invoca las validaciones de negocio
 * y almacena el nuevo usuario en el repositorio si todo es correcto.
 */
public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCedula;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAlias;
    // Si tienes txtCiudad en tu FXML, descomenta esto:
    @FXML private TextField txtCiudad;

    /**
     * Procesa la solicitud de creación de cuenta.
     * <p>
     * Realiza las siguientes validaciones campo por campo:
     * <ul>
     * <li>Formato de nombre y ciudad.</li>
     * <li>Validez de la cédula y correo electrónico.</li>
     * <li>Disponibilidad del alias (que no exista previamente).</li>
     * </ul>
     * Si hay errores, acumula los mensajes y muestra una advertencia.
     * Si todo es válido, guarda el usuario y regresa al Login.
     */
    @FXML
    protected void onBotonGuardarClick() {
        StringBuilder errores = new StringBuilder();

        String nombre = txtNombre.getText();
        String cedula = txtCedula.getText();
        String email = txtEmail.getText();
        String alias = txtAlias.getText();
        String ciudad = txtCiudad.getText();

        // --- VALIDACIONES ---

        try { Validador.validarNombreCampo(nombre); }
        catch (RuntimeException e) { errores.append("- ").append(e.getMessage()).append("\n"); }

        // Aquí puedes comentar la validación de cédula si ya no la quieren usar
        try { Validador.validarCedula(cedula); }
        catch (RuntimeException e) { errores.append("- ").append(e.getMessage()).append("\n"); }

        try { Validador.validarCorreo(email); } // Valida el formato (@gmail.com etc)
        catch (RuntimeException e) { errores.append("- ").append(e.getMessage()).append("\n"); }

        // ¡¡ESTA ES LA NUEVA VALIDACIÓN DE TU AMIGO!!
        // Verifica que el correo no pertenezca a otro usuario
        try {
            Validador.validarCorreoNoRegistrado(email);
        } catch (RuntimeException e) {
            errores.append("- ").append(e.getMessage()).append("\n");
        }
        // -----------------------------------------------------------

        try { Validador.validarAlias(alias); }
        catch (RuntimeException e) { errores.append("- ").append(e.getMessage()).append("\n"); }

        try { Validador.validarUsuarioExistente(alias); } // Valida que el alias no exista
        catch (RuntimeException e) { errores.append("- ").append(e.getMessage()).append("\n"); }

        try { Validador.validarCiudad(ciudad); }
        catch (RuntimeException e) { errores.append("- ").append(e.getMessage()).append("\n"); }

        // --- FIN VALIDACIONES ---

        if (errores.length() > 0) {
            mostrarAlerta("Errores de Validación", errores.toString(), Alert.AlertType.WARNING);
            return;
        }

        try {
            Usuario nuevoUsuario = new Usuario(cedula, nombre, ciudad, alias, email);
            RepositorioUsuarios.guardarUsuario(nuevoUsuario);

            mostrarAlerta("Éxito", "Cuenta creada correctamente.", Alert.AlertType.INFORMATION);
            onBotonVolverClick();

        } catch (Exception e) {
            mostrarAlerta("Error de Registro", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Cancela el proceso de registro y retorna a la vista de inicio de sesión.
     */
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