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

    /**
     * Crea un nuevo usuario validando todos sus campos obligatorios.
     * Al crearse, se le asigna automáticamente una Billetera vacía.
     *
     * @param cedula Identificación única del usuario (10 dígitos).
     * @param nombre Nombre completo (solo letras y espacios).
     * @param ciudad Ciudad de residencia.
     * @param alias Nombre de usuario único para el sistema.
     * @param email Correo electrónico válido.
     * @throws IllegalArgumentException Si algún formato (nombre, ciudad) es inválido.
     * @throws RuntimeException Si las validaciones de negocio fallan (ej: email inválido).
     */
    public Usuario(String cedula, LocalDate fechaNacimiento, String nombre, String ciudad, String alias, String email) {
        //Zona de validaciones
        Validador.validarCedula(cedula);
        Validador.validarNombreCampo(nombre);
        Validador.validarAlias(alias);
        Validador.validarCorreo(email);

        this.cedula = cedula;
        this.fechaNacimiento = fechaNacimiento;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.alias = alias;
        this.email = email;
        this.billetera = new Billetera();
    }

    public Usuario(String cedula, String nombre, String ciudad, String alias, String email) {

        Validador.validarCedula(cedula);
        Validador.validarNombreCampo(nombre);
        Validador.validarAlias(alias);
        Validador.validarCorreo(email);
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