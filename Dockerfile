#################
# Build the JAR #
#################
FROM gradle:jdk17-jammy as builder
WORKDIR /builder
COPY gradle gradle/
COPY src src/
COPY build.gradle.kts .
COPY gradlew .
COPY settings.gradle.kts .
RUN gradle bootJar

###############
# Run the JAR #
###############
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /builder/build/libs/umcu-api-1.0.jar app.jar
CMD ["java","-jar","app.jar"]
