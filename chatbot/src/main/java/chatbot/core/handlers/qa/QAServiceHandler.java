package chatbot.core.handlers.qa;

import chatbot.core.handlers.Handler;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.io.response.EntryInformation;
import chatbot.io.response.Response;
import chatbot.io.response.ResponseList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author sajjad
 */
public class QAServiceHandler extends Handler {

    private static final Logger log = Logger.getLogger(QAServiceHandler.class);

    private static final String URL = "http://localhost:8082/qa/{var}"; // TODO: 11/07/2018 read from application.yml later

    private final RestTemplate restTemplate;

    public QAServiceHandler() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        restTemplate = builder.build();
    }

    public ResponseList search(IncomingRequest request) {
        EntryInformation entry = new EntryInformation();
        ResponseList responseList = new ResponseList();
        StringBuilder stringBuilder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        String answer = askQAService(request);
        LinkedHashMap answerDto;
        try {
            answerDto = (LinkedHashMap) objectMapper.readValue(answer, Object.class);
            // TODO: 19/01/2019 inefficient implementation, this could possibly be done with Regex, or find a way to desrialize JSON
            // TODO: the problem with deserialization is that the incoming JSON response has different data types based on answer,
            // TODO: therefor no static POJO can be used to deserialize it
            answerDto.keySet().forEach(a -> {
                ArrayList questions = (ArrayList) answerDto.get(a);
                LinkedHashMap questionProperties = (LinkedHashMap) questions.get(0);
                questionProperties.keySet().forEach(key -> {
                    if ("answers".equals(key.toString())) {
                        ArrayList answers = (ArrayList) questionProperties.get(key);
                        LinkedHashMap answersMap = (LinkedHashMap) answers.get(0);
                        answersMap.keySet().forEach(answerKey -> {
                            if ("results".equals(answerKey)) {
                                LinkedHashMap bindingsMap = (LinkedHashMap) answersMap.get(answerKey);
                                bindingsMap.keySet().forEach(binding -> {
                                    ArrayList bindingList = (ArrayList) bindingsMap.get(binding);
                                    bindingList.forEach(bindingElement -> {
                                        LinkedHashMap bindingElementMap = (LinkedHashMap) bindingElement;
                                        bindingElementMap.keySet().forEach(bElementKey -> {
                                            LinkedHashMap finalResults = (LinkedHashMap) bindingElementMap.get(bElementKey);
                                            finalResults.keySet().forEach(fKey -> {
                                                if ("value".equals(fKey)) {
                                                    stringBuilder.append(finalResults.get(fKey));
                                                    stringBuilder.append(" ");
                                                    stringBuilder.append("\n");
                                                }
                                            });
                                        });
                                    });
                                });
                            }
                        });
                    }
                });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        entry.setDisplayText(stringBuilder.toString());
        entry.setButtonType(EntryInformation.Type.URL);
        Response response = new Response();
        response.addEntry(entry);
        responseList.addMessage(response);
        responseList.setMessageType(ResponseList.MessageType.TEXT_WITH_URL);
        return responseList;
    }

    private String askQAService(IncomingRequest request) {
        log.info("Requesting an answer from QA service...");
        String query = request.getRequestContent().get(0).getText();
        URI uri = new UriTemplate(URL).expand(query);
        return exchange(uri, String.class);
    }


    // TODO: 11/07/2018 This implementation should be moved to some common package,
    // TODO: since this will be used by every component to make rest call

    /**
     * Generic method to return response from a RestCall endpoint
     *
     * @param uri
     * @param response
     * @param <T>
     * @return
     */
    private <T> T exchange(URI uri, Class<T> response) {
        RequestEntity<?> requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<T> responseEntity = this.restTemplate.exchange(requestEntity, response);
        return responseEntity.getBody();
    }
}
