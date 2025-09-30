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
@ToString(callSuper = true, exclude = {"citas", "hospital", "historiaClinica"})
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Paciente extends Persona implements Serializable {
    HistoriaClinica historiaClinica;
    String telefono;
    String direccion;
    Hospital hospital;

    @Builder.Default
    List<Cita> citas = new ArrayList<>();

    // Constructor para crear Paciente con nueva HistoriaClinica
    public Paciente(String nombre, String apellido, String dni, LocalDate fechaNacimiento,
            TipoSangre tipoSangre, String telefono, String direccion) {
        super(nombre, apellido, dni, fechaNacimiento, tipoSangre);
        this.telefono = ValidationUtils.validarStringNoVacio(telefono, "El teléfono no puede ser nulo ni vacío");
        this.direccion = ValidationUtils.validarStringNoVacio(direccion, "La dirección no puede ser nula ni vacía");
        this.historiaClinica = new HistoriaClinica(this);
        this.citas = new ArrayList<>();
    }

    // Constructor privado usado por @SuperBuilder
    @SuperBuilder
    private Paciente(String dni, String nombre, String apellido, LocalDate fechaNacimiento, TipoSangre tipoSangre,
                     HistoriaClinica historiaClinica, String telefono, String direccion, Hospital hospital, List<Cita> citas) {
        super(nombre, apellido, dni, fechaNacimiento, tipoSangre);
        this.historiaClinica = historiaClinica;
        this.telefono = telefono;
        this.direccion = direccion;
        this.hospital = hospital;
        this.citas = citas != null ? citas : new ArrayList<>();
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
