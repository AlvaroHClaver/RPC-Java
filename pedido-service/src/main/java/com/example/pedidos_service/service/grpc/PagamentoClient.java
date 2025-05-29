package com.example.pedidos_service.service.grpc;


import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import payment.Pagamento;
import payment.PagamentoServiceGrpc;

@Service
public class PagamentoClient {

    @GrpcClient("pagamento")
    PagamentoServiceGrpc.PagamentoServiceBlockingStub stub;


    public Pagamento.PagamentoResponse processar(int pedidoId, String metodo) {
        Pagamento.PagamentoRequest req = Pagamento.PagamentoRequest.newBuilder()
                .setPedidoId(pedidoId)
                .setMetodo(metodo)
                .build();
        return stub.processarPagamento(req);
    }
}
