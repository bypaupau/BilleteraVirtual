package org.example.billeteravirtual.persistencia;

import java.io.IOException;

/**
 * Interfaz genérica que define el contrato para la persistencia de datos.
 *
 * @param <T> El tipo de dato que se desea guardar o cargar (ej: List, Map, Usuario).
 */public interface Persistible<T> {

    /**
     * Guarda un objeto en un archivo físico.
     * @param ruta Ruta del archivo destino.
     * @param datos Objeto a serializar y guardar.
     * @throws IOException Si hay error de escritura.
     */
    void guardar(String ruta, T datos) throws IOException;

    /**
     * Lee un objeto desde un archivo físico.
     * @param ruta Ruta del archivo origen.
     * @return El objeto deserializado.
     * @throws IOException Si hay error de lectura.
     * @throws ClassNotFoundException Si la clase del objeto guardado no coincide.
     */
    T cargar(String ruta) throws IOException, ClassNotFoundException;
}