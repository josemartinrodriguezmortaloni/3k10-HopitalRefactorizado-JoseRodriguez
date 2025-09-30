package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@Getter
@SuperBuilder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Persona implements Serializable {
    @EqualsAndHashCode.Include
    String dni;
    String nombre;
    String apellido;
    @NonNull LocalDate fechaNacimiento;
    @NonNull TipoSangre tipoSangre;

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
