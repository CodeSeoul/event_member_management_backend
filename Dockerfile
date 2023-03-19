FROM eclipse-temurin:17-alpine

EXPOSE 8080

ENV DATABASE_HOST="postgres"
ENV DATABASE_USERNAME="manager"
ENV DATABASE_PASSWORD="devpassword"
ENV DATABASE_PORT=5432

RUN mkdir /app

COPY ./build/libs/event_member_management-*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]