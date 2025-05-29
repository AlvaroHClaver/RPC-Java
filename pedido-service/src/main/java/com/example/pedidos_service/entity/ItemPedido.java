package com.example.pedidos_service.entity;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemPedido {


    private Integer id;
    private Integer produtoId;
    private Integer quantidade;
    private Double precoUnitario;
}
