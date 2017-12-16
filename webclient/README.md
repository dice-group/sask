# sask
Projectgroups Search and Extraction
WebClient for User Interface and MicroService Registrations.

## Getting Started

The following command can be run in every module folder to register the microservices on the localmachine. The Eureka server should be started first.

```
mvn spring-boot:run
```

Release Notes: 
1. Port ID changed from default port.
2. Added Zuul to support Auto Discovery of Microservices through Eureka Server instead of explicitly passing them from UI. New Routes can be added if required.
