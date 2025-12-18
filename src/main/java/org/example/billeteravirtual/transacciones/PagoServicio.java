package org.example.billeteravirtual.transacciones;


import org.example.billeteravirtual.agentes.Usuario;
import org.example.billeteravirtual.agentes.Validador;

public class PagoServicio extends Transaccion {
    //atributos
    private String empresa, tipoServicio;

    //Constructor
    public PagoServicio(double monto, Usuario usuario, String empresa, String tipoServicio) {
        super(monto, usuario);
        this.empresa = empresa;
        this.tipoServicio = tipoServicio;

        validarTransaccion();

        usuario.getBilletera().restarSaldo(monto);
        usuario.getBilletera().agregarTransaccion(this);
    }


    @Override
    public void validarTransaccion() {
        Validador.validarTransaccion(this.usuario,this.monto);
    }

    @Override
    public void getInfoTransaccion() {
        super.getInfoTransaccion();
        System.out.println("Empresa:" + this.empresa);
        System.out.println("Servicio:" + this.tipoServicio);
    }
}
