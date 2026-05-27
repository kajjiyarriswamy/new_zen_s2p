# Procurement Microservices Project Documentation

## 1. Project Overview

This project is a procurement system built as a microservices application. It supports multiple modules, user roles, workflows, and approval paths. The system is designed so a developer can understand how requests flow from the client through an API gateway to individual services and how components are deployed in the cloud.

### 1.1 Main Modules

- **Dashboard**: Central entry screen showing summary metrics, alerts, and pending approvals.
- **Purchase Request (PR)**: Create, review, and approve purchase requests.
- **Purchase Order (PO)**: Create POs from PRs or Bids, review, and approve them.
- **Invoice**: Vendor raises invoices against receipts and finance approves and pays.
- **User Management**: Manage users, roles, departments, and access.
- **Reports**: Generate reports across PR, PO, invoices, bids, delivery, and budget.
- **Bids**: Manage RFQ, RFP, Tender, Auctions, and supplier bidding processes.
- **Administration**: Configuration, master data, policies, and audit settings.
- **Vendor**: Vendor portal for delivery notes, invoices, and bidding documents.
- **Budget**: Budget validation before PR/PO creation.

### 1.2 Key Roles

- **Department User**: Creates PRs.
- **Department Head**: Approves PRs.
- **Procurement Officer**: Creates POs.
- **Procurement Manager**: Approves POs.
- **Vendor**: Raises delivery note and invoice.
- **Buyer or Receipt Owner**: Raises receipt after delivery.
- **Finance Officer**: Reviews and approves invoices and initiates payment.
- **Finance Manager**: Final invoice approval.
- **Admin**: System-level configuration and management.

### 1.3 High-level Flow

1. Department User creates a PR.
2. Department Head approves the PR.
3. Procurement Officer creates a PO from the approved PR or from a Bid.
4. Procurement Manager approves the PO.
5. Vendor delivers goods/services and creates a Delivery Note.
6. Buyer raises a Receipt against the delivery.
7. Vendor raises an Invoice for the Receipt.
8. Finance Officer verifies and approves the Invoice.
9. Finance Manager does final approval.
10. Finance Officer makes payment.

---

## 2. Architecture Overview

### 2.1 Microservices Architecture

Each major module is implemented as a separate microservice. That means each service can run independently and scale independently.

- `api-gateway`: Entry point for all external requests and central authentication/authorization.
- `po-service`: Handles purchase order logic.
- `pr-service`: Handles purchase request logic.
- `invoice-service`: Handles invoice creation, approval, and payment logic.
- `bid-service`: Handles RFQ/RFP/Tender/Auction workflows.
- `user-service`: Handles users, roles, and departments.
- `report-service`: Returns consolidated reports.
- `budget-service`: Verifies budgets before PR/PO creation.
- `vendor-service`: Handles vendor documents and portal actions.
- `dashboard-service`: Aggregates status and metrics.

### 2.2 API Gateway and Load Balancer

The API gateway is the single public endpoint for the system. It performs:

- Authentication and JWT token validation
- Request routing to appropriate microservice
- Rate limiting, logging, and monitoring
- Service composition for aggregated responses

A load balancer sits in front of the gateway and microservices. It distributes traffic across service instances to ensure high availability and performance.

### 2.3 AWS Cloud Deployment

Suggested AWS architecture:

- **Amazon VPC**: Private network for services.
- **Elastic Load Balancer (ALB)**: Distributes requests to gateway instances.
- **ECS / EKS / EC2**: Host microservice containers or JVM apps.
- **Amazon RDS**: Relational database for transactional data.
- **Amazon S3**: Store reports, bid documents, and invoice attachments.
- **Amazon CloudWatch**: Logs and metrics.
- **AWS Secrets Manager**: Secure secret storage for database credentials and JWT secret.
- **AWS IAM**: Manage access and permissions.

---

## 3. Detailed Project Flow

### 3.1 PR Creation and Approval Flow

1. Department User logs in and opens the PR module.
2. The PR screen collects:
   - Vendor details
   - Budget details
   - PR description
   - Need-by date
   - Total cost, tax, currency
   - Payment terms
3. PR Header stores summary fields.
4. PR Line items store:
   - Line ID
   - Item description
   - Quantity
   - Unit cost
   - Total cost
   - Tax percentage
5. The system sums all line totals into the PR header totals.
6. Budget service verifies available budget.
7. After PR creation, the Department Head reviews and approves it.

### 3.2 PO Creation and Approval Flow

1. Procurement Officer can create PO from:
   - Approved PR
   - Bid (RFQ/RFP/Tender/Auction)
2. PO header contains:
   - Vendor info
   - Budget reference
   - PO description
   - Delivery / payment terms
   - Total cost and tax
   - PO number (e.g., PO_1001)
3. PO line items mirror PR lines.
4. Procurement Manager approves PO.
5. Approved PO becomes the contract for supplier delivery.

### 3.3 Vendor and Receipt Flow

1. Vendor receives an approved PO.
2. Vendor raises a Delivery Note after shipping.
3. Buyer or receiving department raises a Receipt once goods are received.
4. Receipt validates delivery against PO quantities and values.

### 3.4 Invoice and Payment Flow

1. Vendor raises an Invoice against the Receipt.
2. Finance Officer reviews invoice details.
3. Finance Officer approves or rejects the invoice.
4. Finance Manager gives final approval.
5. Finance Officer initiates payment and records payment status.

### 3.5 Bid and Tender Flow

1. Bid module supports:
   - RFQ (Request for Quotation)
   - RFP (Request for Proposal)
   - Tender
   - Auction
2. Buyers invite vendors and publish bid documents.
3. Vendors submit proposals or bids.
4. System compares vendor responses and selects winner.
5. Winning bid can result in PO creation.

---

## 4. API and Data Model Concepts

### 4.1 PR Data Model

- **PR Header**
  - PR Number
  - Department
  - Vendor
  - Budget code
  - Description
  - Required date
  - Currency
  - Total cost
  - Tax
  - Payment terms
  - Status

- **PR Lines**
  - Line ID
  - Item code
  - Item name
  - Quantity
  - Unit cost
  - Line total
  - Tax percentage

### 4.2 PO Data Model

- **PO Header**
  - PO Number
  - Supplier
  - Budget approval
  - Delivery date
  - Payment terms
  - Total amount
  - Tax
  - Currency
  - Status

- **PO Lines**
  - Line ID
  - Item
  - Quantity
  - Unit price
  - Total cost
  - Tax percentage

### 4.3 Invoice Data Model

- Invoice Number
- PO reference
- Receipt reference
- Vendor details
- Invoice date
- Amount and tax
- Payment due date
- Status

### 4.4 Bid Data Model

- Bid ID
- Bid type (RFQ/RFP/Tender/Auction)
- Document attachments
- Bid opening date
- Bidding vendors
- Received proposals
- Award decision

---

## 5. System Architecture Diagram

### 5.1 Logical Architecture

```
User Browser / Mobile App
        |
      CDN / DNS
        |
  Internet Traffic
        |
   AWS ALB / Load Balancer
        |
  API Gateway Service
        |
  --------------------------
  |    |    |    |    |    |
 PR  PO  Invoice  Bid  User Management
Service Service Service Service Service
  |    |      |      |       |
 Budget Report Vendor Dashboard Admin
 Service Service Service Service Service
        |
      Database
```

### 5.2 Architecture Diagrams

#### 5.2.1 Component Diagram

```mermaid
flowchart TB
    A[Client (Browser / Mobile)] --> B[Load Balancer / ALB]
    B --> C[API Gateway]
    C --> D[PR Service]
    C --> E[PO Service]
    C --> F[Invoice Service]
    C --> G[Bid Service]
    C --> H[User Management Service]
    C --> I[Budget Service]
    C --> J[Vendor Service]
    C --> K[Report Service]
    C --> L[Dashboard Service]
    D --> M[(Relational Database)]
    E --> M
    F --> M
    G --> M
    H --> M
    I --> M
    J --> M
    K --> M
    L --> M
    J --> N[S3 / Document Storage]
    K --> N
```

#### 5.2.2 Sequence Diagram - PR to PO Flow

```mermaid
sequenceDiagram
    participant User
    participant Gateway
    participant PRService as PR Service
    participant Budget as Budget Service
    participant Approval as Department Head
    participant POService as PO Service
    participant ProcurementMgr as Procurement Manager

    User->>Gateway: POST /pr/create
    Gateway->>PRService: forward request
    PRService->>Budget: verify budget
    Budget-->>PRService: budget ok
    PRService-->>Gateway: PR created
    Gateway-->>User: 201 Created
    User->>Gateway: POST /pr/{id}/approve
    Gateway->>Approval: request approval
    Approval-->>Gateway: approved
    Gateway-->>POService: create PO from PR
    POService-->>ProcurementMgr: request PO approval
    ProcurementMgr-->>POService: approve PO
    POService-->>Gateway: PO approved
    Gateway-->>User: 200 OK
```

#### 5.2.3 Deployment Diagram

```mermaid
flowchart LR
    subgraph AWS
      direction TB
      LB[Application Load Balancer]
      subgraph Cluster
        direction LR
        GW[API Gateway Instances]
        PRS[PR Service Instances]
        POS[PO Service Instances]
        INV[Invoice Service Instances]
        BID[Bid Service Instances]
        UMS[User Management Instances]
        BUD[Budget Service Instances]
        VNS[Vendor Service Instances]
        RPS[Report Service Instances]
        DSH[Dashboard Service Instances]
      end
      DB[(Amazon RDS)]
      S3[(Amazon S3)]
    end
    LB --> GW
    GW --> PRS
    GW --> POS
    GW --> INV
    GW --> BID
    GW --> UMS
    GW --> BUD
    GW --> VNS
    GW --> RPS
    GW --> DSH
    PRS --> DB
    POS --> DB
    INV --> DB
    BID --> DB
    UMS --> DB
    BUD --> DB
    VNS --> S3
    RPS --> S3
    DSH --> S3
```

### 5.3 Data Flow

- Client calls API Gateway.
- Gateway validates JWT and routes to relevant service.
- Service executes business rules and returns result.
- Gateway forwards response to client.
- For aggregated pages like Dashboard, gateway may call multiple services.

---

## 6. AWS Deployment Architecture

### 6.1 Components

- **VPC**: An isolated network for the application.
- **Subnets**: Public for load balancer, private for services and database.
- **ALB (Application Load Balancer)**: Distributes HTTP traffic across gateway instances.
- **ECS/EKS/EC2**: Deploy microservice containers or application servers.
- **RDS**: Central relational database for transaction data.
- **S3**: Store reports, documents, and attachments.
- **CloudWatch**: Monitor logs, metrics, and alarms.
- **Secrets Manager**: Secure management of secrets.
- **Route 53**: DNS routing.

### 6.2 Deployment Flow

1. User request enters Route 53 and reaches the ALB.
2. ALB forwards traffic to API Gateway instances.
3. API Gateway validates JWT and routes to appropriate microservice.
4. Microservices communicate with RDS and optionally S3.
5. Responses return to API Gateway and back to the user.

---

## 7. API Gateway Role

The API Gateway is the project’s central component. It performs:

- Authentication using JWT
- Request routing
- Authorization enforcement
- Circuit breaking and retry policies
- Logging and metrics
- API versioning and request transformation

### 7.1 Why use an API Gateway?

- Single entry point for clients
- Decouples clients from microservice URLs
- Centralizes security and policies
- Enables routing, monitoring, and analytics

---

## 8. Load Balancer Role

The load balancer ensures high availability and distributes requests across multiple instances.

### 8.1 Why use a load balancer?

- Evenly spread traffic
- Prevent a single service instance from becoming overloaded
- Automatically reroute when instances fail
- Support scale-out under heavy load

---

## 9. Approval Workflow Summary

### 9.1 PR Workflow

- Department User creates PR.
- Budget check occurs.
- Department Head approves PR.
- PR becomes eligible to generate PO.

### 9.2 PO Workflow

- Procurement Officer creates PO from PR or Bid.
- Procurement Manager approves PO.
- PO is sent to Vendor.

### 9.3 Invoice Workflow

- Vendor submits invoice against receipt.
- Finance Officer reviews invoice.
- Finance Manager approves invoice.
- Finance Officer processes payment.

### 9.4 Budget Workflow

- Every PR or PO passes budget validation.
- If budget is insufficient, PR/PO creation is blocked.
- Budget module reports current budget and reserved amount.

---

## 10. Student-Friendly Explanation

### 10.1 Why this project is useful

This procurement system is a real-world example of how modern enterprise applications are built using modular services.

Students can learn:

- How to separate concerns using microservices.
- How to use an API gateway for security and routing.
- How to design approval workflows with roles and stages.
- How to deploy applications in the cloud.
- How to integrate budgeting with procurement.
- How to secure communication using JWT.

### 10.2 How to explain it in class

- Start with the business process: PR → PO → delivery → receipt → invoice → payment.
- Show how each role interacts with the system.
- Explain the microservices structure, with each module owning its data and APIs.
- Describe the API gateway as the front door and AWS ALB as the traffic director.
- Emphasize the importance of budget validation before purchase.
- Use the diagrams in this document to connect user actions to backend services.

---

## 11. Example User Stories

### 11.1 Department user story

“As a department user, I want to create a purchase request with line items so that my department can buy needed goods.”

### 11.2 Procurement officer user story

“As a procurement officer, I want to convert approved PRs to POs so that procurement can fulfill orders.”

### 11.3 Vendor user story

“As a vendor, I want to submit a delivery note and invoice so that I can get paid.”

### 11.4 Finance manager user story

“As a finance manager, I want to approve invoices so that payments are authorized and correct.”

---

## 12. Additional Notes

- The gateway and services can be developed using Spring Boot and Spring Cloud Gateway.
- The JWT secret must be securely stored, not hard-coded, in production.
- In a real deployment, use a database such as AWS RDS and a secrets store.
- The bid module should support standard bid formats and vendor evaluation.
- Reports can be generated from the transaction database or a data warehouse.
- The dashboard should show pending approvals, budgets, invoices, and delivery status.

---

## 13. Recommended Learning Path

1. Learn the procurement business process.
2. Understand REST APIs and HTTP methods.
3. Study microservices architecture.
4. Learn JWT authentication.
5. Learn how API gateways work.
6. Learn basic AWS cloud architecture components.
7. Practice building a small service and exposing it through a gateway.

---

## 14. Summary

This project shows how a procurement application is built as a set of microservices with role-based workflows. It combines procurement business logic with modern cloud architecture, authentication, and scalable design. Students can use this document to understand both the business process and the system engineering behind it.
