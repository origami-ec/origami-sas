package org.ibarra.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TareaVariable {

    private String usuario;
    private Integer tareasAsignadas;
    private Integer tareasCandidatas;
    private String taskId;
    private String nombreVariable;
    private String valorVariable;


    public TareaVariable() {
    }

}

