INSERT INTO api_definitions (name, description, category, visibility, status, owner_id, tags, created_at, updated_at)
VALUES
    ('Petstore API', 'A sample API that uses Petstore schema', 'Sample', 'PUBLIC', 'ACTIVE', 'default-user', 'sample,petstore,openapi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Payment Gateway API', 'Payment processing and management API', 'Finance', 'PRIVATE', 'ACTIVE', 'default-user', 'payment,gateway,finance', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('User Management API', 'User authentication and profile management', 'Users', 'PUBLIC', 'ACTIVE', 'default-user', 'users,auth,management', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Inventory API', 'Product inventory and stock management', 'E-Commerce', 'TEAM', 'ACTIVE', 'default-user', 'inventory,products,stock', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('Notification API', 'Email and push notification service', 'Communication', 'PUBLIC', 'ACTIVE', 'default-user', 'notifications,email,push', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO api_versions (api_definition_id, version_number, spec, spec_format, status, changelog, created_at, updated_at)
VALUES
    (1, '1.0.0', 'openapi: 3.0.0
info:
  title: Petstore API
  version: 1.0.0
paths:
  /pets:
    get:
      summary: List all pets
      operationId: listPets
      responses:
        "200":
          description: Successful response', 'YAML', 'PUBLISHED', 'Initial release', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, '2.0.0', 'openapi: 3.0.0
info:
  title: Petstore API
  version: 2.0.0
paths:
  /pets:
    get:
      summary: List all pets with pagination
      operationId: listPets
      parameters:
        - name: limit
          in: query
          schema:
            type: integer
      responses:
        "200":
          description: Successful response', 'YAML', 'DRAFT', 'Added pagination support', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, '1.0.0', 'openapi: 3.0.0
info:
  title: Payment Gateway API
  version: 1.0.0
paths:
  /payments:
    post:
      summary: Process a payment
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
      responses:
        "201":
          description: Payment created', 'YAML', 'PUBLISHED', 'Initial release', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, '1.0.0', 'openapi: 3.0.0
info:
  title: User Management API
  version: 1.0.0
paths:
  /users:
    get:
      summary: Get all users
      responses:
        "200":
          description: List of users', 'YAML', 'PUBLISHED', 'Initial release', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, '1.0.0', 'openapi: 3.0.0
info:
  title: Inventory API
  version: 1.0.0
paths:
  /products:
    get:
      summary: Get product inventory
      responses:
        "200":
          description: Product list', 'YAML', 'PUBLISHED', 'Initial release', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
