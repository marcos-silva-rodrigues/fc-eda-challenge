FROM amazoncorretto:21-alpine AS deps
WORKDIR /app

COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml

RUN ./mvnw dependency:go-offline -DskipTests

FROM deps AS package
WORKDIR /package

COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml

COPY src src
RUN ./mvnw package -DskipTests
RUN mv target/*.jar target/app.jar

FROM amazoncorretto:21-alpine AS final

WORKDIR /app

COPY --from=package /package/target/app.jar ./

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]