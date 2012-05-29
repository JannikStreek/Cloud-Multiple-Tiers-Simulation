package de.hpi_web.cloudSim.multitier.cloudlet;

import java.util.HashMap;
import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CustomGrowth extends MultiTierWorkload {
	
	CustomGrowth() {
		workloadPerTier = new HashMap<Integer, Integer>();
	}

	@Override
	public int getWorkloadForTier(int tier) {
		if(workloadPerTier.containsKey(tier))
			return workloadPerTier.get(tier);
		return 0;
	}

	@Override
	public void setWorkloadMap(Map<Integer, Integer> workloadMap) {
		workloadPerTier = workloadMap;
	}

	@Override
	protected int calculateWorkloadForTier(int tier) {
		throw new NotImplementedException();
	}

}
