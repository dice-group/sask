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
!!!!!!!Intermediate Code update:
This is intermediate checkin which will break all functionality. DO NOT USE THIS CODE BASE UNTIL ENTIRE CODE HAS BEEN CHECKED IN!!!!!!!
PURPOSE: CODE TO PROVIDE GOOD READABILITY BY USING DEPENDANCY INJECTION OF SPRING TO MANIPULATE OUTPUT. WHEN OTHERS HAVE TO IMPLEMENT THIS CLASS CAN BE USED FOR EASY IMPLEMENTATION INSTEAD OF UI GUESS WORK!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!
Release Notes:[Last update: 14.12]
1. Added Merged UI with Extraction team's UI framework. Chatbot is temporarily accessible via the usual URL and also the Common URL by running the web client package present. Future changes would not be made under chatbot folder.
2. Changed port ID from default port number.
3. Java 9 support.
4. HAWK Works But UI displays output some what crudely. HAWK still has bugs. Returns erratic output.
5. UI needs to be smoothed and displayed with proper Indents and niceties.
6. Support for Eliza. Currently all personal queries are re-directed to Eliza.
7. Initial Base Intent classifier Support. Supports Request to HAWK and Eliza. Default would throw Exception due to pending support for Sessa, Personal Query Handling.
