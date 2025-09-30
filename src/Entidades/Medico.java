package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString(callSuper = true, exclude = { "citas", "departamento" })
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medico extends Persona implements Serializable {
    @NonNull
    final Matricula matricula;
    @NonNull
    final EspecialidadMedica especialidad;
    @Setter
    Departamento departamento;
    @Builder.Default
    List<Cita> citas = new ArrayList<>();

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }
}