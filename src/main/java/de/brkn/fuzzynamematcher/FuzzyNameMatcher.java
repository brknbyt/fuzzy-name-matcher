package de.brkn.fuzzynamematcher;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class FuzzyNameMatcher {

	public static final Pattern DIACRITICS_AND_FRIENDS = Pattern
			.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

	private IStringMetric stringMetric;

	public FuzzyNameMatcher(int stringMetric) {
		StringMetricFactory stringMetricFactory = new StringMetricFactory();
		this.stringMetric = stringMetricFactory
				.createStringMetric(stringMetric);
	}

	public boolean matchNames(String name0, String name1) {
		if (name0.isEmpty() || name1.isEmpty()) {
			return false;
		}
		String[] firstName = tokenizeName(cleanName(name0));
		String[] secondName = tokenizeName(cleanName(name1));
		return fuzzyNameMatching(firstName, secondName);
	}

	private String[] tokenizeName(String name) {
		String[] splittetName = name.split("\\W");
		ArrayList<String> tokenList = new ArrayList<String>();
		for (String nPart : splittetName) {
			if (null != nPart && nPart.length() > 0 && !nPart.matches("\\W+")) {
				tokenList.add(nPart.trim());
			}
		}
		return tokenList.toArray(new String[tokenList.size()]);
	}

	private String cleanName(String name) {
		String cleanedName = name.toLowerCase();
		cleanedName = Normalizer.normalize(cleanedName, Normalizer.Form.NFD);
		cleanedName = DIACRITICS_AND_FRIENDS.matcher(cleanedName).replaceAll("");
		cleanedName = cleanedName.replaceAll("(-|'|\\(|\\))", "");
		cleanedName = cleanedName.replaceAll("[ivx]+$", "");
		return cleanedName.trim();

	}

	private boolean fuzzyNameMatching(String[] nameA, String[] nameB) {
		int matches = 0;

		List<String> nameBList = new ArrayList<String>(Arrays.asList(nameB));
		for (int i = 0; i < nameA.length; i++) {
			Iterator<String> it = nameBList.iterator();
			while (it.hasNext()) {
				String nameBPart = (String) it.next();


				if (nameA[i].length() < 2 || nameBPart.length() < 2) {
					if (nameA[i].equals(nameBPart)) {
						it.remove();
						matches++;
						break;
					}
				} else {
					double similarity = stringMetric.compare(nameA[i],
							nameBPart);

					if (similarity > 0.5) {
						it.remove();
						matches++;
						break;
					}
				}
			}
		}

		if (matches == nameA.length) {
			return true;
		}
		return false;

	}

}
