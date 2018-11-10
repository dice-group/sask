# Task 2 of the second phase of project

#### To the already framed questions in task 1, create SPARQL queries for each of them and load the OKE data into the triple store (Fuseki server).

Write a small piece of Java code which you can get in the [link 1](https://jena.apache.org/tutorials/rdf_api.html#ch-Reading%20RDF) and [link 2](https://jena.apache.org/documentation/query/app_api.html) and create your SPARQL queries for each question.

Let's consider the example for the question which was described in the task 1 i.e., "Who is the trainer of Conor McGregor?"

As you remember, this question was taken from the response data (refer task 1 if needed for explanantion) where "dbo:trainer dbr:Conor_McGregor"

You can take a look at the code which is there by name SparqlQuery.java.

Using the mentioned code above, frame the SPARQL queries for each of the 50 questions which you created from the dataset.

Load OKE data into Fuseki server.

In order to load the data, install and run Fuseki server [visit the link](http://jena.apache.org/download/index.cgi).\
The [documentation](http://jena.apache.org/documentation/serving_data/) on how to get started with Fuseki server is here.


Once you are done with the installation process, you can write your SPARQL queries on the Fuseki server by uploading the file(here we have been uploading Turtle file i.e., .ttl).

Kindly take a look at the following screenshots:

1. This is how the Apache Jena Fuseki server at localhost:3030 which is by default looks like and the dataset name I have named it as dataset(don't get confused, refer to the documentation provided in the above link):

![screenshot 39](https://user-images.githubusercontent.com/39854185/48192282-9f52ff80-e347-11e8-8d96-631227ad0fe3.png)


2. Next step is to upload your file or the data. For that proceed as:
    
        Go to upload files -> select files -> upload now or upload all
        
        
![screenshot 40](https://user-images.githubusercontent.com/39854185/48192638-a75f6f00-e348-11e8-9cb8-c560154a062c.png)


3. The last step is to write a query on the selected file. You write the query in the blank box as shown in the screenshot and run it to get the required result.


![screenshot 41](https://user-images.githubusercontent.com/39854185/48192815-1d63d600-e349-11e8-9d0f-77f07988cfc1.png)

        

    
    

