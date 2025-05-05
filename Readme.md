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
   
2.📦 Empaquetado y Dockerfile multietapa

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
newman run postman_collection.json \
    --env-var "baseUrl=http://localhost:8081" \
    --delay-request 200
```

---

## 🤝 Contribuir

1. Fork & branch: `git checkout -b feature/foo`
2. Commit: `git commit -am 'Add foo'`
3. Push: `git push origin feature/foo`
4. Abrir Pull Request

---

















# APITrain

Este proyecto provee una API REST para gestionar entrenamientos, atletas, eventos, instalaciones y entrenadores. Se acompaña de:

* **Docker Compose** para orquestar la base de datos MySQL y la propia aplicación Spring Boot
* **API Manager** (Apiman) para exponer la API, gestionar planes, cuotas y rate-limits
* **WireMock** standalone para mocks en pruebas de integración
* **Newman** para ejecutar colecciones de Postman
* Tests unitarios e integración con JUnit y Spring Test

---

## 🛠 Requisitos

* Docker & Docker Compose (v3.9+)
* Java 21 JDK
* Maven 3.9+
* Newman (opcional, para pruebas Postman)
* [WireMock Standalone 3.13.0][wiremock] (incluido en el proyecto)

---

## 🚀 Arranque con Docker Compose

En la raíz del proyecto (donde está `docker-compose.yml`):

```bash
export MYSQL_ROOT_PASSWORD=secret
export MYSQL_DATABASE=entrenamiento
export MYSQL_USER=usuario
export MYSQL_PASSWORD=usuario123
export DB_PORT=3306
export API_PORT=8081
export SPRING_PROFILES_ACTIVE=apiman

docker-compose up -d
```

* **MySQL** se levanta en el contenedor `db`
* **Spring Boot** en el contenedor `miapi` (perfil `apiman`)
* **Apiman Manager & Gateway** en los contenedores `apiman-manager` y `apiman-gateway`

Para detener y limpiar volúmenes:

```bash
docker-compose down -v
```

---

## 📑 Configuración de Apiman

1. Accede a la consola de Manager en
   `http://localhost:8080/apiman-manager`
2. Crea tu organización, espacio y sube/importa la API (OpenAPI).
3. **Planes**

   * Crea un “Basic Plan”
   * Agrega políticas de **Quota** (50 peticiones/día) y **Rate Limit** (20 peticiones/minuto)
   * Publica y enlaza el plan a tu API
4. **Client App**

   * Crea una aplicación cliente, obtén su `apikey` y haz un contrato con el plan.

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

Los endpoints están bajo `/apiorga/apitrainss/1.1/...` y requieren el parámetro `?apikey=...`.

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
     --port 8089 \
     --verbose
```

Asegúrate de apuntar tus tests de integración a `http://localhost:8089`.

### Newman (Postman)

```bash
newman run postman_collection.json \
    --env-var "baseUrl=http://localhost:8081" \
    --delay-request 200
```

---

## 📦 Empaquetado y Dockerfile multietapa

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

## 🤝 Contribución

1. Haz un *fork* y crea tu branch:
   `git checkout -b feature/mi-cambio`
2. Asegura que todos los tests pasan
3. Envía tu *pull request* describiendo los cambios

---

## 📄 Licencia

MIT © Tu Nombre

[wiremock]: ./src/wiremock-api-virtual/wiremock-standalone-3.13.0.jar

