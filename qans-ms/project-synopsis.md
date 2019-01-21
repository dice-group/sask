# What's the project all about?

#### The objective of the project is to build a SPARQL template that captures the internal structure of the users' input question and return the precise answers for the queries written and extract the data that's queried from the RDF database and other knowledge bases.

## Technical approach:

Before proceeding to the further steps we have to setup few microservices such as Eureka Server, Chatbot, Webclient and Autoindex on the 
Virtual Machine (VM) via terminal using SSH login.  

To run these microservices the following command is used:
```
						mvn clean spring-boot:run
				
```
					
We have built a microservice named qa-ms which is solely making a call to web service at the moment. Further, it will be enhanced to 
identify with entities, classes and resources of the incoming questions. The qa-ms microservice needs to be connected to SurniaQA which is 
a POS (Part of Speech) based Question Answering System (QA System). In the SASK system there is a Fuseki server which is an RDF database 
(Triplestore) where the URIs will be used to query the database to fetch answers that we can communicate to. The next step is to deploy 
the Autoindex and connect it to Fuseki sever. Then setup the SurniaQA on VM and connect it to Autoindex. When the Chatbot gets its input, 
it decides whether it’s a question and sends it to the microservice (qa-ms). From there it goes to the SurniaQA and then to Autoindex and
get back the URIs as the response. The retrieved URIs from the Autoindex goes to the SurniaQA and finally they are displayed on the 
Chatbot UI.


Following is the figure on how it works:

![screenshot](https://user-images.githubusercontent.com/39854185/47263960-0f6e2280-d50c-11e8-8955-4dbd1136324d.png)
