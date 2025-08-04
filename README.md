
# 🛒 RPC-Java - Sistema Distribuído com gRPC para E-commerce

Este projeto implementa uma arquitetura de **microserviços baseada em RPC** utilizando **gRPC** e **Java com Spring Boot**. Trata-se de uma simulação realista de um sistema de e-commerce, onde diferentes serviços se comunicam entre si usando **Protocol Buffers (.proto)** como definição de contrato.

---

## 🎯 Objetivo

Demonstrar como estruturar um sistema distribuído com serviços independentes que se comunicam por meio de chamadas gRPC, uma alternativa moderna, eficiente e fortemente tipada ao uso de REST APIs.

---

## 🧱 Serviços Implementados

Cada serviço é um microserviço isolado, com responsabilidade única. Todos se comunicam via gRPC:

- **catalogo-service**: gerencia produtos disponíveis no sistema.
- **estoque-service**: responde por quantidades em estoque e integração com logística.
- **fiscal-service**: emite notas fiscais e comunica-se com os serviços de pagamento e logística.
- **logistica-service**: responsável pela simulação de envios.
- **pagamento-service**: processa os pagamentos de pedidos.
- **pedido-service**: orquestra o pedido como um todo, fazendo chamadas RPC para os demais serviços.

---

## 🔄 Comunicação RPC com gRPC

Todos os serviços utilizam **Protocol Buffers (.proto)** para definir suas mensagens e serviços. As chamadas gRPC são feitas utilizando `ManagedChannel`, `Stub` e `GrpcServiceListener` para receber chamadas de outros serviços.

Exemplo de arquivos `.proto`:

```
pedido-service/src/main/proto/catalogo.proto
estoque-service/src/main/proto/nota.proto
pagamento-service/src/main/proto/pagamento.proto
...
```

Cada serviço possui sua própria definição de protocolo para comunicação eficiente, rápida e com serialização binária.

---

## 📦 Estrutura do Projeto

```
RPC-Java/
├── catalogo-service/
│   └── src/
│       ├── main/java/.../CatalogoService.java     # gRPC Service
│       └── main/proto/catalogo.proto              # Interface gRPC
├── estoque-service/
│   └── src/main/proto/estoque.proto
├── pedido-service/
│   └── src/main/proto/pagamento.proto
├── pagamento-service/
│   └── src/main/proto/pagamento.proto
├── run-all.sh                                     # Script para executar tudo
└── pom.xml                                        # Projeto multi-módulo Maven
```

---

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot**
- **gRPC Java**
- **Protocol Buffers (protobuf)**
- **Maven (multi-module)**
- **Docker (opcional)**

---

## ▶️ Como Executar

### Pré-requisitos

- Java 17
- Maven
- Protoc instalado (ou use gerado automaticamente com plugins)

### Passo a passo

```bash
# Clone o repositório
git clone https://github.com/AlvaroHClaver/RPC-Java.git
cd RPC-Java

# Execute todos os serviços (paralelamente)
./run-all.sh
```

Cada serviço sobe em uma porta diferente, e o `pedido-service` realiza chamadas gRPC reais para os demais.

---

## 📡 Exemplo de Comunicação gRPC

O serviço `pedido-service` realiza uma chamada gRPC para o `catalogo-service` para buscar detalhes de um produto, e em seguida contata o `estoque-service` para verificar disponibilidade. Toda a orquestração acontece via chamadas gRPC com contratos definidos em `.proto`.

---

## 📄 Licença

Distribuído sob a licença MIT. Veja `LICENSE` para mais informações.

---

> Projeto desenvolvido por [Alvaro H. Claver](https://github.com/AlvaroHClaver) como estudo e demonstração prática de RPC com gRPC em Java e Spring Boot.
