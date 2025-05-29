package com.example.catalogo_service.repository;

import com.example.catalogo_service.entity.ItemPedido;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemPedidoRepository {

    private final JdbcTemplate jdbc;

    public ItemPedidoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Retorna todos os itens de um pedido.
     */
    public List<ItemPedido> findByPedidoId(int pedidoId) {
        String sql = """
                
                    SELECT
                  id,
                  pedido_id       AS pedidoId,
                  produto_id      AS produtoId,
                  quantidade,
                  preco_unitario  AS precoUnitario
                FROM itens_pedido
                WHERE pedido_id = ?
                """;
        return jdbc.query(
                sql,
                BeanPropertyRowMapper.newInstance(ItemPedido.class),
                pedidoId
        );
    }

    /**
     * Insere um novo item de pedido.
     */
    public void save(ItemPedido item) {
        String sql =
                """
                        INSERT INTO
                            itens_pedido (
                        
                        
                                         qua
                                      preco_unita
                               ) VALUES (?, ?, ?, ?)
                        """;
        jdbc.update(
                sql,
                item.getPedidoId(),
                item.getProdutoId(),
                item.getQuantidade(),
                item.getPrecoUnitario()
        );
    }

    /**
     * Remove todos os itens de um pedido (útil em cancelamentos ou updates completos).
     */
    public void deleteByPedidoId(int pedidoId) {
        String sql = "DELETE FROM itens_pedido WHERE pedido_id = ?";
        jdbc.update(sql, pedidoId);
    }
}
