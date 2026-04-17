# 🏁 Sprint 0 — Review & Retrospettiva
**Data:** 11-12 Aprile 2026 | **Team:** 4 persone | **Stato:** ✅ COMPLETATO

## 📋 Sprint Goal
Bootstrap del progetto: struttura repository, scaffold frontend e backend, schema DB, CI/CD, push su GitHub

## ✅ Definition of Done — Checklist
Tutte le story completate:
- [x] S0-1 (2 SP) — Setup repository GitHub + struttura monorepo
- [x] S0-2 (3 SP) — Bootstrap React 18 + Vite 5 + Ant Design 5.x
- [x] S0-3 (3 SP) — Bootstrap Spring Boot 3.2.5 + H2 + JPA
- [x] S0-4 (2 SP) — Schema DB, entità JPA, contratti API REST (9 endpoint)
- [x] S0-5 (1 SP) — GitHub Actions CI/CD + push su GitHub

**Story Points consegnati: 11/11 (100%)**

## 🎯 Cosa è stato consegnato

### Frontend (React)
Creati i seguenti artefatti:
- `package.json` con tutte le dipendenze (React 18, Vite 5, Ant Design 5.x, React Router 6, React Query)
- Struttura file organizzata: `src/layouts`, `src/pages`, `src/services`, `src/hooks`, `src/components`
- Configurazione Vite proxy verso backend (localhost:8080)
- React Router 6 con routing completo (Dashboard, ApiList, ApiDetail, NotFound)
- React Query per state management e caching dei dati
- Layout MainLayout con Ant Design: Sider (navigation), Header (brand), Content (main area)
- Pagine implementate:
  - Dashboard: visualizzazione statistiche, card count (Total APIs, Published, Drafts)
  - ApiList: tabella con CRUD (create, read, update, delete), filtri, paginazione
  - ApiDetail: visualizzazione dettagli con tab (Overview, Versions, Documentation)
- `apiService.js` con metodi HTTP wrapper: GET, POST, PUT, DELETE, PATCH
- Custom hooks con React Query: `useApis()`, `useApiDetail()`, `useCreateApi()`, ecc.
- Styling responsive e dark mode ready

### Backend (Spring Boot)
Creati i seguenti artefatti:
- `pom.xml` con dipendenze: Spring Boot 3.2.5, Spring Data JPA, H2, Lombok, SpringDoc OpenAPI, Spring Security (configurato per CORS)
- Entità JPA:
  - `ApiDefinition`: id, title, description, basePath, status, visibility, createdAt, updatedAt
  - `ApiVersion`: id, apiDefinition, version, status, createdAt, releasedAt
- Enum per validazione: `ApiStatus` (DRAFT, PUBLISHED, DEPRECATED), `ApiVisibility` (PUBLIC, PRIVATE, TEAM), `VersionStatus` (DRAFT, RELEASED, ARCHIVED)
- DTO Request/Response per separazione modello esterno/interno
- Repository: `ApiDefinitionRepository`, `ApiVersionRepository` (con query custom)
- Service layer:
  - `ApiDefinitionService`: logica CRUD con soft delete, validazione
  - `ApiVersionService`: gestione versioni, versionamento semantico
- Controller REST:
  - `ApiDefinitionController`: 5 endpoint (GET /api, POST /api, GET /api/{id}, PUT /api/{id}, DELETE /api/{id})
  - `ApiVersionController`: 4 endpoint (GET /api/{apiId}/versions, POST /api/{apiId}/versions, ecc.)
- CORS configurato per accept origin localhost:3000
- H2 console abilitata su /h2-console (user: sa, no password)
- `data.sql` con 5 API di esempio (Petstore, Todo App, User Service, ecc.)
- Test base con JUnit 5 e Mockito
- Endpoint totali documentati: 9 REST API

### Infrastruttura
- `.gitignore` completo: Node modules, Maven target, IDE specifiche (IntelliJ, VS Code), OS specifiche (macOS, Windows)
- `README.md` professionale con:
  - Badge (build status, license, Java version)
  - Descrizione progetto
  - Istruzioni setup frontend e backend
  - Comandi di avvio
  - Struttura repository
  - Contribuzione guidelines
- GitHub Actions CI/CD configurati:
  - `frontend-ci.yml`: Node 20, npm ci, npm run lint, npm run build (caching dipendenze)
  - `backend-ci.yml`: Java 17, mvn verify, sonarqube ready (configurato per future integrazioni)
- Repository pushato su https://github.com/antocecere77/swagger-hub-clone con branch protection su main

## 📊 Velocity & Metriche
- Story Points pianificati: 11
- Story Points completati: 11
- Velocity Sprint 0: 11 SP
- Story completate: 5/5 (100%)
- Bug riscontrati e risolti: 1 (token GitHub mancante scope "workflow" — risolto da PO in 30 min)
- Assenze: nessuna
- Code Review tempo medio: 4 ore
- Tempo setup ambiente: 20 min per developer

## 🔍 Sprint Review — Demo highlights
Cosa è immediatamente funzionante:

1. **Frontend live:**
   - Clone repository e lanciare `npm install && npm run dev` → applicazione su http://localhost:3000
   - Dashboard con statistiche mock
   - Lista API con tabella filtrata e paginata
   - Form creazione/modifica API con validazione client

2. **Backend live:**
   - Lanciare `mvn spring-boot:run` → server su http://localhost:8080
   - H2 console su http://localhost:8080/h2-console (credenziali di default)
   - Swagger UI documentazione su http://localhost:8080/swagger-ui.html
   - 5 API di esempio già presenti nel database in-memory

3. **Integrazione:**
   - Frontend comunica con backend attraverso proxy Vite
   - CORS abilitato e testato
   - Messaggi di errore gestiti lato client

4. **CI/CD:**
   - Ogni push a main triggera build automatico
   - Feedback immediato su errori di compilazione/test

## 🔄 Retrospettiva

### ✅ What went well (cosa ha funzionato)

**Parallelizzazione frontend e backend**
- Due developer indipendenti hanno lavorato in parallelo senza bottleneck
- Schema DB definito subito ha permesso contratti API chiari da inizio sprint
- Tempo di setup ridotto a metà rispetto a stima iniziale

**Struttura progetto**
- Organizzazione delle cartelle chiara e scalabile
- Convenzioni naming coerenti in tutto il monorepo
- Package.json e pom.xml ben documentati

**Comunicazione team**
- Kanban aggiornata giornalmente con visibilità task
- Daily standup di 10 min, sufficienti per coordinamento
- Slack per risoluzioni veloci di impedimenti

**Schema DB solido**
- Design ER semplice ma completo
- Entità JPA ben strutturate
- Soft delete implementato per audit trail

### 🔧 What to improve (cosa migliorare)

**Visibilità in tempo reale**
- Kanban iniziale mostrava sprint alla volta senza vista team
- Azione: aggiungere view "Chi fa cosa" che mostri sviluppatori e task assegnati in real-time

**Configurazione CI/CD**
- Token GitHub generato senza scope "workflow" inizialmente
- Azione: creare checklist pre-sprint per verificare permessi token prima di configurare pipeline

**Testing**
- Test base scritti ma copertura bassa (40%)
- Azione: per Sprint 1, aggiungere unit test coverage target 70% e integration test per endpoint

**Documentazione API**
- Swagger yml correttamente generato da Spring Boot, ma manca documentazione delle DTO
- Azione: arricchire annotazioni @ApiParam, @ApiResponse per Swagger più leggibile

### 💡 Action Items per Sprint 1

1. **Process improvement:**
   - Aggiungere sempre la view "Chi fa cosa" alla Kanban da subito (template standard)
   - Creare checklist pre-sprint per setup infrastruttura (token, credenziali, permessi)

2. **Technical debt:**
   - Aggiungere integration test per gli 9 endpoint REST
   - Implementare logging strutturato (SLF4J + Logback)
   - Aggiungere validazione input con @Valid e custom validators

3. **Daily practice:**
   - Daily standup: aggiornare la Kanban in tempo reale durante sviluppo (non al termine della giornata)
   - Code review: assegnare reviewer entro 2 ore da PR creation
   - Merge: squash commits per history pulito

## 🚀 Ready for Sprint 1?

### Pre-conditions verificate
- [x] Repository GitHub funzionante con CI/CD automatico
- [x] Frontend avviabile su localhost:3000 senza errori
- [x] Backend avviabile su localhost:8080 senza errori
- [x] Schema DB definito, validato e testato con data di esempio
- [x] 9 endpoint REST documentati, contrattati con frontend e testabili con Postman/Swagger
- [x] Team allineato su architettura (React + Ant Design, Spring Boot 3.2.5)
- [x] Ambiente CI/CD in produzione con successo
- [x] Backlog per Sprint 1 raffinato e stimato

### Sprint 1 Preview

**Goal:** CRUD completo su backend con validazione, paginazione e versioning base

**Descrizione:**
Implementare logica backend completa per gestione API:
- Validazione input (title minLength, description format)
- Paginazione con sorting e filtri avanzati
- Versionamento base (CREATE VERSION, RELEASE VERSION)
- Soft delete con tracciamento data cancellazione
- Audit trail (createdBy, updatedBy, timestamps)

**Durata:** 2 settimane (20-30 Aprile 2026)

**Story Points pianificati:** 19 SP
- S1-1: Validazione completa endpoint ApiDefinition (3 SP)
- S1-2: Paginazione e filtri ApiList con sorting (3 SP)
- S1-3: CRUD ApiVersion con versionamento (5 SP)
- S1-4: Soft delete e audit trail (2 SP)
- S1-5: Integration test 100% endpoint coverage (4 SP)
- S1-6: Documentazione API con Swagger avanzato (2 SP)

**Team focus:** Backend Dev (BE) + QA + 1 FE supporto integrazioni

**Dipendenze:** nessuna blockkante

⏳ **In attesa di autorizzazione PO per avviare Sprint 1 il 20 Aprile 2026**

---
*Documento generato il 12 Aprile 2026 — SwaggerHub Clone Team*
*Facilitator: Scrum Master | Approvato da: Product Owner*
