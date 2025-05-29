package com.example.catalogo_service.controller;

import com.example.catalogo_service.Entity.dto.EstoqueDTO;
import com.example.catalogo_service.Entity.dto.Produto;
import com.example.catalogo_service.service.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);


    @Autowired
    private ProdutoService produtoService;


    @GetMapping
    public ResponseEntity<List<Produto>> findCatalog(){
        try{
            return new ResponseEntity<>(produtoService.findAll(),HttpStatus.OK);
        }catch (Exception e){
            logger.error("Erro ao buscar produto",e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}/estoque")
    public ResponseEntity<?> atualizarEstoque(
            @PathVariable Integer id,
            @RequestBody EstoqueDTO dto) {
        try {
            int atualizado = produtoService.atualizarEstoque(id, dto.getQuantidade());
            return ResponseEntity.ok(atualizado);

        } catch (NoSuchElementException e) {
            logger.warn("Produto com ID {} não encontrado", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produto com ID " + id + " não encontrado.");

        } catch (IllegalArgumentException e) {
            logger.warn("Requisição inválida: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            logger.error("Erro ao atualizar estoque do produto com ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao atualizar estoque.");
        }
    }

}
