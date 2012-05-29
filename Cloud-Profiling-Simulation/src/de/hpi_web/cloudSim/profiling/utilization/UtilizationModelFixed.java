package de.hpi_web.cloudSim.profiling.utilization;

import org.cloudbus.cloudsim.UtilizationModel;

public class UtilizationModelFixed implements UtilizationModel{

	/*
	 * (non-Javadoc)
	 * @see cloudsim.power.UtilizationModel#getUtilization(double)
	 */
	
	int util = 1;
	
	public UtilizationModelFixed(int util) {
		util = util;
	}
	
	@Override
	public double getUtilization(double time) {
		return util;
	}

}
