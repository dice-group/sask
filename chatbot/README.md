# chatbot
Artifacts for chatbot feature:

### Steps to install:

Check if Eureka-server is running. If not, start it.
Start chatbot microservice and webclient microservice for UI. For Start procedure, Refer ReadMe of sask.

### Categories of Query:

Automatic Workflow Generation (For ex: Extract from File1.txt using FOX).

QAHandler: Question Answer (For ex: What is the capital of Germany? Who are the members of the German national Football team? etc).

SessaHandler: Keyword Search (For ex: Berlin Mayor, President of USA etc).

Eliza: Psychologist (For ex: I want to know something etc.)

Rivescript: Personal Greetings( For ex: Hello, How are you, good bye etc).

### About Current Handlers:

Current SessaHandler is based on dice-group/Sessa. But it requires Sessa to be manually started on 7070 port. Other keyword search algorithms can also be linked as long as SessaHandler.java is modified.

Current QA handler is based on TebaQa, a third party Question answering system. Refer to their website for more details.

Eliza is based on the open source Eliza bot which was re-adapted to java. The entire open source code without the UI Applet logic is used in our code.

Rivescript is a set of static files present under resources. Please refer to standard rivescript query response on their website to modify the files.

Classifier.java uses Machine Learning to predict whether the query needs to be forwarded to QA, Sessa or Eliza. It uses LibSVM of weka library on a bag of words model. The Training data set is present under resources.

### UI

The test UI is integrated with Extraction UI and it is available under webclient microservice. To access, use http://localhost:9090/

### Rest API

/chatbot/chat : For any chat query.

Request and Response classes are defined for easy readability of the expected input and output. It is important that any class extended uses the Response class to return data to webclient UI. Other data types cannot be returned. Similarly, parameters passed with /chat Rest interface must be a json of type Request. Use parameters as defined under Request with the parameter names similar to the name of the member variables. For more detail, Read dependency injection of Spring.

/chatbot/feedback: For providing feedback (whether the user is happy with the response received to his query). 

The input for this method must be of type FeedbackRequest with two parameters (Query Name and whether it is a positive or negative feedback). Returns None. This feedback is important for improvement of the Machine learning algorithm used.

For other details, Please refer to the wiki page or general ReadMe of sask.

