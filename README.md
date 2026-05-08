# Controle de Estoque de Relógios

API RESTful desenvolvida em **Spring Boot** para gerenciamento e controle de estoque de relógios. O projeto simula um backend robusto, utilizando boas práticas de engenharia de software, arquitetura em camadas e design patterns para garantir manutenção e escalabilidade.

---

## Tecnologias e Ferramentas

- **Linguagem:** Java 17+
- **Framework:** Spring Boot (Web, Data JPA, Validation)
- **Banco de Dados:** PostgreSQL
- **Produtividade:** Lombok

---

## Arquitetura e Decisões Técnicas

O projeto segue a arquitetura tradicional em camadas:

```text
Entity → Repository → Service → Controller
```

garantindo a separação clara de responsabilidades.

### DTO + Mapper Manual

Controle total sobre os contratos de entrada e saída da API.

A conversão de `Entity` para `DTO` é feita manualmente, permitindo:
- campos calculados dinamicamente
- enriquecimento de resposta
- desacoplamento da camada de persistência
- maior controle sobre serialização

---

### Filtros Dinâmicos (Specifications)

Utilização de:

```text
JpaSpecificationExecutor
```

para composição dinâmica de filtros complexos e combináveis, evitando:
- múltiplos `if-else`
- queries estáticas enormes
- duplicação de lógica

---

### Tratamento Global de Erros

Uso de:

```text
@ControllerAdvice
```

para padronização das respostas HTTP:
- `400 Bad Request`
- `404 Not Found`
- erros de validação
- erros de parsing JSON
- exceções de domínio

---

### Paginação Segura

O parâmetro:

```text
porPagina
```

possui limite máximo travado em:

```text
60
```

evitando:
- payloads excessivos
- sobrecarga de banco
- abuso da API

---

### Enums Seguros

Implementação de:
- `fromApi`
- `toApi`

garantindo:
- consistência de dados
- independência entre entrada da API e persistência no banco
- validação centralizada

---

## Regras de Negócio e Campos Calculados

O `RelogioDto` retorna informações enriquecidas automaticamente.

---

### Etiqueta de Resistência à Água

Classificação dinâmica baseada em:

```text
resistenciaAguaM
```

| Faixa | Classificação |
|---|---|
| `< 50m` | Respingos |
| `50m - 99m` | Uso diário |
| `100m - 199m` | Natação |
| `>= 200m` | Mergulho |

---

### Collector Score

Sistema de pontuação baseado nas características do relógio.

Exemplos:
- `+25` pontos → vidro safira
- `+20` pontos → movimento automático
- bônus por materiais premium
- bônus por resistência elevada

---

## Como Executar

### Pré-requisitos

- Java 17 instalado
- PostgreSQL rodando na porta `5432`

---

### Configuração

1. Clone o repositório

```bash
git clone <repositorio>
```

2. Crie o banco PostgreSQL

Verifique as credenciais em:

```text
application.properties
```

3. Execute a aplicação

Via Maven:

```bash
./mvnw spring-boot:run
```

ou pela IDE.

---

## Seed Automático

O sistema possui um:

```text
CarregadorDadosInicial
```

responsável por popular automaticamente o banco com dados de exemplo na primeira execução caso a tabela esteja vazia.

---


## Query Parameters

| Parâmetro | Padrão | Descrição |
|---|---|---|
| pagina | 1 | Página atual |
| porPagina | 12 | Itens por página (máx 60) |
| busca | - | Busca textual |
| marca | - | Filtro exato por marca |
| tipoMovimento | - | quartz, automatic, manual |
| materialCaixa | - | steel, titanium, resin, bronze, ceramic |
| tipoVidro | - | mineral, sapphire, acrylic |
| resistenciaMin / Max | - | Faixa de resistência |
| precoMin / Max | - | Faixa de preço |
| diametroMin / Max | - | Faixa de diâmetro |
| ordenar | newest | newest, price_asc, price_desc, diameter_asc, wr_desc |

---

# Tratamento de Erros

A API retorna payloads estruturados para facilitar integração com frontend e clientes externos.

---

## Exemplo — 400 Bad Request

```json
{
  "timestamp": "2026-01-15T22:00:00Z",
  "status": 400,
  "erro": "Requisição inválida",
  "mensagem": "Erro de validação",
  "caminho": "/api/relogios",
  "errosDeCampo": [
    {
      "campo": "marca",
      "mensagem": "must not be blank"
    }
  ]
}
```

---

## 📂 Estrutura do Projeto

```text
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── entity/
 ├── dto/
 ├── mapper/
 ├── specification/
 ├── exception/
 └── config/
```

---

## Conceitos Aplicados

- Spring Boot
- REST API
- DTO Pattern
- Mapper Manual
- Specifications Dinâmicas
- Paginação
- Bean Validation
- Exception Handling
- Arquitetura em Camadas
- Clean Code
- JDBC/JPA
- PostgreSQL
- Serialização JSON
- Design de APIs RESTful

---