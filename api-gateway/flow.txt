# Microservices API Flow - Complete Step by Step Guide

## Overview

This document explains how a request flows through the API Gateway to reach PO and PR services with JWT authentication.

---

## FLOW DIAGRAM

```
┌─────────────┐
│   Client    │
│ (Postman)   │
└──────┬──────┘
       │ 1. POST /auth/login
       ▼
┌──────────────────────────────────┐
│      API GATEWAY (Port 8080)      │
│  ┌────────────────────────────┐   │
│  │  SecurityConfig.java       │   │ Checks if request is allowed
│  └────────────────────────────┘   │
│  ┌────────────────────────────┐   │
│  │  JwtFilter.java            │   │ Validates JWT token
│  └────────────────────────────┘   │
│  ┌────────────────────────────┐   │
│  │  AuthController.java       │   │ Login endpoint
│  └────────────────────────────┘   │
└──────┬──────────────────────────────┘
       │ 2. Returns JWT Token
       │
       │ 3. GET /po/all (with token)
       ▼
┌──────────────────────────────────┐
│      API GATEWAY (Port 8080)      │
│  ┌────────────────────────────┐   │
│  │  JwtFilter.java            │   │ ✓ Token Valid
│  │  - Validates Token         │   │
│  │  - Extracts Username/Role  │   │
│  │  - Sets Authentication     │   │
│  └────────────────────────────┘   │
└──────┬──────────────────────────────┘
       │ 4. Routes to PO Service
       ▼
┌──────────────────────────────────┐
│    PO SERVICE (Port 8081)        │
│  ┌────────────────────────────┐   │
│  │  PoController.java         │   │ Handles /po/all request
│  │  - getAllPurchaseOrders()  │   │
│  └────────────────────────────┘   │
│  ┌────────────────────────────┐   │
│  │  PoData.java               │   │ Data model
│  │  ApiResponse.java          │   │ Response wrapper
│  └────────────────────────────┘   │
└──────┬──────────────────────────────┘
       │ 5. Returns Response with PO data
       ▼
┌─────────────┐
│   Client    │
│ (Postman)   │ Receives 200 OK with data
└─────────────┘
```

---

## DETAILED STEP-BY-STEP FLOW

### **STEP 1: LOGIN REQUEST**

**Client Action:** Send POST request to login

```
POST http://localhost:8080/auth/login
Body: {
  "username": "admin",
  "password": "admin123"
}
```

**What Happens:**

1. **SecurityConfig.java** (Port 8080)
   - Location: `api-gateway/src/main/java/com/gateway/config/SecurityConfig.java`
   - Action: Checks if `/auth/login` path is allowed
   - Result: ✓ PERMITTED (public endpoint)

2. **JwtFilter.java** (Port 8080)
   - Location: `api-gateway/src/main/java/com/gateway/filter/JwtFilter.java`
   - Check Line: `if (path.contains("/auth/login")) { return chain.filter(exchange); }`
   - Action: SKIPS JWT validation for login endpoint
   - Result: ✓ Allowed to proceed

3. **AuthController.java** (Port 8080)
   - Location: `api-gateway/src/main/java/com/gateway/controller/AuthController.java`
   - Method: `login(@RequestBody LoginRequest request)`
   - Actions:
     - Fetches user from database using `UserRepository`
     - Validates password against stored password
     - If valid → calls `JwtService.generateToken(username, role)`
     - Returns `ApiResponse` with JWT token

4. **JwtService.java** (Port 8080)
   - Location: `api-gateway/src/main/java/com/gateway/service/JwtService.java`
   - Method: `generateToken(String username, String role)`
   - Actions:
     - Creates JWT token with username as subject
     - Adds role as claim
     - Sets expiration to 1 hour from now
     - Signs with SECRET key
   - Returns: Encoded JWT token string

5. **LoginResponse.java** (Port 8080)
   - Location: `api-gateway/src/main/java/com/gateway/dto/LoginResponse.java`
   - Contains: token, username, role, expiresIn
   - Wrapped in: `ApiResponse<LoginResponse>`

**Response to Client:**

```json
{
  "statusCode": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin",
    "role": "ADMIN",
    "expiresIn": 3600000
  },
  "timestamp": "2024-05-19T10:35:00"
}
```

---

### **STEP 2: GET PO DATA REQUEST (with JWT Token)**

**Client Action:** Send GET request with JWT token

```
GET http://localhost:8080/po/all
Headers: {
  "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**What Happens:**

#### **PHASE 1: API GATEWAY (Port 8080)**

1. **SecurityConfig.java** (Port 8080)
   - Location: `api-gateway/src/main/java/com/gateway/config/SecurityConfig.java`
   - Check: `exchange.anyExchange().permitAll()`
   - Result: ✓ Request allowed to proceed to filter

2. **JwtFilter.java** (Port 8080)
   - Location: `api-gateway/src/main/java/com/gateway/filter/JwtFilter.java`
   - Processing Chain:

   **Line 1: Check Path**

   ```java
   String path = exchange.getRequest().getPath().toString();
   // path = "/po/all"
   ```

   **Line 2: Skip login endpoint?**

   ```java
   if (path.contains("/auth/login")) { return chain.filter(exchange); }
   // NO - path is /po/all, so continue
   ```

   **Line 3: Extract Authorization Header**

   ```java
   String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
   // authHeader = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
   ```

   **Line 4: Validate Header Format**

   ```java
   if (authHeader == null || !authHeader.startsWith("Bearer ")) {
       exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
       return exchange.getResponse().setComplete();
   }
   // ✓ Valid format - continue
   ```

   **Line 5: Extract Token**

   ```java
   String token = authHeader.substring(7);
   // Removes "Bearer " and gets the actual token
   ```

   **Line 6: Validate Token Signature & Expiration**

   ```java
   if (!jwtService.validateToken(token)) {
       exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
       return exchange.getResponse().setComplete();
   }
   // ✓ Token valid - continue
   ```

3. **JwtService.java** (Port 8080) - Token Validation
   - Location: `api-gateway/src/main/java/com/gateway/service/JwtService.java`
   - Method: `validateToken(String token)`
   - Actions:
     - Parses JWT using the SECRET key
     - Checks signature matches
     - Checks expiration time hasn't passed
   - Result: ✓ Token Valid

4. **JwtFilter.java** (continued) - Extract Claims
   - Location: `api-gateway/src/main/java/com/gateway/filter/JwtFilter.java`
   - Code:

   ```java
   Claims claims = jwtService.extractClaims(token);
   String username = claims.getSubject();        // "admin"
   String role = claims.get("role", String.class); // "ADMIN"
   ```

5. **JwtFilter.java** - Set Authentication Context
   - Creates `UsernamePasswordAuthenticationToken` with:
     - Username: "admin"
     - Authority: "ROLE_ADMIN"
   - Writes to reactive security context
   - Allows request to continue

6. **Gateway Routing** - Forward to PO Service
   - Location: `api-gateway/src/main/resources/application.yml`
   - Route Config:
   ```yaml
   routes:
     - id: po-service
       uri: http://localhost:8081
       predicates:
         - Path=/po/**
   ```

   - Action: Routes `/po/all` to `http://localhost:8081/po/all`

---

#### **PHASE 2: PO SERVICE (Port 8081)**

1. **PoController.java** (Port 8081)
   - Location: `po-service/src/main/java/com/po/controller/PoController.java`
   - Mapping: `@GetMapping("/po/all")`
   - Method: `getAllPurchaseOrders()`
   - Actions:
     - Receives request with authentication context
     - Logs "Fetching all Purchase Orders"
     - Creates sample PO data list
     - Returns `ResponseEntity<ApiResponse<List<PoData>>>`

2. **PoData.java** (Port 8081)
   - Location: `po-service/src/main/java/com/po/dto/PoData.java`
   - Fields:
     - id, poNumber, vendor, amount, status, description
   - Created Objects:

   ```java
   new PoData("PO001", "PO-2024-001", "Vendor A", 5000.00, "APPROVED", "Office Supplies")
   new PoData("PO002", "PO-2024-002", "Vendor B", 15000.00, "PENDING", "IT Equipment")
   ```

3. **ApiResponse.java** (Port 8081)
   - Location: `po-service/src/main/java/com/po/dto/ApiResponse.java`
   - Wraps response with:
     - statusCode: 200
     - message: "Purchase orders retrieved successfully"
     - data: List<PoData>
     - timestamp: Current time

---

### **STEP 3: RESPONSE BACK TO CLIENT**

**Response Flows Back:**

```
PO Service (8081)
       ↓
API Gateway (8080)
       ↓
Client (Postman)
```

**Final Response to Client:**

```json
{
  "statusCode": 200,
  "message": "Purchase orders retrieved successfully",
  "data": [
    {
      "id": "PO001",
      "poNumber": "PO-2024-001",
      "vendor": "Vendor A",
      "amount": 5000.0,
      "status": "APPROVED",
      "description": "Office Supplies"
    },
    {
      "id": "PO002",
      "poNumber": "PO-2024-002",
      "vendor": "Vendor B",
      "amount": 15000.0,
      "status": "PENDING",
      "description": "IT Equipment"
    }
  ],
  "timestamp": "2024-05-19T10:35:00"
}
```

**HTTP Status: 200 OK** ✓

---

## FILE INTERACTION SUMMARY

### **For Login Request (/auth/login):**

1. **SecurityConfig.java** → Allows request
2. **JwtFilter.java** → Skips validation
3. **AuthController.java** → Fetches user, validates password
4. **JwtService.java** → Generates JWT token
5. **LoginResponse.java** → Formats response
6. **ApiResponse.java** → Wraps final response

### **For Authenticated Request (/po/all):**

1. **SecurityConfig.java** → Allows request
2. **JwtFilter.java** → Validates token, extracts claims, sets authentication
3. **JwtService.java** → Validates signature and expiration
4. **application.yml** → Routes to PO service (8081)
5. **PoController.java** → Handles request, fetches PO data
6. **PoData.java** → Creates data objects
7. **ApiResponse.java** → Wraps response

---

## KEY FILES REFERENCE

### **API Gateway (Port 8080):**

| File                | Purpose                  |
| ------------------- | ------------------------ |
| SecurityConfig.java | Allow/Block requests     |
| JwtFilter.java      | Validate JWT token       |
| AuthController.java | Login endpoint           |
| JwtService.java     | Generate/Validate tokens |
| LoginResponse.java  | Login response format    |
| ApiResponse.java    | Generic response wrapper |
| LoginRequest.java   | Login request format     |
| application.yml     | Gateway routes & config  |

### **PO Service (Port 8081):**

| File              | Purpose          |
| ----------------- | ---------------- |
| PoController.java | API endpoints    |
| PoData.java       | PO data model    |
| ApiResponse.java  | Response wrapper |

### **PR Service (Port 8082):**

| File              | Purpose          |
| ----------------- | ---------------- |
| PrController.java | API endpoints    |
| PrData.java       | PR data model    |
| ApiResponse.java  | Response wrapper |

---

## IMPORTANT CONCEPTS

### **JWT Token Structure:**

```
Header.Payload.Signature
```

### **Header:**

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

### **Payload (Claims):**

```json
{
  "sub": "admin", // subject (username)
  "role": "ADMIN", // custom claim
  "iat": 1716105000, // issued at
  "exp": 1716108600 // expiration (1 hour later)
}
```

### **Signature:**

```
HMAC-SHA256(
  Base64(header) + "." + Base64(payload),
  "mysecretkeymysecretkeymysecretkey123456"
)
```

---

## ERROR SCENARIOS

### **Scenario 1: No Authorization Header**

```
Request: GET /po/all (no Authorization header)
JwtFilter: Detects missing header
Response: 401 Unauthorized
```

### **Scenario 2: Invalid Token Format**

```
Request: GET /po/all
Headers: Authorization: InvalidToken
JwtFilter: Header doesn't start with "Bearer "
Response: 401 Unauthorized
```

### **Scenario 3: Expired Token**

```
Request: GET /po/all
Headers: Authorization: Bearer <expired_token>
JwtService.validateToken(): Token expiration passed
Response: 401 Unauthorized
```

### **Scenario 4: Modified Token**

```
Request: GET /po/all
Headers: Authorization: Bearer <tampered_token>
JwtService: Signature verification fails
Response: 401 Unauthorized
```

---

## EXECUTION ORDER CHECKLIST

For successful API call:

- [ ] **Step 1:** Client is online and API Gateway (8080) is running
- [ ] **Step 2:** PO Service (8081) or PR Service (8082) is running
- [ ] **Step 3:** Database `batch_hyd` exists with user table
- [ ] **Step 4:** User `admin` exists with password `admin123`
- [ ] **Step 5:** Login to get JWT token
- [ ] **Step 6:** Copy token from response
- [ ] **Step 7:** Use token in Authorization header for next request
- [ ] **Step 8:** Request goes through SecurityConfig → JwtFilter → PoController
- [ ] **Step 9:** Response returns with 200 OK and data

---

## DEBUGGING TIPS FOR STUDENTS

**If 401 Unauthorized:**

- Check if Authorization header is present
- Check token format: `Bearer <token>` (space after Bearer)
- Check token hasn't expired (1 hour)
- Check token wasn't modified

**If PO/PR endpoints fail:**

- Verify services are running on correct ports
- Check application.yml gateway routes are correct
- Look at console logs for error messages

**If Login fails:**

- Check database has user table
- Check admin user exists in database
- Check password matches exactly
