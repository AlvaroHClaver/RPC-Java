package com.example.catalogo_service.service;

import com.example.catalogo_service.entity.ItemPedido;
import com.example.catalogo_service.repository.EstoqueRepository;
import com.example.catalogo_service.repository.ItemPedidoRepository;

import nf.Nota.NotaFiscalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueService.class);

    private final ItemPedidoRepository itemPedidoRepository;
    private final EstoqueRepository estoqueRepository;
    private final NotaFiscalClient notaFiscalClient;

    public EstoqueService(ItemPedidoRepository itemPedidoRepository,
                          EstoqueRepository estoqueRepository,
                          NotaFiscalClient notaFiscalClient) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.estoqueRepository = estoqueRepository;
        this.notaFiscalClient = notaFiscalClient;
    }

    public boolean realizarSeparacao(int pedidoId) {
        logger.info("Iniciando separação para pedido {}", pedidoId);

        List<ItemPedido> itens = itemPedidoRepository.findByPedidoId(pedidoId);
        if (itens.isEmpty()) {
            logger.warn("Pedido {} não encontrado ou sem itens", pedidoId);
            return false;
        }


        for (ItemPedido item : itens) {
            int produtoId = item.getProdutoId();
            int qtdNecessaria = item.getQuantidade();
            int qtdEmEstoque = estoqueRepository.getQuantidade(produtoId);

            if (qtdEmEstoque < qtdNecessaria) {
                logger.warn("Estoque insuficiente para produto {}: precisa {}, tem {}",
                        produtoId, qtdNecessaria, qtdEmEstoque);
                return false;
            }
        }


        for (ItemPedido item : itens) {
            estoqueRepository.decrementarQuantidade(item.getProdutoId(), item.getQuantidade());
            logger.info("Decrementado {} unidades do produto {}",
                    item.getQuantidade(), item.getProdutoId());
        }

        logger.info("Separação concluída para pedido {}", pedidoId);


        logger.info("Solicitando emissão de nota fiscal para pedido {}", pedidoId);
        try {
            NotaFiscalResponse nf = notaFiscalClient.emitirNota(pedidoId);
            logger.info("Nota fiscal emitida: número='{}', chaveAcesso='{}', dataEmissao='{}'",
                    nf.getNumero(), nf.getChaveAcesso(), nf.getDataEmissao());
        } catch (Exception ex) {
            logger.error("Falha ao emitir nota fiscal para pedido {}: {}", pedidoId, ex.getMessage());

            return false;
        }

        return true;
    }
}
