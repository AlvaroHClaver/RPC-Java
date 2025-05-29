package com.example.pedidos_service.service;


import catalogo.Catalogo;
import com.example.pedidos_service.entity.Pedido;
import com.example.pedidos_service.entity.dto.PedidoDTO;
import com.example.pedidos_service.repository.PedidoRepository;
import com.example.pedidos_service.service.grpc.PagamentoClient;
import com.example.pedidos_service.service.grpc.ProdutoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment.Pagamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {


    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

   @Autowired
    private PedidoRepository pedidoRepository;
   @Autowired
    private ProdutoClient produtoClient;

   @Autowired
   private PagamentoClient pagamentoClient;

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public Pedido findById(Integer id) {
        return pedidoRepository.findById(id);

    }



    public Integer criarPedido(PedidoDTO dto) throws Exception {

        List<Catalogo.ProdutoResponse> respostas = new ArrayList<>();
        double total = 0.0;
        for (Integer produtoId : dto.getProdutoIds()) {
            Catalogo.ProdutoResponse resp = produtoClient.getProduto(produtoId);
            if (resp.getId()==0) {
                logger.error("Produdo nao encontrado para o id: {}",produtoId);
                throw new IllegalArgumentException("Produto não encontrado: " + produtoId);
            }
            respostas.add(resp);
            total += resp.getPreco();
        }


        Integer pedidoId = pedidoRepository.savePedido(
                dto.getClienteId(),
                LocalDateTime.now(),
                "pendente",
                Math.round(total * 100.0) / 100.0
        );


        for (Catalogo.ProdutoResponse prod : respostas) {
            pedidoRepository.saveItemPedido(
                    pedidoId,
                    prod.getId(),
                    1,
                    prod.getPreco()
            );
        }
        Pagamento.PagamentoResponse response=  pagamentoClient.processar(pedidoId,"CARTAO");

        if(!response.getAprovado()){
            pedidoRepository.atualizarStatusPedido(pedidoId,"cancelado");
            logger.error("Pagamento recusado pedido cancelado");
            throw new Exception("Pagamento recusado");
        }else{
            pedidoRepository.atualizarStatusPedido(pedidoId,"pagamento_aprovado");
            logger.error("Pagamento aprovado para o pedido");
        }

        return pedidoId;
    }

}
