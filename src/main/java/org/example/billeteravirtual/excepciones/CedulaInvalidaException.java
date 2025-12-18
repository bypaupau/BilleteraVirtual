package org.example.billeteravirtual.excepciones;

public class CedulaInvalidaException extends RuntimeException {
    public CedulaInvalidaException(String message) {
        super(message);
    }
}
