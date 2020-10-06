FROM openjdk:8
ADD target/search-replace-application.jar search-replace-application.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","search-replace-application.jar"]