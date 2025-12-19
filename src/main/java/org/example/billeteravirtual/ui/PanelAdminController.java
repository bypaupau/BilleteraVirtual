package org.example.billeteravirtual.ui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.transacciones.Transaccion;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;
import org.example.billeteravirtual.repositorios.RepositorioTransacciones;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador del panel de administración.
 * Permite visualizar todos los usuarios registrados y el historial global de transacciones.
 * También gestiona la persistencia manual (guardar/cargar) de los datos.
 */
public class PanelAdminController {

    @FXML private VBox sidebarMenu;
    @FXML private VBox menuContent;

    // Vistas principales
    @FXML private VBox vistaUsuarios;
    @FXML private VBox vistaTransacciones;

    // Contenedores de búsqueda
    @FXML private HBox contenedorBusquedaCedula;
    @FXML private HBox contenedorBusquedaAlias; // <--- NUEVO
    @FXML private HBox contenedorBusquedaTransacciones;

    @FXML private Label lblTituloUsuarios;
    @FXML private Label lblTituloTransacciones;

    // Campos de texto
    @FXML private TextField txtBuscarCedula;
    @FXML private TextField txtBuscarAlias; // <--- NUEVO
    @FXML private TextField txtBuscarTransaccion;

    // Tabla Usuarios
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colCedula;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colAlias;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colSaldo;

    // Tabla Transacciones
    @FXML private TableView<Transaccion> tablaTransacciones;
    @FXML private TableColumn<Transaccion, String> colID;
    @FXML private TableColumn<Transaccion, String> colTipo;
    @FXML private TableColumn<Transaccion, Double> colMonto;
    @FXML private TableColumn<Transaccion, String> colFecha;
    @FXML private TableColumn<Transaccion, String> colUsuario;

    /**
     * Configura las tablas y las animaciones del menú lateral al iniciar la vista.
     */
    @FXML
    public void initialize() {
        configurarTablaUsuarios();
        configurarTablaTransacciones();
        configurarAnimacionMenu();
        mostrarTodosUsuarios();
    }

    /**
     * Cambia la vista activa para mostrar la tabla de usuarios.
     * Reinicia los filtros de búsqueda.
     */
    @FXML
    public void mostrarTodosUsuarios() {
        cambiarVista(true); // true = usuarios
        lblTituloUsuarios.setText("Todos los Usuarios");

        // Ocultar barras de búsqueda
        contenedorBusquedaCedula.setVisible(false); contenedorBusquedaCedula.setManaged(false);
        contenedorBusquedaAlias.setVisible(false); contenedorBusquedaAlias.setManaged(false);

        cargarDatosUsuarios();
    }

    /**
     * Filtra la tabla de usuarios buscando por número de cédula exacto.
     * Actualiza la tabla con el resultado o muestra alerta si no se encuentra.
     */
    @FXML
    public void mostrarBusquedaCedula() {
        cambiarVista(true);
        lblTituloUsuarios.setText("Buscar por Cédula");

        contenedorBusquedaCedula.setVisible(true); contenedorBusquedaCedula.setManaged(true);
        contenedorBusquedaAlias.setVisible(false); contenedorBusquedaAlias.setManaged(false);

        txtBuscarCedula.clear();
        tablaUsuarios.getItems().clear();
    }

    @FXML
    public void mostrarBusquedaAlias() {
        cambiarVista(true);
        lblTituloUsuarios.setText("Buscar por Alias");

        contenedorBusquedaCedula.setVisible(false); contenedorBusquedaCedula.setManaged(false);
        contenedorBusquedaAlias.setVisible(true); contenedorBusquedaAlias.setManaged(true);

        txtBuscarAlias.clear();
        tablaUsuarios.getItems().clear();
    }


    @FXML
    public void mostrarTodasTransacciones() {
        cambiarVista(false); // false = transacciones
        lblTituloTransacciones.setText("Todas las Transacciones");
        contenedorBusquedaTransacciones.setVisible(false); contenedorBusquedaTransacciones.setManaged(false);
        cargarDatosTransacciones();
    }

    /**
     * Busca una transacción específica por su ID único (ej: TRX-1).
     * Muestra el resultado en la tabla de transacciones.
     */
    @FXML
    public void mostrarBusquedaTransacciones() {
        cambiarVista(false);
        lblTituloTransacciones.setText("Buscar Transacción");
        contenedorBusquedaTransacciones.setVisible(true); contenedorBusquedaTransacciones.setManaged(true);
        txtBuscarTransaccion.clear();
        tablaTransacciones.getItems().clear();
    }


    @FXML
    public void accionBuscarUsuarioCedula() {
        String cedula = txtBuscarCedula.getText().trim();
        if (cedula.isEmpty()) return;
        Usuario u = RepositorioUsuarios.buscarPorCedula(cedula);
        actualizarTablaUsuario(u);
    }

    @FXML
    public void accionBuscarUsuarioAlias() {
        String alias = txtBuscarAlias.getText().trim();
        if (alias.isEmpty()) return;
        Usuario u = RepositorioUsuarios.buscarPorAlias(alias);
        actualizarTablaUsuario(u);
    }

    private void actualizarTablaUsuario(Usuario u) {
        if (u != null) {
            tablaUsuarios.setItems(FXCollections.observableArrayList(u));
        } else {
            mostrarAlerta("Sin resultados", "Usuario no encontrado.");
            tablaUsuarios.getItems().clear();
        }
    }

    /**
     * Filtra la tabla de transacciones buscando por ID específico.
     * Se ejecuta al hacer clic en el botón de búsqueda de transacciones.
     */
    @FXML
    public void accionBuscarTransaccion() {
        String id = txtBuscarTransaccion.getText().trim();
        if (id.isEmpty()) return;

        List<Transaccion> todas = RepositorioTransacciones.obtenerHistorialGlobal();
        ObservableList<Transaccion> filtrada = FXCollections.observableArrayList();
        for (Transaccion t : todas) {
            if (t.getIdTransaccion().equalsIgnoreCase(id)) {
                filtrada.add(t);
                break;
            }
        }
        if (!filtrada.isEmpty()) tablaTransacciones.setItems(filtrada);
        else mostrarAlerta("Sin resultados", "ID no encontrado.");
    }


    private void cambiarVista(boolean mostrarUsuarios) {
        vistaUsuarios.setVisible(mostrarUsuarios);
        vistaUsuarios.setManaged(mostrarUsuarios);

        vistaTransacciones.setVisible(!mostrarUsuarios);
        vistaTransacciones.setManaged(!mostrarUsuarios);
    }

    private void configurarAnimacionMenu() {
        Duration duration = Duration.millis(250);
        // Ajustamos los valores:
        // TranslateX: -180 oculta la mayoría, 0 muestra todo.
        TranslateTransition openNav = new TranslateTransition(duration, sidebarMenu);
        openNav.setToX(0);
        FadeTransition fadeIn = new FadeTransition(duration, menuContent);
        fadeIn.setToValue(1.0);

        ParallelTransition open = new ParallelTransition(openNav, fadeIn);

        TranslateTransition closeNav = new TranslateTransition(duration, sidebarMenu);
        closeNav.setToX(-180); // Deja 60px visibles (240 ancho total - 180)
        FadeTransition fadeOut = new FadeTransition(duration, menuContent);
        fadeOut.setToValue(0.0);

        ParallelTransition close = new ParallelTransition(closeNav, fadeOut);

        sidebarMenu.setOnMouseEntered(e -> { close.stop(); open.play(); });
        sidebarMenu.setOnMouseExited(e -> { open.stop(); close.play(); });
    }

    // ... (El resto de métodos cargarDatosUsuarios, cargarDatosTransacciones, configurarTabla, guardar y cargar archivos son IGUALES que antes) ...

    private void cargarDatosUsuarios() {
        tablaUsuarios.setItems(FXCollections.observableArrayList(RepositorioUsuarios.obtenerTodos()));
    }
    private void cargarDatosTransacciones() {
        tablaTransacciones.setItems(FXCollections.observableArrayList(RepositorioTransacciones.obtenerHistorialGlobal()));
    }
    private void configurarTablaUsuarios() {
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAlias.setCellValueFactory(new PropertyValueFactory<>("alias"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSaldo.setCellValueFactory(cell -> new SimpleStringProperty(String.format("$ %.2f", cell.getValue().getBilletera().getSaldo())));
    }
    private void configurarTablaTransacciones() {
        colID.setCellValueFactory(new PropertyValueFactory<>("idTransaccion"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colTipo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getClass().getSimpleName()));
        colFecha.setCellValueFactory(cell -> {
            if(cell.getValue().getFecha() != null) return new SimpleStringProperty(cell.getValue().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            return new SimpleStringProperty("-");
        });
        colUsuario.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getUsuario().getCedula()));
    }

    /**
     * Guarda manualmente el estado actual de usuarios y transacciones en archivos binarios (.dat).
     * Utiliza los repositorios para invocar la persistencia.
     */
    @FXML protected void onBotonGuardarArchivo() {
        try { RepositorioUsuarios.guardarEnArchivo(); RepositorioTransacciones.guardarEnArchivo(); mostrarAlerta("Éxito", "Datos guardados."); } catch (Exception e) { mostrarAlerta("Error", e.getMessage()); }
    }

    /**
     * Carga los datos desde los archivos binarios, sobrescribiendo o fusionando
     * con los datos en memoria, y refresca las tablas visuales.
     */
    @FXML protected void onBotonCargarArchivo() {
        try { RepositorioUsuarios.cargarDesdeArchivo("usuarios.dat"); RepositorioTransacciones.cargarDesdeArchivo("transacciones.dat"); if (vistaUsuarios.isVisible()) cargarDatosUsuarios(); else cargarDatosTransacciones(); mostrarAlerta("Éxito", "Datos cargados."); } catch (Exception e) { mostrarAlerta("Información", "Error al cargar archivos."); }
    }
    @FXML protected void onBotonSalirClick() {
        try { FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/billeteravirtual/login-view.fxml")); Parent root = loader.load(); vistaUsuarios.getScene().setRoot(root); } catch (IOException e) { e.printStackTrace(); }
    }
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setTitle(titulo); alert.setHeaderText(null); alert.setContentText(contenido); alert.showAndWait();
    }
}