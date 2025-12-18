package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.transacciones.*;
import org.example.billeteravirtual.repositorios.RepositorioTransacciones;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;
import java.util.List;

public class PanelUsuarioController {

    private Usuario usuarioActivo;

    @FXML private Label lblNombre;
    @FXML private Label lblSaldo;

    public void setUsuarioActivo(Usuario usuario) {
        this.usuarioActivo = usuario;
        actualizarDatos();
    }

    private void actualizarDatos() {
        if (usuarioActivo != null) {
            lblNombre.setText("Hola, " + usuarioActivo.getNombre());
            // Formato limpio para el saldo
            lblSaldo.setText(String.format("$ %.2f", usuarioActivo.getBilletera().getSaldo()));
        }
    }

    @FXML
    protected void onBotonDepositar() {
        pedirMontoYEjecutar("Depositar", (monto) -> {
            Deposito t = new Deposito(monto, usuarioActivo);
            RepositorioTransacciones.guardarTransaccion(t);
        });
    }

    @FXML
    protected void onBotonRetirar() {
        pedirMontoYEjecutar("Retirar", (monto) -> {
            Retiro t = new Retiro(usuarioActivo, monto);
            RepositorioTransacciones.guardarTransaccion(t);
        });
    }

    @FXML
    protected void onBotonTransferir() {
        TextInputDialog dialogCedula = new TextInputDialog();
        dialogCedula.setTitle("Transferir");
        dialogCedula.setHeaderText("Cédula o Alias destino:");
        dialogCedula.showAndWait().ifPresent(destinoStr -> {
            Usuario destino = RepositorioUsuarios.buscarPorCedula(destinoStr);
            if(destino == null) destino = RepositorioUsuarios.buscarPorAlias(destinoStr);

            if (destino != null) {
                Usuario finalDestino = destino;
                pedirMontoYEjecutar("Transferir a " + destino.getNombre(), (monto) -> {
                    Transferencia t = new Transferencia(monto, usuarioActivo, finalDestino);
                    RepositorioTransacciones.guardarTransaccion(t);
                });
            } else {
                mostrarNotificacion("Error", "Usuario no encontrado.");
            }
        });
    }

    @FXML
    protected void onBotonPagoServicios() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Luz", "Agua", "Internet");
        dialog.setTitle("Pagar Servicio");
        dialog.setHeaderText("Elige el servicio:");
        dialog.showAndWait().ifPresent(servicio -> {
            pedirMontoYEjecutar("Pagar " + servicio, (monto) -> {
                PagoServicio t = new PagoServicio(monto, usuarioActivo, servicio, "REF-" + System.currentTimeMillis());
                RepositorioTransacciones.guardarTransaccion(t);
            });
        });
    }

    // Interfaz funcional simple para manejar las transacciones
    private interface TransaccionAction {
        void ejecutar(double monto) throws Exception;
    }

    private void pedirMontoYEjecutar(String titulo, TransaccionAction accion) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Ingrese monto:");
        dialog.showAndWait().ifPresent(montoStr -> {
            try {
                double monto = Double.parseDouble(montoStr);
                accion.ejecutar(monto);
                actualizarDatos();
                mostrarNotificacion("Éxito", "Operación realizada.");
            } catch (NumberFormatException e) {
                mostrarNotificacion("Error", "Monto inválido.");
            } catch (Exception e) {
                mostrarNotificacion("Error", e.getMessage());
            }
        });
    }

    @FXML
    protected void onBotonHistorial() {
        try {
            List<Transaccion> lista = RepositorioTransacciones.obtenerHistorialPorUsuario(usuarioActivo.getCedula());
            StringBuilder texto = new StringBuilder();
            if (lista.isEmpty()) texto.append("No hay movimientos.");
            else {
                for (Transaccion t : lista) {
                    texto.append(t.getClass().getSimpleName()).append(": $").append(t.getMonto()).append("\n");
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Historial");
            alert.setHeaderText(null);
            TextArea area = new TextArea(texto.toString());
            area.setEditable(false);
            alert.getDialogPane().setContent(area);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBotonInfo() {
        mostrarNotificacion("Mis Datos",
                "Nombre: " + usuarioActivo.getNombre() + "\nCédula: " + usuarioActivo.getCedula());
    }

    @FXML
    protected void onBotonSalir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/login-view.fxml"));
            Parent root = loader.load();
            // Navegación simple: Volver al login
            lblNombre.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarNotificacion(String titulo, String contenido) {
        Alert.AlertType tipo = titulo.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION;
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}