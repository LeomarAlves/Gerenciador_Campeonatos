# Gerenciador de Campeonatos 🏎️🏆 (Versão 1.0)

Este é um sistema profissional para gestão de campeonatos de Kart, permitindo o controle completo desde o cadastro de pilotos até a geração de relatórios oficiais em PDF com pontuações automatizadas, tratamento de empates e grids mistos.

## 🚀 Funcionalidades

- **Gestão de Pilotos:** Cadastro, edição e listagem de competidores vinculados a categorias específicas.
- **Controle de Campeonatos:** Criação e gerenciamento de múltiplos campeonatos simultâneos.
- **Organização de Baterias (Grids Mistos):** Criação de baterias que podem envolver uma ou mais categorias correndo simultaneamente na pista.
- **Registro de Resultados:** Lançamento de posições de chegada, marcação de NC (Não Completou) e Pole Position.
- **Cálculo de Pontuação Automático:** Motor de regras flexível que calcula pontos com base em tabelas de pontuação configuráveis por categoria.
- **Relatórios Oficiais:** 
    - Visualização de classificação consolidada em tempo real com critérios de desempate manuais.
    - Exportação de resultados oficiais e pódios em formato PDF profissional (via POST).
- **Interface Moderna:** Frontend em Vanilla JS rápido e responsivo, sem dependências externas pesadas.

## 🛠️ Tecnologias Utilizadas

- **Backend:** Java 17 com [Spring Boot](https://spring.io/projects/spring-boot).
- **Persistência:** [Spring Data JPA](https://spring.io/projects/spring-data-jpa) e Hibernate.
- **Banco de Dados:** [SQLite](https://www.sqlite.org/) (Embarcado, não requer instalação).
- **Geração de PDF:** [iText](https://itextpdf.com/) (Geração dinâmica de tabelas profissionais).
- **Frontend:** HTML5, CSS3 e JavaScript Moderno (ES6+).
- **Padronização:** Clean Code e refatoração arquitetural para alta manutenibilidade.

## 📂 Estrutura do Projeto

```text
src/main/java/com/leomar/gerenciador_campeonatos/
├── controller/     # Endpoints da API REST (Controllers)
├── dto/            # Objetos de Transferência de Dados (ClassificacaoDTO)
├── model/          # Entidades do banco de dados (Piloto, Campeonato, Bateria, etc.)
├── repository/     # Interfaces de acesso ao banco (Spring Data JPA)
└── service/        # Lógica de negócio (Cálculo de pontos e exportação de PDF)

src/main/resources/
├── static/         # Frontend (Interface Web)
└── application.properties # Configurações do sistema e SQLite
```

## 📊 Modelo de Dados Principais

- **Campeonato:** Agrupador mestre de toda a competição.
- **Piloto:** Competidor com número de kart e vínculo obrigatório a uma categoria.
- **Categoria:** Divisão dos pilotos (ex: F4 Graduados, Indoor Estreantes).
- **Bateria (Heat):** Uma corrida individual que gera resultados e pontuações.
- **ResultadoBateria:** Registro da performance do piloto (Posição, NC, Pole, Pontos).
- **TabelaPontuacao:** Regras de negócio que definem o peso de cada posição.

## ⚙️ Como Executar

### Pré-requisitos
- JDK 17 ou superior.
- Maven instalado (ou use o `mvnw` incluso).

### Passos
1. Clone o repositório.
2. Na raiz do projeto, execute:
   ```bash
   ./mvnw spring-boot:run
   ```
3. O sistema estará disponível em: `http://localhost:8080`
4. O banco de dados (`campeonatos.db`) será gerado automaticamente no primeiro acesso.

## 🔌 API Endpoints (Principais)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/api/campeonatos` | Lista todos os campeonatos cadastrados |
| **POST** | `/api/campeonatos` | Cria um novo campeonato |
| **GET** | `/api/categorias/campeonato/{id}` | Lista categorias de um campeonato específico |
| **POST** | `/api/resultados/calcular/{id}` | Processa a pontuação de uma bateria |
| **POST** | `/api/relatorios/etapa/pdf` | Gera o PDF oficial baseado nos dados ordenados |

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---
Desenvolvido por [LeomarAlves](https://github.com/LeomarAlves)
