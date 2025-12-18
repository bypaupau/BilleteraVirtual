package org.example.billeteravirtual.agentes;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Usuario implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    //Atributos
    private String cedula;
    private LocalDate fechaNacimiento;
    private String nombre;
    private String ciudad;
    private String alias;
    private String email;
    private Billetera billetera;

    //Constructores

    public Usuario(String cedula, LocalDate fechaNacimiento, String nombre, String ciudad, String alias, String email) {


        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.alias = alias;
        this.email = email;
        this.billetera = new Billetera();
    }

    public Usuario(String cedula, String nombre, String ciudad, String alias, String email) {
        Validador.validarNombreCampo(nombre);
        Validador.validarCedula(cedula);
        Validador.validarCorreo(email);
        Validador.validarAlias(alias);
        Validador.validarCiudad(ciudad);

        // Inicialización
        this.cedula = cedula;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.alias = alias;
        this.email = email;
        this.fechaNacimiento = null;
        this.billetera = new Billetera();
    }


    //Getters
    public String getCedula() {
        return cedula;
    }

    public String getAlias() {
        return alias;
    }

    public Billetera getBilletera() {
        return billetera;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {return ciudad;}

    public String getEmail() {return email;}

    public LocalDate getFechaNacimiento() {return fechaNacimiento;}

    @Override
    public String toString() {
        return this.nombre + " | " + this.cedula + " | " + this.alias;
    }
}