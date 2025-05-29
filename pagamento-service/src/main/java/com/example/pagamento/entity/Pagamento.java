package com.example.pagamento.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pagamento {

    private Integer id;
    private Integer pedidoId;
    private LocalDateTime dataProcessamento;
    private String status;
    private String metodo;



    public Pagamento(Integer pedidoId,
                     LocalDateTime dataProcessamento,
                     String status,
                     String metodo) {
        this.pedidoId = pedidoId;
        this.dataProcessamento = dataProcessamento;
        this.status = status;
        this.metodo = metodo;
    }

}
