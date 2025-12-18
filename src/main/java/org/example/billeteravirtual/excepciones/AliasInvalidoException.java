package org.example.billeteravirtual.excepciones;

public class AliasInvalidoException extends RuntimeException {
    public AliasInvalidoException(String message) {
        super(message);
    }
}