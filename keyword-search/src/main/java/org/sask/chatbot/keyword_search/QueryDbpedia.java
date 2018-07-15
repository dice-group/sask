package org.sask.chatbot.keyword_search;
/**
 * @author Muzammil Ahmed
 * @since 10-05-2018
 */

import org.apache.jena.query.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to query dbpedia to get Semantically Connected Component.
 */
public class QueryDbpedia {

    /**
     * This method can be given parameters in any order (subject and then object or vice versa.
     * @param kword1 requires one keyword in string.
     * @param kword2 requires second keyword in string.
     * @return it returns string (with one or multiple strings concatenated together.
     */
    public String getSCC(String kword1, String kword2 ){

        List<String> answer = new ArrayList<String>();
        //This query finds takes the first keyword(literal) and finds its resource
        ParameterizedSparqlString qs = getParameterizedSparqlString(kword1);
        QueryExecution exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );

        ResultSet results = ResultSetFactory.copyResults( exec.execSelect() );
        List<QuerySolution> lsSubjects = ResultSetFormatter.toList(results);

        //This query takes the second keyword(literal) and find its resource
        qs = new ParameterizedSparqlString( "" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?subject where {\n" +
                "?subject rdfs:label \'" + kword2 +"\'@en.\n" +
//                "FILTER ( regex(?subject, \""+ property.split(" ")[0] +"\"))\n" +
                "FILTER ( !regex(?subject, \"Category\"))\n" +
                "}" );
        exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );
        results = ResultSetFactory.copyResults( exec.execSelect() );
        List<QuerySolution> lsProperties = ResultSetFormatter.toList(results);


        checkResources(lsSubjects, lsProperties, qs, exec, results, answer);
        return answer.toString();
    }

    /**
     * This query checks each resource in subject and each property to find object(resource or literal).
     * if property is a subject and subject is a property.
     * @param lsSubjects List<QuerySolution>
     * @param lsProperties List<QuerySolution>
     * @param qs ParameterizedSparqlString
     * @param exec QueryExecution
     * @param results ResultSet
     * @param answer List<String> answer
     */
    private void checkResources(List<QuerySolution> lsSubjects, List<QuerySolution> lsProperties,
                                ParameterizedSparqlString qs, QueryExecution exec,
                                ResultSet results, List<String> answer) {
        for (QuerySolution sub: lsSubjects) {
            for (QuerySolution prop: lsProperties) {
                if(!sub.getResource("subject").toString().contains("resource")){
                    qs = createQuery2(prop, sub);
                }
                else{ //if the subject is a subject is the actual subject and property is the actual property
                    qs = createQuery2(sub, prop);
                }

                exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );

                results = ResultSetFactory.copyResults( exec.execSelect() );
                List<QuerySolution> lsAnswer = ResultSetFormatter.toList(results);
                if(lsAnswer.size()== 0 && lsAnswer.toString().equals("[]")){
                    if(!sub.getResource("subject").toString().contains("resource")){
                        qs = createQuery(prop, sub);
                    }
                    else{
                        qs = createQuery(sub, prop);
                    }

                    exec = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", qs.asQuery() );

                    results = ResultSetFactory.copyResults( exec.execSelect() );
                    lsAnswer = ResultSetFormatter.toList(results);
                }
                for (QuerySolution ans : lsAnswer) {
                    if(ans.get("object").isLiteral() && !ans.getLiteral("object").toString().equals("")){
                            answer.add(ans.getLiteral("object").toString());
                    }
                    if(ans.get("object").isResource() && !ans.getResource("object").toString().equals("")){
                            answer.add(ans.getResource("object").toString());
                    }
                }
            }
        }
    }

    /**
     * Creates Paramenterized query for given {@link QuerySolution}
     * @param sub QuerySolution
     * @param prop QuerySolution
     * @return ParameterizedSparqlString
     */
    private ParameterizedSparqlString createQuery2(QuerySolution sub, QuerySolution prop) {
        return new ParameterizedSparqlString( "" +
                "select distinct ?object where {\n" +
                "?subject ?predicate ?object . \n" +
                "FILTER ( ?subject = <" + sub.getResource("subject").toString() + "> )\n" +
                "FILTER ( ?predicate = <" + prop.getResource("subject").toString() + "> )\n" +
                "}" );
    }

    /**
     * Creates Paramenterized query for given {@link QuerySolution}.
     * @param sub QuerySolution
     * @param prop QuerySolution
     * @return ParameterizedSparqlString
     */
    private ParameterizedSparqlString createQuery(QuerySolution prop, QuerySolution sub) {
        return new ParameterizedSparqlString( "" +
                "select distinct ?object where {\n" +
                "?object ?predicate ?subject . \n" +
                "FILTER ( ?subject = <" + prop.getResource("subject").toString() + "> )\n" +
                "FILTER ( ?predicate = <" + sub.getResource("subject").toString() + "> )\n" +
                "}" );
    }

    /**
     * Creates Paramenterized query for given string.
     * @param kword1 {@link String}
     * @return ParameterizedSparqlString
     */
    public ParameterizedSparqlString getParameterizedSparqlString(String kword1) {
        return new ParameterizedSparqlString( "" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?subject where {\n" +
                "?subject rdfs:label \'" + kword1 +"\'@en.\n" +
                "FILTER ( regex(?subject, \""+ kword1.split(" ")[0] +"\"))\n" +
                "FILTER ( !regex(?subject, \"Category\"))\n" +
                "}" );
    }
}
