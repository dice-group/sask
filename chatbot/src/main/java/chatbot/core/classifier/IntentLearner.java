
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.Evaluation;

import java.util.ArrayList;
import java.util.Random;
import weka.classifiers.bayes.NaiveBayes;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.rules.ZeroR;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.stemmers.LovinsStemmer;

import java.io.*;

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
	
	/**
	 * This method evaluates the classifier. As recommended by WEKA documentation,
	 * the classifier is defined but not trained yet. Evaluation of previously
	 * trained classifiers can lead to unexpected results.
	 */
	public void evaluate() {
		try {
			trainData.setClassIndex(trainData.numAttributes() - 1);
			filter = new StringToWordVector();
			sfilter = new Standardize();
			filter.setAttributeIndices("first");
			classifier = new FilteredClassifier();
			classifier.setFilter(sfilter);
			classifier.setClassifier(new ZeroR());
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
			filter = new StringToWordVector();
			filter.setAttributeIndices("first");
			classifier = new FilteredClassifier();
			
			filter.setInputFormat(trainData);
			sfilter.setInputFormat(trainData);
			 filter.setIDFTransform(true);
			    LovinsStemmer stemmer = new LovinsStemmer();
			    filter.setStemmer(stemmer);
			    filter.setLowerCaseTokens(true);
			trainData = Filter.useFilter(trainData, sfilter);
			classifier.setFilter(sfilter);
			classifier.setClassifier(new ZeroR());
			classifier.buildClassifier(trainData);
			// Uncomment to see the classifier
			// System.out.println(classifier);
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
	 * This method creates the instance to be classified, from the text that has been read.
	 */
	public void makeInstance(String query) {
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
		testInstance.setClassIndex(testInstance.numAttributes() - 1);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setValue(attribute1, query);
		instance.setDataset(trainData);
		instance.classIsMissing();
		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		testInstance.add(instance);
		
			try {
				testInstance = Filter.useFilter(testInstance, sfilter);
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
	}
	
	/**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 */
	public void classify() {
		try {
			double pred = classifier.classifyInstance(testInstance.instance(0));
			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: " + testInstance.classAttribute().value((int) pred));
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
			learner.saveModel("C:\\Users\\Divya\\Documents\\try\\intentdata.model");
			learner.makeInstance("where can i find the best burgers");
			learner.classify();
		
	}
}	
