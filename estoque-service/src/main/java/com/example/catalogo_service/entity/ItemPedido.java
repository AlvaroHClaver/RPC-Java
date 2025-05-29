package com.example.catalogo_service.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemPedido {

    private Integer id;
    private Integer produtoId;
    private Integer pedidoId;
    private Integer quantidade;
    private Double precoUnitario;
}
