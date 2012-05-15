package de.hpi_web.cloudSim.multitier.workload;

import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

import de.hpi_web.cloudSim.multitier.MultiTierCloudTags;
import de.hpi_web.cloudSim.multitier.MultiTierCloudlet;
import de.hpi_web.cloudSim.multitier.staticTier.CloudletFactory;

public class SpikeWorkloadGenerator extends WorkloadGenerator {
	public static final int INTERVALL = 500;
	public static final int SPIKE_TIMEFRAME = 80;			// TODO: set this in correlation with exponentFactor
	public static final double EXPONENT_FACTOR = -0.001;	// defines how steep the curve is
	public static final double STEP_TIME = 5;				// how often new items are scheduled

	@Override
	public void scheduleWorkloadForBroker(DatacenterBroker broker, double timeLimit) {
		int brokerId = broker.getId();
		int workload = LOAD_MIN;
		int startId = 0;
		List<MultiTierCloudlet> cl;// = CloudletFactory.createCloudlets(0, 300, brokerId);
		
		for (double t = 0.0; t <= timeLimit; t+= STEP_TIME) {
			if (t > timeLimit)
				break;
			
			// schedule min load until we reach the intervall-timeframe/2 when we start increasing the workload
			workload = LOAD_MIN;
			double modT = t % INTERVALL;
			
			if (modT > INTERVALL - SPIKE_TIMEFRAME/2 && modT <= INTERVALL)
				workload = LOAD_MIN + extraLoad(modT - INTERVALL);
			else if (modT < SPIKE_TIMEFRAME/2 && modT >= 0.0)
				workload = LOAD_MIN + extraLoad(modT);
			
			// make sure we don't exceed maximum workload
			workload = Math.min(LOAD_MAX, workload);
			cl = CloudletFactory.createCloudlets(startId, workload, brokerId);
			for (MultiTierCloudlet c : cl)
				broker.schedule(brokerId, t, MultiTierCloudTags.REQUEST_TAG, c);

			startId += workload;
			Log.printLine("Scheduled " + workload + " items at time: " + t);
		}
	}

	private int extraLoad(double x) {
		int extraLoad = 0;
		// Application of Gaussian distribution
		extraLoad = (int) Math.round((100.0*(1.0/Math.sqrt(2.0*Math.PI))*Math.pow(Math.E, (EXPONENT_FACTOR*(Math.pow(x, 2))))));
		return extraLoad;
	}

	@Override
	public void scheduleWorkloadForBroker(DatacenterBroker broker) {
		// TODO Auto-generated method stub

	}

}
