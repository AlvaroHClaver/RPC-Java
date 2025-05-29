package com.example.catalogo_service.service;


import com.example.catalogo_service.Entity.dto.Produto;
import com.example.catalogo_service.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;


    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }


    public int atualizarEstoque(Integer id, Integer novaQuantidade) {
        produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado"));

        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }

        return produtoRepository.updateQuantidadeEstoque(id,novaQuantidade);
    }
}
