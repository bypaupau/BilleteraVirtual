package org.example.billeteravirtual.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.transacciones.Transaccion;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;
import org.example.billeteravirtual.repositorios.RepositorioTransacciones;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PanelAdminController {

    // --- TABLA USUARIOS ---
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colCedula;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colAlias;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colSaldo;

    // --- TABLA TRANSACCIONES ---
    @FXML private TableView<Transaccion> tablaTransacciones;
    @FXML private TableColumn<Transaccion, String> colID;
    @FXML private TableColumn<Transaccion, String> colTipo;
    @FXML private TableColumn<Transaccion, Double> colMonto;
    @FXML private TableColumn<Transaccion, String> colFecha;
    @FXML private TableColumn<Transaccion, String> colUsuario;

    // YA NO NECESITAMOS LOS VBOX (vistaUsuarios, etc) PORQUE EL TABPANE LO HACE SOLO

    @FXML
    public void initialize() {
        configurarTablaUsuarios();
        configurarTablaTransacciones();
    }

    private void configurarTablaUsuarios() {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSaldo.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(cell.getValue().getBilletera().getSaldo())));

        cargarUsuarios();
    }

    private void configurarTablaTransacciones() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idTransaccion"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colTipo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getClass().getSimpleName()));

        // Formato de fecha seguro
        colFecha.setCellValueFactory(cell -> {
            if(cell.getValue().getFecha() != null)
                return new SimpleStringProperty(cell.getValue().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            return new SimpleStringProperty("-");
        });

        // Obtener cédula del usuario dueño de la transacción
        colUsuario.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getUsuario().getCedula()));

        cargarTransacciones();
    }

    @FXML
    public void cargarUsuarios() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(RepositorioUsuarios.obtenerTodos()));
    }

    @FXML
    public void cargarTransacciones() {
        tablaTransacciones.setItems(FXCollections.observableArrayList(RepositorioTransacciones.obtenerHistorialGlobal()));
    }

    @FXML
    protected void onBotonGuardarArchivo() {
        try {
            RepositorioUsuarios.guardarEnArchivo();
            RepositorioTransacciones.guardarEnArchivo();
            mostrarAlerta("Éxito", "Datos guardados correctamente.");
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
        }
    }

    @FXML
    protected void onBotonCargarArchivo() {
        try {
            RepositorioUsuarios.cargarDesdeArchivo("usuarios.dat");
            RepositorioTransacciones.cargarDesdeArchivo("transacciones.dat");
            cargarUsuarios();
            cargarTransacciones();
            mostrarAlerta("Éxito", "Datos cargados y tablas actualizadas.");
        } catch (Exception e) {
            mostrarAlerta("Información", "Error al cargar (puede que los archivos no existan aún): " + e.getMessage());
        }
    }

    @FXML
    protected void onBotonSalirClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/login-view.fxml"));
            Parent root = loader.load();
            // Navegación simple al login
            tablaUsuarios.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}