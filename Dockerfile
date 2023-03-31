FROM openjdk:17
COPY ./build/libs/ /usr/searcehpost/
ENTRYPOINT ["java","-jar","/usr/searcehpost/searchpostcode-0.0.1-SNAPSHOT.jar"]
