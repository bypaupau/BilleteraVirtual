package org.example.billeteravirtual.agentes;

import org.example.billeteravirtual.excepciones.MontoInvalidoException;
import org.example.billeteravirtual.transacciones.Transaccion;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa la billetera digital asociada a un usuario.
 * Gestiona el saldo actual y mantiene un historial de todas las transacciones realizadas.
 */
public class Billetera implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //Atributos
    private double saldo;
    private List<Transaccion> transacciones;

    /**
     * Constructor por defecto. Inicializa el saldo en 0 y crea una lista vacía de transacciones.
     */
    public Billetera() {this.saldo = 0;
        transacciones = new ArrayList<>();} // si no ingresa un monto, entonces empieza con 0

    /**
     * Constructor que inicializa la billetera con un saldo específico.
     * * @param saldo Monto inicial. Debe ser un valor positivo.
     * @throws MontoInvalidoException Si el saldo inicial es negativo.
     */
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

    /**
     * Aumenta el saldo de la billetera.
     * Utilizado en depósitos y transferencias recibidas.
     * * @param monto Cantidad a sumar al saldo actual.
     */
    public void aumentarSaldo(double monto){
        saldo+=monto;
    }


    /**
     * Disminuye el saldo de la billetera.
     * Utilizado en retiros, pagos de servicios y transferencias enviadas.
     * Nota: No valida fondos aquí, la validación debe hacerse antes de llamar a este método.
     * * @param monto Cantidad a restar del saldo actual.
     */
    public void restarSaldo(double monto){saldo-=monto;}


    /**
     * Registra una nueva transacción en el historial de la billetera.
     * * @param transaccion Objeto Transaccion que contiene los detalles de la operación.
     */
    public void agregarTransaccion(Transaccion transaccion){
        this.transacciones.add(transaccion);
    }

}


