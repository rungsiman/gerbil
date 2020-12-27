#############################
# BUILD THE WAR FILE
#############################

FROM maven:3.6.0-jdk-8 AS build

COPY src /gerbil/src/
COPY repository /gerbil/repository/
COPY pom.xml /gerbil/

# overwrite gerbil-data path: 
COPY docker-config/* /gerbil/src/main/properties/

WORKDIR /gerbil/

RUN mvn package -U -DskipTests

#############################
# BUILD THE DOCKER CONTAINER
#############################

FROM tomcat:7-jre8-alpine

COPY --from=build /gerbil/target/gerbil-*.war $CATALINA_HOME/webapps/gerbil.war

CMD catalina.sh run
