import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.log4j.BasicConfigurator;

import java.io.*;

public class  responseReader extends Object {

    /**
     NOTE that the file is loaded from the class-path and so requires that
     the data-directory, as well as the directory containing the compiled
     class, must be added to the class-path when running this and
     subsequent examples.
     */
    static final String inputFileName  = "file_24.ttl";
//    static final String inputFileName  = "extractor2222response.ttl";

    public static void main (String args[]) {
        BasicConfigurator.configure();
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
        //Model model2 = ModelFactory.createDefaultModel();


        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException("File: " + inputFileName + " not found");
        }

        // read the RDF/XML file
//         read() method call is the URI which will be used for resolving relative URI's
//        model.read(in, "TURTLE");
        model.read(inputFileName);
        //model2.read("extractor2227response.ttl");

//      to run in command line sparql.bat --data=vc-db-1.rdf --query=q1.rq
        // write it to standard outString queryString = " .... " ;
        String queryString =

                "Prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                " CONSTRUCT {?s ?p ?o}" +
                        " WHERE {" +
                        "  ?ss rdf:subject ?s." +" ?ss rdf:predicate ?p." +" ?ss rdf:object ?o." +
                        "      }";



        Query query = QueryFactory.create(queryString) ;
        //String responseIs = null;

        try (QueryExecution qexec = QueryExecutionFactory.create(query, model))
        {

            Model results = qexec.execConstruct() ;
            StmtIterator iter = results.listStatements();
            System.out.println(results.toString());
            while(iter.hasNext())
            {
                System.out.println(iter.next());

            }
        }
//        System.out.println("   Response after rdf read");
//     model.write(System.out,"TURTLE");
//     System.out.println(".............................");
//     model2.write(System.out,"TURTLE");
       // System.out.println(responseIs);

    }
}
