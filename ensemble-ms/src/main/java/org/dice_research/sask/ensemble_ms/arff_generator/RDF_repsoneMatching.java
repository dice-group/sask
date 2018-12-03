package org.dice_research.sask.ensemble_ms.arff_generator;


public class RDF_repsoneMatching {

	  /**
	   * Calculates the similarity of string  result between 0 and 1 b.
	   * @author Harsh Shah
	   */
	  public static double similarity(String s1, String s2) {
	    String longer = s1, shorter = s2;
	    if (s1.length() < s2.length()) { // longer should always have greater length
	      longer = s2; shorter = s1;
	    }
	    int longerLength = longer.length();
	    if (longerLength == 0)
	    {
	    	return 1.0; 
	    }
	    /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
	    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
	    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
	    return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

	  }

	  // Example implementation of the Levenshtein Edit Distance
	  // See http://rosettacode.org/wiki/Levenshtein_distance#Java
	  public static int editDistance(String s1, String s2) {
	    s1 = s1.toLowerCase();
	    s2 = s2.toLowerCase();

	    int[] costs = new int[s2.length() + 1];
	    for (int i = 0; i <= s1.length(); i++) {
	      int lastValue = i;
	      for (int j = 0; j <= s2.length(); j++) {
	        if (i == 0)
	          costs[j] = j;
	        else {
	          if (j > 0) {
	            int newValue = costs[j - 1];
	            if (s1.charAt(i - 1) != s2.charAt(j - 1))
	              newValue = Math.min(Math.min(newValue, lastValue),
	                  costs[j]) + 1;
	            costs[j - 1] = lastValue;
	            lastValue = newValue;
	          }
	        }
	      }
	      if (i > 0)
	        costs[s2.length()] = lastValue;
	    }
	    return costs[s2.length()];
	  }
//sim(Mpdel geold, Model extractor){
//	return int
//}
	  public static void check_response_similarity(String s, String t) {
	    System.out.println(String.format(
	      "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
	  }

	  public static void main(String[] args) {
		 SentenceExtractor obj = new SentenceExtractor();
		 
		 String bc;
		 String squery_result;
		 squery_result = obj.responseReader(0);
		 bc = "[http://dbpedia.org/resource/Google, http://dbpedia.org/ontology/Ceo, http://dbpedia.org/resource/Sundar_Pichai]";
		String Extractor_response = "<http://dbpedia.org/ontology/ceo> <null>.<null> <http://dbpedia.org/ontology/employer> <null>.<null> <http://dbpedia.org/ontology/parent> <null>.";
	    check_response_similarity(bc,Extractor_response);
		check_response_similarity(squery_result, Extractor_response);
	    check_response_similarity("mit", "sit");	    
//	    check_response_similarity(Ext,bc);
	  }

	}
