package com.example.servico.fiscal.service;

import com.example.servico.fiscal.entity.NotaFiscal;
import com.example.servico.fiscal.message.LogisticaClient;
import com.example.servico.fiscal.repository.NotaFiscalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class NotaFiscalService {

    private static final Logger logger = LoggerFactory.getLogger(NotaFiscalService.class);

    private final NotaFiscalRepository repository;
    private final LogisticaClient logisticaClient;

    public NotaFiscalService(NotaFiscalRepository repository, LogisticaClient logisticaClient) {
        this.repository = repository;
        this.logisticaClient = logisticaClient;
    }

    public NotaFiscal emitirNotaFiscal(Integer pedidoId) {
        logger.info("Iniciando emissão de nota fiscal para pedido {}", pedidoId);

        String numero = gerarNumeroSequencial();
        logger.debug("Número sequencial gerado: {}", numero);

        String chave = gerarChaveAcesso();
        logger.debug("Chave de acesso gerada: {}", chave);

        LocalDateTime agora = LocalDateTime.now();
        logger.info("Data de emissão: {}", agora);

        NotaFiscal nf = new NotaFiscal(
                pedidoId,
                numero,
                agora,
                chave
        );

        Integer idGerado = repository.save(nf);
        nf.setId(idGerado);
        logger.info("Nota fiscal persistida com ID {}", idGerado);
        logisticaClient.sendPedidoParaLogistica(pedidoId,idGerado);
        return nf;
    }

    private String gerarNumeroSequencial() {
        int suffix = (int) (Math.random() * 900) + 100;
        String seq = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + suffix;
        logger.trace("Sequência antes do pad/truncate: {}", seq);
        return seq;
    }

    private String gerarChaveAcesso() {
        String base = UUID.randomUUID().toString().replaceAll("-", "");
        logger.trace("UUID base sem traços: {}", base);
        String chave = safePadOrTruncate(base, 44, '0');
        logger.trace("Chave após pad/truncate: {}", chave);
        return chave;
    }

    /**
     * Se a string for menor que size, preenche com padChar à direita.
     * Se for maior, trunca para size.
     */
    private String safePadOrTruncate(String s, int size, char padChar) {
        if (s.length() == size) {
            return s;
        } else if (s.length() > size) {
            String trunc = s.substring(0, size);
            logger.trace("Truncando '{}' para '{}'", s, trunc);
            return trunc;
        } else {
            StringBuilder sb = new StringBuilder(s);
            while (sb.length() < size) {
                sb.append(padChar);
            }
            String padded = sb.toString();
            logger.trace("Preenchendo '{}' para '{}'", s, padded);
            return padded;
        }
    }
}
