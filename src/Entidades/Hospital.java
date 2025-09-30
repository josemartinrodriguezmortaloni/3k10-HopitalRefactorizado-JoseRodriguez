package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString(exclude = { "departamentos", "pacientes" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Hospital implements Serializable {
    @EqualsAndHashCode.Include
    @NonNull
    final String nombre;
    @NonNull
    final String direccion;
    @NonNull
    final String telefono;
    @Builder.Default
    List<Departamento> departamentos = new ArrayList<>();
    @Builder.Default
    List<Paciente> pacientes = new ArrayList<>();

    /**
     * [HU-01] Agrega un departamento al hospital.
     * [HU-02] Establece relación bidireccional Hospital-Departamento.
     *
     * @param departamento El departamento a agregar
     */
    public void agregarDepartamento(Departamento departamento) {
        if (departamento != null && !departamentos.contains(departamento)) {
            departamentos.add(departamento);
            departamento.setHospital(this);
        }
    }

    /**
     * [HU-01] Registra un paciente en el hospital.
     * [HU-04] Establece relación bidireccional Hospital-Paciente.
     *
     * @param paciente El paciente a registrar
     */
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