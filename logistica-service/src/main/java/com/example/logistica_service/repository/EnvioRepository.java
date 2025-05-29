package com.example.logistica_service.repository;

import com.example.logistica_service.entity.Envio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class EnvioRepository {

    private final JdbcTemplate jdbc;

    public EnvioRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    public Integer create(Envio envio) {
        final String sql = """
            INSERT INTO envios
              (pedido_id, nota_fiscal_id, data_despacho, codigo_rastreamento, status)
            VALUES (?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong   (1, envio.getPedidoId());
            ps.setLong   (2, envio.getNotaFiscalId());
            ps.setTimestamp(3, envio.getDataDespacho() != null
                    ? Timestamp.valueOf(envio.getDataDespacho())
                    : null);
            ps.setString (4, envio.getCodigoRastreamento());
            ps.setString (5, envio.getStatus());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }


    public int markAsDeliveredByPedidoId(Long pedidoId) {
        final String sql = """
            UPDATE envios
               SET status = 'entregue'
             WHERE pedido_id = ?
            """;
        return jdbc.update(sql, pedidoId);
    }
}
