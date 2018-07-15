package org.sask.chatbot.keyword_web_service;
/**
 * @author Muzammil Ahmed
 * @since 10-07-2018
 */


import org.sask.chatbot.keyword_search.QueryDbpedia;

import java.io.Serializable;

/**
 * This class is the business logic for keyword search webservice that is mappeds.
 */
public class KeywordReponse implements Serializable{
    private String subject;
    private String property;
    private QueryDbpedia queryDbpedia;

    public KeywordReponse() {
        this.subject = subject;
        this.property = property;
        queryDbpedia = new QueryDbpedia();

    }

      public String getSCCTwoKeywords(){
        String s = queryDbpedia.getSCC(getSubject(), getProperty());
        return s;

      }

    public String getSubject() {
        return subject;
    }


    public String getProperty() {
        return property;
    }

}
