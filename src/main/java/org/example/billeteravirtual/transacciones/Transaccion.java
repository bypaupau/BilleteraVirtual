package org.example.billeteravirtual.transacciones;


import org.example.billeteravirtual.agentes.Usuario;
import java.io.Serial;
import java.io.Serializable;
import  java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Transaccion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    //atributos
    //private static final double  MAX_MONTO_TRANSACCION = 10000.00;  Se puede considerar implementar un monto maximo por transacción en el futuro
    protected double monto;
    protected String idTransaccion;
    private LocalDateTime fechaHora;
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
    private String fechaFormateada;
    protected Usuario usuario;
    private static int contadorID; // es estatico porque no sera unico de cada instancia

    //Constructores
    public Transaccion(double monto, Usuario usuario) {
        this.monto = monto;
        this.usuario = usuario;
        this.fechaHora = LocalDateTime.now();
        this.fechaFormateada = this.fechaHora.format(formato);
        contadorID++;
        this.idTransaccion = "TRX-" + contadorID;
    }

    public double getMonto() {
        return monto;
    }

    public void getInfoTransaccion(){
        System.out.println("Tipo: " + this.getClass().getSimpleName());
        System.out.println("ID Transacción: " + this.idTransaccion);
        System.out.println("Fecha: " + fechaFormateada);
        System.out.println("Monto: $" + this.monto);
        if (this.usuario != null) {
            System.out.println("Realizado por: " + this.usuario.getNombre() + "\n");
        }
    }

    public void validarTransaccion(){ //va a ser aplicado polimorfismo en cada transaccion
    }

    //public abstract boolean esIngreso(); // va a ser aplicada polimorfismo

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getFecha() {return fechaHora;}
    }


