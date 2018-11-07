package org.diceresearch.sask.qa;

import org.diceresearch.sask.integration.SurniaQAService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/qa")
public class QaMsController {

    private final SurniaQAService surniaQAService;

    public QaMsController(SurniaQAService surniaQAService) {
        this.surniaQAService = surniaQAService;
    }

    @GetMapping("/{query}")
    public String getAnswerFromSurnia(@Valid @PathVariable String query) {
        return surniaQAService.askSurnia(query);
    }
}
