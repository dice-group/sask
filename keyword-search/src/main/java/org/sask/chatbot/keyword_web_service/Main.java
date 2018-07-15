package org.sask.chatbot.keyword_web_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the class that is used to run keyword search web service.
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args){

        SpringApplication.run(Main.class);

        //QueryRDF.getSCCOneKeyword(ReadModel.readModel("./src/main/resources/example3.ttl"));
        //QueryRDF.getSCCTwoKeywords(ReadModel.readModel("./src/main/resources/example3.ttl"));
        //QueryRDF.getTriples(ReadModel.readModel("./src/main/resources/example3.ttl"));


    }
}
