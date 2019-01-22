## Predict the Efficiency of Extractor using ML model


#### Goal 

To extract the sentences from training data (OKE-Open Knowledge Extraction files) provide it to every extractors currently working and take the responses of every extractors and implement machine learning model on it. Machine leaning model will predict the efficiency of extractor and provide result which extractor is efficient for which types of data and can be used in future on new dataset to find and run on the most efficient extractor for given data.

### Example Sentence: 
John F. Kennedy was elected as US President in 1960. 
#### Result
Best Extractor for this sentence is: Fox Extractor 
### Work-Flow Diagram

Figure describes work-flow diagram for this task.

* As shown in figure sentence first extracted form the oke files and given to different extractors.
*  After that reponses of every extractors will be taken and transformed in to unified format.
* After transforming in unified format result will compare with oke triple and based on the result best extractor for this given sentence will be selected.
* This knowledge will be used to built machine learning model and machine learning model will predict best extractor for given sentence.
* Brief description of basic functionality of every classes is mentioned after the image. 
![project-task-overview](https://user-images.githubusercontent.com/38283252/51509760-a6dc5d80-1dfa-11e9-893b-7977661ae2fc.jpg)

##### SentenceExtractor class 
* Take  data from oke folder fosort them and provide it to different extractors and store response from extractors

 * Perform Sparql query on the ttl files from oke files to construct rdf triple and write in ttl format
 
 * Filter the extractor response by reading as rdf model and write in N - Triple format
  
 * foxResponseProcessing(), openIEResponseProcessing(), sorokinResponseProcessing() functions will separate the subjects, object, predicate and save it to the stringlist for every sentence and extractors
 * ResponseMatching functions will compare the triples of every extractor with OKE response and based on the comparison it will calculate the score for every extractor
 * findmaxscore() function will find the max score and select it as best extractor
 * trainingFileWriter will write and prepare the training data for machine learning model and select best-extractor for that sentence. 
 
#####  Testingdatabuilder class

* This class will prepare the testing file in arff format for the machine learning model for testing best extractor for the sentence.
* Class will take sentences as input and prepare arff file
File will be saved in WekaMlDataset\\testingData.arf

##### Sentence Extractor Testing class

* This class write junit test the score for response matching classes for every extractor in different type of and also test function of the senteceextractor class.
 * possibility for subject object predicate in oke and extractors   
 
#####  TestMLmodel class
 
  * This class take training data  and traindata as input for machine learning model and build machine learning model using RandomForest classifiers
 * Class also uses strtowordvector filter which convert string text data to the token and sets the different properties for the filter strtoword vector such as stopword, stemmers,tokens 
 * Test dataset on the arff file builded using class TestingDataBuilder  using Meta Filter Classifiers
 * Class will predict the best extractor class for the given sentence 
 * Class can be find the best extractor for any sentence given in the testing data
 
#####  SplitMLmodel class

 * This class take training data for machine learning model and split data set into train and test instances by percentage
 * Train machine learning model on train set using random forest tree classifier
 * Test dataset and compare the result of the classification with actual class to find the performance of Machine Learning Model
 
#####   CrossvalidateDataset class

 * This class take training data for machine learning model and train and test using 10 fold crossValidation
 * class perform cross validation using Random Forest classifier 
 *  Compares the predicted the result with actual to check the performance of Machine Learning model
 
#####   MLmodelTesting class

 * This class test the machine learning model which predict the best extractor for each sentence
 * This junit-test test  build the machine learning model using different classifiers such as RandomForest, J48, Naivebayes classifier and Support vector machine
 * For each classifier it predict the best extractor for given training data 

#####   SplitMLmodelTest class
 
  * SplitingMlmodelTest junit test class take training data for machine learning model and split data set into train and test instances by different split percentage Test 
 * For every test Train machine learning model on train set using random forest tree classifier
 * Test dataset and compare the result of the classification and f value for the every spliting percentage tested