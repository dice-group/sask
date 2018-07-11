package weka.api;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.core.Attribute;
import weka.core.Instance;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.meta.FilteredClassifier;


public class SplitDataset 
{

	public static void main(String[] args) throws Exception
	{
//		load the arff file
		String filename = "C:\\Users\\harsh\\Desktop\\WEKA\\datasetExtraction.arff";
		DataSource source = new DataSource(filename);

		Instances dataset = source.getDataSet();
		System.out.println("printing summary of training data.....");
		System.out.println(dataset.toSummaryString());
//save arff file
		
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataset);
		saver.setFile(new File("C:\\Users\\harsh\\Desktop\\WEKA\\datasetArffExtraction.arff"));
		saver.writeBatch();
//		provide filter
		StringToWordVector converterVector = new StringToWordVector();
		converterVector.setInputFormat(dataset);
		//apply the filter
		Instances filteredDataset = Filter.useFilter(dataset, converterVector);
		saver.setInstances(filteredDataset);
		filteredDataset.setClassIndex(0);
//		System.out.println(filteredDataset.toString());
		System.out.println(filteredDataset.classAttribute());
		

		
		saver.setFile(new File("C:\\Users\\harsh\\Desktop\\WEKA\\datasetArff_Filtered.arff"));
		saver.writeBatch();
		//classify using j48
		J48 Treeclassifier = new J48();
		Treeclassifier.buildClassifier(filteredDataset);
		System.out.println("Classifier result.................");
		System.out.println(Treeclassifier.toString());
		System.out.println("printin data after filter.................");

//		System.out.println(filteredDataset.toString());
		System.out.println("Number of Classifier.................");
		System.out.println(filteredDataset.numClasses());
		System.out.println(filteredDataset.classAttribute());
		 int percent =66;
		 
		 int trainSize = (int) Math.round(filteredDataset.numInstances() * percent
				    / 100);
				int testSize = filteredDataset.numInstances() - trainSize;
				Instances train = new Instances(filteredDataset, 0, trainSize);
				Instances test = new Instances(filteredDataset, trainSize, testSize);
				 Evaluation evaluation = new Evaluation(train);
				 evaluation.evaluateModel(Treeclassifier, test);
				 System.out.println(evaluation.toSummaryString());
				 
		
		
		Evaluation eval = new Evaluation(filteredDataset);
//		System.out.println(eval.predictions().size());
		eval.crossValidateModel(Treeclassifier, filteredDataset, 10, new Random(1));
		
//		eval.evaluateModelOnceAndRecordPrediction(Treeclassifier, (Instance) filteredDataset);
		System.out.println("Printing evalution summary.........");
		System.out.println(evaluation.toSummaryString());
		System.out.println("printing evalution details result.......");
		System.out.println(evaluation.toClassDetailsString());
		System.out.println(evaluation.toMatrixString("=== Overall Confusion Matrix ===\n"));

//	printing result comparison......
		System.out.println("===================");
		System.out.println("Actual Class, Predicted Class");
		for (int i = 0; i < test.numInstances(); i++) 
		{
			//get class double value for current instance
			double actualClass = test.instance(i).classValue();
			//get class string value using the class index using the class's int value
			String actual = test.classAttribute().value((int) actualClass);
			//get Instance object of current instance
			Instance newInst = test.instance(i);
			//call classifyInstance, which returns a double value for the class
			double predNB = Treeclassifier.classifyInstance(newInst);
			//use this value to get string value of the predicted class
			String predString = test.classAttribute().value((int) predNB);
			System.out.println(actual+",         "+predString);
		}

			
			
			
			
		
		
		
		
		
		
		
		
		
		


			
		// TODO Auto-generated method stub

	}

}
