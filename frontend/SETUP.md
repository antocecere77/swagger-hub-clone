# SwaggerHub Clone - Frontend Setup

## Installazione

```bash
cd frontend
npm install
```

## Avvio dello sviluppo

```bash
npm run dev
```

L'applicazione sarà disponibile a `http://localhost:3000`

## Build per produzione

```bash
npm run build
```

## Linting

```bash
npm run lint
```

## Stack tecnologico

- **React 18.3** - UI library
- **Vite 5** - Build tool e dev server
- **Ant Design 5.x** - UI component library
- **React Router 6** - Client-side routing
- **React Query (TanStack Query v5)** - Server state management
- **Axios** - HTTP client
- **dayjs** - Date formatting

## Struttura del progetto

```
frontend/
├── src/
│   ├── main.jsx           # Entry point React
│   ├── App.jsx            # Route setup
│   ├── layouts/
│   │   └── MainLayout.jsx # Main layout con sidebar, header, footer
│   ├── pages/
│   │   ├── Dashboard/     # Dashboard con statistiche
│   │   ├── ApiList/       # Tabella lista API con CRUD
│   │   └── ApiDetail/     # Dettaglio API con tabs (Editor, Doc, Versions)
│   ├── services/
│   │   └── apiService.js  # Axios instance e API calls
│   ├── hooks/
│   │   └── useApis.js     # React Query custom hooks
│   └── styles/
│       └── global.css     # Stili globali
├── vite.config.js         # Configurazione Vite
├── .env.development       # Variabili ambiente dev
├── .eslintrc.cjs          # ESLint config
└── package.json           # Dipendenze

```

## Route disponibili

- `/` - Dashboard
- `/apis` - Lista API con CRUD
- `/apis/:id` - Dettaglio API

## Funzionalità implementate

### Dashboard
- Statistiche (Total APIs, Published APIs, API Versions)
- Tabella API recenti
- Integrazione React Query

### API List
- Tabella con colonne: Nome, Descrizione, Versione, Status, Owner, Updated, Actions
- Search bar funzionante
- Modal per creare nuova API
- Paginazione
- Delete con conferma
- Edit placeholder

### API Detail
- 3 Tab: Editor, Documentation, Versions
- Placeholder Monaco Editor (Sprint 3)
- Placeholder Swagger UI (Sprint 4)
- Tabella versioni

## API Service

Configurato con Axios:
- Base URL da `.env.development`: `http://localhost:8080/api/v1`
- Interceptor per auth token (Bearer)
- Gestione errori 401 (logout)

### Metodi disponibili

**APIs:**
- `getApis(params)` - GET /apis
- `getApiById(id)` - GET /apis/:id
- `createApi(data)` - POST /apis
- `updateApi(id, data)` - PUT /apis/:id
- `deleteApi(id)` - DELETE /apis/:id
- `searchApis(query)` - GET /apis/search

**Versions:**
- `getVersions(apiId, params)` - GET /apis/:apiId/versions
- `getVersionById(apiId, versionId)` - GET /apis/:apiId/versions/:versionId
- `createVersion(apiId, data)` - POST /apis/:apiId/versions
- `updateVersion(apiId, versionId, data)` - PUT /apis/:apiId/versions/:versionId
- `deleteVersion(apiId, versionId)` - DELETE /apis/:apiId/versions/:versionId
- `publishVersion(apiId, versionId)` - POST /apis/:apiId/versions/:versionId/publish

**Specifications:**
- `getSpecification(apiId, versionId)` - GET /apis/:apiId/versions/:versionId/spec
- `updateSpecification(apiId, versionId, spec)` - PUT /apis/:apiId/versions/:versionId/spec
- `validateSpecification(spec)` - POST /validate-spec

## Custom Hooks

Tutti i custom hooks si trovano in `src/hooks/useApis.js`:

### Query Hooks
- `useApis(params)` - Recupera lista API
- `useApi(id)` - Recupera singola API
- `useVersions(apiId, params)` - Recupera versioni API
- `useVersion(apiId, versionId)` - Recupera singola versione
- `useSpecification(apiId, versionId)` - Recupera spec

### Mutation Hooks
- `useCreateApi()` - Crea nuova API
- `useUpdateApi()` - Aggiorna API
- `useDeleteApi()` - Elimina API
- `useCreateVersion()` - Crea versione
- `useUpdateVersion()` - Aggiorna versione
- `useDeleteVersion()` - Elimina versione
- `usePublishVersion()` - Pubblica versione
- `useUpdateSpecification()` - Aggiorna spec
- `useValidateSpecification()` - Valida spec

## Configurazione Vite

- Port: 3000
- Proxy: `/api` → `http://localhost:8080`
- Plugin React con Fast Refresh

## Prossimi step (Sprint 3-4)

1. **Sprint 3:** Integrazione Monaco Editor per editing swagger/OpenAPI
2. **Sprint 4:** Integrazione Swagger UI per visualizzazione API
3. **Sprint 5:** Autenticazione e autorizzazione
4. **Sprint 6:** Testing e optimizzazione

## Comandi utili

```bash
# Installa dipendenze
npm install

# Avvia dev server
npm run dev

# Build progetto
npm run build

# Preview build locale
npm run preview

# Lint code
npm run lint
```
