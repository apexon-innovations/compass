package com.apexon.compass.dashboard.util;

import com.apexon.compass.dashboard.model.Feature;

import java.util.Comparator;

/**
 * Comparator utility class for handling comparisons of Super Features (epics) to other
 * Super Features. This is used when manipulating results of MongoDB repository responses
 * and sorting them in an efficient manner within memory.
 *
 * @author kfk884
 *
 */
public class SuperFeatureComparator implements Comparator<Feature> {

	public SuperFeatureComparator() {
		// No instantiation requirements
	}

	/**
	 * Compares two feature object models and sorts based on their subsequent epic IDs, as
	 * a string comparator
	 * @return A list of Features sorted by EpicID, descending
	 */
	@Override
	public int compare(Feature f, Feature ff) {
		if (f.getsEpicID().compareToIgnoreCase(ff.getsEpicID()) <= -1) {
			return -1;
		}
		else if (f.getsEpicID().compareToIgnoreCase(ff.getsEpicID()) >= 1) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
