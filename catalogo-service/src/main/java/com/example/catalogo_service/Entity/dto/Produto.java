package com.example.catalogo_service.Entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Produto {
    private Integer id;
    private String nome;
    private Double preco;
    private Integer quantidadeEstoque;
}
