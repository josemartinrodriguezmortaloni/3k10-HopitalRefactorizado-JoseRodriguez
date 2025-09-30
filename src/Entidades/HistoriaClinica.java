package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString(exclude = { "paciente" })
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoriaClinica implements Serializable {
    @NonNull
    final Paciente paciente;
    @Builder.Default
    final LocalDateTime fechaCreacion = LocalDateTime.now();
    @Builder.Default
    final List<String> diagnosticos = new ArrayList<>();
    @Builder.Default
    final List<String> tratamientos = new ArrayList<>();
    @Builder.Default
    final List<String> alergias = new ArrayList<>();

    public String getNumeroHistoria() {
        return "HC-" + paciente.getDni() + "-" + fechaCreacion.getYear();
    }

    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico != null && !diagnostico.trim().isEmpty()) {
            diagnosticos.add(diagnostico);
        }
    }

    public void agregarTratamiento(String tratamiento) {
        if (tratamiento != null && !tratamiento.trim().isEmpty()) {
            tratamientos.add(tratamiento);
        }
    }

    public void agregarAlergia(String alergia) {
        if (alergia != null && !alergia.trim().isEmpty()) {
            alergias.add(alergia);
        }
    }

    public List<String> getDiagnosticos() {
        return Collections.unmodifiableList(diagnosticos);
    }

    public List<String> getTratamientos() {
        return Collections.unmodifiableList(tratamientos);
    }

    public List<String> getAlergias() {
        return Collections.unmodifiableList(alergias);
    }
}