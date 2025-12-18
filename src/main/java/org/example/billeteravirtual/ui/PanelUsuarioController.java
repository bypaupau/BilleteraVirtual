package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.transacciones.*; // Importa todas las transacciones
import org.example.billeteravirtual.repositorios.RepositorioTransacciones;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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
            lblSaldo.setText(String.format("$ %.2f", usuarioActivo.getBilletera().getSaldo()));
        }
    }

    @FXML
    protected void onBotonDepositar() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Depositar");
        dialog.setHeaderText("Monto a depositar:");
        dialog.showAndWait().ifPresent(montoStr -> {
            try {
                double monto = Double.parseDouble(montoStr);
                Deposito deposito = new Deposito(monto, usuarioActivo);
                RepositorioTransacciones.guardarTransaccion(deposito);
                actualizarDatos();
                mostrarNotificacion("Éxito", "Depósito realizado.");
            } catch (Exception e) {
                mostrarNotificacion("Error", "Error al depositar: " + e.getMessage());
            }
        });
    }

    @FXML
    protected void onBotonRetirar() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Retirar");
        dialog.setHeaderText("Monto a retirar:");
        dialog.showAndWait().ifPresent(montoStr -> {
            try {
                double monto = Double.parseDouble(montoStr);
                Retiro retiro = new Retiro(usuarioActivo, monto);
                RepositorioTransacciones.guardarTransaccion(retiro);
                actualizarDatos();
                mostrarNotificacion("Éxito", "Retiro realizado.");
            } catch (Exception e) {
                mostrarNotificacion("Error", e.getMessage());
            }
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
                TextInputDialog dialogMonto = new TextInputDialog();
                dialogMonto.setHeaderText("Monto a transferir a " + destino.getNombre());
                Usuario finalDestino = destino;
                dialogMonto.showAndWait().ifPresent(montoStr -> {
                    try {
                        double monto = Double.parseDouble(montoStr);
                        Transferencia t = new Transferencia(monto, usuarioActivo, finalDestino);
                        RepositorioTransacciones.guardarTransaccion(t);
                        actualizarDatos();
                        mostrarNotificacion("Éxito", "Transferencia enviada.");
                    } catch (Exception e) {
                        mostrarNotificacion("Error", e.getMessage());
                    }
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
            TextInputDialog dialogMonto = new TextInputDialog();
            dialogMonto.setHeaderText("Monto a pagar:");
            dialogMonto.showAndWait().ifPresent(montoStr -> {
                try {
                    double monto = Double.parseDouble(montoStr);
                    PagoServicio pago = new PagoServicio(monto, usuarioActivo, servicio, "REF-" + System.currentTimeMillis());
                    RepositorioTransacciones.guardarTransaccion(pago);
                    actualizarDatos();
                    mostrarNotificacion("Éxito", "Servicio pagado.");
                } catch (Exception e) {
                    mostrarNotificacion("Error", e.getMessage());
                }
            });
        });
    }

    @FXML
    protected void onBotonHistorial() {
        try {
            StringBuilder texto = new StringBuilder();

            // --- CORRECCIÓN AQUÍ ---
            // Usamos el método que SÍ existe en tu repositorio y le pasamos la Cédula (String)
            List<Transaccion> lista = RepositorioTransacciones.obtenerHistorialPorUsuario(usuarioActivo.getCedula());
            // -----------------------

            if (lista.isEmpty()) {
                texto.append("No hay movimientos.");
            } else {
                for (Transaccion t : lista) {
                    // Usamos toString() simple por si getFecha() da problemas de formato
                    texto.append(t.getClass().getSimpleName())
                            .append(": $").append(t.getMonto())
                            .append("\n");
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
            mostrarNotificacion("Error", "No se pudo cargar el historial: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBotonInfo() {
        mostrarNotificacion("Mis Datos",
                "Nombre: " + usuarioActivo.getNombre() + "\n" +
                        "Cédula: " + usuarioActivo.getCedula() + "\n" +
                        "Email: " + usuarioActivo.getEmail());
    }

    @FXML
    protected void onBotonSalir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
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