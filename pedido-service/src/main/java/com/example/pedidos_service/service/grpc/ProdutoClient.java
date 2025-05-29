package com.example.pedidos_service.service.grpc;

import catalogo.Catalogo;
import catalogo.CatalogoServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class ProdutoClient {

    @GrpcClient("catalogo")
    private CatalogoServiceGrpc.CatalogoServiceBlockingStub stub;

    public Catalogo.ProdutoResponse getProduto(int produtoId) {
        Catalogo.ProdutoRequest req = Catalogo.ProdutoRequest.newBuilder()
                .setProdutoId(produtoId)
                .build();
        return stub.getProduto(req);
    }
}