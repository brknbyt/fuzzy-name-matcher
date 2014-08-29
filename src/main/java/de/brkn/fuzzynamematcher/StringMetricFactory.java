package de.brkn.fuzzynamematcher;

public class StringMetricFactory {
	
	public IStringMetric createStringMetric(int metric){
		switch (metric) {
		case 0:
			return new LevenshteinDistance();
		case 1:
			return null;
		default:
			return null;
		}
	}

}
