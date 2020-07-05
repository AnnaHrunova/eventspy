FROM adoptopenjdk/openjdk14:ubi

ADD ./target/eventmanager.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/eventmanager.jar"]

EXPOSE 6000