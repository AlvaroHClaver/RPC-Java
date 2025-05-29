package com.example.pedidos_service.entity;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Cliente {


    private Integer id;

    private String nome;

    private String email;
}
