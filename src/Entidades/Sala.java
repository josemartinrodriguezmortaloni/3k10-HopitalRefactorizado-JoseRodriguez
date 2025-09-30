package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString(exclude = { "citas" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Sala implements Serializable {
    @EqualsAndHashCode.Include
    @NonNull
    final String numero;
    @NonNull
    final String tipo;
    @NonNull
    final Departamento departamento;
    @Builder.Default
    List<Cita> citas = new ArrayList<>();

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }
}