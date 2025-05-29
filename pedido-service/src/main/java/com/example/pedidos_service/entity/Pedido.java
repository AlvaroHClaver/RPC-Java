package com.example.pedidos_service.entity;


import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pedido {

    private Integer id;
    private Cliente cliente;
    private LocalDateTime dataCriacao;
    private String status;
    private Double valorTotal;
    private List<ItemPedido> itens = new ArrayList<>();


    public Pedido(Integer id, Cliente cliente, LocalDateTime dataCriacao, String status, Double valorTotal) {
        this.id = id;
        this.cliente = cliente;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.valorTotal = valorTotal;
        this.itens = new ArrayList<>();
    }

}
