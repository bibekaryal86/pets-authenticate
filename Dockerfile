FROM openjdk:11-jre-slim-bullseye
RUN adduser --system --group springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/pets-authenticate.jar
COPY ${JAR_FILE} pets-authenticate.jar
ENTRYPOINT ["java","-jar", \
#"-DSPRING_PROFILES_ACTIVE=docker", \
#"-DTZ=America/Denver", \
#"-DBASIC_AUTH_USR_PETSSERVICE=some_username", \
#"-DBASIC_AUTH_PWD_PETSSERVICE=some_password", \
"/pets-authenticate.jar"]
# provide environment variables in docker-compose
