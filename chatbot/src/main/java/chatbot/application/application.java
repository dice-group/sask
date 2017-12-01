package chatbot.application;
import chatbot.core.IncomingRequest.*;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import chatbot.core.classifier.*;
import chatbot.core.handlers.*;
/** This is the main function , entry point to the chatbot. Forward everything that comes here to our handler functions.
 * @author Prashanth
 *
 */
@RestController
@RequestMapping("/chat")
public class application {
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    String route(@RequestBody final IncomingRequest request) throws Exception {
       classifier obj =  new classifier();
       Handler newobj = obj.classify(request);
       String answer = newobj.search(request);
       return answer;
    }
}
