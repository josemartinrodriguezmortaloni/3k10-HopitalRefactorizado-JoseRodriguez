package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@ToString(exclude = {"departamentos", "pacientes"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Hospital implements Serializable {
    @EqualsAndHashCode.Include
    String nombre;
    String direccion;
    String telefono;

    @Builder.Default
    List<Departamento> departamentos = new ArrayList<>();

    @Builder.Default
    List<Paciente> pacientes = new ArrayList<>();

    @Builder
    private Hospital(String nombre, String direccion, String telefono, List<Departamento> departamentos, List<Paciente> pacientes) {
        this.nombre = ValidationUtils.validarStringNoVacio(nombre, "El nombre del hospital no puede ser nulo ni vacío");
        this.direccion = ValidationUtils.validarStringNoVacio(direccion, "La dirección no puede ser nula ni vacía");
        this.telefono = ValidationUtils.validarStringNoVacio(telefono, "El teléfono no puede ser nulo ni vacío");
        this.departamentos = departamentos != null ? departamentos : new ArrayList<>();
        this.pacientes = pacientes != null ? pacientes : new ArrayList<>();
    }

    public void agregarDepartamento(Departamento departamento) {
        if (departamento != null && !departamentos.contains(departamento)) {
            departamentos.add(departamento);
            departamento.setHospital(this);
        }
    }

    public void agregarPaciente(Paciente paciente) {
        if (paciente != null && !pacientes.contains(paciente)) {
            pacientes.add(paciente);
            paciente.setHospital(this);
        }
    }

    public List<Departamento> getDepartamentos() {
        return Collections.unmodifiableList(departamentos);
    }

    public List<Paciente> getPacientes() {
        return Collections.unmodifiableList(pacientes);
    }

    List<Departamento> getInternalDepartamentos() {
        return departamentos;
    }

    List<Paciente> getInternalPacientes() {
        return pacientes;
    }
}
