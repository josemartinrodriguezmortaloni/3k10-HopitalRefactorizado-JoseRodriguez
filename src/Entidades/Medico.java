package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true, exclude = {"citas", "departamento"})
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medico extends Persona implements Serializable {
    Matricula matricula;
    @NonNull EspecialidadMedica especialidad;
    Departamento departamento;

    @Builder.Default
    List<Cita> citas = new ArrayList<>();

    // Constructor para crear Medico con numeroMatricula
    public Medico(String nombre, String apellido, String dni, LocalDate fechaNacimiento,
            TipoSangre tipoSangre, String numeroMatricula, EspecialidadMedica especialidad) {
        super(nombre, apellido, dni, fechaNacimiento, tipoSangre);
        this.matricula = new Matricula(numeroMatricula);
        this.especialidad = especialidad;
        this.citas = new ArrayList<>();
    }

    // Constructor privado usado por @SuperBuilder
    @SuperBuilder
    private Medico(String dni, String nombre, String apellido, LocalDate fechaNacimiento, TipoSangre tipoSangre,
                   Matricula matricula, EspecialidadMedica especialidad, Departamento departamento, List<Cita> citas) {
        super(nombre, apellido, dni, fechaNacimiento, tipoSangre);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.departamento = departamento;
        this.citas = citas != null ? citas : new ArrayList<>();
    }

    public void setDepartamento(Departamento departamento) {
        if (this.departamento != departamento) {
            this.departamento = departamento;
        }
    }

    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }
}
