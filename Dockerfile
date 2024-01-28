FROM openjdk

WORKDIR /home/project

COPY target/v2-0.0.1-SNAPSHOT.jar v2-0.0.1-SNAPSHOT.jar

EXPOSE 8081

CMD ["java", "-jar", "v2-0.0.1-SNAPSHOT.jar", \
     "--spring.datasource.url=jdbc:mysql://mysql:3306/notes_db", \
     "--spring.datasource.username=username", \
     "--spring.datasource.password=password", \
     "--spring.jpa.hibernate.ddl-auto=update", \
     "--server.port=8081"]
