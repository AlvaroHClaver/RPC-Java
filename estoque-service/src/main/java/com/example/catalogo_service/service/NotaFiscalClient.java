package com.example.catalogo_service.service;

import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import nf.Nota;
import nf.NotaFiscalServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotaFiscalClient {

    private static final Logger logger = LoggerFactory.getLogger(NotaFiscalClient.class);


    @GrpcClient("nf")
    private NotaFiscalServiceGrpc.NotaFiscalServiceBlockingStub stub;


    public Nota.NotaFiscalResponse emitirNota(int pedidoId) {
        Nota.NotaFiscalRequest request = Nota.NotaFiscalRequest.newBuilder()
                .setPedidoId(pedidoId)
                .build();

        try {
            Nota.NotaFiscalResponse response = stub.emitirNota(request);
            logger.info("Nota fiscal emitida para pedido {}: número={}, chave={}",
                    pedidoId,
                    response.getNumero(),
                    response.getChaveAcesso());
            return response;
        } catch (StatusRuntimeException ex) {
            logger.error("Erro ao chamar gRPC NotaFiscalService.emitirNota para pedido {}: {}",
                    pedidoId, ex.getStatus());
            throw ex;
        }
    }
}