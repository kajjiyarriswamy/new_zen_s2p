# API Gateway Microservices - Testing Guide

## Overview

This guide explains how to test the API flow through the API Gateway with JWT authentication to both PO and PR services.

---

## 1. Authentication Flow

### Step 1: Login to Get JWT Token

**Endpoint:** `POST http://localhost:8080/auth/login`

**Request Body:**

```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Expected Response (Status: 200 OK):**

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
  "timestamp": "2024-05-19T10:30:00"
}
```

**Notes:**

- Copy the `token` value from the response
- This token is valid for 1 hour
- Default users: admin/admin123, user/user123

---

## 2. PO Service APIs

All PO endpoints require the Authorization header with JWT token.

### GET All Purchase Orders

**Endpoint:** `GET http://localhost:8080/po/all`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

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

### GET Purchase Order by ID

**Endpoint:** `GET http://localhost:8080/po/PO001`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

```json
{
  "statusCode": 200,
  "message": "Purchase order retrieved successfully",
  "data": {
    "id": "PO001",
    "poNumber": "PO-2024-001",
    "vendor": "Vendor A",
    "amount": 5000.0,
    "status": "APPROVED",
    "description": "Office Supplies"
  },
  "timestamp": "2024-05-19T10:35:00"
}
```

### POST Create Purchase Order

**Endpoint:** `POST http://localhost:8080/po/create`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
Content-Type: application/json
```

**Request Body:**

```json
{
  "poNumber": "PO-2024-004",
  "vendor": "Vendor D",
  "amount": 7500.0,
  "description": "Supplies Order"
}
```

**Expected Response (Status: 201 Created):**

```json
{
  "statusCode": 201,
  "message": "Purchase order created successfully",
  "data": {
    "id": "PO1716105000000",
    "poNumber": "PO-2024-004",
    "vendor": "Vendor D",
    "amount": 7500.0,
    "status": "PENDING",
    "description": "Supplies Order"
  },
  "timestamp": "2024-05-19T10:35:00"
}
```

### PUT Update PO Status

**Endpoint:** `PUT http://localhost:8080/po/PO001/status?status=APPROVED`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

```json
{
  "statusCode": 200,
  "message": "Purchase order status updated successfully",
  "data": {
    "id": "PO001",
    "poNumber": "PO-2024-001",
    "vendor": "Vendor A",
    "amount": 5000.0,
    "status": "APPROVED",
    "description": "Office Supplies"
  },
  "timestamp": "2024-05-19T10:35:00"
}
```

### GET PO Service Health

**Endpoint:** `GET http://localhost:8080/po/health`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

```json
{
  "statusCode": 200,
  "message": "Health check successful",
  "data": "PO Service is running",
  "timestamp": "2024-05-19T10:35:00"
}
```

---

## 3. PR Service APIs

All PR endpoints require the Authorization header with JWT token.

### GET All Purchase Requests

**Endpoint:** `GET http://localhost:8080/pr/all`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

```json
{
  "statusCode": 200,
  "message": "Purchase requests retrieved successfully",
  "data": [
    {
      "id": "PR001",
      "prNumber": "PR-2024-001",
      "department": "IT Department",
      "requester": "John Doe",
      "budget": 10000.0,
      "status": "SUBMITTED",
      "description": "Server hardware"
    },
    {
      "id": "PR002",
      "prNumber": "PR-2024-002",
      "department": "HR Department",
      "requester": "Jane Smith",
      "budget": 5000.0,
      "status": "APPROVED",
      "description": "Office furniture"
    }
  ],
  "timestamp": "2024-05-19T10:35:00"
}
```

### GET Purchase Request by ID

**Endpoint:** `GET http://localhost:8080/pr/PR001`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

```json
{
  "statusCode": 200,
  "message": "Purchase request retrieved successfully",
  "data": {
    "id": "PR001",
    "prNumber": "PR-2024-001",
    "department": "IT Department",
    "requester": "John Doe",
    "budget": 10000.0,
    "status": "SUBMITTED",
    "description": "Server hardware"
  },
  "timestamp": "2024-05-19T10:35:00"
}
```

### POST Create Purchase Request

**Endpoint:** `POST http://localhost:8080/pr/create`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
Content-Type: application/json
```

**Request Body:**

```json
{
  "prNumber": "PR-2024-004",
  "department": "Finance",
  "requester": "Sarah Wilson",
  "budget": 20000.0,
  "description": "Financial software licenses"
}
```

**Expected Response (Status: 201 Created):**

```json
{
  "statusCode": 201,
  "message": "Purchase request created successfully",
  "data": {
    "id": "PR1716105000000",
    "prNumber": "PR-2024-004",
    "department": "Finance",
    "requester": "Sarah Wilson",
    "budget": 20000.0,
    "status": "SUBMITTED",
    "description": "Financial software licenses"
  },
  "timestamp": "2024-05-19T10:35:00"
}
```

### PUT Update PR Status

**Endpoint:** `PUT http://localhost:8080/pr/PR001/status?status=APPROVED`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

```json
{
  "statusCode": 200,
  "message": "Purchase request status updated successfully",
  "data": {
    "id": "PR001",
    "prNumber": "PR-2024-001",
    "department": "IT Department",
    "requester": "John Doe",
    "budget": 10000.0,
    "status": "APPROVED",
    "description": "Server hardware"
  },
  "timestamp": "2024-05-19T10:35:00"
}
```

### POST Approve Purchase Request

**Endpoint:** `POST http://localhost:8080/pr/PR001/approve`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

```json
{
  "statusCode": 200,
  "message": "Purchase request approved successfully",
  "data": {
    "id": "PR001",
    "prNumber": "PR-2024-001",
    "department": "IT Department",
    "requester": "John Doe",
    "budget": 10000.0,
    "status": "APPROVED",
    "description": "Server hardware"
  },
  "timestamp": "2024-05-19T10:35:00"
}
```

### GET PR Service Health

**Endpoint:** `GET http://localhost:8080/pr/health`

**Headers:**

```
Authorization: Bearer <your_jwt_token>
```

**Expected Response (Status: 200 OK):**

```json
{
  "statusCode": 200,
  "message": "Health check successful",
  "data": "PR Service is running",
  "timestamp": "2024-05-19T10:35:00"
}
```

---

## 4. Error Responses

### 401 Unauthorized (Missing or Invalid Token)

**Request:** `GET http://localhost:8080/po/all` (without Authorization header)

**Response (Status: 401):**

```json
{
  "statusCode": 401,
  "message": "Authorization header missing or invalid format"
}
```

### 403 Forbidden (Insufficient Permissions)

**Scenario:** User with "USER" role trying to access PO endpoints (requires "ADMIN" role)

**Response (Status: 403):**

```json
{
  "statusCode": 403,
  "message": "Access denied"
}
```

### 400 Bad Request (Missing Required Field)

**Request:** `POST http://localhost:8080/po/create` (without poNumber)

**Response (Status: 400):**

```json
{
  "statusCode": 400,
  "message": "PO Number is required",
  "timestamp": "2024-05-19T10:35:00"
}
```

### 404 Not Found

**Request:** `GET http://localhost:8080/po/INVALID_ID`

**Response (Status: 404):**

```json
{
  "statusCode": 404,
  "message": "Purchase Order not found",
  "timestamp": "2024-05-19T10:35:00"
}
```

### 500 Internal Server Error

**Response (Status: 500):**

```json
{
  "statusCode": 500,
  "message": "Error fetching purchase orders",
  "timestamp": "2024-05-19T10:35:00"
}
```

---

## 5. Postman Collection Setup

### In Postman:

1. **Create a collection**: "Microservices API"
2. **Create a folder**: "Auth"
3. **Create requests**:
   - Login → POST to `/auth/login`
4. **Create a folder**: "PO Service"
5. **Create requests**:
   - Get All POs → GET to `/po/all`
   - Get PO by ID → GET to `/po/{id}`
   - Create PO → POST to `/po/create`
   - Update PO Status → PUT to `/po/{id}/status`
   - Health Check → GET to `/po/health`

6. **Create a folder**: "PR Service"
7. **Create requests**:
   - Get All PRs → GET to `/pr/all`
   - Get PR by ID → GET to `/pr/{id}`
   - Create PR → POST to `/pr/create`
   - Update PR Status → PUT to `/pr/{id}/status`
   - Approve PR → POST to `/pr/{id}/approve`
   - Health Check → GET to `/pr/health`

### Set Authorization:

- **Environment Variable**: Create a variable `token` (initially empty)
- **After Login**: Copy token and set it in Postman's environment
- **All Other Requests**: In Headers tab, add:
  ```
  Key: Authorization
  Value: Bearer {{token}}
  ```

---

## 6. Debugging Tips

### View Logs in Console:

- API Gateway logs: Check console output for `[JwtFilter]` messages
- PO Service logs: Check for `[PoController]` messages
- PR Service logs: Check for `[PrController]` messages

### Filter Flow Verification:

1. Request sent to gateway with authorization header
2. JwtFilter validates token
3. Filter extracts username and role from token
4. Request forwarded to appropriate microservice
5. Microservice processes and returns response

### Common Issues:

- **Token Expired**: Login again to get a new token
- **401 Unauthorized**: Check if Authorization header is correctly formatted (Bearer <token>)
- **403 Forbidden**: Verify user role matches endpoint requirements
- **Cannot connect to service**: Ensure PO and PR services are running on ports 8081 and 8082

---

## 7. Service Ports

- **API Gateway**: 8080
- **PO Service**: 8081
- **PR Service**: 8082

---

## 8. Database Configuration

Default users in database:

- **Username**: admin, **Password**: admin123, **Role**: ADMIN
- **Username**: user, **Password**: user123, **Role**: USER

Note: Make sure these users exist in your `batch_hyd` database before testing.
