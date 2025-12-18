package org.example.billeteravirtual.persistencia;

import java.io.IOException;

// T es el tipo de dato (ej: Map, List, Usuario, etc.)
public interface Persistible<T> {
    void guardar(String ruta, T datos) throws IOException;
    T cargar(String ruta) throws IOException, ClassNotFoundException;
}