package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class Persona implements Serializable {
    @NonNull
    final String nombre;
    @NonNull
    final String apellido;
    @EqualsAndHashCode.Include
    @NonNull
    final String dni;
    @NonNull
    final LocalDate fechaNacimiento;
    @NonNull
    final TipoSangre tipoSangre;

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public int getEdad() {
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }
}