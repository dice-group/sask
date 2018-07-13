package QueryProcessing;
/**
 * @author Muzammil Ahmed
 * @since 25-04-2018
 */
import QueryProcessing.QPMethods;
import QueryProcessing.StopWords;
/**
 * This main class is used to query process the string to retrieve keywords but they are still not
 * processed keywords that are used to query dbpedia for Strongly Connected Component
 */
public class MainClass {

    public static void main(String[] args) throws Exception {
        String s = "Who was the wife of president lincoln?";
        // this code is used to remove repetitive words
        ///////////////////////////////////////////////////
        final String regex = "\\b(\\w+)\\b\\s*(?=.*\\b\\1\\b)";
        System.out.println("This is the String before removing repetitive words  " + s);
        s = s.replaceAll(regex, "");
        ///////////////////////////////////////////////////
        System.out.println("This is the String before Query Processing:  " + s);
        System.out.println("Removing Stopwords");
        StopWords sW = new StopWords();
        String n = sW.removeStopWords(s);
        System.out.println(n);
        try {
            //The getRelationsFromString has been used to lemmatize the string
            System.out.println(QPMethods.getRelationsFromString(n));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
