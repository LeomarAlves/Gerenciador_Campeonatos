# Gerenciador de Campeonatos 🏎️🏆

Este é um sistema robusto para gestão de campeonatos (focado em Kart, mas adaptável), permitindo o controle completo desde o cadastro de pilotos até a geração de relatórios oficiais em PDF com pontuações automatizadas.

## 🚀 Funcionalidades

- **Gestão de Pilotos:** Cadastro, edição e listagem de competidores por categoria.
- **Controle de Campeonatos:** Criação de múltiplos campeonatos simultâneos.
- **Organização de Baterias:** Criação de baterias vinculadas a campeonatos específicos.
- **Registro de Resultados:** Lançamento de posições de chegada para cada bateria.
- **Cálculo de Pontuação Automático:** Sistema flexível que calcula pontos com base em tabelas de pontuação configuráveis.
- **Relatórios Oficiais:** 
    - Visualização de classificação em tempo real na tela.
    - Exportação de resultados oficiais e pódios em formato PDF.
- **Banco de Dados Embarcado:** Utiliza SQLite para facilidade de transporte e execução sem necessidade de configuração complexa de servidor de banco.

## 🛠️ Tecnologias Utilizadas

- **Backend:** Java 17 com [Spring Boot](https://spring.io/projects/spring-boot).
- **Persistência:** [Spring Data JPA](https://spring.io/projects/spring-data-jpa) e Hibernate.
- **Banco de Dados:** [SQLite](https://www.sqlite.org/).
- **Geração de PDF:** [iText](https://itextpdf.com/) e [OpenPDF](https://github.com/LibrePDF/OpenPDF).
- **Frontend:** HTML5, CSS3 e JavaScript (Vanilla).
- **Gerenciador de Dependências:** Maven.

## 📂 Estrutura do Projeto

```text
src/main/java/com/leomar/gerenciador_campeonatos/
├── controller/     # Endpoints da API REST
├── dto/            # Objetos de Transferência de Dados (Classificação, Ranking)
├── model/          # Entidades do banco de dados (Piloto, Campeonato, Bateria, etc.)
├── repository/     # Interfaces de acesso ao banco (Spring Data JPA)
└── service/        # Lógica de negócio (Cálculo de pontos e geração de relatórios)

src/main/resources/
├── static/         # Frontend (HTML, CSS, JS)
└── application.properties # Configurações do Spring e SQLite
```

## 📊 Modelo de Dados

O sistema é baseado nas seguintes entidades principais:

- **Campeonato:** Entidade mestre que agrupa baterias e define o contexto da competição.
- **Piloto:** Cadastro de competidores com nome, número de kart e vínculo a uma categoria.
- **Categoria:** Permite separar pilotos por níveis ou tipos de veículos (ex: Graduados, Estreantes).
- **Bateria (Heat):** Representa uma corrida individual dentro de um campeonato, com data e hora.
- **ResultadoBateria:** Vincula um piloto a uma bateria, registrando sua posição de chegada, se houve NC (Não Completou) e a pontuação obtida.
- **TabelaPontuacao:** Define quantos pontos cada posição de chegada vale (ex: 1º = 25 pts, 2º = 20 pts).
- **AjustePenalizacao:** Permite aplicar ajustes manuais de pontos por infrações ou bônus.

## ⚙️ Como Executar

### Pré-requisitos
- JDK 17 ou superior.
- Maven instalado.

### Passos
1. Clone o repositório.
2. Na raiz do projeto, execute:
   ```bash
   ./mvnw spring-boot:run
   ```
3. O sistema estará disponível em: `http://localhost:8080`
4. O arquivo de banco de dados `campeonatos.db` será criado automaticamente na raiz do projeto.

## 🔌 API Endpoints (Principais)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **GET** | `/api/campeonatos` | Lista todos os campeonatos |
| **POST** | `/api/campeonatos` | Cria um novo campeonato |
| **GET** | `/api/campeonatos/{id}/classificacao` | Retorna o pódio/classificação final |
| **GET** | `/api/pilotos` | Lista todos os pilotos |
| **POST** | `/api/resultados/calcular/{bateriaId}` | Calcula os pontos de uma bateria específica |
| **GET** | `/api/relatorios/etapa/pdf` | Gera e baixa o PDF oficial do resultado |

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---
Desenvolvido por [Leomar](https://github.com/leomar)
