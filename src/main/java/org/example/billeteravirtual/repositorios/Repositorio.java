package org.example.billeteravirtual.repositorios;

import java.util.List;

public interface Repositorio<T> {
    void guardar(T t);
    T buscar(String id);
    List<T> obtenerTodos();

}
