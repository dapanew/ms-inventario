FROM openjdk:17-jdk

WORKDIR /app
COPY target/*.jar micro.jar

CMD java $JAVA_OPTS -jar /app/micro.jar