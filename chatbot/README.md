# chatbot
Artifacts for chatbot feature
Chatbot:

Steps to install:

Check if the Eureka server from the sask/eureka-server module is running.
	curl -s -o /dev/null -w "%{http_code}" http://localhost:1111 is return value

If return value is 200 then goto step *
Else then run the eureka-server module using mvn clean spring-boot:run

* Maven Build:

mvn clean package
mvn clean spring-boot:run
Release Notes:[Last update: 11.01.2018]
1. Added Merged UI with Extraction team's UI framework. Chatbot is temporarily accessible via the usual URL and also the Common URL by running the web client package present. But it will not work any more . Please use the  UI available under http://ip:9090 to access the UI of chatbot. The UI code present under chatbot will probably load a UI but will not provide any functionality. 
2. Changed port ID from default port number.
3. Java 9 support.
4. HAWK Works But UI displays output some what crudely. HAWK still has bugs. Returns erratic output.
5. UI needs to be smoothed and displayed with proper Indents and niceties.
6. Support for Eliza. Currently all personal queries are re-directed to Eliza.
7. Initial Base Intent classifier Support. Supports Request to HAWK and Eliza. Default would throw Exception due to pending support for Sessa, Personal Query Handling.
