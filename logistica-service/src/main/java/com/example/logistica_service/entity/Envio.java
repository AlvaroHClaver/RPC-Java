package com.example.logistica_service.entity;

import lombok.*;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Envio {

    private Integer id;
    private Long pedidoId;
    private Long notaFiscalId;
    private LocalDateTime dataDespacho;
    private String codigoRastreamento;
    private String status;


    public Envio(Long pedidoId, Long notaFiscalId, LocalDateTime dataDespacho, String codigoRastreamento, String status) {
        this.pedidoId = pedidoId;
        this.notaFiscalId = notaFiscalId;
        this.dataDespacho = dataDespacho;
        this.codigoRastreamento = codigoRastreamento;
        this.status = status;
    }
}
