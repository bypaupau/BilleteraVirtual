package org.example.billeteravirtual.repositorios;

import org.example.billeteravirtual.transacciones.Transaccion;
import org.example.billeteravirtual.persistencia.Persistencia;
import org.example.billeteravirtual.persistencia.Persistible;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Quitamos implements para libertad estática
public class RepositorioTransacciones {

    // 1. DATOS ESTÁTICOS
    private static Map<String, Transaccion> mapaTransacciones = new HashMap<>();

    // 2. PERSISTENCIA ESTÁTICA
    private static Persistible<Map<String, Transaccion>> servicioPersistencia = new Persistencia<>();

    // --- MÉTODOS LÓGICA ---

    public static void guardarTransaccion(Transaccion t) {
        mapaTransacciones.put(t.getIdTransaccion(), t);
    }

    public static List<Transaccion> obtenerHistorialGlobal() {
        return new ArrayList<>(mapaTransacciones.values());
    }

    public static List<Transaccion> obtenerHistorialPorUsuario(String cedulaUsuario) {
        List<Transaccion> resultado = new ArrayList<>();
        for (Transaccion t : mapaTransacciones.values()) {
            // Verificamos que la transacción tenga usuario y coincida la cédula
            if (t.getUsuario() != null && t.getUsuario().getCedula().equals(cedulaUsuario)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    // --- MÉTODOS ARCHIVOS (ESTÁTICOS) ---

    public static void guardarEnArchivo() throws IOException {
        // Cambio: Usar la constante Paths.ARCHIVO_TRANSACCIONES
        servicioPersistencia.guardar(Paths.ARCHIVO_TRANSACCIONES, mapaTransacciones);
    }

    public static void cargarDesdeArchivo(String ruta) {
        try {
            Map<String, Transaccion> cargado = servicioPersistencia.cargar(ruta);
            if (cargado != null) {
                mapaTransacciones.putAll(cargado);
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar transacciones: " + e.getMessage());
        }
    }
}