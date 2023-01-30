FROM openjdk:8
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY ./build/libs/* ./demo.jar
EXPOSE 8080
CMD ["java","-jar","demo.jar"]