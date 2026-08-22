FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src/ src/
RUN chmod +x mvnw \
    && ./mvnw --batch-mode clean package -DskipTests \
    && cp target/gym-api-*.jar /workspace/application.jar

FROM eclipse-temurin:17-jre-alpine AS runtime

RUN addgroup -S gym --gid 10001 \
    && adduser -S gym --uid 10001 -G gym

WORKDIR /app
COPY --from=build --chown=gym:gym /workspace/application.jar application.jar

USER 10001:10001
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/application.jar"]
