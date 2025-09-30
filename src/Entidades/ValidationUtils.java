package Entidades;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils {

    public static String validarStringNoVacio(@NonNull String valor, String mensajeError) {
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }

    public static String validarDni(@NonNull String dni) {
        if (!dni.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 dígitos");
        }
        return dni;
    }

    public static String validarMatricula(@NonNull String numero) {
        if (!numero.matches("MP-\\d{4,6}")) {
            throw new IllegalArgumentException("Formato de matrícula inválido. Debe ser como MP-12345");
        }
        return numero;
    }
}