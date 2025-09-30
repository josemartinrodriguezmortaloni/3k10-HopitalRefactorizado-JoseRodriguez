package Entidades;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Matricula implements Serializable {
    @EqualsAndHashCode.Include
    private final String numero;

    public Matricula(String numero) {
        this.numero = ValidationUtils.validarMatricula(numero);
    }
}
