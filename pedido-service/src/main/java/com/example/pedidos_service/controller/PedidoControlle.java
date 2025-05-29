package com.example.pedidos_service.controller;


import com.example.pedidos_service.entity.Pedido;
import com.example.pedidos_service.entity.dto.PedidoDTO;
import com.example.pedidos_service.entity.dto.ProdutoDTO;
import com.example.pedidos_service.service.PedidoService;
import com.example.pedidos_service.service.grpc.ProdutoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoControlle {


    private static final Logger logger = LoggerFactory.getLogger(PedidoControlle.class);

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProdutoClient produtoClient;



    @GetMapping
    public ResponseEntity<List<Pedido>> findAll() {
        try{
            return new ResponseEntity<>(pedidoService.findAll(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(pedidoService.findById(id), HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teste/{id}")
    public ResponseEntity<ProdutoDTO> buscarProduto(@PathVariable int id) {
        // chama o gRPC e já converte para DTO
        catalogo.Catalogo.ProdutoResponse resp = produtoClient.getProduto(id);
        ProdutoDTO dto = new ProdutoDTO(resp.getId(),resp.getNome(),resp.getPreco(),resp.getQuantidadeEstoque());
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    public ResponseEntity<String> criar(@RequestBody PedidoDTO dto) {
       try{
           Integer id =  pedidoService.criarPedido(dto);
           logger.info("Pedido criado com id {}",id);
           return new ResponseEntity<>("Pedido criado com id "+ id,HttpStatus.CREATED);
       } catch (IllegalArgumentException e) {
           logger.error("Produto id não encontrado. Falha ao criar pedido");
         return new ResponseEntity<>("Produto id não encontrado",HttpStatus.NOT_FOUND);
       }
       catch (Exception e){
           logger.error("Falha ao criar pedido");
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
}
