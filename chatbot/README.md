# chatbot
Artifacts for chatbot feature
Chatbot:

Steps to install:

Maven Build:

mvn clean package
mvn clean spring-boot:run

Release Notes:
1. HAWK Works But UI displays output some what crudely. HAWK still has bugs. Returns erratic output.
2. UI needs to be smoothed and displayed with proper Indents and niceties.
3. Support for Eliza. Currently all personal queries are re-directed to Eliza.
4. Initial Base Intent classifier Support. Supports Request to HAWK and Eliza. Default would throw Exception due to pending support for Sessa, Personal Query Handling.
