package de.hpi_web.cloudSim.multitier.cloudlet;

import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ExponentialGrowth extends MultiTierWorkload {

	@Override
	public int getWorkloadForTier(int tier) {
		return calculateWorkloadForTier(tier);
	}

	@Override
	protected int calculateWorkloadForTier(int tier) {
		return (int) Math.pow(tier, 2);
	}

	@Override
	public void setWorkloadMap(Map<Integer, Integer> workloadMap) {
		throw new NotImplementedException();
	}

}
