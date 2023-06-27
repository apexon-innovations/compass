package com.apexon.compass.dashboard.model.score;

import com.apexon.compass.dashboard.model.CollectorItem;
import org.bson.types.ObjectId;

public class ScoreCollectorItem extends CollectorItem {

	private static final String DASHBOARD_ID = "dashboardId";

	public ObjectId getDashboardId() {
		return (ObjectId) getOptions().get(DASHBOARD_ID);
	}

	public void setDashboardId(ObjectId dashboardId) {
		getOptions().put(DASHBOARD_ID, dashboardId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof ScoreCollectorItem))
			return false;
		ScoreCollectorItem that = (ScoreCollectorItem) o;
		return getDashboardId().equals(that.getDashboardId());
	}

	@Override
	public int hashCode() {
		int result = getDashboardId().hashCode();
		return result;
	}

}
