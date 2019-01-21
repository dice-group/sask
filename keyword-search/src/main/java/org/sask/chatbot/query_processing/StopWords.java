package org.sask.chatbot.query_processing;
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
                BufferedReader reader = new BufferedReader(new FileReader(".\\keyword-search\\src\\main\\resources\\stopwords.txt"));
                String stopWord = "";
                while ((stopWord = reader.readLine()) != null) {
                    stopWords.add(stopWord);

                }
                reader.close();
            }

            public String removeStopWords(String s){

                String[] wordTokens = s.split(" ");
                String result = "";
                for (String word: wordTokens) {
                    if(word.length() == 0 ||isStopWord(word))
                        continue;
                    result += (word+ " ");
                }

                return result;

            }

            public boolean isStopWord(String word){
                return stopWords.contains(word);
            }

}
