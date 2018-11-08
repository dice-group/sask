import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import java.io.*;


public class SparqlQuery extends Object{

    static final String inputFileName = "new 2.ttl";

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
                        + " SELECT ?person" +
                        "WHERE {" +
                        "  []  rdf:subject  ?person;" +
                        "      rdf:predicate  dbo:trainer;" +
                        "      rdf:object   dbr:Conor_McGregor." +
                        "      }";

// write it to standard out
        model.write(System.out);

    }

}
