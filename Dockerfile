FROM openjdk:8-jre-alpine
EXPOSE 8888
ADD /target/school-pgi-0.0.1-SNAPSHOT.jar spgi.jar
ENTRYPOINT ["java","-jar","spgi.jar"]