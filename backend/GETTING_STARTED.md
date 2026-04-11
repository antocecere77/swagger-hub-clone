# Swagger Hub Clone - Backend Setup Guide

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- curl or Postman for API testing

## Building the Project

```bash
cd /sessions/eloquent-intelligent-sagan/mnt/Claude/swagger-clone/backend
mvn clean install
```

## Running the Application

### Option 1: Using Maven
```bash
mvn spring-boot:run
```

### Option 2: Using JAR
```bash
mvn clean package
java -jar target/swagger-hub-clone-backend-0.1.0.jar
```

## Accessing the API

- **Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console

## API Endpoints

### API Definitions
```
GET    /api/v1/apis                    - List all APIs (paginated, searchable)
POST   /api/v1/apis                    - Create new API
GET    /api/v1/apis/{id}               - Get API details
PUT    /api/v1/apis/{id}               - Update API
DELETE /api/v1/apis/{id}               - Delete API
```

### API Versions
```
GET    /api/v1/apis/{apiId}/versions                    - List API versions
POST   /api/v1/apis/{apiId}/versions                    - Create new version
GET    /api/v1/apis/{apiId}/versions/{versionId}        - Get version details
PUT    /api/v1/apis/{apiId}/versions/{versionId}        - Update version
```

## Example Requests

### Create an API
```bash
curl -X POST http://localhost:8080/api/v1/apis \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My API",
    "description": "API Description",
    "category": "Development",
    "visibility": "PUBLIC",
    "tags": "api,rest"
  }'
```

### Get all APIs (with pagination)
```bash
curl "http://localhost:8080/api/v1/apis?page=0&size=10&search="
```

### Create a Version
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

## Database

The application uses H2 (in-memory database) by default. Data is automatically initialized from `data.sql` on startup.

### H2 Console Access
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:swaggerhubdb`
- Username: `sa`
- Password: (empty)

## Testing

Run the test suite:
```bash
mvn test
```

## Project Structure

```
backend/
├── pom.xml                           # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/com/swaggerhub/clone/
│   │   │   ├── SwaggerHubCloneApplication.java
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── service/             # Business logic
│   │   │   ├── repository/          # Data access
│   │   │   ├── model/               # JPA Entities & Enums
│   │   │   └── dto/                 # Data Transfer Objects
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
│       └── java/com/swaggerhub/clone/
│           └── controller/          # Integration tests
```

## Key Features

- **JPA/Hibernate ORM** for database operations
- **Spring Data Repositories** for CRUD operations
- **Bean Validation** for request validation
- **Pagination & Search** support on API listing
- **Soft Delete** mechanism for safe data removal
- **CORS Configuration** for frontend integration
- **SpringDoc OpenAPI** for API documentation
- **Lombok** for boilerplate reduction
- **H2 In-memory Database** for development

## Configuration

Edit `src/main/resources/application.properties` to customize:
- Server port
- Database connection
- JPA/Hibernate settings
- SpringDoc configuration

## CORS Policy

Currently allows requests from:
- `http://localhost:3000`

To modify, edit `src/main/java/com/swaggerhub/clone/config/CorsConfig.java`

## Troubleshooting

### Port already in use
Change the port in `application.properties`:
```properties
server.port=8081
```

### Database not initializing
Ensure `spring.jpa.defer-datasource-initialization=true` is set in `application.properties`

### CORS errors in frontend
Verify the frontend origin is allowed in `CorsConfig.java`

## Next Steps

1. Configure frontend URL in CorsConfig
2. Add authentication/security layer
3. Implement additional business logic
4. Set up production database
5. Add more DTOs and validations as needed
