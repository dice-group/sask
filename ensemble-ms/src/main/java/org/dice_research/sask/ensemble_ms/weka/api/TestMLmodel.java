package org.dice_research.sask.ensemble_ms.weka.api;



import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.LovinsStemmer;

import weka.core.stopwords.WordsFromFile;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.filters.Filter;


public class TestMLmodel {

	public static void main(String[] args) {
		
		TestMLmodel obj = new TestMLmodel();
		try 
		{
			Instances traindata = obj.loadMLmodel();
			Instances testingSet = obj.loadTestData();
			
			obj.evaluateMLmodel(traindata,testingSet);
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		

	}

	public Instances loadMLmodel() throws Exception 
	{
		String filename = "WekaMlDataset\\traindata.arff";
		DataSource source= new DataSource(filename);
		Instances traindata = source.getDataSet();
		System.out.println(traindata.toSummaryString());
		traindata.setClassIndex(1);
		return traindata;		
	}
	public Instances loadTestData() throws Exception 
	{
		String testFilename = "WekaMlDataset\\testingData.arff";
		DataSource source2= new DataSource(testFilename);
		Instances testingSet = source2.getDataSet();
		testingSet.setClassIndex(1);
		return testingSet;
	}

	public void evaluateMLmodel(Instances traindata, Instances testingSet) throws Exception {
//		J48 treeClassifier = new J48();
		RandomForest treeClassifier = new RandomForest();	
		StringToWordVector stringToWordVectorFilter = new StringToWordVector();
//		set properties for strtoword vector filter
		
		stringToWordVectorFilter.setIDFTransform(true);
		
        WordsFromFile stopwordsHandler = new WordsFromFile();

	    LovinsStemmer lovins_stemmer = new LovinsStemmer();
	    
	    stringToWordVectorFilter.setStemmer(lovins_stemmer);
	    NGramTokenizer nGramTokenizer = new NGramTokenizer();
	    nGramTokenizer.setNGramMaxSize(1);
	    nGramTokenizer.setNGramMinSize(1);
	    nGramTokenizer.setDelimiters(" \n 	.,;1234567890'\"()?!-/<>‘’“”…«»•&:{[|`^]}$*%");
	    
	    
	    stringToWordVectorFilter.setStopwordsHandler(stopwordsHandler);
//	    stringToWordVectorFilter.setWordsToKeep(500);
//	    nGramTokenizer.setDelimiters(nGramTokenizer.getDelimiters().replaceAll(":", ""));
	    stringToWordVectorFilter.setTokenizer(nGramTokenizer);

	    stringToWordVectorFilter.setLowerCaseTokens(true);
	    
	   
		stringToWordVectorFilter.setInputFormat(traindata);
		traindata = Filter.useFilter(traindata,stringToWordVectorFilter);
		System.out.println(traindata.toSummaryString());
		FilteredClassifier filteredClassifier = new FilteredClassifier();
		filteredClassifier.setFilter(stringToWordVectorFilter);
		filteredClassifier.setClassifier(treeClassifier);
		Evaluation evaluation = new Evaluation(traindata);
		 
		treeClassifier.buildClassifier(traindata);
		evaluation.evaluateModel(filteredClassifier, testingSet);
		System.out.println("Printing evalution summary.........");
		System.out.println(evaluation.toSummaryString());
		System.out.println("printing evalution details result.......");
		System.out.println(evaluation.toClassDetailsString());
		System.out.println(evaluation.toMatrixString("=== Overall Confusion Matrix ===\n"));
		System.out.println("Sentence"+            " Actual  "   +           "Predicted Class");
		
		
		for (int i = 0; i < testingSet.numInstances(); i++) 
		  {
			
			// get class double value for current instance
			double actualClass = testingSet.instance(i).classValue();
			// get class string value using the class index using the class's int value
			String sentence  = testingSet.attribute(0).value(i);
			String actual = testingSet.classAttribute().value((int) actualClass);
			// get Instance object of current instance
			Instance newInst = testingSet.instance(i);
			// call classifyInstance, which returns a double value for the class
			double predNB = filteredClassifier.classifyInstance(newInst);
	
			// use this value to get string value of the predicted class
			String predString = testingSet.classAttribute().value((int) predNB);
			System.out.println(sentence+ "   " +actual + "   " + predString);
			System.out.println("Sentence:");
			System.out.println(sentence);
			System.out.println("Prdicated class for Sentence:");
			System.out.println(predString);
			System.out.println("---------------------------");
			

	    }
	}

}
