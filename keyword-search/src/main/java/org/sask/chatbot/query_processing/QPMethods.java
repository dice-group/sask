package org.sask.chatbot.query_processing;
/**
 * @author Muzammil Ahmed
 * @since 25-04-2018
 */
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class QPMethods {

    /**
     * This method removes special characters from a string.
     * @param query string with special characters
     * @return string without special characters
     */
    public static String removeSpecialCharacters(String query){
        return query.replaceAll("[^a-zA-Z0-9' ']", "").toLowerCase();
    }

    // this method is used to remove lemma from the string and POS tagging
    public static String getRelationsFromString(String input) throws Exception{

        //Create the stanford CoreNLP pipeline
        Properties props  = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        /* Annotate an example document */
        Annotation doc = new Annotation(input);
        pipeline.annotate(doc);

        List<CoreMap> sentences = doc.get(CoreAnnotations.SentencesAnnotation.class);
        List<String> word = new ArrayList<String>();
        List<String> lemma = new ArrayList<String>();
        List<String> Pos = new ArrayList<String>();
        String wrd = "";
        String lema = "";
        String pos = "";
        String[] split = input.split(" ");
        String processedString = input;

        //traversing sentences in the document
        for (CoreMap sentence: sentences) {

            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                wrd = token.getString(CoreAnnotations.TextAnnotation.class);
                word.add(wrd);
                System.out.println("token: "+ wrd);
                lema = token.get(CoreAnnotations.LemmaAnnotation.class);

                if(!(word.equals(lema))){
                    processedString = processedString.replaceAll(wrd, lema);
                }
                lemma.add(lema);
                pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                Pos.add(pos);
                System.out.println("POS: " + pos);

            }
        }return processedString;




    }
}