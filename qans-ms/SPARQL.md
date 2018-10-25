# Generation of SPARQL queries

Data on the Web has been increasing greatly at a constant rate. The mere amount of data that needs to be queried poses an important challenge. For end users it's indeed a difficult task, to find the information on the web they are looking for. We have services such as _Sindice_, _Sig.ma_, _Swoogle_ or _Watson_ that offer simple search services. But they are restricted either to the retrieval of single RDF document or the retrieval of information about a single entity from different sources as in the case of _Sig.ma_. On the hand, some srevices provide/load the complete Web of Data into a large triple store cluster and enable issuing SPARQL queries. Users have to understand the SPARQL concepts, understand the SPARQL syntax and at last they should know what information structures are available to formulate the queries that are also going to return the results, in order to express their information. 

## What is SPARQL?
SPARQL is the standard language to query RDF data (Triple format).

**SPARQL** stands for **S**PARQL **P**rotocol and **R**DF **Q**uery **L**anguage. \
It's one of the three core standards of the Semantic Web, along with RDF (Resource Description Framework) and OWL (Web Ontology Language).

  There are four types of SPARQL queries:
  
  1. SELECT - Return the results in a table form for all variables satisfying the given conditions.
  2. CONSTRUCT - Return the results as the valid RDF graph constructed by substituting variables in a set of triple templates.
  3. ASK - Returns a boolean indicating whether a query pattern matches.
  4. DESCRIBE - Returns an RDF graph that describes the resources found (identified by name or description).
  
  A simple SPARQL query looks as follows:
  
 **SELECT** ?a ?b ?c \
 **WHERE**\
  {
  
     x, y ?a.
     m, n ?b.
     ?b f ?c.
  
  }
  
  **SELECT** clause lists all the variables that needs to be returned. Variables start with a question mark.\
  **WHERE** clause contains restrictions on them (variables), mostly in the form of triples.
  
  When we run the query, the variables need to be filled with actual values so that resulting triples appear in the knowledge base (KB) and returns one result for each combination of variables it finds.\
  

Let's consider a simple example:

Here is the data given: http://example.org/book/book1  http://purl.org/dc/elements/1.1/title "SPARQL Tutorial" .

The query would be as follows:

SELECT ?title\
WHERE\
{

     <http://example.org/book/book1> <http://purl.org/dc/elements/1.1/title> ?title.
     
}

In the above query:\
    1. <http://example.org/book/book1> is the subject\
    2. <http://purl.org/dc/elements/1.1/title> is the predicate \
    3. ?title is the object for which the actual value will be allocated.

The above query has the solution on the data given in the table form as:

| **title**               |
| :----------------------:|
| SPARQL Tutorial         |

This is just the overview on how to create a SPARQL query shown with the help of a simple example. There are lot many things which can be done. For that the [SPARQL Query Language](https://www.w3.org/TR/sparql11-query/) link would be helpful.

