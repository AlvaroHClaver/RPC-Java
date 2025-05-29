package com.example.pagamento.repository;


import com.example.pagamento.entity.Pagamento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class PagamentoRepository {

    private final JdbcTemplate jdbc;

    public PagamentoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Integer save(Pagamento pagamento) {
        String sql = """
            INSERT INTO pagamentos
              (pedido_id, data_processamento, status, metodo)
            VALUES (?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update((PreparedStatementCreator) conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pagamento.getPedidoId());
            ps.setString(2, pagamento.getDataProcessamento().toString());
            ps.setString(3, pagamento.getStatus());
            ps.setString(4, pagamento.getMetodo());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Falha ao inserir Pagamento, chave não retornada");
        }
        return key.intValue();
    }

    public void updateStatus(int pagamentoId, String novoStatus) {
        String sql = """
            UPDATE pagamentos
               SET status = ?
             WHERE id = ?
            """;
        jdbc.update(sql, novoStatus, pagamentoId);
    }

}
