package org.example.billeteravirtual.repositorios;

import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.persistencia.Persistencia;
import org.example.billeteravirtual.persistencia.Persistible; // Asegúrate de tener este archivo o ajustarlo

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase estática encargada de gestionar la colección de usuarios del sistema.
 * Permite guardar, buscar y listar usuarios, así como persistir los datos en archivo.
 */public class RepositorioUsuarios {

    private static Map<String, Usuario> mapaUsuarios = new HashMap<>();

    private static Persistible<Map<String, Usuario>> servicioPersistencia = new Persistencia<>();

    /**
     * Registra un nuevo usuario en el mapa en memoria.
     *
     * @param u El objeto Usuario a guardar.
     * @throws RuntimeException Si ya existe un usuario con esa cédula.
     */
    public static void guardarUsuario(Usuario u) {
        if (mapaUsuarios.containsKey(u.getCedula())) {
            throw new RuntimeException("El usuario con cédula " + u.getCedula() + " ya existe.");
        }
        mapaUsuarios.put(u.getCedula(), u);
    }

    /**
     * Busca un usuario por su número de cédula.
     *
     * @param cedula Cédula a buscar.
     * @return El objeto Usuario si existe, o null si no se encuentra.
     */
    public static Usuario buscarPorCedula(String cedula) {
        return mapaUsuarios.get(cedula);
    }

    /**
     * Busca un usuario por su alias (nombre de usuario).
     *
     * @param alias Alias a buscar (ignora mayúsculas/minúsculas).
     * @return El objeto Usuario si existe, o null si no se encuentra.
     */
    public static Usuario buscarPorAlias(String alias) {
        for (Usuario u : mapaUsuarios.values()) {
            if (u.getAlias().equalsIgnoreCase(alias)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Obtiene una lista con todos los usuarios registrados.
     *
     * @return Lista de objetos Usuario.
     */
    public static List<Usuario> obtenerTodos() {
        return new ArrayList<>(mapaUsuarios.values());
    }

    /**
     * Guarda el estado actual del mapa de usuarios en el archivo definido en {@link Paths#ARCHIVO_USUARIOS}.
     *
     * @throws IOException Si ocurre un error de entrada/salida al escribir el archivo.
     */
    public static void guardarEnArchivo() throws IOException {
        // Cambio: Usar la constante Paths.ARCHIVO_USUARIOS
        servicioPersistencia.guardar(Paths.ARCHIVO_USUARIOS, mapaUsuarios);
    }

    /**
     * Carga los usuarios desde el archivo binario y los fusiona con los datos en memoria.
     *
     * @param ruta Ruta del archivo a cargar.
     */
    public static void cargarDesdeArchivo(String ruta) {
        try {
            Map<String, Usuario> cargado = servicioPersistencia.cargar(ruta);

            if (cargado != null) {

                mapaUsuarios.putAll(cargado);

                System.out.println("Usuarios cargados y fusionados con los actuales.");
            }
        } catch (Exception e) {
            System.out.println("No se pudo cargar usuarios: " + e.getMessage());
        }
    }

    /**
     * Verifica si un alias ya está en uso por otro usuario.
     * @param alias Alias a comprobar.
     * @return true si el alias existe, false en caso contrario.
     */
    public static boolean existeAlias(String alias) {
        return buscarPorAlias(alias) != null;
    }
}