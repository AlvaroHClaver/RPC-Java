package com.example.logistica_service.service;


import com.example.logistica_service.entity.Envio;
import com.example.logistica_service.repository.EnvioRepository;
import io.grpc.stub.StreamObserver;
import logistica.Estoque;
import logistica.LogisiticaServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.UUID;

@GrpcService
public class LogisiticaServiceImpl extends LogisiticaServiceGrpc.LogisiticaServiceImplBase {
    private static final Logger logger = LogManager.getLogger(LogisiticaServiceImpl.class);
    private final EnvioRepository envioRepository;

    public LogisiticaServiceImpl(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    public void sendPedidoNota(Estoque.PedidoNota request, StreamObserver<Estoque.PedidoNotaResponse> responseObserver) {
        long pedidoId      = request.getPedidoId();
        long notaFiscalId  = request.getNotaFiscalId();

        try {

            Envio envio = new Envio(
                    pedidoId,
                    notaFiscalId,
                    LocalDateTime.now(),
                    UUID.randomUUID().toString(),
                    "ENVIADO"
            );
            int created = envioRepository.create(envio);
            logger.info("Envio criado com id={} para pedido={}", created, pedidoId);


            int updated = envioRepository.markAsDeliveredByPedidoId(pedidoId);
            logger.info("Envio marcado como entregue, linhas afetadas={}", updated);


            Estoque.PedidoNotaResponse response = Estoque.PedidoNotaResponse.newBuilder()
                    .setStatus(Estoque.PedidoNotaResponse.Status.STATUS_OK)
                    .setMessage("Pedido " + pedidoId + " processado e marcado como entregue (envioId=" + created + ").")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {

            logger.error("Erro ao processar pedido {}: {}", pedidoId, e.getMessage(), e);


            Estoque.PedidoNotaResponse error = Estoque.PedidoNotaResponse.newBuilder()
                    .setStatus(Estoque.PedidoNotaResponse.Status.STATUS_ERROR)
                    .setMessage("Falha ao processar pedido " + pedidoId + ": " + e.getMessage())
                    .build();
            responseObserver.onNext(error);
            responseObserver.onCompleted();
        }
    }


}