# Task 2 of the second phase of project

#### To the already framed questions in task 1, create SPARQL queries for each of them and load the OKE data into the triple store (Fuseki server).

Write a small piece of Java code which you can get in the [link 1](https://jena.apache.org/tutorials/rdf_api.html#ch-Reading%20RDF) and [link 2](https://jena.apache.org/documentation/query/app_api.html) and create your SPARQL queries for each question.

Let's consider the example for the question which was described in the task 1 i.e., "Who is the trainer of Conor McGregor?"

As you remember, this question was taken from the response data (refer task 1 if needed for explanantion) where "dbo:trainer dbr:Conor_McGregor"

Here goes the code:

import org.apache.jena.rdf.model.Model;\
import org.apache.jena.rdf.model.ModelFactory;\
import org.apache.jena.util.FileManager;

import java.io.*;


public class SparqlQuery extends Object{

    static final String inputFileName = "new.ttl"; // input the ttl (Turtle format) file from the dataset 

    public static void main(String args[]){

        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open( inputFileName );
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: " + inputFileName + " not found");
        }

// read the RDF/XML file 

         model.read(inputFileName);

         String sparqlQuery =

                "Prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                        + " SELECT ?uri" +
                        "WHERE {" +
                        "  ?uri  dbo:trainer  dbr:Conor_McGregor." +
                        "      }";

// write it to standard out 

         model.write(System.out);

    }
}

Like above, frame the SPARQL queries for each of the 50 questions which you created from the dataset.
