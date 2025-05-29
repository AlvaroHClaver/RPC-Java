package com.example.catalogo_service.repository;

import com.example.catalogo_service.Entity.dto.Produto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoRepository {

    private final JdbcTemplate jdbc;

    public ProdutoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Produto> findAll() {
        return jdbc.query(
                "SELECT * FROM produtos",
                BeanPropertyRowMapper.newInstance(Produto.class)
        );
    }

    public Optional<Produto> findById(Integer id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        List<Produto> produtos = jdbc.query(
                sql,
                BeanPropertyRowMapper.newInstance(Produto.class),
                id
        );
        return produtos.stream().findFirst();
    }


    public int updateQuantidadeEstoque(Integer id, Integer novaQuantidade) {
        String sql = "UPDATE produtos SET quantidade_estoque = ? WHERE id = ?";
        return jdbc.update(
                sql,
                novaQuantidade,
                id
        );
    }

}
