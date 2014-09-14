package de.brkn.fuzzynamematcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CosineSimilarity implements IStringMetric {

	private Map<String, Integer> positionVector;

	public double compare(String s0, String s1) {

		String[] biGramA = biGram(s0);
		String[] biGramB = biGram(s1);
		String[] biGram = concat(biGramA, biGramB);
		initCountVector(biGram);

		int[] nameAvector = createVector(biGramA);
		int[] nameBvector = createVector(biGramB);

		int a = 0;
		for (int i = 0; i< nameAvector.length; i++) {
			a += nameAvector[i] * nameBvector[i];
		}
		
		int b = 0;
		for (int i = 0; i< nameAvector.length; i++) {
			b += nameAvector[i] * nameAvector[i];
		}
		
		int c = 0;
		for (int i = 0; i< nameBvector.length; i++) {
			c += nameBvector[i] * nameBvector[i];
		}
		
		double result = a /(Math.sqrt(b) * Math.sqrt(c));
		
		result = result > 1 ? 1.0D : result;

		return result;
	}
	
	public String[] biGram(String str) {
		String[] bigram = new String[str.length() - 1];
		for (int i = 0; i < str.length() - 1; i++) {
			bigram[i] = str.substring(i, i + 2);
		}
		return bigram;
	}

	private void initCountVector(String[] biGram) {
		positionVector = new HashMap<String, Integer>();
		int p = 0;
		for (String b : biGram) {
			if(!positionVector.containsKey(b)){
				positionVector.put(b, p++);
			}
		}

	}

	private int[] createVector(String[] biGrams) {
		int[] vector = new int[positionVector.size()];
		HashMap<String, Integer> countMap = countMap(biGrams);
		
		Set<String> bigrams = countMap.keySet();
		Iterator<String> bigramsIterator = bigrams.iterator();

		while (bigramsIterator.hasNext()) {
			String bigram = (String) bigramsIterator.next();
			vector[positionVector.get(bigram)] = countMap.get(bigram);
		}

		return vector;
	}

	private HashMap<String, Integer> countMap(String[] bigrams) {
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		for (String bigram : bigrams) {
			Integer count = countMap.containsKey(bigram) ? countMap.get(bigram) : 0;
			countMap.put(bigram, count + 1);
		}
		return countMap;
	}

	private String[] concat(String[] as, String[] bs) {
		String[] cArray = new String[as.length + bs.length];
		for (int i = 0; i < as.length + bs.length; i++) {
			if (i < as.length) {
				cArray[i] = as[i];
			} else {
				cArray[i] = bs[i - as.length];
			}
		}
		return cArray;
	}

}
