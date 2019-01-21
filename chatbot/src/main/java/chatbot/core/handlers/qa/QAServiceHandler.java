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
        Object answerDto;
        try {
            answerDto = objectMapper.readValue(answer, Object.class);
            this.findAnswers(answerDto, stringBuilder);
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
    /**
     * Recursively loop over JSON object to find URIs
     * This is relatively inefficient implementation, could possibly be done with Regex, or find a way to desrialize JSON
     * However, the problem with deserialization is that the incoming JSON response has different data types based on answer,
     * therefor no static POJO can be used to deserialize it at the momement
     *
     * @param answerDto
     * @param stringBuilder
     */
    private void findAnswers(Object answerDto, StringBuilder stringBuilder) {
        if (answerDto instanceof LinkedHashMap) {
            LinkedHashMap answerDtoMap = (LinkedHashMap) answerDto;
            answerDtoMap.keySet().forEach(answer -> {
                if ("value".equals(answer)) {
                    stringBuilder.append(answerDtoMap.get(answer));
                    stringBuilder.append(" ");
                    stringBuilder.append("\n");
                } else findAnswers(answerDtoMap.get(answer), stringBuilder);
            });
        }
        if (answerDto instanceof ArrayList) {
            ArrayList answerDTOArray = (ArrayList) answerDto;
            answerDTOArray.forEach(element -> {
                findAnswers(element, stringBuilder);
            });
        }
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
