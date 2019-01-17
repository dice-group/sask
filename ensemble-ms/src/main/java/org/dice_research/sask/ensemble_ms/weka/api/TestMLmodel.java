package org.dice_research.sask.ensemble_ms.weka.api;




import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.Filter;


public class TestMLmodel {

	public static void main(String[] args) {
		
		TestMLmodel obj = new TestMLmodel();
		try {
			obj.evaluateMLmodel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	private void evaluateMLmodel() throws Exception {
		
		
		String filename = "TrainingData\\traindata2.arff";
		DataSource source= new DataSource(filename);
		Instances traindata = source.getDataSet();
		System.out.println(traindata.toSummaryString());
		traindata.setClassIndex(1);
		J48 treeClassifier = new J48();
		StringToWordVector stringToWordVectorFilter = new StringToWordVector();
		stringToWordVectorFilter.setInputFormat(traindata);
		traindata = Filter.useFilter(traindata,stringToWordVectorFilter);
	
		
		
		
		String testFilename = "TrainingData\\testingData.arff";
		DataSource source2= new DataSource(testFilename);
		Instances testingSet = source2.getDataSet();
		testingSet.setClassIndex(1);
 
		
	
		
		FilteredClassifier filteredClassifier = new FilteredClassifier();
	
		filteredClassifier.setFilter(stringToWordVectorFilter);
		filteredClassifier.setClassifier(treeClassifier);
		
		

		
		Evaluation evaluation = new Evaluation(traindata);
		 
		treeClassifier.buildClassifier(traindata);
		evaluation.evaluateModel(filteredClassifier, testingSet);
		System.out.println("Printing evalution summary.........");
		System.out.println(evaluation.toSummaryString());
		System.out.println("printing evalution details result.......");
//		System.out.println(evaluation.toClassDetailsString());
		System.out.println(evaluation.toMatrixString("=== Overall Confusion Matrix ===\n"));
		for (int i = 0; i < testingSet.numInstances(); i++) 
		{
			// get class double value for current instance
			double actualClass = testingSet.instance(i).classValue();
			// get class string value using the class index using the class's int value
			String actual = testingSet.classAttribute().value((int) actualClass);
			// get Instance object of current instance
			Instance newInst = testingSet.instance(i);
			// call classifyInstance, which returns a double value for the class
			double predNB = filteredClassifier.classifyInstance(newInst);
	
			// use this value to get string value of the predicted class
			String predString = testingSet.classAttribute().value((int) predNB);
			System.out.println(actual + ",         " + predString);
		
		
		
		// TODO Auto-generated method stub
		
	}
	}

}
