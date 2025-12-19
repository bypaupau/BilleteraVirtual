package org.example.billeteravirtual.repositorios;

import org.example.billeteravirtual.transacciones.Transaccion;
import org.example.billeteravirtual.persistencia.Persistencia;
import org.example.billeteravirtual.persistencia.Persistible;
import org.example.billeteravirtual.transacciones.Transferencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase estática encargada de gestionar el historial global de transacciones.
 * Permite almacenar y recuperar transacciones, filtrando por usuario si es necesario.
 */
public class RepositorioTransacciones{

    /**
     * Almacena una transacción en el historial global.
     * @param t La transacción a guardar.
     */    private static Map<String, Transaccion> mapaTransacciones = new HashMap<>();

    private static Persistible<Map<String, Transaccion>> servicioPersistencia = new Persistencia<>();

    /**
     * Recupera todas las transacciones realizadas en el sistema.
     * @return Lista completa de transacciones.
     */
    public static void guardarTransaccion(Transaccion t) {
        mapaTransacciones.put(t.getIdTransaccion(), t);
    }

    /**
     * Recupera todas las transacciones realizadas en el sistema.
     * @return Lista completa de transacciones.
     */
    public static List<Transaccion> obtenerHistorialGlobal() {
        return new ArrayList<>(mapaTransacciones.values());
    }

    /**
     * Filtra y recupera las transacciones asociadas a un usuario específico.
     *
     * @param cedulaUsuario Cédula del usuario a consultar.
     * @return Lista de transacciones donde el usuario participó.
     */
    public static List<Transaccion> obtenerHistorialPorUsuario(String cedulaUsuario) {
        List<Transaccion> resultado = new ArrayList<>();

        for (Transaccion t : mapaTransacciones.values()) {
            // 1. Verificar si el usuario fue quien HIZO la transacción (origen)
            boolean esEmisor = t.getUsuario() != null && t.getUsuario().getCedula().equals(cedulaUsuario);

            // 2. Verificar si el usuario fue quien RECIBIÓ la transferencia (destino)
            boolean esReceptor = false;
            if (t instanceof Transferencia) {
                Transferencia transfer = (Transferencia) t;
                if (transfer.getUsuarioDestino() != null && transfer.getUsuarioDestino().getCedula().equals(cedulaUsuario)) {
                    esReceptor = true;
                }
            }

            // Si cumple cualquiera de las dos, se agrega al historial
            if (esEmisor || esReceptor) {
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