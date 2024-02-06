# Build the application
FROM gradle:8.4.0-jdk17 AS build

RUN useradd -rm -d /home/builder -s /bin/bash -g root -G sudo -u 1001 builder
USER builder
WORKDIR /home/builder/

COPY gradle gradle/
COPY src src/
COPY build.gradle.kts .
COPY gradle.properties .
COPY gradlew .
COPY settings.gradle.kts .

RUN ./gradlew installDist

# Run the application
FROM gradle:8.4.0-jdk17

RUN useradd -rm -d /home/umcu -s /bin/bash -g root -G sudo -u 1001 umcu
USER umcu
WORKDIR /home/umcu/

COPY --from=build /home/builder/build build/
CMD "./build/install/upcomingmcu-api/bin/upcomingmcu-api"
