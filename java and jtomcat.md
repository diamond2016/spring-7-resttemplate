sdk list java
25.0.2-tem is installed (Java 25)
sdk install java 21.0.10-tem
set 21 as java jdk for project "server" (spring7-rest-mvc) which uses java 21
to use a specific versione if .sdmkanrc presente use `sdk env` to set the java version for the project

lsof -i :8080 to see who is using port 8080
kill -9 <PID> to kill the process using port 8080
in this project tomcat "client" (spring7-resttemplate) is 8081 while "server" is 8080 (other project "spring-7-rest-mvc" uses 8080)