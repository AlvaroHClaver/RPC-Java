package com.example.servico.fiscal.service;


import com.example.servico.fiscal.entity.NotaFiscal;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import nf.Nota;
import nf.NotaFiscalServiceGrpc;

@GrpcService
public class NotaFiscalServiceGrpcListener
        extends NotaFiscalServiceGrpc.NotaFiscalServiceImplBase {

    private final NotaFiscalService notaFiscalService;

    public NotaFiscalServiceGrpcListener(NotaFiscalService notaFiscalService) {
        this.notaFiscalService = notaFiscalService;
    }

    @Override
    public void emitirNota(Nota.NotaFiscalRequest request,
                          StreamObserver<Nota.NotaFiscalResponse> responseObserver) {
        int pedidoId = request.getPedidoId();


        NotaFiscal nf = notaFiscalService.emitirNotaFiscal(pedidoId);

        Nota.NotaFiscalResponse response = Nota.NotaFiscalResponse.newBuilder()
                .setPedidoId(nf.getPedidoId())
                .setNumero(nf.getNumero())
                .setDataEmissao(nf.getDataEmissao().toString())
                .setChaveAcesso(nf.getChaveAcesso())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}