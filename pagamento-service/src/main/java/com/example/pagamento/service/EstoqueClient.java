package com.example.pagamento.service;

import com.example.estoque.proto.EstoqueServiceGrpc;
import com.example.estoque.proto.SeparacaoRequest;
import com.example.estoque.proto.SeparacaoResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class EstoqueClient {

    @GrpcClient("estoque")
    private EstoqueServiceGrpc.EstoqueServiceBlockingStub stub;


    public boolean separarPedido(int pedidoId) {
        SeparacaoRequest request = SeparacaoRequest.newBuilder()
                .setPedidoId(pedidoId)
                .build();

        SeparacaoResponse response = stub.realizarSeparacao(request);
        return response.getSeparacaoRealizada();
    }
}
