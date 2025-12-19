package org.example.billeteravirtual.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.transacciones.*;
import org.example.billeteravirtual.repositorios.RepositorioTransacciones;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.io.IOException;
import java.util.List;

/**
 * Controlador del panel principal del usuario.
 * Gestiona la visualización del saldo, el saludo personalizado y el acceso
 * a las operaciones financieras (Depósito, Retiro, Transferencia).
 */
public class PanelUsuarioController {

    private Usuario usuarioActivo;

    @FXML private Label lblNombre;
    @FXML private Label lblSaldo;


    /**
     * Asigna el usuario que ha iniciado sesión a este controlador.
     * Actualiza la interfaz gráfica con el nombre y saldo del usuario.
     *
     * @param usuario El objeto Usuario que ha sido autenticado.
     */
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

    /**
     * Despliega un diálogo para realizar un depósito.
     * Solicita el monto, valida que sea positivo y registra la transacción.
     *
     * @param event Evento del mouse que disparó la acción.
     */
    @FXML
    void abrirVentanaDeposito(MouseEvent event) {
        pedirMontoYEjecutar("Depositar", (monto) -> {
            Deposito t = new Deposito(monto, usuarioActivo);
            // IMPORTANTE: Aquí validamos reglas de negocio si fuera necesario
            // Guardamos
            RepositorioTransacciones.guardarTransaccion(t);
        });
    }

    /**
     * Despliega un diálogo para realizar un retiro.
     * Valida que el usuario tenga saldo suficiente antes de confirmar la operación.
     *
     * @param event Evento del mouse que disparó la acción.
     */
    @FXML
    void abrirVentanaRetiro(MouseEvent event) {
        pedirMontoYEjecutar("Retirar", (monto) -> {
            Retiro t = new Retiro(usuarioActivo, monto);
            RepositorioTransacciones.guardarTransaccion(t);
        });
    }


    /**
     * Inicia el flujo de una transferencia de fondos.
     * <ol>
     * <li>Solicita la cédula o alias del destinatario.</li>
     * <li>Verifica que el destinatario exista.</li>
     * <li>Solicita el monto a transferir y valida fondos suficientes.</li>
     * </ol>
     *
     * @param event Evento del mouse que disparó la acción.
     */
    @FXML
    void abrirVentanaTransferencia(MouseEvent event) {
        TextInputDialog dialogCedula = new TextInputDialog();
        dialogCedula.setTitle("Transferir");
        dialogCedula.setHeaderText("Cédula o Alias destino:");
        dialogCedula.setContentText(null); // Estética

        dialogCedula.showAndWait().ifPresent(destinoStr -> {
            // Buscamos usuario destino (por cédula o alias)
            Usuario destino = RepositorioUsuarios.buscarPorCedula(destinoStr);
            if(destino == null) destino = RepositorioUsuarios.buscarPorAlias(destinoStr);

            if (destino != null) {
                // Variable final para usar dentro del lambda
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
    void abrirVentanaHistorial(MouseEvent event) {
        onBotonHistorial(); // Reutilizamos la lógica que ya tenías
    }

    /**
     * Muestra el historial de transacciones del usuario activo.
     * Filtra la lista global de transacciones para mostrar solo las relacionadas con este usuario.
     */    @FXML
    protected void onBotonHistorial() {
        try {
            List<Transaccion> lista = RepositorioTransacciones.obtenerHistorialPorUsuario(usuarioActivo.getCedula());
            StringBuilder texto = new StringBuilder();
            if (lista.isEmpty()) texto.append("No hay movimientos recientes.");
            else {
                // Mostramos las transacciones más recientes primero (invirtiendo la lista visualmente o recorriendo normal)
                for (Transaccion t : lista) {
                    texto.append(t.getFecha().toString()) // O tu formato de fecha
                            .append(" - ")
                            .append(t.getClass().getSimpleName())
                            .append(": $")
                            .append(String.format("%.2f", t.getMonto()))
                            .append("\n");
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Historial de Movimientos");
            alert.setHeaderText(null);
            TextArea area = new TextArea(texto.toString());
            area.setEditable(false);
            area.setWrapText(true);
            area.setMaxWidth(Double.MAX_VALUE);
            area.setMaxHeight(Double.MAX_VALUE);

            alert.getDialogPane().setContent(area);
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- UTILIDADES ---

    // Interfaz funcional simple para manejar las transacciones
    private interface TransaccionAction {
        void ejecutar(double monto) throws Exception;
    }

    private void pedirMontoYEjecutar(String titulo, TransaccionAction accion) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Ingrese monto:");
        dialog.setContentText(null);

        dialog.showAndWait().ifPresent(montoStr -> {
            try {
                double monto = Double.parseDouble(montoStr);
                if (monto <= 0) throw new NumberFormatException();

                // 1. Ejecutar la lógica de la transacción (Retiro/Depósito/Transferencia)
                accion.ejecutar(monto);

                // 2. ¡NUEVO! GUARDAR CAMBIOS INMEDIATAMENTE
                // Guardamos tanto usuarios (para actualizar saldo) como transacciones
                RepositorioUsuarios.guardarEnArchivo();
                RepositorioTransacciones.guardarEnArchivo();

                // 3. Refrescar interfaz
                actualizarDatos();
                mostrarNotificacion("Éxito", "Operación realizada y guardada.");

            } catch (NumberFormatException e) {
                mostrarNotificacion("Error", "Monto inválido. Ingrese un número positivo.");
            } catch (Exception e) {
                e.printStackTrace(); // Imprimir error en consola para depurar
                mostrarNotificacion("Error", "No se pudo realizar: " + e.getMessage());
            }
        });
    }

    @FXML
    protected void onBotonInfo() {
        mostrarNotificacion("Mis Datos",
                "Nombre: " + usuarioActivo.getNombre() + "\n" +
                        "Cédula: " + usuarioActivo.getCedula() + "\n" +
                        "Alias: " + usuarioActivo.getAlias() + "\n" +
                        "Email: " + usuarioActivo.getEmail());
    }

    @FXML
    protected void onBotonSalir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/login-view.fxml"));
            Parent root = loader.load();
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