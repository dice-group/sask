/**
 * 
 */



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

/**
 * @author Divya class to Classify the User Input as QA or KS or Normal
 *         emotion.
 */
public class ClassifierMl {
	/**
	 * String that stores the text to classify
	 */
	private String query;
	/**
	 * Object that stores the instance.
	 */
	private Instances instances;
	/**
	 * Object that stores the classifier.
	 */
	private FilteredClassifier classifier;
	/**
	 * This method loads the model to be used as classifier.
	 * @param fileName The name of the file that stores the text.
	 */
	public void loadModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
            in.close();
 			System.out.println("===== Loaded model: " + fileName + " =====");
       } 
		catch (Exception e) {
			e.printStackTrace();
			// Given the cast, a ClassNotFoundException must be caught along with the IOException
			System.out.println("Problem found when reading: " + fileName);
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
		instances = new Instances("Test relation", fvWekaAttributes, 1);           
		// Set class index
		instances.setClassIndex(instances.numAttributes() - 1);
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setValue(attribute1, query);
		// Another way to do it:
		// instance.setValue((Attribute)fvWekaAttributes.elementAt(1), text);
		instances.add(instance);
		 ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream("C:\\Users\\Divya\\Documents\\test.txt"));
		
         out.writeObject(instances.toString());
         out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		System.out.println("===== Instance created with reference dataset =====");
		System.out.println(instances);
	}
	
	/**
	 * This method performs the classification of the instance.
	 * Output is done at the command-line.
	 */
	public void classify() {
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			System.out.println("===== Classified instance =====");
			System.out.println("Class predicted: " + instances.classAttribute().value((int) pred));
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
	
		ClassifierMl classifier;
		
			classifier = new ClassifierMl();
			classifier.loadModel("C:\\Users\\Divya\\Documents\\intentdata.model");
			classifier.makeInstance("where can i find the best burgers");
			classifier.classify();
		
	}
}	

