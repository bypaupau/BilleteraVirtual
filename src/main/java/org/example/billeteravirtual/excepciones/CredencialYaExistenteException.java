package org.example.billeteravirtual.excepciones;

public class CredencialYaExistenteException extends RuntimeException {
    public CredencialYaExistenteException(String message) {
        super(message);
    }
}
