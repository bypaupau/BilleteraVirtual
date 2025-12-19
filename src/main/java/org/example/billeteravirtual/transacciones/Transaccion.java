package org.example.billeteravirtual.transacciones;


import org.example.billeteravirtual.agentes.Usuario;
import java.io.Serial;
import java.io.Serializable;
import  java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase abstracta que representa una operación financiera base dentro del sistema.
 * Todas las operaciones específicas (Retiro, Depósito, etc.) deben heredar de esta clase.
 */
public abstract class Transaccion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    protected double monto;
    protected String idTransaccion;
    private LocalDateTime fechaHora;
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
    protected Usuario usuario;
    private String fechaFormateada;
    private static int contadorID; // es estatico porque no sera unico de cada instancia

    /**
     * Constructor base para todas las transacciones.
     * Genera automáticamente la fecha/hora actual y un ID único de transacción.
     * * @param monto El valor monetario de la transacción.
     * @param usuario El usuario que origina o solicita la transacción.
     */
    public Transaccion(double monto, Usuario usuario) {
        this.monto = monto;
        this.usuario = usuario;
        this.fechaHora = LocalDateTime.now();
        this.fechaFormateada = this.fechaHora.format(FORMATO);
        contadorID++;
        this.idTransaccion = "TRX-" + contadorID;
    }

    public double getMonto() {
        return monto;
    }

    /**
     * Imprime en consola los detalles de la transacción.
     * Puede ser sobrescrito para agregar detalles específicos del tipo de transacción.
     */
    public void getInfoTransaccion(){
        System.out.println("Tipo: " + this.getClass().getSimpleName());
        System.out.println("ID Transacción: " + this.idTransaccion);
        System.out.println("Fecha: " + fechaFormateada);
        System.out.println("Monto: $" + this.monto);
        if (this.usuario != null) {
            System.out.println("Realizado por: " + this.usuario.getNombre() + "\n");
        }
    }

    /**
     * Método abstracto para validar las reglas de negocio específicas de cada transacción.
     * Debe ser implementado por las subclases (ej: verificar saldo en Retiro).
     * * @throws RuntimeException (o subclases específicas) si la validación falla.
     */
    public void validarTransaccion(){ //va a ser aplicado polimorfismo en cada transaccion
    }

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getFecha() {return fechaHora;}
    }


