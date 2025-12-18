package org.example.billeteravirtual.agentes;

import org.example.billeteravirtual.transacciones.Transaccion;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Billetera implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //Atributos
    private double saldo;
    private List<Transaccion> transacciones;

    //Constructores
    public Billetera() {this.saldo = 0;
        transacciones = new ArrayList<>();} // si no ingresa un monto, entonces empieza con 0


    public Billetera(double saldo) {
        Validador.validarMonto(saldo); //verifica que no se haya introducido un saldo negativo.
        this.saldo = saldo;
        transacciones = new ArrayList<Transaccion>();
    } // creamos nuestra billetera con un saldo inicial


    //Metodos
    //getter para obtener el saldo
    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {}

    //metodo para presentar el saldo al usuario
    public void infoSaldo(){
        System.out.println("Su saldo actual es: " + " $" + saldo);
    }

    public List<Transaccion> getHistorial(){
        return transacciones;
    } // nos devuelve la lista de transacciones

    //Metodo para aumentar el Saldo, empleado dependiendo de las distintas transacciones
    public void aumentarSaldo(double monto){
        saldo+=monto;
    }
    //Metodo para restar el Saldo, empleado dependiendo de las distintas transacciones
    public void restarSaldo(double monto){saldo-=monto;}


    /**
     * Para agregar la transaccion a la lista de transacciones de nuestra billetera
     */
    public void agregarTransaccion(Transaccion transaccion){
        this.transacciones.add(transaccion);
    }

    public static class UI {
    }
}


