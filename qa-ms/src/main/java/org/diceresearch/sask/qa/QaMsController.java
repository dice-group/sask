package org.diceresearch.sask.qa;

import org.diceresearch.sask.integration.SurniaQAService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qa")
public class QaMsController {

    private final SurniaQAService surniaQAService;

    public QaMsController(SurniaQAService surniaQAService) {
        this.surniaQAService = surniaQAService;
    }

    @GetMapping("/{query:.+}")
    public ResponseEntity<String> getAnswerFromSurnia(@PathVariable String query) {
        return surniaQAService.askSurnia(query);
    }
}
