# SwaggerHub Clone — Sprint Planning

## Sprint Goal
Deliver a fully functional SwaggerHub clone with authentication, API spec management (CRUD + versioning), organizations, real-time collaboration via WebSocket, and a rich React editor with Monaco + Swagger preview. The system must be containerized with Docker and deployable in one command.

---

## User Stories

### US-01 — User Registration
**As a** new visitor  
**I want to** create an account with email and password  
**So that** I can start managing my API specifications  

**Acceptance Criteria:**
- Email must be unique and valid format
- Password must be at least 8 characters
- On success, receive JWT token pair (access + refresh)
- Return 409 if email already registered

**Story Points:** 3

---

### US-02 — User Login / Logout
**As a** registered user  
**I want to** log in with my credentials and log out securely  
**So that** my account is protected  

**Acceptance Criteria:**
- Login returns access token (24h) and refresh token (7d)
- Logout invalidates refresh token in DB
- Invalid credentials return 401

**Story Points:** 2

---

### US-03 — JWT Token Refresh
**As a** logged-in user  
**I want to** automatically renew my session without re-entering credentials  
**So that** I have an uninterrupted experience  

**Acceptance Criteria:**
- Frontend interceptor calls /api/auth/refresh on 401
- New access token returned; invalid refresh token returns 401
- Refresh token rotated on use

**Story Points:** 3

---

### US-04 — Create API Specification
**As a** developer  
**I want to** create a new API specification (OpenAPI 3.x or Swagger 2.x)  
**So that** I can document my API  

**Acceptance Criteria:**
- Title, description, version required
- Visibility: PRIVATE | PUBLIC | ORG
- Returns 201 with created spec
- Supports association with an organization

**Story Points:** 5

---

### US-05 — Edit API Spec with Monaco Editor
**As a** developer  
**I want to** edit my API spec YAML/JSON in a rich code editor  
**So that** I get syntax highlighting, autocompletion, and error markers  

**Acceptance Criteria:**
- Monaco Editor configured for YAML
- Live parse and validate OpenAPI on every change
- Errors shown inline and in sidebar
- Changes auto-saved with debounce (2s)

**Story Points:** 8

---

### US-06 — Swagger Preview
**As a** developer  
**I want to** see a live rendered Swagger UI next to my editor  
**So that** I can validate the appearance of my documentation  

**Acceptance Criteria:**
- Split-pane view: editor left, preview right
- Preview updates on valid YAML save
- Shows all endpoints, schemas, examples

**Story Points:** 5

---

### US-07 — Spec Versioning
**As a** developer  
**I want to** save named versions of my API spec  
**So that** I can track evolution and roll back  

**Acceptance Criteria:**
- Save version with version number + release notes
- List all versions for a spec
- Load any version into the editor
- Latest version shown by default

**Story Points:** 5

---

### US-08 — Organization Management
**As a** team lead  
**I want to** create an organization and invite members  
**So that** we can collaborate on shared API specs  

**Acceptance Criteria:**
- Create org with name, slug (unique), description
- Invite users by email (OWNER / ADMIN / MEMBER roles)
- Org specs visible to members
- Owner can transfer ownership or delete org

**Story Points:** 8

---

### US-09 — Public API Spec Discovery
**As a** API consumer  
**I want to** browse public API specifications  
**So that** I can find and explore APIs I want to integrate  

**Acceptance Criteria:**
- Public specs searchable without login
- Search by title, tag, organization
- Paginated results (20 per page)
- Filter by format (OpenAPI 3.x, Swagger 2.x)

**Story Points:** 5

---

### US-10 — API Spec Tags
**As a** developer  
**I want to** tag my API specs with keywords  
**So that** they are discoverable by other users  

**Acceptance Criteria:**
- Add/remove tags on spec create/update
- Tags shown on spec card and detail page
- Filter dashboard by tag

**Story Points:** 3

---

### US-11 — User Profile
**As a** registered user  
**I want to** update my profile (name, bio, avatar URL)  
**So that** others can identify me in organizations  

**Acceptance Criteria:**
- Update name, bio, avatar URL
- View other users' public profiles
- Profile shows list of public specs

**Story Points:** 3

---

### US-12 — Real-time Collaboration (WebSocket)
**As a** team member  
**I want to** see who else is editing the same spec  
**So that** we avoid conflicting edits  

**Acceptance Criteria:**
- WebSocket via STOMP shows presence indicators
- Notification when another user saves changes
- Future: operational transformation for concurrent edits

**Story Points:** 13

---

## Kanban Board

```
┌─────────────────────────────┬──────────────────────────────┬─────────────────────────────┐
│           TO DO             │         IN PROGRESS          │            DONE             │
├─────────────────────────────┼──────────────────────────────┼─────────────────────────────┤
│ US-12 Collaboration WS (13) │ US-05 Monaco Editor (8)      │ US-01 Registration (3)      │
│ US-10 Tags filter UI (3)    │ US-08 Organizations (8)      │ US-02 Login/Logout (2)      │
│ US-11 Profile page (3)      │ US-07 Versioning (5)         │ US-03 Token Refresh (3)     │
│                             │ US-09 Public Discovery (5)   │ US-04 Create Spec (5)       │
│                             │                              │ US-06 Swagger Preview (5)   │
│                             │                              │ US-10 Tags (3)              │
└─────────────────────────────┴──────────────────────────────┴─────────────────────────────┘
```

**Sprint Velocity Target:** 55 story points  
**Sprint Duration:** 2 weeks  
**Team Size:** 5 engineers (Marco, Sara, Luca, Elena, Giulia)

---

## Technical Risks

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|-----------|
| Monaco Editor bundle size | Medium | Medium | Lazy loading + code splitting |
| YAML validation complexity | Medium | High | Use js-yaml + openapi-parser lib |
| JWT secret rotation | Low | High | Store in env vars, rotate procedure documented |
| PostgreSQL UUID gen_random_uuid() requires pgcrypto | Low | High | Verify extension or use sequence |
| WebSocket CORS with Spring Security | High | Medium | Explicit WebSocket CORS config |
| Concurrent edit conflicts | Medium | High | Pessimistic locking or OT in v2 |
| Docker build time for Maven | Medium | Low | Multi-stage with layer caching |
| swagger-ui-react bundle conflicts | Low | Medium | Pin to compatible version |
