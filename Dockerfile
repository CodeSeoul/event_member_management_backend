FROM eclipse-temurin:17-jdk

EXPOSE 8080

ENV DATABASE_HOST="postgres"
ENV DATABASE_USERNAME="manager"
ENV DATABASE_PASSWORD="devpassword"
ENV DATABASE_PORT=5432

RUN mkdir /app

COPY ./build/libs/event_member_management*.jar /app/event_member_management.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/event_member_management.jar"]