# Étape build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copier tout le projet pour garder src intact
COPY . .

# Installer les dépendances Maven sans tests
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

# Étape exécution
FROM maven:3.9.6-eclipse-temurin-17
WORKDIR /app

# Copier tout depuis la build pour garder src et pom.xml
COPY --from=build /app /app

# Exposer le port
EXPOSE 8080

# Lancer l'application avec Maven (pas de jar)
CMD ["mvn", "spring-boot:run"]