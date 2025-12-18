package org.example.billeteravirtual.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.transacciones.Transaccion;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;
import org.example.billeteravirtual.repositorios.RepositorioTransacciones;

import java.io.IOException;

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

    @FXML
    public void initialize() {
        configurarTablaUsuarios();
        configurarTablaTransacciones();
    }

    private void configurarTablaUsuarios() {
        // Enlazamos las columnas con los getters de la clase Usuario: getCedula(), getNombre()...
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // El saldo es especial porque está dentro de 'billetera'
        colSaldo.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getBilletera().getSaldo())));

        cargarUsuarios();
    }

    private void configurarTablaTransacciones() {
        // Enlazamos columnas con getters de Transaccion
        colID.setCellValueFactory(new PropertyValueFactory<>("idTransaccion"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha")); // JavaFX llama a toString() automáticamente

        // Tipo: Obtenemos el nombre de la clase (Deposito, Retiro, etc.)
        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClass().getSimpleName()));

        // Usuario: Obtenemos la cédula del usuario dueño de la transacción
        colUsuario.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsuario().getCedula()));

        cargarTransacciones();
    }

    @FXML
    public void cargarUsuarios() {
        // Convertimos la lista normal a una lista observable de JavaFX
        ObservableList<Usuario> lista = FXCollections.observableArrayList(RepositorioUsuarios.obtenerTodos());
        tablaUsuarios.setItems(lista);
    }

    @FXML
    public void cargarTransacciones() {
        ObservableList<Transaccion> lista = FXCollections.observableArrayList(RepositorioTransacciones.obtenerHistorialGlobal());
        tablaTransacciones.setItems(lista);
    }

    // --- GESTIÓN DE ARCHIVOS (Lo que hará tu compañero) ---
    @FXML
    protected void onBotonGuardarArchivo() {
        try {
            // AHORA SÍ FUNCIONA PORQUE SON ESTÁTICOS
            RepositorioUsuarios.guardarEnArchivo();
            RepositorioTransacciones.guardarEnArchivo();
            mostrarAlerta("Éxito", "Guardado en disco.");
        } catch (Exception e) {
            mostrarAlerta("Error", "Fallo al guardar: " + e.getMessage());
        }
    }

    @FXML
    protected void onBotonCargarArchivo() {
        try {
            // Suponiendo que los archivos se llamen así
            RepositorioUsuarios.cargarDesdeArchivo("usuarios.dat");
            RepositorioTransacciones.cargarDesdeArchivo("transacciones.dat");

            // Refrescamos las tablas visuales
            cargarUsuarios();
            cargarTransacciones();

            mostrarAlerta("Éxito", "Datos cargados del disco.");
        } catch (Exception e) {
            mostrarAlerta("Aviso de Desarrollo", "La funcionalidad de cargar la implementará tu compañero.\nError técnico: " + e.getMessage());
        }
    }

    // --- NAVEGACIÓN ---
    @FXML
    protected void onBotonSalirClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) tablaUsuarios.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
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