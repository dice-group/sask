package chatbot.core.classifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * This class implements a simple text learner in Java using WEKA.
 * It loads a text dataset written in ARFF format, evaluates a classifier on it,
 * and saves the learnt model for further use.
 * @author Divya
 * @see MyFilteredClassifier
 */
public class IntentLearner {
	/**
	 * Object that stores the instance.
	 */
	private Instances testInstance;
	/**
	 * Object that stores training data.
	 */
	Instances trainData;
	/**
	 * Object that stores the filter
	 */
	StringToWordVector filter;
	Standardize sfilter;
	/**
	 * Object that stores the classifier
	 */
	FilteredClassifier classifier;
		
	/**
	 * This method loads a dataset in ARFF format. If the file does not exist, or
	 * it has a wrong format, the attribute trainData is null.
	 * @param fileName The name of the file that stores the dataset.
	 */
	public void loadDataset(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(reader);
			trainData = arff.getData();
			
			System.out.println("===== Loaded dataset: " + fileName + " =====");
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem found when reading: " + fileName);
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
			eval.crossValidateModel(classifier, trainData, 4, new Random(1));
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println("===== Evaluating on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem found when evaluating");
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
			filter.setInputFormat(trainData);
			trainData = Filter.useFilter(trainData, filter);
			System.out.println("Number of attributes in train=" + trainData.numAttributes());
			classifier.setFilter(filter);
			classifier.setClassifier(createClassifierModel());
			classifier.buildClassifier(trainData);
			// Uncomment to see the classifier
			System.out.println("===== Training on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem found when training");
		}
	}
	
	/**
	 * This method saves the trained model into a file. This is done by
	 * simple serialization of the classifier object.
	 * @param fileName The name of the file that will store the trained model.
	 */
	public void saveModel(String fileName) {
		try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName/*"resources/classifier/intentdata.model"*/));
            out.writeObject(classifier);
            out.close();
 			System.out.println("===== Saved model: " + fileName + " =====");	
        } 
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem found when writing: " + fileName);
		}
	}
	
	/**
	 * Next steps after obtaining the prediction from classifier.
	 * 
	 * @param  prediction
	 */
	/*public Handler usePrediction(IncomingRequest request,String query,String prediction) {
		Classifier deterministicClassifier = new Classifier();
		String detPrediction = deterministicClassifier.classify(request);
		try {
           if(prediction.equalsIgnoreCase("hawk")) {
        	   if(detPrediction.equals("hawk")) {
        		   saveNewInstance("\'"+query+"\',"+prediction);
        	   }else {
        		   saveNewInstance("\'"+query+"\',"+detPrediction);
        	   }
        		return new QAHandler();
           }
           else if(prediction.equalsIgnoreCase("sessa")) {
        	   if(detPrediction.equals("sessa")) {
        		   saveNewInstance("\'"+query+"\',"+prediction);
        	   }
        	   else {
        		   saveNewInstance("\'"+query+"\',"+detPrediction);
        	   }
        	   return new SessaHandler();
           }
           else if(prediction.equalsIgnoreCase("eliza")) {
        	   if(detPrediction.equals("eliza")) {
        		   saveNewInstance("\'"+query+"\',"+prediction);
        	   }
        	   else {
        		   saveNewInstance("\'"+query+"\',"+detPrediction);
        	   }
        	   return new ElizaHandler();
           }
        	   
        } 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	/**
	 * This method creates the instance to be classified, from the text that has been read.
	 */
	public Instances makeTestInstance(String query) {
		// Create the attributes, class and text
		ArrayList<String> fvNominalVal = new ArrayList<String>(3);
		fvNominalVal.add("eliza");
		fvNominalVal.add("hawk");
		fvNominalVal.add("sessa");
		Attribute attribute1 = new Attribute("query",(ArrayList<String>) null);
		Attribute attribute2 = new Attribute("queryIntent", fvNominalVal);
		
		// Create list of instances with one element
		ArrayList<Attribute> fvWekaAttributes = new ArrayList<Attribute>(2);
		fvWekaAttributes.add(attribute1);
		fvWekaAttributes.add(attribute2);
		testInstance = new Instances("intent", fvWekaAttributes, 1);           
		// Set class index
		//testInstance.setClassIndex(trainData.numAttributes() - 1);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setDataset(trainData);
		instance.setValue(fvWekaAttributes.get(0), query);
		//instance.setClassMissing();
		instance.classIsMissing();
		//instance.setClassValue(1);

		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		testInstance.add(instance);
		
			try {
				testInstance = Filter.useFilter(testInstance, filter);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 try {
			 ObjectOutputStream out;
		
			out = new ObjectOutputStream(new FileOutputStream("C:\\Users\\Divya\\Documents\\test.txt"));
		
         out.writeObject(testInstance.toString());
         out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(testInstance);
		return testInstance;
	}
	/**
	 * This method creates the new instance to be saved.
	 */
	public void saveNewInstance(String query) {
		// Create the attributes, class and text
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {


			File file = new File("C:\\Users\\Divya\\Documents\\try\\intentdata.arff");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(System.lineSeparator());
			bw.write(query);


		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}	
	
	/**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 */
	public void classify(String query) {
		try {
			Instances instances = makeTestInstance(query);
			System.out.println("OK till here");
			//Instances newTest2 = Filter.useFilter(test2, filter); 
			//for (int i = 0; i < instances.numInstances(); i ++){
				System.out.println("OK till here1");
				double pred = classifier.classifyInstance(instances.get(0));
				System.out.println("===== Classified instance =====");
				System.out.println("Class predicted: " + testInstance.classAttribute().value((int) pred));
				saveNewInstance("\'"+query+"\',"+ testInstance.classAttribute().value((int) pred));
			//}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problem found when classifying the text");
		}		
	}
	
	/**
	 * Main method. It is an example of the usage of this class.
	 * @param args Command-line arguments: fileData and fileModel.
	 */
	public static void main (String[] args) {
	
		IntentLearner learner;
		
		learner = new IntentLearner();
		learner.loadDataset("C:\\Users\\Divya\\Documents\\try\\intentdata.arff");
		
		learner.evaluate();
		learner.learn();
		//
		learner.saveModel("C:\\Users\\Divya\\Documents\\try\\intentdata.model");
		//learner.makeTestInstance("prince of persia");
		learner.classify("prince of persia");
			//learner.usePrediction(request, "what would it mean to you", testInstance.classAttribute().value((int) pred));
		
	}
}	
