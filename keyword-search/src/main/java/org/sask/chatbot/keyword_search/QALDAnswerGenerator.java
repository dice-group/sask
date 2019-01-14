package org.sask.chatbot.keyword_search;
/**
 * @author Muzammil Ahmed
 * @since 03-07-2018
 */
import java.io.*;
/*
* This class is used to benchmark Qald-7 results with the results returned with SCC query.
* It takes the QALD-7 results and creates another file with answers returned from the query
* for comparison.
*/

public class QALDAnswerGenerator {

    public static final String SAMPLE_XLSX_FILE_PATH = ".\\QALD_TSV.txt";
    public static final String SAMPLE_OUTPUT_FILE_PATH = ".\\QALD_TSV_OUTPUT.txt";

    public static void main(String[] args) throws IOException {
        BufferedReader TSVFile = new BufferedReader(new FileReader(SAMPLE_XLSX_FILE_PATH));
        BufferedWriter write = new BufferedWriter(new FileWriter(SAMPLE_OUTPUT_FILE_PATH));
        QueryDbpedia query = new QueryDbpedia();
        String result = "";
        try{
            String dataRow = TSVFile.readLine();
            while (dataRow != null){
                String[] parts = dataRow.split("\\t");
                if(!parts[3].equals("")){
//                    System.out.println(parts[3]);
                    String[] input = parts[3].replace("\"", "").split(",");
//                    System.out.println(input[0].trim());
//                    System.out.println(input[1].trim());
                    result = query.getSCC(input[0].trim(), input[1].trim());
//                    System.out.println("Result is : " + result);
                }
                write.append(dataRow);
                write.append("\t");
                write.append(result);
                write.append("\n");
                dataRow = TSVFile.readLine();
            }
            TSVFile.close();
            write.flush();
            write.close();
        }
        catch (Exception exception){
            System.out.println(exception);
        }
    }
}

