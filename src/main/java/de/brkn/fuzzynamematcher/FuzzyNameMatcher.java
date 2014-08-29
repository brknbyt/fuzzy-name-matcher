package de.brkn.fuzzynamematcher;

import java.util.ArrayList;

public class FuzzyNameMatcher {

	private IStringMetric stringMetric;

	public FuzzyNameMatcher(int stringMetric) {
		StringMetricFactory stringMetricFactory = new StringMetricFactory();
		this.stringMetric = stringMetricFactory
				.createStringMetric(stringMetric);
	}

	public boolean matchNames(String name0, String name1) {
		String[] firstName = tokenizeName(name0);
		String[] secondName = tokenizeName(name1);
		return fuzzyNameMatching(firstName, secondName);

	}
	
	private String[] tokenizeName(String name){
		String[] splittetName = name.split("\\W");
		ArrayList<String> tokenList = new ArrayList<String>();
		
		for(String nPart: splittetName){
			if(null != nPart && nPart.length() > 0 && !nPart.matches("\\W")){
				tokenList.add(nPart);
			}
		}
		
		return tokenList.toArray(new String[tokenList.size()]);
	}

	private boolean fuzzyNameMatching(String[] nameA, String[] nameB) {
		String nA = "";
		String nB = "";
		String lastFirstA = nameA[nameA.length - 1];
		String lastFirstB = nameB[nameB.length - 1];
		
		for (int i = 0; i < nameA.length; i++) {
			nA += nameA[i];
			if (nameA.length > 1 && i < nameA.length - 1) {
				lastFirstA += nameA[i];
			}
		}
		for (int j = 0; j < nameB.length; j++) {
			nB += nameB[j];
			if (nameB.length > 1 && j < nameB.length - 1) {
				lastFirstB += nameB[j];
			}
		}

		nA = nA.replaceAll("(\\s|\\.|-|')", "");
		nB = nB.replaceAll("(\\s|\\.|-|')", "");
		double lFullName = stringMetric.compare(nA, nB);
		double lLastFirstAB = stringMetric.compare(lastFirstA, nB);
		double lLastFirstBA = stringMetric.compare(nA, lastFirstB);

		int[] positions = new int[nameA.length];
		int matches = 0;
		for (int i = 0; i < nameA.length; i++) {
			for (int j = 0; j < nameB.length; j++) {
				double similarity = stringMetric.compare(nameA[i], nameB[j]);

				float p = similarity == 0 ? 0 : (float) similarity
						/ (float) nameA[i].length();
				if (p <= 0.4) {
					if (matches < positions.length) {
						positions[matches] = j;
						matches++;
					}
				}
			}
		}

		float p = (float) matches / nameA.length * matches / nameB.length;
		float q1 = (float) lFullName / nA.length();
		float q2 = (float) lLastFirstAB / nA.length();
		float q3 = (float) lLastFirstBA / nA.length();

		if (matches == nameA.length || p >= 0.5 || q1 <= 0.33 || q2 <= 0.33
				|| q3 <= 0.33) {
			return true;
		}
		return false;
	}

}
