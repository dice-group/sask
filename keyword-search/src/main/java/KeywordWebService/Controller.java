package KeywordWebService;
/**
 * @author Muzammil Ahmed
 * @since 10-07-2018
 */
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {


    @ResponseBody
    @RequestMapping(
            value = "api/query/",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
        public KeywordReponse getSCC(@RequestBody KeywordReponse keywordReponse){

        return keywordReponse;
    }
}
