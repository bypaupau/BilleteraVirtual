package org.example.billeteravirtual.persistencia;

import java.io.*;

public class Persistencia<T> implements Persistible<T> {

    @Override
    public void guardar(String ruta, T datos) throws IOException {
        // Crea el flujo de salida y escribe el objeto
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(datos);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T cargar(String ruta) throws IOException, ClassNotFoundException {
        // Crea el flujo de entrada y lee el objeto
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            return (T) ois.readObject();
        }
    }
}