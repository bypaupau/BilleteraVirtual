package org.example.billeteravirtual.agentes;

import org.example.billeteravirtual.excepciones.*;
import org.example.billeteravirtual.repositorios.RepositorioUsuarios;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Clase utilitaria encargada de centralizar todas las validaciones lógicas y de formato del sistema.
 * Contiene métodos estáticos para verificar correos, cédulas, alias, montos y saldos.
 */
public class Validador {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final String ALIAS_REGEX = "^[a-zA-Z0-9._]{5,15}$";
    private static final String CEDULA_REGEX = "^[0-9]{10}$";
    private static final String NOMBRE_REGEX = "^[A-Za-zÁÉÍÓÚáéíóúñÑüÜ\\s]+$";

    private static final List<String> DOMINIOS_PERMITIDOS = List.of(
            "gmail.com",
            "hotmail.com",
            "outlook.com",
            "yahoo.com",
            "icloud.com",
            "epn.edu.ec"
    );

    /**
     * Constructor privado para evitar instanciación de esta clase utilitaria.
     */
    public Validador() {
    }

    /**
     * Valida que el correo electrónico tenga un formato correcto y pertenezca a un dominio permitido.
     *
     * @param correo Dirección de correo a verificar.
     * @throws EmailNoValidoException Si el formato es incorrecto o el dominio no está en la lista blanca.
     */
    public static void validarCorreo(String correo) throws EmailNoValidoException {
        if (correo == null || correo.trim().isEmpty()) {
            throw new EmailNoValidoException("El correo es obligatorio");
        }

        // Paso 1: Validar formato general (que tenga @ y punto)
        if (!Pattern.matches(EMAIL_REGEX, correo)) {
            throw new EmailNoValidoException("El correo no tiene un formato válido (ej: usuario@dominio.com).");
        }


        String dominio = correo.substring(correo.indexOf("@") + 1).toLowerCase();

        if (!DOMINIOS_PERMITIDOS.contains(dominio)) {
            throw new EmailNoValidoException("El dominio '" + dominio + "' no está permitido. Solo aceptamos: " + DOMINIOS_PERMITIDOS);
        }
    }

    /**
     * Valida que el alias cumpla con la longitud y caracteres permitidos.
     *
     * @param alias Nombre de usuario.
     * @throws AliasInvalidoException Si es nulo, vacío o no cumple el patrón regex.
     */
    public static void validarAlias(String alias) throws AliasInvalidoException {
        if (alias == null || alias.trim().isEmpty()) {
            throw new AliasInvalidoException("El alias es obligatorio");
        }
        if (!Pattern.matches(ALIAS_REGEX, alias)) {
            throw new AliasInvalidoException("El alias no es válido (mínimo 5 caracteres).");
        }
    }

    /**
     * Verifica que la cédula tenga exactamente 10 dígitos numéricos.
     *
     * @param cedula Número de identificación.
     * @throws CedulaInvalidaException Si es nula o no cumple el patrón de 10 dígitos.
     */
    public static void validarCedula(String cedula) throws CedulaInvalidaException {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new CedulaInvalidaException("La cédula es obligatoria");
        }
        if (!Pattern.matches(CEDULA_REGEX, cedula)) {
            throw new CedulaInvalidaException("La cédula debe tener 10 dígitos numéricos.");
        }
    }

    /**
     * Valida que un nombre contenga solo letras y espacios.
     *
     * @param nombre Nombre a validar.
     * @throws IllegalArgumentException Si el nombre está vacío o contiene caracteres inválidos.
     */
    public static void validarNombreCampo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (!Pattern.matches(NOMBRE_REGEX, nombre)) {
            throw new IllegalArgumentException("El nombre contiene caracteres inválidos.");
        }
    }

    /**
     * Valida que la ciudad contenga solo letras.
     *
     * @param nombre Nombre de la ciudad.
     * @throws IllegalArgumentException Si la ciudad contiene números o símbolos.
     */
    public static void validarCiudad(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad es obligatoria");
        }
        // Mejor usar Regex aquí también para ser consistentes
        if (!Pattern.matches("^[A-Za-zÁÉÍÓÚáéíóúñÑüÜ\\s]+$", nombre)) {
            throw new IllegalArgumentException("La ciudad no debe contener números ni símbolos.");
        }
    }

    /**
     * Verifica si un usuario tiene saldo suficiente para realizar una operación.
     *
     * @param usuario Usuario que intenta realizar la transacción.
     * @param monto Cantidad a descontar.
     * @throws SaldoInsuficienteException Si el saldo en la billetera es menor al monto.
     */
    public static void validarTransaccion(Usuario usuario, double monto) throws SaldoInsuficienteException {
        if (usuario.getBilletera().getSaldo() < monto) {
            throw new SaldoInsuficienteException("Saldo insuficiente para la transacción");
        }
    }

    /**
     * Verifica que el alias no esté registrado previamente en el sistema.
     *
     * @param alias Alias a verificar.
     * @throws CredencialYaExistenteException Si el alias ya se encuentra en el repositorio.
     */
    public static void validarUsuarioExistente(String alias) throws CredencialYaExistenteException {
        if (RepositorioUsuarios.existeAlias(alias)) {
            throw new CredencialYaExistenteException("El alias " + alias + " ya está en uso.");
        }
    }

    /**
     * Asegura que el monto de una operación sea positivo.
     *
     * @param monto Cantidad monetaria.
     * @throws MontoInvalidoException Si el monto es menor o igual a cero.
     */
    public static void validarMonto(double monto) throws MontoInvalidoException {
        if (monto <= 0) {
            throw new MontoInvalidoException("El monto no puede ser negativo ni cero.");
        }
    }

    /**
     * Verifica que la cédula no esté ya registrada asociada a otro usuario.
     *
     * @param usuariosRegistrados Mapa actual de usuarios.
     * @param nuevoUsuario Objeto usuario que se intenta registrar.
     * @throws CredencialYaExistenteException Si la cédula ya existe en el mapa.
     */
    public static void validarCedulaNoRegistrada(Map<String, Usuario> usuariosRegistrados, Usuario nuevoUsuario) {
        if (usuariosRegistrados.containsKey(nuevoUsuario.getCedula())) {
            throw new CredencialYaExistenteException("La cédula " + nuevoUsuario.getCedula() + " ya está registrada.");
        }
    }


    private static final Map<String, String> SERVICIOS_DISPONIBLES = Map.of(
            "agua", "Interagua",
            "luz", "CNEL",
            "internet", "Netlife"
    );

    /**
     * Verifica que el correo no esté ya asociado a otro usuario.
     *
     * @param correo String actual del correo del usuario.
     * @throws CredencialYaExistenteException si el correo ya existe en el mapa.
     */
    public static void validarCorreoNoRegistrado(String correo) {
        for(Usuario u: RepositorioUsuarios.obtenerTodos()) {
            if (Objects.equals(u.getEmail(), correo)) {
                throw new CredencialYaExistenteException("El correo " + correo + " ya está registrado.");
            }
        }
    }

    public static String validarServicio(String servicio) {
        if (servicio == null) {
            throw new IllegalArgumentException("El servicio no puede estar vacío.");
        }
        String servicioNormalizado = servicio.toLowerCase().trim();

        if (!SERVICIOS_DISPONIBLES.containsKey(servicioNormalizado)) {
            throw new IllegalArgumentException("Servicio no disponible. Elija entre: " + SERVICIOS_DISPONIBLES.keySet());
        }

        return SERVICIOS_DISPONIBLES.get(servicioNormalizado);
    }




}