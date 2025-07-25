# Étape de build
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copier uniquement les fichiers de dépendances pour optimiser le cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le reste du code et construire
COPY src ./src
RUN mvn package -DskipTests

# Étape de runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Installer les outils de monitoring
RUN apk add --no-cache curl

# Créer un utilisateur non-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/Streaming-0.0.1-SNAPSHOT.jar app.jar

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms512m -Xmx1024m"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s CMD curl -f http://localhost:8080/actuator/health || exit 1

# Port et commande de démarrage
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
