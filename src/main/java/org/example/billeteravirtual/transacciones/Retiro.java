package org.example.billeteravirtual.transacciones;


import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.agentes.Validador;

//atributos
public class Retiro extends Transaccion {
    //Constructor
    public Retiro(Usuario usuario, double monto) {
        super(monto, usuario);
        //Se llama el metodo de validar la transacción antes de efectuar el Retiro
        validarTransaccion();
        usuario.getBilletera().restarSaldo(monto);
        usuario.getBilletera().agregarTransaccion(this);
    }


    @Override
    //Devuelve la información de la transacción sobrescribiendo el metodo de la clase padre
    public void getInfoTransaccion() {
        super.getInfoTransaccion();
        System.out.println("Retiro de fondos");
    }

    @Override
    public void validarTransaccion() {
        Validador.validarMonto(this.monto); //Valida que el usuario no haya puesto un retiro con un numero negativo
        Validador.validarTransaccion(this.usuario, this.monto); //Valida que el usuario tenga el saldo suficiente para retirar
    }
}

