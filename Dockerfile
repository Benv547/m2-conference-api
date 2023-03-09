# Utilisez une image pour maven
FROM maven:3.8.1-openjdk-17-slim AS build

WORKDIR /usr/src/app

# Copier le code source dans le conteneur
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

# Construire l'application
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

# Utilisez une image de base officielle de Java 17
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /usr/src/app/target/*.jar /app/app.jar

# Exposer le port 8080 pour l'application
EXPOSE 3000

# Définir la commande de démarrage pour l'application
CMD ["java", "-jar", "/app/app.jar"]