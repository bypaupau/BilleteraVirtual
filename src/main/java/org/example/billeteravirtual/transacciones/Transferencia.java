package org.example.billeteravirtual.transacciones;

import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.agentes.Validador;

public class Transferencia extends Transaccion {
    //atributos
    //Para el futuro, se puede implementar una variable de monto maximo por transacción
    private Usuario usuarioDestino;
    private Usuario usuarioOrigen;
    private static final long serialVersionUID = 3466615708913745383L;

    //constructor
    public Transferencia(double monto, Usuario usuarioOrigen, Usuario usuarioDestino) {
        super(monto, usuarioOrigen);
        this.usuarioOrigen = usuarioOrigen;
        this.usuarioDestino = usuarioDestino;

        validarTransaccion();


        usuarioOrigen.getBilletera().restarSaldo(monto);
        usuarioOrigen.getBilletera().agregarTransaccion(this);
        usuarioDestino.getBilletera().aumentarSaldo(monto);
        usuarioDestino.getBilletera().agregarTransaccion(this);
    }

    @Override
    public void validarTransaccion() {
        Validador.validarMonto(this.monto);//Verfica si el monto no es negativo o cero.
        Validador.validarTransaccion(this.usuarioOrigen,this.monto); //Verifica si existe el saldo para realizar la transferencia
    }

    @Override
    //Devuelve la información de la transacción sobrescribiendo el metodo de la clase padre
    public void getInfoTransaccion() {
        super.getInfoTransaccion();
        System.out.println("\nTransferencia realizada a: " + this.usuarioDestino.getNombre() + " de: " + this.usuarioOrigen.getNombre());
        System.out.println("De un valor de: $" + monto);
        System.out.println("Cédula del destinatario: " + this.usuarioDestino.getCedula());
        System.out.println("Cédula del transfiriente: " + this.usuarioOrigen.getCedula());
    }

    public Usuario getUsuarioDestino() {
        return usuarioDestino;
    }
}
