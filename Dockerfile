#24.9:

FROM openjdk:18-jdk-oracle

WORKDIR /app

#ARG, é dockerfile, (24.11)
ARG JAR_FILE

COPY target/${JAR_FILE} /app/api.jar
#24.14 wait-for-it
COPY wait-for-it.sh /wait-for-it.sh
#define que o wait-for-it é um executavel
RUN chmod +x /wait-for-it.sh


# nao publica a porta. É mais pra documentacao mesmo
EXPOSE 8080

CMD ["java", "-jar", "api.jar"]