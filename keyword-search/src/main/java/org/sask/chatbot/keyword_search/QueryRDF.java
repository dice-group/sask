package org.sask.chatbot.keyword_search;
/**
 * @author Muzammil Ahmed
 * @since 11-06-2018
 */
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;

import java.util.List;

/**
 * This class is used to query rdf model for finding SCC.
 */
public class QueryRDF {


    /*
    *  This Method returns Triples (subject, predicate and object) if given rdf if triple model data.
    */
    public static void getTriples(Model model) {
        //String value = "http://dbpedia.org/page/Mona_Lisa";
        ParameterizedSparqlString qs = new ParameterizedSparqlString( "" +
                "select distinct ?subject ?predicate ?object where {\n" +
                "  ?subject ?predicate ?object.\n" +
                "}" );

        System.out.println( qs );

        Query query= QueryFactory.create(qs.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);

        ResultSet results = ResultSetFactory.copyResults( qexec.execSelect() );

        ResultSetFormatter.out(results );
    }


    /*
    *This Method takes one keyword (Mona Lisa) and returns result (Leonardo da Vinci) in Strongly Connected Component in example rdf
    */
    public static void getSCCOneKeyword(Model model){

        // From Keyword to Subject of connected component M.L -> e2
        String value =  "\'Mona Lisa\'";
        ParameterizedSparqlString qs = new ParameterizedSparqlString( "" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?subject where {\n" +
                " ?subject ?property " + value +".\n" +
                "}" );

        System.out.println( qs );

        Query query= QueryFactory.create(qs.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);

        ResultSet results = ResultSetFactory.copyResults( qexec.execSelect() );
        QuerySolution solution = results.next();

        ResultSetFormatter.out(results );

        // From Subject of connected component to intermediary connected components
        // e2 -> property artist
        // e2 -> e1
        qs = new ParameterizedSparqlString( "" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?property ?object where {\n" +
                "?subject ?property ?object. \n" +
                "FILTER ( ?subject = <" + solution.getResource("subject").toString() + "> )\n" +
                "FILTER (?object !="+ value +") \n" +
                "}" );

        System.out.println( qs );

        query= QueryFactory.create(qs.toString());
        qexec = QueryExecutionFactory.create(query, model);

        results = ResultSetFactory.copyResults( qexec.execSelect() );
        solution = results.next();

        ResultSetFormatter.out(results );

        //From Property to Artist label
        qs = new ParameterizedSparqlString( "" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?property ?object where {\n" +
                "?subject ?property ?object. \n" +
                "FILTER ( ?subject = <" + solution.getResource("property").toString() + "> )\n" +
                "}" );

        System.out.println( qs );

        query= QueryFactory.create(qs.toString());
        qexec = QueryExecutionFactory.create(query, model);

        results = ResultSetFactory.copyResults( qexec.execSelect() );

        ResultSetFormatter.out(results );

        //From e2 to L.D
        qs = new ParameterizedSparqlString( "" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?property ?object where {\n" +
                "?subject ?property ?object. \n" +
                "FILTER ( ?subject = <" + solution.getResource("object").toString() + "> )\n" +
                "}" );

        System.out.println( qs );

        query= QueryFactory.create(qs.toString());
        qexec = QueryExecutionFactory.create(query, model);

        results = ResultSetFactory.copyResults( qexec.execSelect() );

        ResultSetFormatter.out(results );
    }

    /*
    * This method takes two keywords ('Mona Lisa' and 'artist') and returns ('Leonardo da Vinci') in SCC
    */
    public static void getSCCTwoKeywords(Model model){

        // From Keyword to Subject of connected component M.L -> e2
        String subject =  "\'Mona Lisa\'";
        String property =  "\'artist\'";
        ParameterizedSparqlString qs = new ParameterizedSparqlString( "" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?subject where {\n" +
                "{?subject ?property " + subject + ".}" +
                " UNION " +
                "{?subject ?property " + property  +".}\n" +
                "}" );

        System.out.println( qs );

        Query query= QueryFactory.create(qs.toString());
        QueryExecution qexec = QueryExecutionFactory.create(query, model);

        ResultSet results = ResultSetFactory.copyResults( qexec.execSelect() );
        ResultSet results2 = ResultSetFactory.copyResults(results);
        List<QuerySolution> ls = ResultSetFormatter.toList(results2);

        QuerySolution solution = results.next();

        ResultSetFormatter.out(results );

        // From Subject and Property find the object which will serve as the subject of the connected component
        // e2 -> artist -> e1
        qs = new ParameterizedSparqlString( "" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?object where {\n" +
                "?subject ?property ?object. \n" +
                "FILTER ( ?subject = <" + ls.get(0).getResource("subject").toString() + "> )\n" +
                "FILTER ( ?property = <" + ls.get(1).getResource("subject").toString() + "> )\n" +
                "}" );

        System.out.println( qs );

        query= QueryFactory.create(qs.toString());
        qexec = QueryExecutionFactory.create(query, model);

        results = ResultSetFactory.copyResults( qexec.execSelect() );
        solution = results.next();

        ResultSetFormatter.out(results );

        //From e1 to L.D
        qs = new ParameterizedSparqlString( "" +
                "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
                "select distinct ?object where {\n" +
                "?subject ?property ?object. \n" +
                "FILTER ( ?subject = <" + solution.getResource("object").toString() + "> )\n" +
                "FILTER (isLiteral(?object)) \n" +
                "}" );

        System.out.println( qs );

        query= QueryFactory.create(qs.toString());
        qexec = QueryExecutionFactory.create(query, model);

        results = ResultSetFactory.copyResults( qexec.execSelect() );

        ResultSetFormatter.out(results );
    }

}
