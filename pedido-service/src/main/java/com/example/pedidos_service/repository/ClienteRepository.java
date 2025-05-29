package com.example.pedidos_service.repository;



import com.example.pedidos_service.entity.Cliente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClienteRepository  {

    private final JdbcTemplate jdbc;
    private final BeanPropertyRowMapper<Cliente> rowMapper =
            BeanPropertyRowMapper.newInstance(Cliente.class);

    public ClienteRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    public List<Cliente> findAll() {
        String sql = "SELECT * FROM clientes";
        return jdbc.query(sql, rowMapper);
    }




}
