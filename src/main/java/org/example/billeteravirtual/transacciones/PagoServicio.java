package org.example.billeteravirtual.transacciones;


import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.agentes.Validador;

/**
 * Representa una transacción de pago a una empresa de servicios externos.
 * Descuenta saldo de la billetera del usuario.
 */
public class PagoServicio extends Transaccion {
    //atributos
    private String nombreEmpresa;
    private String tipoServicio;
    /**
     * Crea una nueva transacción de pago de servicios.
     *
     * @param monto Cantidad a pagar.
     * @param usuario Usuario que realiza el pago.
     * @param nombreEmpresa Nombre de la empresa que recibe el pago.
     * @param tipoServicio Tipo de servicio (Luz, Agua, Internet, etc.).
     */
    public PagoServicio(double monto, Usuario usuario, String nombreEmpresa, String tipoServicio) {
        super(monto, usuario); // Llama al constructor de Transaccion
        this.nombreEmpresa = nombreEmpresa;
        this.tipoServicio = tipoServicio;

        validarTransaccion(); // Valida saldo
        usuario.getBilletera().restarSaldo(monto); // Descuenta
        usuario.getBilletera().agregarTransaccion(this); // Guarda en historial local
    }

    /**
     * Valida que el usuario tenga fondos suficientes para pagar el servicio.
     *
     * @throws org.example.billeteravirtual.excepciones.SaldoInsuficienteException Si no hay fondos.
     */
    @Override
    public void validarTransaccion() {
        Validador.validarTransaccion(this.usuario,this.monto);
    }

    /**
     * Imprime en consola los detalles del pago, incluyendo empresa y tipo de servicio.
     */
    @Override
    public void getInfoTransaccion() {
        super.getInfoTransaccion();
        System.out.println("Empresa:" + this.nombreEmpresa);
        System.out.println("Servicio:" + this.tipoServicio);
    }
}
