package com.example.catalogo_service.service.grpc;

import catalogo.Catalogo;
import catalogo.CatalogoServiceGrpc;
import com.example.catalogo_service.Entity.dto.Produto;
import com.example.catalogo_service.repository.ProdutoRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@GrpcService
public class CatalogoService extends CatalogoServiceGrpc.CatalogoServiceImplBase {


    private final ProdutoRepository produtoRepository;
    private static final Logger logger = LoggerFactory.getLogger(CatalogoService.class);

    public CatalogoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void getProduto(Catalogo.ProdutoRequest request, StreamObserver<Catalogo.ProdutoResponse> responseObserver) {
        int id = request.getProdutoId();

        // 1) buscar seu modelo de domínio (por ex. JPA)
        Produto entity = produtoRepository.findById(id).orElse(null);

        // 2) caso não encontre, loga e retorna id = 0
        if (entity == null) {
            logger.warn("Produto não encontrado: {}", id);
            Catalogo.ProdutoResponse response = Catalogo.ProdutoResponse.newBuilder()
                    .setId(0)
                    // outros campos ficam nos valores default (nome="", preco=0, estoque=0)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        // 3) montar o response gRPC com os dados reais
        Catalogo.ProdutoResponse response = Catalogo.ProdutoResponse.newBuilder()
                .setId(entity.getId())
                .setNome(entity.getNome())
                .setPreco(entity.getPreco())
                .setQuantidadeEstoque(entity.getQuantidadeEstoque())
                .build();

        // 4) enviar e completar
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}