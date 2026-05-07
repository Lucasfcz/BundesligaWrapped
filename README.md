# Bundesliga Wrapped

> Retrospectiva personalizada da Bundesliga, desenvolvido para o AWS World Sports Innovation Cup 2026

Aplicação web full-stack que transforma dados reais da Bundesliga 2024/25 em uma experiência de fã personalizada estilo Spotify Wrapped. O usuário insere seu Fan ID e recebe uma retrospectiva animada em 6 slides — gerada com dados reais da DFL e narrativas criadas por IA.

---

## Demonstração

![Status](https://img.shields.io/badge/status-hackathon-red)

**Landing → Loading → 6 slides animados → Compartilhar**

| Slide | Conteúdo |
|-------|---------|
| 1 — Perfil de Fã | Classificação do tipo de fã + totais de engajamento |
| 2 — Mês Mais Ativo | Mês de pico com visitas ao Match Center |
| 3 — Clube Favorito | Escudo oficial, cores e estatísticas reais da temporada |
| 4 — Top Artilheiro | Maior artilheiro da temporada com gols e assistências |
| 5 — Destaque da Temporada | Maior partida em público |
| 6 — Sua História | Narrativa personalizada gerada por IA |

---

## Arquitetura

```
S3 Bucket (Dados DFL)
        │
        ▼
┌─────────────────────────────┐
│        API Spring Boot       │
│                             │
│  5 Importers → PostgreSQL   │
│  WrappedGeneratorService    │
│  ClaudeNarrativeService     │
│  REST: GET /api/wrapped/{id}│
└────────────┬────────────────┘
             │ JSON
             ▼
┌─────────────────────────────┐
│      React + TypeScript      │
│                             │
│  6 slides animados          │
│  Sistema de partículas      │
│  Compartilhamento via screenshot │
└─────────────────────────────┘
```

---

## Stack Tecnológica

### Backend
- **Java 21** + **Spring Boot 4.0.5**
- **Spring Data JPA** + **Hibernate 7** + **PostgreSQL 16**
- **Flyway 11** — migrations de banco de dados
- **Spring Cloud AWS 4.0** — integração com S3
- **Anthropic Claude API** — geração de narrativas por IA
- **SpringDoc OpenAPI 3** — documentação Swagger
- **Docker** — banco de dados containerizado

### Frontend
- **React 18** + **Vite** + **TypeScript**
- **Canvas API** — sistema de partículas em tempo real
- **Animações CSS Keyframe** — CountUp, scoreSlam, shieldDrop, emojiPop
- **html2canvas** — compartilhamento via screenshot
- **CDN Bundesliga** — escudos oficiais dos clubes

---

## Dataset

Dados reais da DFL para a temporada 2024/25 da Bundesliga, armazenados no AWS S3:

| Tabela | Registros |
|--------|-----------|
| clubs | 18 |
| matches | 306 |
| players | ~500 |
| player_stats | 34 (Bayern München) |
| user_engagements | 26.242 |

---

## Como Rodar

### Pré-requisitos

- Java 21+
- Docker
- Node.js 20+
- Credenciais AWS (acesso ao S3)
- Chave da API Anthropic

### Backend

```bash
# 1. Sobe o PostgreSQL
docker compose up -d

# 2. Configura as variáveis de ambiente
export ANTHROPIC_API_KEY=sk-ant-...
export AWS_ACCESS_KEY_ID=...
export AWS_SECRET_ACCESS_KEY=...
export AWS_SESSION_TOKEN=...

# 3. Roda com perfil local
./mvnw spring-boot:run -Dspring.profiles.active=local
```

API disponível em `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

App disponível em `http://localhost:5173`.  
O servidor Vite faz proxy de `/api/*` para `http://localhost:8080`.

---

## API

### GET /api/wrapped/{userId}

Retorna os dados completos do wrapped para um usuário.

```json
{
  "userId": "79BC2FC64A...",
  "fanIdentity": {
    "fanType": "Match Center Addict",
    "totalEngagements": 308,
    "articles": 95,
    "videos": 3,
    "stories": 8,
    "matchCenter": 202
  },
  "mostActiveMonth": {
    "month": "APRIL",
    "matchCenterCount": 64
  },
  "favoriteClub": {
    "clubName": "FC Bayern München",
    "shortName": "Bayern",
    "clubId": "DFL-CLU-00000G",
    "primaryColor": "#FF003C",
    "secondaryColor": "#FFFFFF",
    "seasonStats": {
      "matchesPlayed": 34,
      "wins": 24,
      "draws": 5,
      "losses": 5,
      "goalsScored": 89,
      "goalsConceded": 37,
      "biggestCrowdHome": 75000
    }
  },
  "topPlayer": {
    "playerName": "Harry Edward Kane",
    "clubName": "FC Bayern München",
    "clubId": "DFL-CLU-00000G",
    "goals": 26,
    "assists": 8
  },
  "seasonHighlight": {
    "homeTeam": "FC Bayern München",
    "guestTeam": "Eintracht Frankfurt",
    "result": "2:0",
    "spectators": 81365,
    "matchDay": 1
  },
  "narrative": {
    "text": "Você é um verdadeiro Match Center Addict..."
  }
}
```

---

## Classificação de Perfil de Fã

| Tipo | Condição |
|------|----------|
| Match Center Addict | matchCenter > 100 |
| Stats Nerd | videos > 50 |
| Story Lover | stories > 30 |
| Casual Fan | demais casos |

---

## Variáveis de Ambiente

| Variável | Descrição |
|----------|-----------|
| `GROK_API_KEY` | Chave da API GROK |
| `AWS_ACCESS_KEY_ID` | Access key da AWS |
| `AWS_SECRET_ACCESS_KEY` | Secret key da AWS |
| `AWS_SESSION_TOKEN` | Token de sessão AWS (credenciais temporárias) |
| `SPRING_DATASOURCE_URL` | URL de conexão PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco |

---

## Decisões Técnicas

**Por que leitura única do S3 e não streaming contínuo?**  
O projeto foi concebido como um "First Draft" para o hackathon — a prioridade era validar o conceito com dados reais. A estratégia de importação única na inicialização mantém a aplicação simples e reutilizável: para uma nova temporada, basta trocar o S3 bucket com os dados correspondentes, sem mudança de código. Para volumes na casa dos milhões ou ingestão contínua, o caminho natural seria mensageria com Kafka.


**Por que separar mapper de importer?**  
Separação de responsabilidades — o importer orquestra o processo (lê S3, deduplica, persiste), o mapper apenas converte XML/JSON para entidade. Facilita testes unitários e manutenção futura.

**Por que GROK API no backend?**
Gerar uma retrospectiva personalizada para cada usuário, saindo do genérico. 

---

## Sobre o Projeto

Desenvolvido individualmente para o **AWS World Sports Innovation Cup 2026**, organizado pela DFL (Bundesliga). 

**Autor:** Lucas Figueiredo Cabral  
**Stack:** Java · Spring Boot · PostgreSQL · AWS · React · TypeScript  

