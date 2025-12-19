package org.example.billeteravirtual.repositorios;

import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.persistencia.Persistencia;
import org.example.billeteravirtual.persistencia.Persistible; // Asegúrate de tener este archivo o ajustarlo

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// NOTA: Quitamos "implements Almacenable" para poder hacerlo todo estático sin problemas
public class RepositorioUsuarios {

    // 1. EL MAPA ES ESTÁTICO (Global para toda la app)
    private static Map<String, Usuario> mapaUsuarios = new HashMap<>();

    // 2. LA HERRAMIENTA DE GUARDADO ES ESTÁTICA
    private static Persistible<Map<String, Usuario>> servicioPersistencia = new Persistencia<>();

    // --- MÉTODOS DE LÓGICA (ESTÁTICOS) ---

    public static void guardarUsuario(Usuario u) {
        if (mapaUsuarios.containsKey(u.getCedula())) {
            throw new RuntimeException("El usuario con cédula " + u.getCedula() + " ya existe.");
        }
        mapaUsuarios.put(u.getCedula(), u);
    }

    public static Usuario buscarPorCedula(String cedula) {
        return mapaUsuarios.get(cedula);
    }

    public static Usuario buscarPorAlias(String alias) {
        for (Usuario u : mapaUsuarios.values()) {
            if (u.getAlias().equalsIgnoreCase(alias)) {
                return u;
            }
        }
        return null;
    }

    public static List<Usuario> obtenerTodos() {
        return new ArrayList<>(mapaUsuarios.values());
    }

    // --- MÉTODOS DE ARCHIVOS (AHORA SON ESTÁTICOS) ---

    public static void guardarEnArchivo() throws IOException {
        // Cambio: Usar la constante Paths.ARCHIVO_USUARIOS
        servicioPersistencia.guardar(Paths.ARCHIVO_USUARIOS, mapaUsuarios);
    }

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

    public static boolean existeAlias(String alias) {
        return buscarPorAlias(alias) != null;
    }
}