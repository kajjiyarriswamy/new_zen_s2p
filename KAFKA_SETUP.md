# Kafka setup and testing (Project-level)

This document explains how to run Kafka locally for this microservices project using Docker Desktop, how to create topics, and how to test the services which publish events.

Important: you said Kafka is not installed in Docker — the steps below will start Kafka using Docker Compose (no preinstalled Kafka required).

## 1) Files added

- `docker-compose.kafka.yml` — minimal Zookeeper + Kafka compose file (maps Kafka to `localhost:9092` via `host.docker.internal` for Docker Desktop).

## 2) Start Kafka (Docker Desktop)

1. Open Docker Desktop and ensure it is running and has sufficient resources (RAM/CPU).
2. From the repository root run:

```bash
docker compose -f docker-compose.kafka.yml up -d
```

3. Check containers:

```bash
docker ps
```

Kafka should expose port `9092` on the host. On Windows/Mac Docker Desktop we set `KAFKA_ADVERTISED_LISTENERS` to `host.docker.internal:9092` so services running on the host can connect to it as `localhost:9092`.

## 3) Create topics

Replace `<kafka_container>` with the kafka container name (listed by `docker ps`, typically `projectname_kafka_1`):

```bash
docker exec -it <kafka_container> bash
# inside container
kafka-topics --create --topic pr-created --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic po-created --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic budget-created --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic vendor-signedup --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic user-created --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic org-created --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
exit
```

Note: Some images use `kafka-topics.sh` or place the binaries in `/usr/bin` or `/opt/bitnami/kafka/bin/`; adjust path if needed.

## 4) Run the microservices

Start each updated service using Maven (one terminal per service) from the project root.

If you see `mvn : The term 'mvn' is not recognized`, Maven is not installed or not on your PATH. Install Maven for Windows and reopen your terminal, or run the service from your IDE if it can launch Spring Boot directly.

```bash
cd pr-service
mvn spring-boot:run
# open another terminal
cd po-service
mvn spring-boot:run
# budget-service
cd budget-service
mvn spring-boot:run
# admin-service
cd admin-service
mvn spring-boot:run
# user-service
cd user-service
mvn spring-boot:run
# vendor-service
cd vendor-service
mvn spring-boot:run
# api-gateway
cd api-gateway
mvn spring-boot:run
```

The gateway will route calls to services. Default ports are set in each service `application.yml`.

> Windows tip: If you later add a Maven wrapper, use `mvnw.cmd spring-boot:run` instead of `mvn spring-boot:run`.

## 5) Send test requests (examples)

- Create a PR (goes to `pr-created` topic):

```bash
curl -X POST http://localhost:8080/pr/create \
 -H "Content-Type: application/json" \
 -d '{"prNumber":"PR_TEST_1","department":"IT","requester":"alice","budget":1000,"description":"Test PR"}'
```

- Create a PO (goes to `po-created` topic):

```bash
curl -X POST http://localhost:8080/po/create \
 -H "Content-Type: application/json" \
 -d '{"poNumber":"PO_TEST_1","vendor":"Vendor A","amount":500,"description":"Test PO"}'
```

- Signup vendor (goes to `vendor-signedup` topic):

```bash
curl -X POST http://localhost:8080/vendor/signup \
 -H "Content-Type: application/json" \
 -d '{"companyName":"Acme Supplies","emailId":"acme@example.com"}'
```

## 6) Observe messages

- Option A: use Kafka console consumer inside the container:

```bash
docker exec -it <kafka_container> bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic pr-created --from-beginning
```

- Option B: check the service logs where the consumer is registered (services log received messages to their console). For example, the `po-service` consumer logs:

```
[po-service] Received kafka message on po-created: { ... }
```

## 7) Troubleshooting

- If services cannot reach Kafka on `localhost:9092`, try replacing `KAFKA_ADVERTISED_LISTENERS` with `PLAINTEXT://localhost:9092` in `docker-compose.kafka.yml` and restart compose. On Windows you may need `host.docker.internal`.
- If Docker Desktop lacks resources, increase memory/CPU in Docker Desktop Settings.
- If the image's CLI tool names differ (e.g., `kafka-topics.sh` vs `kafka-topics`), locate the binary inside the container (e.g. `which kafka-topics` or check `/opt/bitnami/kafka/bin`).

## 8) Next steps (optional)

- I can add a `make` target or small `scripts/` helper to create topics automatically.
- I can run the compose file here and create topics for you if you want (I cannot run Docker on your machine).

---

If you'd like, I will add a small `scripts/create-topics.sh` that uses `docker exec` to create the topics automatically. Say "add script" and I will create it.
