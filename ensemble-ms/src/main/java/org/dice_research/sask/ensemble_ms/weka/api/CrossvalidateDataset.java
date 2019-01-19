package org.dice_research.sask.ensemble_ms.weka.api;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stopwords.WordsFromFile;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.core.Instance;
import java.io.File;
import java.util.Random;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;


/**
 * This class take data from weka dataset and predict the training data class
 * using 10 fold crossValidation
 * class perform cross validation using Random Forest classifier 
 * 
 * @author Harsh Shah(hjshah142)
 */

public class CrossvalidateDataset {
	
	public static void main(String[] args)
     {
		
		CrossvalidateDataset crossvalidateDataset = new CrossvalidateDataset();
		crossvalidateDataset.buildClassfier();
		

	}
	private void buildClassfier() 
	{
		// load the arff file
		try {
			String filename = "WekaMlDataset\\traindata.arff";
			
			DataSource source= new DataSource(filename);
			Instances dataset = source.getDataSet();
			System.out.println(dataset.toSummaryString());
//			set the class index on dataset
			dataset.setClassIndex(1);
//           provide filter and set filter classifier
			StringToWordVector stringToWordVectorFilter = new StringToWordVector();
			dataset.setClassIndex(1);
//			set properties for strtoword vector filter
			stringToWordVectorFilter.setIDFTransform(true);
	        WordsFromFile stopwordsHandler = new WordsFromFile();
		    LovinsStemmer lovins_stemmer = new LovinsStemmer();
		    stringToWordVectorFilter.setStemmer(lovins_stemmer);
		    NGramTokenizer nGramTokenizer = new NGramTokenizer();
		    nGramTokenizer.setNGramMaxSize(3);
		    nGramTokenizer.setNGramMinSize(1);
//		    set the delimiters for ngram tokens
		    nGramTokenizer.setDelimiters(" \n 	.,;1234567890'\"()?!-/<>‘’“”…«»•&:{[|`^]}$*%");
		    stringToWordVectorFilter.setStopwordsHandler(stopwordsHandler);
//		    stringToWordVectorFilter.setWordsToKeep(500);
		    stringToWordVectorFilter.setTokenizer(nGramTokenizer);

		    stringToWordVectorFilter.setLowerCaseTokens(true);
		    
		 
			stringToWordVectorFilter.setInputFormat(dataset);
			dataset = Filter.useFilter(dataset,stringToWordVectorFilter);

//		 dataset.setClassIndex(1);
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataset);
			System.out.println(dataset.toSummaryString());
			saver.setFile(new File("WekaDataset\\datasetArffFiltered.arff"));
			saver.writeBatch();
			saver.setInstances(dataset);	
			// classify using Random Forest
	 
			RandomForest Treeclassifier = new RandomForest();
			Treeclassifier.buildClassifier(dataset);
			System.out.println("Classifier result.................");
			System.out.println(Treeclassifier.toString());
			System.out.println("printin data after filter.................");

			// System.out.println(dataset.toString());
			System.out.println("Number of Classifier.................");
			System.out.println(dataset.numClasses());
			System.out.println(dataset.classAttribute());

			Evaluation eval = new Evaluation(dataset);
			// System.out.println(eval.predictions().size());
			eval.crossValidateModel(Treeclassifier, dataset, 10, new Random(1));

			System.out.println("Printing evalution summary.........");
			System.out.println(eval.toSummaryString());
			System.out.println("printing evalution details result.......");
			System.out.println(eval.toClassDetailsString());
			System.out.println(eval.toMatrixString("=== Overall Confusion Matrix ===\n"));

			// printing result comparison......
			System.out.println("===================");
			System.out.println("Actual Class Predicted Class");
			for (int i = 0; i < dataset.numInstances(); i++) {
				// get class double value for current instance
				double actualClass = dataset.instance(i).classValue();
				// get class string value using the class index using the class's int value
				String actual = dataset.classAttribute().value((int) actualClass);
				// get Instance object of current instance
				Instance newInst = dataset.instance(i);
				// call classifyInstance, which returns a double value for the class
				double predNB = Treeclassifier.classifyInstance(newInst);
				// use this value to get string value of the predicted class
				String predString = dataset.classAttribute().value((int) predNB);
				System.out.println(actual + "            " + predString);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

}
