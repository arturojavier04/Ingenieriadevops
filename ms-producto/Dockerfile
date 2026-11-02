
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn -B -ntp dependency:go-offline

COPY src ./src

RUN mvn -B -ntp -DskipTests package


FROM eclipse-temurin:21-jre

WORKDIR /app

# Instalar curl para comprobar la salud de la API
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/ms-producto-0.0.1-SNAPSHOT.jar app.jar

# Ejecutar con usuario sin privilegios
USER 10001:10001

EXPOSE 8083

# Comprobar que la API responde correctamente
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -fsS http://localhost:8083/api/productos || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
