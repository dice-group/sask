package org.sask.chatbot.keyword_search;
/**
 * @author Muzammil Ahmed
 * @since 12-07-2018
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
/*
* This Class is used to check the correctness of the Qald results (on processed keywords) that are matched with
* returned results using SCC query. The result is 41 percent of the total queries.
*/
public class Comparative {

    public static void main(String[] args)throws IOException {

        String SAMPLE_OUTPUT_FILE_PATH = ".\\QALD_TSV_OUTPUT.txt";
        BufferedReader TSVFile = new BufferedReader(new FileReader(SAMPLE_OUTPUT_FILE_PATH));
        int count = 0;
        boolean flag = false;

        try{
            String dataRow = TSVFile.readLine();
            while (dataRow != null){
                String[] parts = dataRow.split("\\t");
                if(!parts[4].equals("") && !parts[5].equals("")){
                    String[] answers = parts[4].replace("\"", "").split(",");
                    for (String eachAnswer: answers) {
//                        System.out.println(eachAnswer + " ............ " + parts[5]);
//                        System.out.println(parts[5].contains(eachAnswer));
                        if(parts[5].contains(eachAnswer)){
                            flag = true;
                        }
                        else{
                            flag = false;
                            break;
                        }
                    }
//                    System.out.println("flag value : " + flag);
                    if(flag){
                        count++;
                        flag = false;
                    }
                }
                dataRow = TSVFile.readLine();
            }
            TSVFile.close();

            float avg = (count*100)/214;
//            System.out.println(count);
            System.out.println("Correct Answer Percentage is: " + avg + "%");
        }
        catch (Exception exception){
            System.out.println(exception);
        }
    }
}
