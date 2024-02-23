FROM openjdk:21

COPY ./out/artifacts/DataTuch_jar/DataTuch.jar /app/DataTuch.jar

CMD ["java", "-jar", "DataTuch.jar"]

ENV POSTGRES_DB=my_database
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=1488

# Копируем SQL скрипт, если нужно
# COPY init.sql /docker-entrypoint-initdb.d/

# Устанавливаем рабочую директорию
WORKDIR /app