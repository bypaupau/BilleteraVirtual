package org.example.billeteravirtual.agentes;

import org.example.billeteravirtual.excepciones.*;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Validador {

    // Mantenemos el Regex para validar que la estructura sea correcta (letras @ algo. algo)
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final String ALIAS_REGEX = "^[a-zA-Z0-9._]{5,15}$";
    private static final String CEDULA_REGEX = "^[0-9]{10}$";
    private static final String NOMBRE_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúñÑüÜ\\s]+$";

    // 1. NUEVA LISTA: Aquí defines qué dominios aceptas. ¡Agrega los que necesites!
    private static final List<String> DOMINIOS_PERMITIDOS = List.of(
            "gmail.com",
            "hotmail.com",
            "outlook.com",
            "yahoo.com",
            "icloud.com",
            "epn.edu.ec" // Ejemplo: puedes agregar dominios institucionales
    );

    public Validador() {
    }

    // Método modificado
    public static void validarCorreo(String correo) throws EmailNoValidoException {
        if (correo == null || correo.trim().isEmpty()) {
            throw new EmailNoValidoException("El correo es obligatorio");
        }

        // Paso 1: Validar formato general (que tenga @ y punto)
        if (!Pattern.matches(EMAIL_REGEX, correo)) {
            throw new EmailNoValidoException("El correo no tiene un formato válido (ej: usuario@dominio.com).");
        }

        // Paso 2: Validar dominio permitido
        // Extraemos lo que está después del '@'
        String dominio = correo.substring(correo.indexOf("@") + 1).toLowerCase();

        if (!DOMINIOS_PERMITIDOS.contains(dominio)) {
            throw new EmailNoValidoException("El dominio '" + dominio + "' no está permitido. Solo aceptamos: " + DOMINIOS_PERMITIDOS);
        }
    }

    // ... (El resto de tus métodos siguen igual) ...

    public static void validarAlias(String alias) throws AliasInvalidoException {
        if (alias == null || alias.trim().isEmpty()) {
            throw new AliasInvalidoException("El alias es obligatorio");
        }
        if (!Pattern.matches(ALIAS_REGEX, alias)) {
            throw new AliasInvalidoException("El alias no es válido (mínimo 5 caracteres).");
        }
    }

    public static void validarCedula(String cedula) throws CedulaInvalidaException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new CedulaInvalidaException("La cédula es obligatoria");
        }
        if (!Pattern.matches(CEDULA_REGEX, cedula)) {
            throw new CedulaInvalidaException("La cédula debe tener 10 dígitos numéricos.");
        }
    }

    public static void validarNombreCampo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (!Pattern.matches(NOMBRE_REGEX, nombre)) {
            throw new IllegalArgumentException("El nombre contiene caracteres inválidos.");
        }
    }

    public static void validarCiudad(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es obligatoria");
        }
        // Mejor usar Regex aquí también para ser consistentes
        if (!Pattern.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑüÜ\\s]+$", nombre)) {
            throw new IllegalArgumentException("La ciudad no debe contener números ni símbolos.");
        }
    }

    public static void validarOpcion(int opcion, int cantidadOpciones) throws OpcionMenuNoValidoException {
        if (opcion < 1 || opcion > cantidadOpciones) {
            throw new OpcionMenuNoValidoException("Opción inválida. Debe ser entre 1 y " + cantidadOpciones);
        }
    }

    public static void validarTransaccion(Usuario usuario, double monto) throws SaldoInsuficienteException {
        if (usuario.getBilletera().getSaldo() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente para la transacción");
        }
    }

    public static void validarUsuarioExistente(String alias) throws CredencialYaExistenteException {
        if (RepositorioUsuarios.existeAlias(alias)) {
            throw new CredencialYaExistenteException("El alias " + alias + " ya está en uso.");
        }
    }

    public static void validarMonto(double monto) throws MontoInvalidoException {
        if (monto <= 0) {
            throw new MontoInvalidoException("El monto no puede ser negativo ni cero.");
        }
    }

    public static void validarCedulaNoRegistrada(Map<String, Usuario> usuariosRegistrados, Usuario nuevoUsuario) {
        if (usuariosRegistrados.containsKey(nuevoUsuario.getCedula())) {
            throw new CredencialYaExistenteException("La cédula " + nuevoUsuario.getCedula() + " ya está registrada.");
        }
    }
}