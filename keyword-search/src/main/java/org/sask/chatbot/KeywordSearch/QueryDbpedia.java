package org.sask.chatbot.KeywordSearch;
/**
 * @author Muzammil Ahmed
 * @since 10-05-2018
 */
import org.apache.jena.query.*;
import java.util.ArrayList;
import java.util.List;

public class QueryDbpedia {

    public String getSCC(String keyword1, String keyword2 ){

        List<String> answer = new ArrayList<String>();
        //This query finds takes the first keyword(literal) and finds its resource
        ParameterizedSparqlString qs = new ParameterizedSparqlString( "" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?subject where {\n" +
                "?subject rdfs:label \'" + keyword1 +"\'@en.\n" +
                "FILTER ( regex(?subject, \""+ keyword1.split(" ")[0] +"\"))\n" +
                "FILTER ( !regex(?subject, \"Category\"))\n" +
                "}" );
//        System.out.println( qs );

        QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );

        ResultSet results = ResultSetFactory.copyResults( exec.execSelect() );
        List<QuerySolution> lsSubjects = ResultSetFormatter.toList(results);

//        ResultSetFormatter.out( results );

        //This query takes the second keyword(literal) and find its resource
        qs = new ParameterizedSparqlString( "" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?subject where {\n" +
                "?subject rdfs:label \'" + keyword2 +"\'@en.\n" +
//                "FILTER ( regex(?subject, \""+ property.split(" ")[0] +"\"))\n" +
                "FILTER ( !regex(?subject, \"Category\"))\n" +
                "}" );
//        System.out.println( qs );

        exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );

        results = ResultSetFactory.copyResults( exec.execSelect() );
//        System.out.println(results.getResultVars().size());
        List<QuerySolution> lsProperties = ResultSetFormatter.toList(results);
//        ResultSetFormatter.out( results );

        //This query checks each resource in subject and each property to find object(resource or literal).
        // if property is a subject and subject is a property
        for (QuerySolution sub: lsSubjects) {
            for (QuerySolution prop: lsProperties) {
                if(!sub.getResource("subject").toString().contains("resource")){
                    qs = new ParameterizedSparqlString( "" +
                            "select distinct ?object where {\n" +
                            "?subject ?predicate ?object . \n" +
                            "FILTER ( ?subject = <" + prop.getResource("subject").toString() + "> )\n" +
                            "FILTER ( ?predicate = <" + sub.getResource("subject").toString() + "> )\n" +
                            "}" );
//                    System.out.println( qs );
                }
                else{ //if the subject is a subject is the actual subject and property is the actual property
                    qs = new ParameterizedSparqlString( "" +
                            "select distinct ?object where {\n" +
                            "?subject ?predicate ?object . \n" +
                            "FILTER ( ?subject = <" + sub.getResource("subject").toString() + "> )\n" +
                            "FILTER ( ?predicate = <" + prop.getResource("subject").toString() + "> )\n" +
                            "}" );
//                    System.out.println( qs );
                }

                exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );

                results = ResultSetFactory.copyResults( exec.execSelect() );
                List<QuerySolution> lsAnswer = ResultSetFormatter.toList(results);


                if(lsAnswer.size()== 0 && lsAnswer.toString().equals("[]")){
//                    System.out.println("\n Reversed Query \n");
                    if(!sub.getResource("subject").toString().contains("resource")){
                        qs = new ParameterizedSparqlString( "" +
                                "select distinct ?object where {\n" +
                                "?object ?predicate ?subject . \n" +
                                "FILTER ( ?subject = <" + prop.getResource("subject").toString() + "> )\n" +
                                "FILTER ( ?predicate = <" + sub.getResource("subject").toString() + "> )\n" +
                                "}" );
//                        System.out.println( qs );
                    }
                    else{
                        qs = new ParameterizedSparqlString( "" +
                                "select distinct ?object where {\n" +
                                "?object ?predicate ?subject . \n" +
                                "FILTER ( ?subject = <" + sub.getResource("subject").toString() + "> )\n" +
                                "FILTER ( ?predicate = <" + prop.getResource("subject").toString() + "> )\n" +
                                "}" );
//                        System.out.println( qs );
                    }

                    exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );

                    results = ResultSetFactory.copyResults( exec.execSelect() );
                    lsAnswer = ResultSetFormatter.toList(results);

                }

                for (QuerySolution ans : lsAnswer) {
                    if(ans.get("object").isLiteral()){
                        if(!ans.getLiteral("object").toString().equals("")){
                            //System.out.println(ans.getLiteral("object").toString());
//                            answer += ans.getLiteral("object").toString();
                            answer.add(ans.getLiteral("object").toString());
                        }
                    }
                    if(ans.get("object").isResource()){
                        if(!ans.getResource("object").toString().equals("")){
                            //System.out.println(ans.getResource("object").toString());
//                            answer += ans.getResource("object").toString();
                            answer.add(ans.getResource("object").toString());
                        }
                    }
                }
//                ResultSetFormatter.out( results);
            }
        }

        return answer.toString();
    }



}
