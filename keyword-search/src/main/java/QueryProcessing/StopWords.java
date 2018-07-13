package QueryProcessing;
/**
 * @author Muzammil Ahmed
 * @since 25-04-2018
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
* This code has been used from other resources to find resources
*/
public class StopWords {

    public List<String> stopWords;

            public StopWords() throws IOException{

                stopWords =  new ArrayList<String>();
                BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/stopwords.txt"));
                int counter = 0;
                String stopWord = "";
                while ((stopWord = reader.readLine()) != null) {
                    stopWords.add(stopWord);
                    counter++;
                }
                reader.close();
            }

            public String removeStopWords(String string){

                String[] wordTokens =string.replaceAll("[^a-zA-Z0-9' ']", "").toLowerCase().split(" ");
                List<String> outputList = new ArrayList<String>();
                String result = "";
                for (String word: wordTokens) {
                    if(word.length() == 0)
                        continue;
                    if(IsStopWord(word))
                        continue;
                    result += (word+ " ");
                }

                return result;

            }

            public boolean IsStopWord(String word){

                if(stopWords.contains(word))
                    return true;
                else
                    return false;

            }

}
