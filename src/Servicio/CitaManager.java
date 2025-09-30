package Servicio;

import Entidades.Cita;
import Entidades.Medico;
import Entidades.Paciente;
import Entidades.Sala;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [HU-07] Programación de Citas
 * [HU-08] Validación de Disponibilidad
 * [HU-09] Validación de Especialidades
 * [HU-10] Consulta de Citas
 * [HU-11] Persistencia de Datos
 *
 * Gestor central del sistema de citas médicas.
 * Implementa la lógica de negocio para programación, validación y consulta de
 * citas.
 *
 * Criterios de Aceptación:
 * - [HU-07] Programar citas vinculando paciente, médico y sala
 * - [HU-08] Validar disponibilidad (2 horas mínimo entre citas)
 * - [HU-09] Validar compatibilidad de especialidades
 * - [HU-10] Consultas rápidas por paciente, médico o sala (O(1))
 * - [HU-11] Guardar/cargar citas en formato CSV
 *
 * Reglas de Negocio:
 * - RN-07.1: No programar citas en el pasado
 * - RN-07.2: Costo debe ser positivo
 * - RN-08.1: Ventana de 2 horas entre citas del mismo médico
 * - RN-08.2: Ventana de 2 horas entre citas de la misma sala
 * - RN-09.1: Especialidad del médico debe coincidir con departamento de sala
 * - RN-10.1: Consultas deben usar índices para eficiencia
 * - RN-11.1: Formato CSV estándar y predefinido
 *
 * @see CitaService
 * @see Cita
 * @see CitaException
 */
public class CitaManager implements CitaService {
    private final List<Cita> citas = new ArrayList<>();
    private final Map<Paciente, List<Cita>> citasPorPaciente = new ConcurrentHashMap<>();
    private final Map<Medico, List<Cita>> citasPorMedico = new ConcurrentHashMap<>();
    private final Map<Sala, List<Cita>> citasPorSala = new ConcurrentHashMap<>();

    /**
     * [HU-07] Programa una nueva cita médica.
     * [HU-08] Valida disponibilidad de médico y sala.
     * [HU-09] Valida compatibilidad de especialidades.
     *
     * @param paciente  El paciente que solicita la cita
     * @param medico    El médico que atenderá la cita
     * @param sala      La sala donde se realizará la cita
     * @param fechaHora Fecha y hora programada
     * @param costo     Costo de la consulta
     * @return La cita creada y registrada
     * @throws CitaException Si alguna validación falla
     */
    @Override
    public Cita programarCita(Paciente paciente, Medico medico, Sala sala,
            LocalDateTime fechaHora, BigDecimal costo) throws CitaException {

        // [HU-07] RN-07.1 y RN-07.2: Validar fecha futura y costo positivo
        validarCita(fechaHora, costo);

        // [HU-08] RN-08.1: Validar disponibilidad del médico (ventana 2 horas)
        if (!esMedicoDisponible(medico, fechaHora)) {
            throw new CitaException("El médico no está disponible en la fecha y hora solicitadas.");
        }

        // [HU-08] RN-08.2: Validar disponibilidad de la sala (ventana 2 horas)
        if (!esSalaDisponible(sala, fechaHora)) {
            throw new CitaException("La sala no está disponible en la fecha y hora solicitadas.");
        }

        // [HU-09] RN-09.1: Validar compatibilidad de especialidades
        if (!medico.getEspecialidad().equals(sala.getDepartamento().getEspecialidad())) {
            throw new CitaException("La especialidad del médico no coincide con el departamento de la sala.");
        }

        Cita cita = Cita.builder()
                .paciente(paciente)
                .medico(medico)
                .sala(sala)
                .fechaHora(fechaHora)
                .costo(costo)
                .build();
        citas.add(cita);

        actualizarIndicePaciente(paciente, cita);
        actualizarIndiceMedico(medico, cita);
        actualizarIndiceSala(sala, cita);

        paciente.addCita(cita);
        medico.addCita(cita);
        sala.addCita(cita);

        return cita;
    }

    private void validarCita(LocalDateTime fechaHora, BigDecimal costo) throws CitaException {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new CitaException("No se puede programar una cita en el pasado.");
        }

        if (costo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CitaException("El costo debe ser mayor que cero.");
        }
    }

    private boolean esMedicoDisponible(Medico medico, LocalDateTime fechaHora) {
        List<Cita> citasExistentes = citasPorMedico.get(medico);
        if (citasExistentes != null) {
            for (Cita citaExistente : citasExistentes) {
                if (Math.abs(citaExistente.getFechaHora().compareTo(fechaHora)) < 2) { // 2 horas de diferencia
                    return false;
                }
            }
        }
        return true;
    }

    private boolean esSalaDisponible(Sala sala, LocalDateTime fechaHora) {
        List<Cita> citasExistentes = citasPorSala.get(sala);
        if (citasExistentes != null) {
            for (Cita citaExistente : citasExistentes) {
                if (Math.abs(citaExistente.getFechaHora().compareTo(fechaHora)) < 2) { // 2 horas de diferencia
                    return false;
                }
            }
        }
        return true;
    }

    private void actualizarIndicePaciente(Paciente paciente, Cita cita) {
        List<Cita> citasPaciente = citasPorPaciente.get(paciente);
        if (citasPaciente == null) {
            citasPaciente = new ArrayList<>();
            citasPorPaciente.put(paciente, citasPaciente);
        }
        citasPaciente.add(cita);
    }

    private void actualizarIndiceMedico(Medico medico, Cita cita) {
        List<Cita> citasMedico = citasPorMedico.get(medico);
        if (citasMedico == null) {
            citasMedico = new ArrayList<>();
            citasPorMedico.put(medico, citasMedico);
        }
        citasMedico.add(cita);
    }

    private void actualizarIndiceSala(Sala sala, Cita cita) {
        List<Cita> citasSala = citasPorSala.get(sala);
        if (citasSala == null) {
            citasSala = new ArrayList<>();
            citasPorSala.put(sala, citasSala);
        }
        citasSala.add(cita);
    }

    @Override
    public List<Cita> getCitasPorPaciente(Paciente paciente) {
        List<Cita> citasPaciente = citasPorPaciente.get(paciente);
        if (citasPaciente != null) {
            return Collections.unmodifiableList(citasPaciente);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Cita> getCitasPorMedico(Medico medico) {
        List<Cita> citasMedico = citasPorMedico.get(medico);
        if (citasMedico != null) {
            return Collections.unmodifiableList(citasMedico);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Cita> getCitasPorSala(Sala sala) {
        List<Cita> citasSala = citasPorSala.get(sala);
        if (citasSala != null) {
            return Collections.unmodifiableList(citasSala);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void guardarCitas(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Cita cita : citas) {
                writer.println(cita.toCsvString());
            }
        }
    }

    @Override
    public void cargarCitas(String filename, Map<String, Paciente> pacientes,
            Map<String, Medico> medicos, Map<String, Sala> salas)
            throws IOException, ClassNotFoundException, CitaException {
        citas.clear();
        citasPorPaciente.clear();
        citasPorMedico.clear();
        citasPorSala.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Cita cita = Cita.fromCsvString(line, pacientes, medicos, salas);
                    citas.add(cita);
                    actualizarIndicePaciente(cita.getPaciente(), cita);
                    actualizarIndiceMedico(cita.getMedico(), cita);
                    actualizarIndiceSala(cita.getSala(), cita);
                } catch (CitaException e) {
                    System.err.println("Error al cargar cita desde CSV: " + line + " - " + e.getMessage());
                    throw e;
                }
            }
        }
    }
}