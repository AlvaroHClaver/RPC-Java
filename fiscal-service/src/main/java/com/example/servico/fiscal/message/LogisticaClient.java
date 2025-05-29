package com.example.servico.fiscal.message;


import io.grpc.StatusRuntimeException;
import logistica.Estoque;
import logistica.LogisiticaServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogisticaClient {

    private static final Logger logger = LoggerFactory.getLogger(LogisticaClient.class);


    @GrpcClient("logistica")
    private LogisiticaServiceGrpc.LogisiticaServiceBlockingStub logisticaStub;


    public Estoque.PedidoNotaResponse sendPedidoParaLogistica(long pedidoId, long notaFiscalId) {
        Estoque.PedidoNota request = Estoque.PedidoNota.newBuilder()
                .setPedidoId(pedidoId)
                .setNotaFiscalId(notaFiscalId)
                .build();

        try {
            Estoque.PedidoNotaResponse response = logisticaStub.sendPedidoNota(request);
            logger.info("Logística respondeu: status={} message={}",
                    response.getStatus(), response.getMessage());
            return response;
        } catch (StatusRuntimeException e) {
            logger.error("Falha na chamada gRPC para Logística: {}", e.getStatus(), e);
            return Estoque.PedidoNotaResponse.newBuilder()
                    .setStatus(Estoque.PedidoNotaResponse.Status.STATUS_ERROR)
                    .setMessage("Erro de comunicação: " + e.getStatus().getDescription())
                    .build();
        }
    }
}
