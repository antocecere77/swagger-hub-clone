# SwaggerHub Clone

[![Build Status](https://github.com/antocecere77/swagger-hub-clone/actions/workflows/frontend-ci.yml/badge.svg)](https://github.com/antocecere77/swagger-hub-clone/actions)
[![Build Status](https://github.com/antocecere77/swagger-hub-clone/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/antocecere77/swagger-hub-clone/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://react.dev)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)

## Descrizione del Progetto

SwaggerHub Clone è un'applicazione web completa per la gestione e documentazione di API REST. Consente di creare, versioning, e documentare API specification in formato OpenAPI/Swagger.

## Screenshot

[Screenshot placeholder - Dashboard principale con lista API]

## Stack Tecnologico

### Frontend
- **React** 18 - UI library
- **Vite** - Build tool
- **Ant Design** 5.x - Component library
- **React Router** - Client-side routing
- **Axios** - HTTP client

### Backend
- **Spring Boot** 3.2 - Application framework
- **H2 Database** - In-memory database
- **Spring Data JPA** - ORM
- **Spring Web** - REST API support

## Prerequisites

- **Node.js** 18+ (con npm)
- **Java** 17+ (JDK)
- **Maven** 3.8+

## Getting Started

### Frontend

```bash
cd frontend
npm install
npm run dev
```

L'applicazione sarà disponibile su: `http://localhost:3000`

### Backend

```bash
cd backend
mvn spring-boot:run
```

Il server sarà disponibile su: `http://localhost:8080`

## URL Importanti

| Componente | URL |
|-----------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| H2 Console | http://localhost:8080/h2-console |
| Swagger UI | http://localhost:8080/swagger-ui.html |

## Struttura del Progetto

```
swagger-clone/
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   └── App.jsx
│   ├── package.json
│   ├── vite.config.js
│   └── index.html
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/swagger/clone/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── entity/
│   │   │   │   ├── repository/
│   │   │   │   └── config/
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── data.sql
│   │   └── test/
│   ├── pom.xml
│   └── mvn wrapper files
├── .github/
│   └── workflows/
│       ├── frontend-ci.yml
│       └── backend-ci.yml
├── README.md
├── .gitignore
└── .git/
```

## Sprint Roadmap

### Sprint 0 - Project Setup ✓
- Project scaffolding
- GitHub Actions CI/CD
- Basic project structure

### Sprint 1 - API CRUD
- Create API definition
- Read API definitions
- Update API
- Delete API

### Sprint 2 - Versioning
- API version management
- Version history
- Version comparison

### Sprint 3 - Editor
- Visual API editor
- Endpoint configuration
- Parameter management

### Sprint 4 - Documentation
- Auto-generated documentation
- API testing interface
- Export options

### Sprint 5 - Advanced Features
- Collaboration features
- Teams and permissions
- Analytics and monitoring

## Contributing

Le contributions sono benvenute! Per contribuire:

1. Fork il repository
2. Crea un branch per la feature (`git checkout -b feature/AmazingFeature`)
3. Commit i tuoi cambiamenti (`git commit -m 'Add AmazingFeature'`)
4. Push al branch (`git push origin feature/AmazingFeature`)
5. Apri una Pull Request

## License

Questo progetto è licensed sotto la MIT License - vedi il file LICENSE per i dettagli.

---

Developed with care by Antonio Cecere
