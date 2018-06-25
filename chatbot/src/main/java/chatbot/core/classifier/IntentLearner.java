package chatbot.core.classifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

import chatbot.core.handlers.Handler;
import chatbot.core.handlers.eliza.ElizaHandler;
import chatbot.core.handlers.qa.QAHandler;
import chatbot.core.handlers.rivescript.RiveScriptOutputAnalyzer;
import chatbot.core.handlers.rivescript.RiveScriptQueryHandler;
import chatbot.core.handlers.sessa.SessaHandler;
import chatbot.io.incomingrequest.FeedbackRequest;
import chatbot.io.incomingrequest.FeedbackRequest.Feedback;
import chatbot.io.incomingrequest.IncomingRequest;
import chatbot.utils.spellcheck.SpellCheck;
import chatbot.utils.spellcheck.SpellCheck.LanguageList;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Utils;
import weka.core.converters.ArffLoader.ArffReader;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * This class implements a simple text learner in Java using WEKA.
 * It loads a text dataset written in ARFF format, evaluates a classifier on it,
 * and saves the learnt model for further use.
 * @author Divya
 * @see MyFilteredClassifier
 */
public class IntentLearner {
	
	private static final String resourcePath = "classifier/";
	private static final String trainingData = "intentdata.arff";
	private static final String model = "intentdata.model";
	private static Logger log = Logger.getLogger(IntentLearner.class.getName());
	/**
	 * Object that stores the instance.
	 */
	private Instances testInstance;
	/**
	 * Object that stores training data.
	 */
	private Instances trainData;
	/**
	 * Object that stores the filter
	 */
	private StringToWordVector filter;
	/**
	 * Object that stores the classifier
	 */
	private FilteredClassifier classifier;
		
	/**
	 * This method loads a dataset in ARFF format. If the file does not exist, or
	 * it has a wrong format, the attribute trainData is null.
	 * @param fileName The name of the file that stores the dataset.
	 */
	public synchronized void loadDataset(String fileName) {
		try {
			//fileName = "classifier/intentdata.arff";
			//URL resource = this.getClass().getResource(fileName);
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			trainData = arff.getData();
			
			log.debug("===== Loaded dataset: " + fileName + " =====");
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			log.warn("Problem found when reading: " + fileName);
		}
	}
	
	public LibSVM createClassifierModel() {
		LibSVM svm = new LibSVM();
		svm.setKernelType(new SelectedTag(0, LibSVM.TAGS_KERNELTYPE));
		svm.setSVMType(new SelectedTag(0, LibSVM.TAGS_SVMTYPE));
		svm.setProbabilityEstimates(true);
		return svm;
	}
	
	public StringToWordVector createFilter() throws Exception {
		filter = new StringToWordVector();
		/*NGramTokenizer tokenizer = new NGramTokenizer();
		tokenizer.setNGramMaxSize(3);
		filter.setTokenizer(tokenizer);
		filter.setTFTransform(true);
		filter.setIDFTransform(true);
		filter.setInputFormat(trainData);*/
		return filter;
	}
	/**
	 * This method evaluates the classifier. As recommended by WEKA documentation,
	 * the classifier is defined but not trained yet. Evaluation of previously
	 * trained classifiers can lead to unexpected results.
	 */
	public void evaluate() {
		try {
			trainData.setClassIndex(trainData.numAttributes() - 1);
			classifier = new FilteredClassifier();
			classifier.setFilter(createFilter());
			classifier.setClassifier(createClassifierModel());
			//classifier.setClassifier(new J48());
			Evaluation eval = new Evaluation(trainData);
			eval.crossValidateModel(classifier, trainData, 10, new Random(1));
			log.info(eval.toSummaryString());
			log.info(eval.toClassDetailsString());
			log.debug("===== Evaluating on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			e.printStackTrace();
			log.warn("Problem found when evaluating");
		}
	}
	
	/**
	 * This method trains the classifier on the loaded dataset.
	 */
	public void learn() {
		try {
			trainData.setClassIndex(trainData.numAttributes() - 1);
			filter = createFilter();
			
			classifier = new FilteredClassifier();
			//sfilter.setInputFormat(trainData);
//			filter.setInputFormat(trainData);
//			trainData = Filter.useFilter(trainData, filter);
			log.debug("Number of attributes in train=" + trainData.numAttributes());
			classifier.setFilter(filter);
			classifier.setClassifier(createClassifierModel());
			classifier.buildClassifier(trainData);
			// Uncomment to see the classifier
			log.debug("===== Training on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			e.printStackTrace();
			log.warn("Problem found when training");
		}
	}
	
	/**
	 * This method saves the trained model into a file. This is done by
	 * simple serialization of the classifier object.
	 * @param fileName The name of the file that will store the trained model.
	 */
	public synchronized void saveModel(String fileName) {
		try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName/*"resources/classifier/intentdata.model"*/));
            out.writeObject(classifier);
            out.close();
 			log.info("===== Saved model: " + fileName + " =====");	
        } 
		catch (IOException e) {
			e.printStackTrace();
			log.warn("Problem found when writing: " + fileName);
		}
	}
	
	/**
	 * Next steps after obtaining the prediction from classifier.
	 * 
	 * @param  prediction
	 */
	public Handler usePrediction(String prediction) {
		
		try {
           if(prediction.equalsIgnoreCase("hawk")) {
        	  
        		return new QAHandler();
           }
           else if(prediction.equalsIgnoreCase("sessa")) {
        	 
        	   return new SessaHandler();
           }
           else if(prediction.equalsIgnoreCase("eliza")) {
        	  
        	   return new ElizaHandler();
           }
        	   
        } 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * This method creates the instance to be classified, from the text that has been read.
	 */
	public Instances makeTestInstance(String query) {
		
		// Create the attributes, class and text
		ArrayList<String> fvNominalVal = new ArrayList<String>(3);
		fvNominalVal.add("eliza");
		fvNominalVal.add("hawk");
		fvNominalVal.add("sessa");
		ArrayList<String> queryVal=new  ArrayList<String>();
		queryVal.add(query);
		Attribute attribute1 = new Attribute("query",(ArrayList<String>) null);
//		Attribute attribute1 = new Attribute("query",queryVal);
		Attribute attribute2 = new Attribute("queryIntent", fvNominalVal);
		
		// Create list of instances with one element
		ArrayList<Attribute> fvWekaAttributes = new ArrayList<Attribute>(2);
		fvWekaAttributes.add(attribute1);
		fvWekaAttributes.add(attribute2);
		testInstance = new Instances("intent", fvWekaAttributes, 1);  
		testInstance.setClass(attribute2);
		// Set class index
		//testInstance.setClassIndex(trainData.numAttributes() - 1);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setDataset(trainData);
		instance.setValue((Attribute)fvWekaAttributes.get(0), query);
		instance.setClassMissing();
		instance.classIsMissing();
		//instance.setClassValue(1);

		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		//testInstance.add(instance);
		
			try {
//				testInstance = Filter.useFilter(testInstance, filter);
				testInstance.add(instance);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		/* try {
			 ObjectOutputStream out;
		
			out = new ObjectOutputStream(new FileOutputStream("classifier/test.txt"));
		
         out.writeObject(testInstance.toString());
         out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
 		log.info("===== Instance created with reference dataset =====");
		log.info(testInstance);
		return testInstance;
	}
	/**
	 * This method creates the new instance to be saved.
	 */
	public synchronized void saveNewInstance(String filePath , String query) {
		// Create the attributes, class and text
		BufferedWriter bw = null;
		//FileWriter fw = null;

		try {


			File file = new File(filePath);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			//fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(new FileWriter(filePath , true));
			bw.write(System.lineSeparator());
			bw.write(query);


		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null) {
					bw.flush();
					bw.close();
				}

				/*if (fw != null)
					fw.close();*/

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}	
	public synchronized void deleteFromInstanceFile(String query) {
		// Create the attributes, class and text
		BufferedWriter bw = null;
		//FileWriter fw = null;
		query = query.replace("'", "");
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
			URL urlTrainingData = classLoader.getResource(resourcePath+ trainingData);
			String trainingDataFile = urlTrainingData.getFile();
			File trainFile = new File(trainingDataFile);
			URL urlTempData = classLoader.getResource(resourcePath); 	
			String tempDataFile = urlTempData.getFile();
			File tempFile = new File(tempDataFile+ "temp.arff");
			tempFile.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(trainFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			String sCurrentLine;

			while ((sCurrentLine = reader.readLine()) != null) {
				if(sCurrentLine.trim().contains(query)) {
					continue;
				}else {
					 writer.write(sCurrentLine + System.getProperty("line.separator"));
				}
			}
			writer.flush();
			writer.close();
			reader.close();
			tempFile.renameTo(trainFile);
			

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null) {
					bw.flush();
					bw.close();
				}

				/*if (fw != null)
					fw.close();*/

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
	/**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 * @param  
	 */
	public String classify(String filePath , String query) {
		try {
			Instances instances = makeTestInstance(query);
			//Instances newTest2 = Filter.useFilter(test2, filter); 
			//for (int i = 0; i < instances.numInstances(); i ++){
				double pred = classifier.classifyInstance(instances.get(instances.numInstances()-1));
				log.debug("===== Classified instance =====");
				String prediction =testInstance.classAttribute().value((int) pred);
				log.warn("Query= " + query + ",Class predicted: " + prediction);
				saveNewInstance(filePath , "\'"+query+"\',"+ prediction);
				return prediction;
			//}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.warn("Problem found when classifying the text");
		}
		return null;		
	}
	/**
	 * check rivescript first
	 * @return 
	 */
	public Handler classify(IncomingRequest request) {
		try {
			String query = request.getRequestContent().get(0).getText().toLowerCase();
			//Preprocess User Input. Do not expect it to be perfect.
			//Query may contain some basic spelling mistakes which require to be corrected.Currently Language is hardcoded. 
			//It should also come from IncomingRequest class in future since it should ideally depend on browser language so that user queries can be answered efficiently.
			//Check Input. It should not contain bad inputs.
			if(query.isEmpty()) {
				log.warn("Handle Null inputs, Throwing Exception here");
				throw new IllegalArgumentException("Null Input");
			}
			query = handlePreProcessing(query);
			//Set Query here to Request for now?
			request.getRequestContent().get(0).setText(query); //Update Request class.
			query = query.toLowerCase(); //Sometimes spell check returns caps.
			RiveScriptQueryHandler basicText = new RiveScriptQueryHandler();
			// isQueryFound method is now moved to RiveScriptOutputAnalyzer class
			// TODO: Do we need two classes for Rivescripts or can we merge of of them
			boolean flag = RiveScriptOutputAnalyzer.isQueryFound(query);
			log.debug("query is :: "+ query);
			if (flag) {
				log.info("basicText execution");
				return basicText;
			}
			else {
				return handleIntentClassification(request);
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return null;
	}
	
	private String handlePreProcessing(String query) {
		//Spell Check
		String result = query;
		SpellCheck spell = new SpellCheck(LanguageList.ENGLISH);
		result = spell.correctSpelling(query);
		log.info("Corrected Query:" + result); 
		return result;
	}
	/**
	 * Handle intent classification
	 */
	public Handler handleIntentClassification(IncomingRequest request) {
		//IntentLearner learner;
		String query = request.getRequestContent().get(0).getText().toLowerCase();
		query = query.replace("'", "");
		//learner = new IntentLearner();
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL urlTrainingData = classLoader.getResource(resourcePath+ trainingData);
		String trainingDataFile = urlTrainingData.getFile();
		this.loadDataset(trainingDataFile);
		
		this.evaluate();
		this.learn();
		URL urlModel = classLoader.getResource(resourcePath + model);
		this.saveModel(urlModel.getFile());
		String prediction=this.classify(trainingDataFile , query);
		return this.usePrediction(prediction);
		
	}
	/**
	 * process user feedback and delete entries that get negative feedback
	 * @param request
	 */
	public void processFeedback(FeedbackRequest request) {
		// TODO Auto-generated method stub
		String query=request.getQuery();
		Feedback feedback = request.getFeedback();
		if(feedback.equals(Feedback.NEGATIVE)) {
			deleteFromInstanceFile(query);
		}
	}
	/**
	 * Main method. It is an example of the usage of this class.
	 * @param args Command-line arguments: fileData and fileModel.
	 */
/*	public static void main (String[] args) {
	
		IntentLearner learner;
		
		learner = new IntentLearner();
		learner.loadDataset("C:\\Users\\Divya\\Documents\\try\\intentdata.arff");
		
		learner.evaluate();
		learner.learn();
		learner.deleteFromInstanceFile("obama");
		//
		learner.saveModel("C:\\Users\\Divya\\Documents\\try\\intentdata.model");
		//learner.makeTestInstance("prince of persia");
		learner.classify("how do you feel about that");
		//learner.usePrediction(request, "what would it mean to you", testInstance.classAttribute().value((int) pred));
		
		
	}*/


}	