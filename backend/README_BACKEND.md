# Swagger Hub Clone - Backend Spring Boot Application

## Overview
A complete, production-ready Spring Boot 3.2.5 backend for a Swagger Hub clone application. This is a fully functional REST API with complete entity models, services, repositories, and controllers.

## Project Details
- **Version**: 0.1.0
- **Java Version**: 17
- **Build Tool**: Maven 3.6+
- **Database**: H2 (in-memory, development)
- **Port**: 8080

## Quick Start

### Prerequisites
```bash
Java 17+
Maven 3.6+
```

### Build
```bash
cd /sessions/eloquent-intelligent-sagan/mnt/Claude/swagger-clone/backend
mvn clean package
```

### Run
```bash
java -jar target/swagger-hub-clone-backend-0.1.0.jar
```

### Access
- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console

## API Endpoints

### API Definitions
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/apis | List all APIs (paginated, searchable) |
| POST | /api/v1/apis | Create new API |
| GET | /api/v1/apis/{id} | Get API details |
| PUT | /api/v1/apis/{id} | Update API |
| DELETE | /api/v1/apis/{id} | Delete API (soft delete) |

### API Versions
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/v1/apis/{apiId}/versions | List API versions |
| POST | /api/v1/apis/{apiId}/versions | Create new version |
| GET | /api/v1/apis/{apiId}/versions/{versionId} | Get version details |
| PUT | /api/v1/apis/{apiId}/versions/{versionId} | Update version |

## Architecture

### Layered Structure
```
Controllers (REST endpoints)
       ↓
Services (Business logic)
       ↓
Repositories (Data access)
       ↓
JPA Entities (Database models)
       ↓
H2 Database (In-memory)
```

### File Organization
```
src/main/
  java/com/swaggerhub/clone/
    ├── controller/          REST API endpoints
    ├── service/             Business logic
    ├── repository/          Data access
    ├── model/               JPA entities & enums
    ├── dto/                 Data transfer objects
    ├── config/              Configuration classes
    └── SwaggerHubCloneApplication.java
  resources/
    ├── application.properties
    └── data.sql

src/test/
  java/com/swaggerhub/clone/
    └── controller/          Integration tests
```

## Technologies

### Core Framework
- **Spring Boot 3.2.5** - Application framework
- **Spring Data JPA** - ORM and data access
- **Spring Web MVC** - REST support
- **Tomcat** - Embedded web server

### Database
- **H2 2.x** - In-memory relational database
- **Hibernate** - JPA implementation

### Validation & Serialization
- **Jakarta Bean Validation** - Request validation
- **Jackson** - JSON serialization/deserialization

### Development Tools
- **Lombok** - Boilerplate reduction
- **SpringDoc OpenAPI 2.5.0** - API documentation
- **JUnit 5** - Testing framework

## Features

### Core Features
1. **REST API** with 9 endpoints
2. **Pagination** and search support
3. **Validation** with Bean Validation
4. **Soft Delete** mechanism
5. **CORS** configuration for frontend
6. **Swagger UI** for API documentation
7. **H2 Console** for database inspection
8. **Type-Safe** generics with PageResponse<T>

### Entity Features
- Automatic timestamp management (@PrePersist/@PreUpdate)
- Relationship mapping (ManyToOne with lazy loading)
- Enum-based status fields
- Soft deletion (status-based)

### Service Features
- Transaction management
- Pagination with Spring Data
- Full-text search
- Manual entity-to-DTO mapping
- Proper exception handling

### Controller Features
- Proper HTTP status codes
- Request validation
- Query parameter pagination
- Path variable routing

## Sample Data

The application comes with 5 sample APIs:
1. **Petstore API** - Sample API (v1.0.0, v2.0.0)
2. **Payment Gateway API** - Finance (v1.0.0)
3. **User Management API** - Users (v1.0.0)
4. **Inventory API** - E-Commerce (v1.0.0)
5. **Notification API** - Communication (v1.0.0)

All with sample YAML specifications and multiple versions.

## Configuration

### Database Configuration
```properties
spring.datasource.url=jdbc:h2:mem:swaggerhubdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
```

### Server Configuration
```properties
server.port=8080
spring.application.name=swagger-hub-clone
```

### CORS Configuration
```java
// Allows requests from: http://localhost:3000
// Methods: GET, POST, PUT, DELETE, OPTIONS
// Headers: All permitted
// maxAge: 3600 seconds
```

## Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
- GET all APIs
- POST create API
- GET single API
- DELETE API

All tests use MockMvc for integration testing.

## Database Schema

### api_definitions table
- id (BIGINT, PK)
- name (VARCHAR, NOT NULL)
- description (TEXT)
- category (VARCHAR)
- visibility (VARCHAR, Enum)
- status (VARCHAR, Enum)
- owner_id (VARCHAR)
- tags (TEXT)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

### api_versions table
- id (BIGINT, PK)
- api_definition_id (BIGINT, FK)
- version_number (VARCHAR)
- spec (TEXT)
- spec_format (VARCHAR)
- status (VARCHAR, Enum)
- changelog (TEXT)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)

## Example Usage

### Create an API
```bash
curl -X POST http://localhost:8080/api/v1/apis \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My New API",
    "description": "API description",
    "category": "Development",
    "visibility": "PUBLIC",
    "tags": "rest,api"
  }'
```

### Get All APIs
```bash
curl "http://localhost:8080/api/v1/apis?page=0&size=10&search="
```

### Get API Details
```bash
curl http://localhost:8080/api/v1/apis/1
```

### Create API Version
```bash
curl -X POST http://localhost:8080/api/v1/apis/1/versions \
  -H "Content-Type: application/json" \
  -d '{
    "versionNumber": "1.0.0",
    "spec": "openapi: 3.0.0\ninfo:\n  title: My API\n  version: 1.0.0",
    "specFormat": "YAML",
    "status": "PUBLISHED",
    "changelog": "Initial release"
  }'
```

### Update API
```bash
curl -X PUT http://localhost:8080/api/v1/apis/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated API",
    "description": "Updated description",
    "category": "Production",
    "visibility": "PRIVATE",
    "tags": "updated"
  }'
```

### Delete API
```bash
curl -X DELETE http://localhost:8080/api/v1/apis/1
```

## Next Steps for Production

1. **Authentication**: Add JWT/OAuth2 security
2. **Database**: Configure PostgreSQL/MySQL
3. **Logging**: Add SLF4J/Logback configuration
4. **Monitoring**: Set up Spring Actuator
5. **Caching**: Add Redis support
6. **Validation**: Enhance with custom validators
7. **Error Handling**: Add global @ControllerAdvice
8. **API Versioning**: Implement header-based versioning
9. **Documentation**: Expand SpringDoc OpenAPI annotations
10. **Deployment**: Create Docker image

## Troubleshooting

### Port Already in Use
```properties
# In application.properties
server.port=8081
```

### CORS Errors
Verify frontend origin in `CorsConfig.java`:
```java
registry.addMapping("/**")
    .allowedOrigins("http://localhost:3000")
```

### Database Not Initializing
Ensure in `application.properties`:
```properties
spring.jpa.defer-datasource-initialization=true
```

### Swagger UI Not Accessible
Verify SpringDoc dependency is included in `pom.xml`

## Additional Documentation

See also:
- `GETTING_STARTED.md` - Setup and usage guide
- `MANIFEST.txt` - Project overview and statistics
- `VERIFICATION_CHECKLIST.txt` - Implementation verification
- `FILES_CREATED.txt` - File creation summary

## License
This is a scaffold project for educational purposes.

## Support
For issues or questions, refer to the documentation files included with the project.
