package org.sas.zull.util;

public class Constantes {

    public static String ACTIVO = "ACTIVO";
    public static String FALLIDO = "FALLIDO";
    public static String CORRECTO = "CORRECTO";

    public static Integer INTENTOS_SESION_DEFAULT = 3;
    public static String localhost = "http://localhost:";

    public static String usuarioNoEncontrado = "Usuario no encontrado";
    public static String usuarioBloqueado = "El usuario se encuentra bloqueado.";
    public static String usuarioFallido = "Lleva %s intentos, al %s intento deberá pedir un cambio de clave en SGTI.";
    public static String usuarioActivo = "El usuario se encuentra activo.";
    public static String usuarioADCredencialError = "Usuario o contraseña incorrecto.";
    public static String usuarioADNoEncontrado = "El usuario %s no se encuentra vinculado a un funcionario.";

    /**
     * Variables de la tabla valores
     */

    public static String INTENTOS_SESION = "NUM_INTENTOS_ACCESO";
    public static String USUARIO_ACTIVO = "ESTADO_USUARIO_ACTIVO";
    public static String USUARIO_INACTIVO = "ESTADO_USUARIO_INACTIVO";
    public static String USUARIO_PENDIENTE = "ESTADO_USUARIO_PENDIENTE";
    public static String USUARIO_BLOQUEADO = "ESTADO_USUARIO_BLOQUEADO";

}
