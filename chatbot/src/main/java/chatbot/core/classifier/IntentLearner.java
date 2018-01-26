package chatbot.core.classifier;


import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.Evaluation;
import java.util.Random;

import org.apache.log4j.Logger;

import weka.classifiers.bayes.NaiveBayes;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.converters.ArffLoader.ArffReader;
import java.io.*;

/**
 * This class implements a simple text learner in Java using WEKA.
 * It loads a text dataset written in ARFF format, evaluates a classifier on it,
 * and saves the learnt model for further use.
 * @author Divya
 * @see MyFilteredClassifier
 */
public class IntentLearner {
	
	private static Logger log = Logger.getLogger(IntentLearner.class.getName());


	/**
	 * Object that stores training data.
	 */
	Instances trainData;
	/**
	 * Object that stores the filter
	 */
	StringToWordVector filter;
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
			
			log.info("===== Loaded dataset: " + fileName + " =====");
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			log.debug("Problem found when reading: " + fileName);
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
			filter.setAttributeIndices("first");
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
			classifier.setClassifier(new NaiveBayes());
			Evaluation eval = new Evaluation(trainData);
			eval.crossValidateModel(classifier, trainData, 4, new Random(1));
			log.debug(eval.toSummaryString());
			log.debug(eval.toClassDetailsString());
			log.debug("===== Evaluating on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			e.printStackTrace();
			log.debug("Problem found when evaluating");
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
			trainData = Filter.useFilter(trainData, filter);
			classifier.setFilter(filter);
			classifier.setClassifier(new NaiveBayes());
			classifier.buildClassifier(trainData);
			// Uncomment to see the classifier
			// log.debug(classifier);
			log.debug("===== Training on filtered (training) dataset done =====");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method saves the trained model into a file. This is done by
	 * simple serialization of the classifier object.
	 * @param fileName The name of the file that will store the trained model.
	 */
	public void saveModel() {
		try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("src/main/resources/classifier/intentdata.model"));
            out.writeObject(classifier);
            out.close();
        } 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main method. It is an example of the usage of this class.
	 * @param args Command-line arguments: fileData and fileModel.
	 */
	public static void main (String[] args) {
	
		IntentLearner learner;
		
			learner = new IntentLearner();
			learner.loadDataset("src/main/resources/classifier/intentdata.arff");
		
			learner.evaluate();
			learner.learn();
			learner.saveModel();
		
	}
}	
