package org.example.billeteravirtual.excepciones;

/**
 * Excepción lanzada cuando un usuario intenta realizar una transacción (retiro, transferencia, pago)
 * por un monto superior al saldo disponible en su billetera.
 */
public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
