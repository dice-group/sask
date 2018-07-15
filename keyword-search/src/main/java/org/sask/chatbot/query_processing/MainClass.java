package org.sask.chatbot.query_processing;
/**
 * @author Muzammil Ahmed
 * @since 25-04-2018
 */
import java.io.IOException;
/**
 * This main class is used to query process the string to retrieve keywords but they are still not
 * processed keywords that are used to query dbpedia for Strongly Connected Component
 */
public class MainClass {

    public static void main(String[] args) throws IOException{

        String s = "Who was the wife wife ???of presidenting lincoln?";
        System.out.println("This is the user string: " + s);
        System.out.println("Removing special characters!");
        //this code is used to remove special characters
        s = QPMethods.removeSpecialCharacters(s);
        System.out.println(s);
        System.out.println("Removing repetitive words!");
        // this code is used to remove repetitive words
        ///////////////////////////////////////////////////
        final String regex = "\\b(\\w+)\\b\\s*(?=.*\\b\\1\\b)";
        s = s.replaceAll(regex, "");
        ///////////////////////////////////////////////////
        System.out.println(s);
        System.out.println("Removing Stopwords");
        StopWords sW = new StopWords();
        String n = sW.removeStopWords(s);
        System.out.println(n);
        System.out.println("Lemmatzing string and POS tagging");

        try {
            //The getRelationsFromString has been used to lemmatize the string
            System.out.println(QPMethods.getRelationsFromString(n));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
