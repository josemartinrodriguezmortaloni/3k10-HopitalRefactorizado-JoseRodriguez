package Entidades;

import lombok.*;
import lombok.experimental.FieldDefaults;
import Servicio.CitaException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@ToString(exclude = { "paciente", "medico", "sala" })
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cita implements Serializable {
    @NonNull
    final Paciente paciente;
    @NonNull
    final Medico medico;
    @NonNull
    final Sala sala;
    @NonNull
    final LocalDateTime fechaHora;
    @NonNull
    final BigDecimal costo;
    @Setter
    @Builder.Default
    EstadoCita estado = EstadoCita.PROGRAMADA;
    @Setter
    @Builder.Default
    String observaciones = "";

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                paciente.getDni(),
                medico.getDni(),
                sala.getNumero(),
                fechaHora.toString(),
                costo.toString(),
                estado.name(),
                observaciones.replaceAll(",", ";"));
    }

    public static Cita fromCsvString(String csvString,
            Map<String, Paciente> pacientes,
            Map<String, Medico> medicos,
            Map<String, Sala> salas) throws CitaException {
        String[] values = csvString.split(",");
        if (values.length != 7) {
            throw new CitaException("Formato de CSV inválido para Cita: " + csvString);
        }

        String dniPaciente = values[0];
        String dniMedico = values[1];
        String numeroSala = values[2];
        LocalDateTime fechaHora = LocalDateTime.parse(values[3]);
        BigDecimal costo = new BigDecimal(values[4]);
        EstadoCita estado = EstadoCita.valueOf(values[5]);
        String observaciones = values[6].replaceAll(";", ",");

        Paciente paciente = pacientes.get(dniPaciente);
        Medico medico = medicos.get(dniMedico);
        Sala sala = salas.get(numeroSala);

        if (paciente == null) {
            throw new CitaException("Paciente no encontrado: " + dniPaciente);
        }
        if (medico == null) {
            throw new CitaException("Médico no encontrado: " + dniMedico);
        }
        if (sala == null) {
            throw new CitaException("Sala no encontrada: " + numeroSala);
        }

        return Cita.builder()
                .paciente(paciente)
                .medico(medico)
                .sala(sala)
                .fechaHora(fechaHora)
                .costo(costo)
                .estado(estado)
                .observaciones(observaciones)
                .build();
    }
}
