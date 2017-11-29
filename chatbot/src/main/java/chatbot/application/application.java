package chatbot.application;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class application {

    @RequestMapping(value="/conversation" , method = RequestMethod.POST)
    public @ResponseBody
    String route(@RequestParam("message") String value) throws Exception {
    //	System.out.print(value + ",.");
       classifier obj =  new classifier();
       Handler newobj = obj.classify(value);
       String answer = newobj.search(value);
       return answer;
    }
}