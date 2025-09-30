package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@ToString(exclude = {"citas"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Sala implements Serializable {
    @EqualsAndHashCode.Include
    String numero;
    String tipo;
    @NonNull Departamento departamento;

    @Builder.Default
    List<Cita> citas = new ArrayList<>();

    @Builder
    private Sala(String numero, String tipo, Departamento departamento, List<Cita> citas) {
        this.numero = ValidationUtils.validarStringNoVacio(numero, "El número de sala no puede ser nulo ni vacío");
        this.tipo = ValidationUtils.validarStringNoVacio(tipo, "El tipo de sala no puede ser nulo ni vacío");
        this.departamento = departamento;
        this.citas = citas != null ? citas : new ArrayList<>();
    }

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }
}
