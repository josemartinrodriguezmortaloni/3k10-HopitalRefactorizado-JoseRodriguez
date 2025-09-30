package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@ToString(exclude = {"medicos", "salas", "hospital"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Departamento implements Serializable {
    @EqualsAndHashCode.Include
    final String nombre;
    @NonNull final EspecialidadMedica especialidad;
    Hospital hospital;

    @Builder.Default
    final List<Medico> medicos = new ArrayList<>();

    @Builder.Default
    final List<Sala> salas = new ArrayList<>();

    @Builder
    private Departamento(String nombre, EspecialidadMedica especialidad, Hospital hospital, List<Medico> medicos, List<Sala> salas) {
        this.nombre = ValidationUtils.validarStringNoVacio(nombre, "El nombre del departamento no puede ser nulo ni vacío");
        this.especialidad = especialidad;
        this.hospital = hospital;
        this.medicos = medicos != null ? medicos : new ArrayList<>();
        this.salas = salas != null ? salas : new ArrayList<>();
    }

    public void setHospital(Hospital hospital) {
        if (this.hospital != hospital) {
            if (this.hospital != null) {
                this.hospital.getInternalDepartamentos().remove(this);
            }
            this.hospital = hospital;
            if (hospital != null) {
                hospital.getInternalDepartamentos().add(this);
            }
        }
    }

    public void agregarMedico(Medico medico) {
        if (medico != null && !medicos.contains(medico)) {
            medicos.add(medico);
            medico.setDepartamento(this);
        }
    }

    public Sala crearSala(String numero, String tipo) {
        Sala sala = new Sala(numero, tipo, this);
        salas.add(sala);
        return sala;
    }

    public List<Medico> getMedicos() {
        return Collections.unmodifiableList(medicos);
    }

    public List<Sala> getSalas() {
        return Collections.unmodifiableList(salas);
    }
}
