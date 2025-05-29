package com.example.pagamento.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import payment.Pagamento;
import payment.PagamentoServiceGrpc;


@GrpcService
public class PagamentoServiceGrpcListener  extends PagamentoServiceGrpc.PagamentoServiceImplBase{

    private static final Logger logger = LoggerFactory.getLogger(PagamentoServiceGrpcListener.class);
    private final PagamentoService pagamentoService;

    public PagamentoServiceGrpcListener(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @Override
    public void processarPagamento(Pagamento.PagamentoRequest request, StreamObserver<Pagamento.PagamentoResponse> responseObserver) {
        int pedidoId = request.getPedidoId();
        String metodo = request.getMetodo();

        logger.info("Recebendo requisição de pagamento para pedido {} com método {}", pedidoId, metodo);

        try {

            com.example.pagamento.entity.Pagamento pagamento = pagamentoService.processar(pedidoId, metodo);

            boolean aprovado = "aprovado".equalsIgnoreCase(pagamento.getStatus());


            Pagamento.PagamentoResponse response = Pagamento.PagamentoResponse.newBuilder()
                    .setPedidoId(pagamento.getPedidoId())
                    .setAprovado(aprovado)
                    .setStatus(pagamento.getStatus())
                    .build();


            responseObserver.onNext(response);
            responseObserver.onCompleted();

            logger.info("Pagamento processado para pedido {}: status={}", pedidoId, pagamento.getStatus());
        } catch (Exception ex) {
            logger.error("Erro ao processar pagamento para pedido {}: {}", pedidoId, ex.getMessage());
            responseObserver.onError(ex);
        }
    }
}
