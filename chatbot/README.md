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

Release Notes:
1. HAWK Works But UI displays output some what crudely. HAWK still has bugs. Returns erratic output.
2. UI needs to be smoothed and displayed with proper Indents and niceties.
3. Support for Eliza. Currently all personal queries are re-directed to Eliza.
4. Initial Base Intent classifier Support. Supports Request to HAWK and Eliza. Default would throw Exception due to pending support for Sessa, Personal Query Handling.
