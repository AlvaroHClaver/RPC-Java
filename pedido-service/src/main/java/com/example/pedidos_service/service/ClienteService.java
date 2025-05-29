package com.example.pedidos_service.service;

import com.example.pedidos_service.entity.Cliente;
import com.example.pedidos_service.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
   public ClienteService (ClienteRepository clienteRepository) {
        this.clienteRepository=clienteRepository;
    }

    public List<Cliente> findAllClientes(){
        return  clienteRepository.findAll();
    }

}
