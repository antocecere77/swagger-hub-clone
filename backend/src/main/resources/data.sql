INSERT INTO api_definitions (name, description, category, visibility, status, owner_id, tags, created_at, updated_at) VALUES
('Petstore API',      'A sample API for a pet store',                       'E-Commerce',    'PUBLIC', 'ACTIVE', 'default-user', 'pets,store,openapi',         NOW(), NOW()),
('Payment Gateway',   'REST API for payment processing',                     'Finance',       'PUBLIC', 'ACTIVE', 'default-user', 'payments,finance,stripe',    NOW(), NOW()),
('User Management',   'API for user authentication and profile management',  'Identity',      'PUBLIC', 'ACTIVE', 'default-user', 'users,auth,identity',        NOW(), NOW()),
('Inventory Service', 'Microservice for inventory tracking',                  'Logistics',     'TEAM',   'ACTIVE', 'default-user', 'inventory,logistics',        NOW(), NOW()),
('Notification Hub',  'Send emails, SMS and push notifications',              'Communication', 'PUBLIC', 'ACTIVE', 'default-user', 'notifications,email,sms',   NOW(), NOW());

INSERT INTO api_versions (api_definition_id, version_number, spec_format, status, changelog, created_at, updated_at, spec) VALUES
(1, '1.0.0', 'YAML', 'PUBLISHED', 'Initial release', NOW(), NOW(),
'openapi: "3.0.0"
info:
  title: Petstore API
  version: 1.0.0
  description: A sample API for a pet store
paths:
  /pets:
    get:
      summary: List all pets
      operationId: listPets
      responses:
        "200":
          description: A list of pets
  /pets/{petId}:
    get:
      summary: Info for a specific pet
      operationId: showPetById
      parameters:
        - name: petId
          in: path
          required: true
          schema:
            type: string
      responses:
        "200":
          description: Expected response to a valid request'),
(2, '1.0.0', 'YAML', 'PUBLISHED', 'Initial release', NOW(), NOW(),
'openapi: "3.0.0"
info:
  title: Payment Gateway API
  version: 1.0.0
paths:
  /payments:
    post:
      summary: Create a payment
      responses:
        "201":
          description: Payment created
  /payments/{id}:
    get:
      summary: Get payment status
      responses:
        "200":
          description: Payment details'),
(3, '2.1.0', 'YAML', 'DRAFT', 'Added OAuth2 support', NOW(), NOW(),
'openapi: "3.0.0"
info:
  title: User Management API
  version: 2.1.0
paths:
  /users:
    get:
      summary: List users
      responses:
        "200":
          description: List of users
  /users/{id}:
    get:
      summary: Get user by ID
      responses:
        "200":
          description: User details');
