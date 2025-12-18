package org.example.billeteravirtual.agentes;

import org.example.billeteravirtual.excepciones.*;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.util.Map;
import java.util.regex.Pattern;

//Clase con el objetivo de agrupar todos los metodos validadores de datos
public class Validador {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String ALIAS_REGEX = "^[a-zA-Z0-9._]{5,15}$";
    private static final String CEDULA_REGEX = "^[0-9]{10}$";
    private static final String NOMBRE_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúñÑüÜ\\s]+$";

    public Validador() {}

        public static void validarCedula(String cedula) {
            if (cedula == null || cedula.length() != 10) {
                throw new CedulaInvalidaException("La cédula debe tener exactamente 10 dígitos.");
            }
            if (!cedula.matches("\\d+")) { // Solo números
                throw new CedulaInvalidaException("La cédula solo puede contener números.");
            }
        }

        public static void validarCorreo(String email) {
            if (email == null || !email.contains("@")) {
                throw new EmailNoValidoException("El correo debe contener un '@'.");
            }
            if (!email.contains(".")) {
                throw new EmailNoValidoException("El correo debe tener un dominio (ej: .com).");
            }
            if (email.length() < 5) {
                throw new EmailNoValidoException("El correo es demasiado corto.");
            }
        }

        public static void validarAlias(String alias) {
            if (alias == null || alias.length() < 3) {
                throw new AliasInvalidoException("El alias debe tener al menos 3 caracteres.");
            }
            // Ejemplo de regla específica que pediste:
            long numeros = alias.chars().filter(Character::isDigit).count();
            if (numeros < 2) {
                throw new AliasInvalidoException("Tu alias (" + alias + ") es muy débil. Agrega al menos 2 números.");
            }
        }

        public static void validarNombreCampo(String nombre) {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new RuntimeException("El nombre es obligatorio.");
            }
        }

    public static void validarCiudad(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Es obligatorio registrar la ciudad");
        }
        for (char c : nombre.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new IllegalArgumentException("No se permiten numeros en la ciudad"); // Found a digit
            }
        }
    }


    //metodo que valida la opcion insertada, la cual debe ser acorde a la condición
    public static void validarOpcion(int opcion, int cantidadOpciones) throws OpcionMenuNoValidoException {
        if (opcion < 1 || opcion > cantidadOpciones) {
            throw new OpcionMenuNoValidoException("Opción inválida. Debe ser entre 1 y " + cantidadOpciones);
        }
    }

    //sirve para validar que una transacción sea realizable
    public static void validarTransaccion(Usuario usuario, double monto) throws SaldoInsuficienteException {
        if (usuario.getBilletera().getSaldo() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente para la transacción");
        }
    }

    //Aquí va el metodo para validar a un usuario existente
    public static void validarUsuarioExistente(String alias) throws CredencialYaExistenteException {
        if (RepositorioUsuarios.existeAlias(alias)) {
            throw new CredencialYaExistenteException("El alias " + alias + " ya está en uso.");
        }
    }

    //Esta metodo sirve para validar que el monto no sea menor a 0
    public static void validarMonto(double monto) throws MontoInvalidoException {
        if (monto <= 0) {
            throw new MontoInvalidoException("El monto no puede ser negativo, ni cero.");
        }

    }

    public static void validarCedulaNoRegistrada (Map<String,Usuario> usuariosRegistrados, Usuario nuevoUsuario){
        if (usuariosRegistrados.containsKey(nuevoUsuario.getCedula())) {
            throw new CredencialYaExistenteException("La cédula " + nuevoUsuario.getCedula() + " ya está registrada.");
        }
    }

}