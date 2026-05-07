# Bundesliga Wrapped

> Retrospectiva personalizada da Bundesliga, desenvolvido para o AWS World Sports Innovation Cup 2026

Aplicação web full-stack que transforma dados reais da Bundesliga 2024/25 em uma experiência de fã personalizada estilo Spotify Wrapped. O usuário insere seu Fan ID e recebe uma retrospectiva animada em 6 slides gerada com dados reais da DFL e narrativas criadas por IA.

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
│        API Spring Boot      │
│                             │
│  5 Importers → PostgreSQL   │
│  WrappedGeneratorService    │
│  NarrativeService           │
│  REST: GET /api/wrapped/{id}│
└────────────┬────────────────┘
             │ JSON
             ▼
┌─────────────────────────────┐
│      React + TypeScript     │
│                             │
│  6 slides animados          │
│  Sistema de partículas      │
│  Compartilhamento via       │
│  screenshot                 │                          
└─────────────────────────────┘
```

---

## Stack Tecnológica

### Backend
- **Java 21** + **Spring Boot 4.0.5**
- **Spring Data JPA** + **Hibernate 7** + **PostgreSQL 16**
- **Flyway 11** — migrations de banco de dados
- **Spring Cloud AWS 4.0** — integração com S3
- **Groq API** — geração de narrativas por IA
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

| Tabela | Registros           |
|--------|---------------------|
| clubs | 18                  |
| matches | 306                 |
| players | 892                 |
| player_stats | 34 (Bayern München) |
| user_engagements | 26.242              |

---

## Como testar

Acesse: **https://bundesligawrapped-1-7ksh.onrender.com**

> ⚠️ O backend está hospedado no plano gratuito do Render e pode demorar
> para responder na primeira requisição (cold start). Aguarde o carregamento.

### Fan IDs para teste

Cole qualquer um dos IDs abaixo no campo da tela inicial:

```
C2A5E4CF606C4BED40C6DC8A77103FCE43BE30502F2BF5347CEF6E4D06630CE3
57D114C3B02E42940083C50A2EA90C3BBB8C1A126A9E8ED11D94CD84B1822722
9741A97AC565DAF81964FF75987E01045C43A38A6894ED758A91B847BF6C323D
5349F4656DDEE2D90F54DC986D648DF36A87CE0A9B7A78058E0158F97FC7D59D
3EA95FF592AAECC22A9205A4F32A26FAB71742DE532BBB25D10FBF7EAE9BE42B
4C4151E1D63B0AEA6E8CEE58C6727DF047B409B6A4560D5448588F30E34FE769
0B436978508B06A5BBFA01E1E7B96FB6C3FDC1022F9582D07D5E4B55006FB0FB
8C9E230683A8D590A16B74A4915D6D0607C43E09020B7BEA46811C32714A31FD
5D3611A8FB2BCB6B9CD7355BF77F98D268A81D48215B9F154BDADDFE942CEDE9
9D73469EE9E1B8435501E8719CB8582E2F49B2D7D1667FB4C80A345F4C344B12
7E7E02BA6166A449B95DE37962C474FBFDF2A6249C12420EAFC515EED58C8048
```

---

## API

### GET /api/wrapped/{userId}

Retorna os dados completos do wrapped para um usuário.
Exemplo de resposta:
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

| Variável                     | Descrição                                     |
|------------------------------|-----------------------------------------------|
| `GROQ_API_KEY`               | Chave da API GROQ                             |
| `AWS_ACCESS_KEY_ID`          | Access key da AWS                             |
| `AWS_SECRET_ACCESS_KEY`      | Secret key da AWS                             |
| `AWS_SESSION_TOKEN`          | Token de sessão AWS (credenciais temporárias) |
| `SPRING_DATASOURCE_URL`      | URL de conexão PostgreSQL                     |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco                              |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco                                |

---

## Decisões Técnicas

**Por que leitura única do S3 e não streaming contínuo?**  
O projeto foi concebido como um "First Draft" para o hackathon — a prioridade era validar o conceito com dados reais. A estratégia de importação única na inicialização mantém a aplicação simples e reutilizável: para uma nova temporada, basta trocar o S3 bucket com os dados correspondentes, sem mudança de código. Para volumes na casa dos milhões ou ingestão contínua, o caminho natural seria mensageria com Kafka.


**Por que separar mapper de importer?**  
Separação de responsabilidades — o importer orquestra o processo (lê S3, deduplica, persiste), o mapper apenas converte XML/JSON para entidade. Facilita testes unitários e manutenção futura.

**Por que GROQ API no backend?**
Gerar uma retrospectiva personalizada para cada usuário, saindo do genérico. 

---

## Sobre o Projeto

Desenvolvido individualmente para o **AWS World Sports Innovation Cup 2026**, organizado pela DFL (Bundesliga). 

**Autor:** Lucas Figueiredo Cabral  
**Stack:** Java · Spring Boot · PostgreSQL · AWS · React · TypeScript  

