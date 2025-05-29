package com.example.catalogo_service.service;

import com.example.estoque.proto.EstoqueServiceGrpc;
import com.example.estoque.proto.SeparacaoRequest;
import com.example.estoque.proto.SeparacaoResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class EstoqueServiceGrpcListener extends EstoqueServiceGrpc.EstoqueServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(EstoqueServiceGrpcListener.class);

    private final EstoqueService estoqueService;

    public EstoqueServiceGrpcListener(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @Override
    public void realizarSeparacao(SeparacaoRequest request,
                                  StreamObserver<SeparacaoResponse> responseObserver) {
        int pedidoId = request.getPedidoId();
        logger.info("Recebendo separação para pedido {}", pedidoId);

        boolean sucesso = estoqueService.realizarSeparacao(pedidoId);
        String mensagem = sucesso
                ? "Separação concluída com sucesso"
                : "Falha na separação: itens insuficientes";

        SeparacaoResponse resp = SeparacaoResponse.newBuilder()
                .setPedidoId(pedidoId)
                .setSeparacaoRealizada(sucesso)
                .setMensagem(mensagem)
                .build();

        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

}
