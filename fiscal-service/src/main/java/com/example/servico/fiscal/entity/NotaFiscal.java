package com.example.servico.fiscal.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotaFiscal {

    private Integer id;
    private Integer pedidoId;
    private String numero;
    private LocalDateTime dataEmissao;
    private String chaveAcesso;

    public NotaFiscal(Integer pedidoId,
                      String numero,
                      LocalDateTime dataEmissao,
                      String chaveAcesso) {
        this.pedidoId = pedidoId;
        this.numero = numero;
        this.dataEmissao = dataEmissao;
        this.chaveAcesso = chaveAcesso;
    }
}
