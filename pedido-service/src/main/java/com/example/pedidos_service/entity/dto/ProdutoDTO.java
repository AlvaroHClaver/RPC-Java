package com.example.pedidos_service.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoDTO {


        private int id;
        private String nome;
        private double preco;
        private int quantidadeEstoque;
}
