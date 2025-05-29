package com.example.pedidos_service.repository;


import com.example.pedidos_service.entity.Cliente;
import com.example.pedidos_service.entity.ItemPedido;
import com.example.pedidos_service.entity.Pedido;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PedidoRepository {

    private final JdbcTemplate jdbc;

    public PedidoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Pedido> pedidoMapper = (rs, rowNum) -> {
        Cliente c = new Cliente(
                rs.getInt("c_id"),
                rs.getString("nome"),
                rs.getString("email")
        );
       return new Pedido(
                rs.getInt("p_id"),
                c,
                LocalDateTime.parse(rs.getString("data_criacao")),
                rs.getString("status"),
                rs.getDouble("valor_total"),
                new java.util.ArrayList<>()
        );

    };

    private final RowMapper<ItemPedido> itemMapper = BeanPropertyRowMapper.newInstance(ItemPedido.class);

    public List<Pedido> findAll() {
        // 1. busca só os pedidos + cliente
        String sqlPedidos = """
            SELECT
              p.id AS p_id,
              p.data_criacao,
              p.status,
              p.valor_total,
              c.id AS c_id,
              c.nome,
              c.email
            FROM pedidos p
            JOIN clientes c ON p.cliente_id = c.id
            ORDER BY p.id
            """;

        List<Pedido> pedidos = jdbc.query(sqlPedidos, pedidoMapper);

        // 2. para cada pedido, busca itens
        String sqlItens = """
            SELECT
              id, produto_id, quantidade, preco_unitario
            FROM itens_pedido
            WHERE pedido_id = ?
            """;

        for (Pedido p : pedidos) {
            List<ItemPedido> itens = jdbc.query(
                    sqlItens,
                    itemMapper,
                    p.getId()
            );
            p.setItens(itens);
        }

        return pedidos;
    }

    public Pedido findById(int id) {
        String sqlPedido = """
            SELECT
              p.id AS p_id,
              p.data_criacao,
              p.status,
              p.valor_total,
              c.id AS c_id,
              c.nome,
              c.email
            FROM pedidos p
            JOIN clientes c ON p.cliente_id = c.id
            WHERE p.id = ?
            """;

        Pedido pedido = jdbc.queryForObject(sqlPedido, pedidoMapper, id);

        if (pedido != null) {
            String sqlItens = """
                SELECT
                  id, produto_id, quantidade, preco_unitario
                FROM itens_pedido
                WHERE pedido_id = ?
                """;
            List<ItemPedido> itens = jdbc.query(
                    sqlItens,
                    itemMapper,
                    id
            );
            pedido.setItens(itens);
        }
        return pedido;
    }


    public Integer savePedido(int clienteId,
                              LocalDateTime dataCriacao,
                              String status,
                              double valorTotal) {

        String sql = """
            INSERT INTO pedidos (cliente_id, data_criacao, status, valor_total)
            VALUES (?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();


        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, clienteId);
            ps.setString(2, dataCriacao.toString());
            ps.setString(3, status);
            ps.setDouble(4, valorTotal);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Falha ao inserir pedido: chave gerada não retornada pelo banco");
        }
        return key.intValue();
    }

    /**
     * Insere um item de pedido.
     */
    public void saveItemPedido(int pedidoId,
                               int produtoId,
                               int quantidade,
                               double precoUnitario) {

        String sql = """
            INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario)
            VALUES (?, ?, ?, ?)
            """;

        jdbc.update(sql, pedidoId, produtoId, quantidade, precoUnitario);
    }


    public void atualizarStatusPedido(int pedidoId, String status) {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
        jdbc.update(sql, status, pedidoId);
    }
}
