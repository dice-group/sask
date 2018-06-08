# sask
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b6ad49d1a5cf409e940fb632d3242ab6)](https://www.codacy.com/app/idreestahir/sask?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dice-group/sask&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/dice-group/sask?branch=master)](https://bettercodehub.com/)

Projectgroups Search and Extraction

## Prerequisites
- Java 1.9 or higher
- Docker

## Getting Started

To get started with the application, you need to run the individual microservices and Docker containers. The following command can be executed in every module folder to start and register the microservices in eureka. The Eureka server should be started first.

```
mvn clean spring-boot:run
```

If all microservices are running, but are not available in the application: Wait, till the webclient pulled a refreshed list.

- Currently discovered microservices by the UI: http://localhost:9090/status.html
- Currently registered microservices by eureka: http://localhost:1111/

You will find example data for the extraction in the wiki: https://github.com/dice-group/sask/wiki/Example-Data

## Necessary components
- hadoop docker container [see here](https://github.com/dice-group/sask/wiki/Hadoop)
- jena docker container [see here](https://github.com/dice-group/sask/wiki/Jena-DB-On-Fuseki-Server)
- eureka ms
- webclient ms
- executer ms
- repo ms
- at least one extractor
- database ms
