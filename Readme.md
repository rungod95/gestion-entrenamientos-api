# API Train — Gestión de Entrenamientos Deportivos

**API Train** es un servicio REST para gestionar entrenamientos deportivos, entrenadores, deportistas, eventos e instalaciones. Incluye:

* **OpenAPI 3.0**: Documentación y definición de rutas
* **Spring Boot 3** con JPA/Hibernate y MySQL
* **Docker Compose** para levantar la base de datos y el servicio
* **API Management** con Red Hat API Manager (Gateway + Portal)
* **Seguridad JWT** para autenticación
* **Testing**

  * Unitarios (JUnit + Mockito)
  * Integración (SpringBootTest + WireMock)
  * Newman (Postman Collection Runner)

---

## 📋 Contenidos

* [Requisitos](#-requisitos)
* [Instalación y arranque](#-instalación-y-arranque)
* [API Management](#-api-management)
* [Especificación OpenAPI](#-especificación-openapi)
* [Ejemplos de llamada](#-ejemplos-de-llamada)
* [Testing](#-testing)
* [Postman & Newman](#-postman--newman)
* [WireMock](#-wiremock)
* [Contribuir](#-contribuir)

---

## 🔧 Requisitos

* Docker (>= 20.10) y Docker Compose (>= 1.29)
* Maven (>= 3.6)
* Node.js & npm (para Newman)
* Java 21

---

## 🚀 Instalación y arranque

1. Clona el repositorio:

   ```bash
   git clone git@github.com:TU_USUARIO/apitrain.git
   cd apitrain
   ```
   
2. 📦 Empaquetado y Dockerfile multietapa

El `Dockerfile` usa un build con Maven y luego copia el `.jar`:

```dockerfile
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

---

3. Copia y ajusta variables de entorno en `.env` (basado en `.env.example`):

   ```ini
   MYSQL_ROOT_PASSWORD=usuario123
   MYSQL_DATABASE=entrenamiento
   MYSQL_USER=usuario
   MYSQL_PASSWORD=usuario123
   DB_PORT=3306

   API_PORT=8081
   SPRING_PROFILES_ACTIVE=docker
   ```

3. Levanta los contenedores:

   ```bash
   docker-compose up -d
   ```

   * MySQL en `localhost:${DB_PORT}`
   * API en `http://localhost:${API_PORT}`

4. Detener/reiniciar:

   ```bash
   docker-compose down
   docker-compose up -d
   ```

> **Nota**: Al primer arranque carga `data.sql` en la base; en posteriores los datos persisten en el volumen `db_data`.

---

## 🔐 API Management

Se publica la API con **Red Hat API Manager 3.1.3** (containers `apiman-manager` y `apiman-gateway`):

* **Portal de desarrolladores**: `http://apiman.local.gd:8080/portal/`
* **Gateway**: `http://gateway.local.gd:8080/{org}/{api}/{version}`
* **Planes y políticas**:

  * Plan “Basic Plan” con **Rate Limiting** (20 req/min) y **Quota** (50 req/día)

Para regenerar tras apagar:

```bash
docker-compose down
docker-compose up -d
```

Los contadores de límite se reinician automáticamente según el periodo configurado.

---

## 🔌 Endpoints principales

* **Trainings**
  `GET /trainings`
* **Athletes**
  `GET  /athletes`
* **Events**
  `GET  /events`
* **Facilities**
  `GET  /facilities`

## 📖 Especificación OpenAPI

La definición OpenAPI está en `apitrain.yaml` (OpenAPI 3.0.1). Puedes importarla en Swagger UI o Postman.

---

## ⚙️ Ejemplos de llamada

```bash
# Listar entrenadores
curl -H "Authorization: Bearer $TOKEN" \
     http://localhost:${API_PORT}/trainers

# Crear deportista
curl -X POST http://localhost:${API_PORT}/athletes \
     -H "Content-Type: application/json" \
     -d '{"nombre":"Ana Pérez","categoria":"Amateur","edad":22}'
```

---

## 🧪 Pruebas

### Unitarias & Integración

```bash
mvn clean test
```

### WireMock Standalone

Dentro de `src/wiremock-api-virtual` encontrarás:

```
wiremock-standalone-3.13.0.jar
```

Para levantar el servidor mock en el puerto 8089 (directorio de mappings y \_\_files dentro de la misma carpeta):

```bash
cd src/wiremock-api-virtual
java -jar wiremock-standalone-3.13.0.jar \
     --port 8082 \
     --verbose
```

Asegúrate de apuntar tus tests de integración a `http://localhost:8082`.

### Newman (Postman)

```bash
newman run postman_collection.json 
```

---

## 🤝 Contribuir

1. Fork & branch: `git checkout -b feature/foo`
2. Commit: `git commit -am 'Add foo'`
3. Push: `git push origin feature/foo`
4. Abrir Pull Request

---

















