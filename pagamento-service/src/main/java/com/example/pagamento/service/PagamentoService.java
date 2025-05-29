package com.example.pagamento.service;

import com.example.pagamento.entity.Pagamento;
import com.example.pagamento.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final Random random = new Random();
    private final EstoqueClient estoqueClient;
    private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);

    public PagamentoService(PagamentoRepository pagamentoRepository, EstoqueClient estoqueClient) {
        this.pagamentoRepository = pagamentoRepository;
        this.estoqueClient = estoqueClient;
    }


    public Pagamento processar(int pedidoId, String metodo) {
        LocalDateTime agora = LocalDateTime.now();


        boolean aprovado = decidirAprovacao();
        String statusInicial = aprovado ? "aprovado" : "rejeitado";


        Pagamento pagamento = new Pagamento(pedidoId, agora, statusInicial, metodo);
        Integer idGerado = pagamentoRepository.save(pagamento);
        pagamento.setId(idGerado);
        logger.info("Pagamento criado (id={}): status={}", idGerado, statusInicial);


        if (aprovado) {
            logger.info("Tentando separação de estoque para pedido {}", pedidoId);
            boolean separado = estoqueClient.separarPedido(pedidoId);

            if (separado) {
                logger.info("Separação de estoque SUCESSO para pedido {}", pedidoId);

            } else {

                logger.warn("Separação de estoque FALHOU para pedido {}; estornando pagamento {}", pedidoId, idGerado);
                pagamento.setStatus("estornado");
                pagamentoRepository.updateStatus(idGerado, "estornado");
            }

        } else {

            logger.warn("Pagamento REJEITADO pelo sistema para pedido {}", pedidoId);
        }


        return pagamento;
    }



    /**
     * Retorna true ou false aleatoriamente.
     */
    private boolean decidirAprovacao() {
        return random.nextBoolean();
    }
}
