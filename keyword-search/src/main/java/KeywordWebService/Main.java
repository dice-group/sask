package KeywordWebService;

import org.apache.jena.base.Sys;
import org.sask.chatbot.KeywordSearch.QueryDbpedia;
import org.sask.chatbot.KeywordSearch.QueryRDF;
import org.sask.chatbot.KeywordSearch.ReadModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args){

        SpringApplication.run(Main.class);

        //QueryRDF.getSCCOneKeyword(ReadModel.readModel("./src/main/resources/example3.ttl"));
        //QueryRDF.getSCCTwoKeywords(ReadModel.readModel("./src/main/resources/example3.ttl"));
        //QueryRDF.getTriples(ReadModel.readModel("./src/main/resources/example3.ttl"));


    }
}
