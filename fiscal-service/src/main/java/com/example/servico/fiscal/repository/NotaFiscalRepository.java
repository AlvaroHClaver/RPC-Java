package com.example.servico.fiscal.repository;

import com.example.servico.fiscal.entity.NotaFiscal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class NotaFiscalRepository {

    private final JdbcTemplate jdbc;

    public NotaFiscalRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Salva uma nota fiscal no banco e retorna o ID gerado.
     */
    public Integer save(NotaFiscal notaFiscal) {
        final String sql = """
            
                INSERT INTO notas_fiscais (
                pedido_id,
                numero,
                data_emissao,
                chave_acesso
            ) VALUES (?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, notaFiscal.getPedidoId());
            ps.setString(2, notaFiscal.getNumero());
            ps.setString(3, notaFiscal.getDataEmissao().toString()); // ISO-8601
            ps.setString(4, notaFiscal.getChaveAcesso());
            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId == null) {
            throw new IllegalStateException("Falha ao inserir nota fiscal: chave não retornada");
        }
        return generatedId.intValue();
    }
}