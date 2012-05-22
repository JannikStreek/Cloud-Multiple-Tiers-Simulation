package de.hpi_web.cloudSim.multitier.cloudlet;

import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class StaticWorkload extends MultiTierWorkload {

	@Override
	public int getWorkloadForTier(int tier) {
		return 1;
	}

	@Override
	public void setWorkloadMap(Map<Integer, Integer> workloadMap) {
		throw new NotImplementedException();

	}

	@Override
	protected int calculateWorkloadForTier(int tier) {
		throw new NotImplementedException();
	}

}
