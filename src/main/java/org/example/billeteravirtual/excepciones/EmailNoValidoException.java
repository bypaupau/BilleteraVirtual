package org.example.billeteravirtual.excepciones;

public class EmailNoValidoException extends RuntimeException {
    public EmailNoValidoException(String message) {
        super(message);
    }

}