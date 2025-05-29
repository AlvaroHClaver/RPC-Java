package com.example.catalogo_service.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EstoqueRepository {

    private final JdbcTemplate jdbc;

    public EstoqueRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Retorna a quantidade em estoque para um produto */
    public int getQuantidade(int produtoId) {
        String sql = "SELECT quantidade_estoque FROM produtos WHERE id = ?";
        return jdbc.queryForObject(sql, Integer.class, produtoId);
    }

    /** Decrementa a quantidade — só atualiza se houver estoque suficiente */
    public int decrementarQuantidade(int produtoId, int quantidade) {
        String sql = """
            UPDATE produtos
            SET quantidade_estoque = quantidade_estoque - ?
            WHERE id = ? 
              AND quantidade_estoque >= ?
            """;
        return jdbc.update(sql, quantidade, produtoId, quantidade);
    }
}
