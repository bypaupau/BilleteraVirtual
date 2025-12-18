package org.example.billeteravirtual.transacciones;

import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.agentes.Validador;

//Constructor
public class Deposito extends Transaccion {
    public Deposito(double monto, Usuario usuario) {
        super(monto, usuario);
        Validador.validarMonto(monto); //Valida que la cantidad a depositar no sea negativa.
        usuario.getBilletera().aumentarSaldo(monto);
        usuario.getBilletera().agregarTransaccion(this);
    }
    @Override
    //Devuelve la información de la transacción sobrescribiendo el metodo de la clase padre
    public void getInfoTransaccion() {
        super.getInfoTransaccion();
        System.out.println("Deposito en efectivo");
    }


}
