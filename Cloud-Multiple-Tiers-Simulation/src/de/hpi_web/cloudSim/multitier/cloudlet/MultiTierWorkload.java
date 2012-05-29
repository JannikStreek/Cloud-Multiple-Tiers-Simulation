package de.hpi_web.cloudSim.multitier.cloudlet;

import java.util.Map;
import java.util.Vector;

public abstract class MultiTierWorkload {
	protected Map<Integer, Integer> workloadPerTier;
	public abstract int getWorkloadForTier(int tier);
	public abstract void setWorkloadMap(Map<Integer, Integer> workloadMap);
	protected abstract int calculateWorkloadForTier(int tier);
}
