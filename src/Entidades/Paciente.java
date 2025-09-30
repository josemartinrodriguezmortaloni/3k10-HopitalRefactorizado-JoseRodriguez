package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString(callSuper = true, exclude = { "citas", "hospital", "historiaClinica" })
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Paciente extends Persona implements Serializable {
    @NonNull
    final String telefono;
    @NonNull
    final String direccion;
    Hospital hospital;
    @Builder.Default
    List<Cita> citas = new ArrayList<>();
    // HistoriaClinica se gestiona externamente o con factory; evitar 'this' en
    // builder
    HistoriaClinica historiaClinica;

    public HistoriaClinica getHistoriaClinica() {
        if (this.historiaClinica == null) {
            this.historiaClinica = HistoriaClinica.builder()
                    .paciente(this)
                    .build();
        }
        return this.historiaClinica;
    }

    public void setHospital(Hospital hospital) {
        if (this.hospital != hospital) {
            if (this.hospital != null) {
                this.hospital.getInternalPacientes().remove(this);
            }
            this.hospital = hospital;
            if (hospital != null) {
                hospital.getInternalPacientes().add(this);
            }
        }
    }

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }
}