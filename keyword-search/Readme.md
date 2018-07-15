# Keyword Search
Artifacts for chatbot feature:

### Steps to install and run:
Import or run the keyword-search module as a separate maven project. Build The project and run the Main Class in keyword_web_service package which runs the webservice on 8080 port. Use Postman or any other service to check the rest based POST webservice and pass processed 2 keywords from dbpedia like "Elton John" and "music by" to get the answer(resources or literals) in JSON format.

#### Basic Idea
Hypothetically the keyword search engine should be used with chatbot where it takes the user query, process it and find relative keywords and pass the literals(from query) to dbpedia or any rdf resource to find related answers. But the basic query processing is not able to extract processed keywords. For this reason this module has been reduced to giving two processed keyword literals (in any order) to get the answer.

#### Categories of Keyword Search:
This module is an implementation of paper "Exploring Term Networks for Semantic Search over Large RDF Knowledge Graphs" which queries rdf resource to find resource or literals by providing keywords form the user query and implements the logic of creating Semantic Connected Component.

#### keyword_search
This package contains the business logic. The QueryRDF class is proof of the concept. Providing two literals("Mona Lisa","artist") which find the "Leonardo da Vinci" using the technique of SCC. The QueryDbpedia class queries dbpedia to find answers using only two processed keywords. The QALDAnswerGenerator is used to benchmark answer from QALD-7 dataset to ours and Comparative Class is used to compare results and return the percentage of passed queries. 

#### keyword_web_service
This package consists of the REST api for the keyword search. KeywordResponse is the model class and Controller class maps KeywordResponse. The Main class is the entry point for running this webservice.

#### query_processing
It contains the query pre processing methods like Special character removal, repetitive words removal, stop words removal, lemmatization and POS tagging of the query. This is to show that with basic preprocessing techniques the keywords that we get from user query are not equal to processed keywords that are required by dbpedia to search for subject property and objects.
