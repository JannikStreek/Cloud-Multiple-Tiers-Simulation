package de.hpi_web.cloudSim.multitier.datacenter;

import org.cloudbus.cloudsim.Log;

public class BrokerWithoutDatacenterException extends Exception {
	
	public BrokerWithoutDatacenterException() {
		Log.printLine("Broker without Datacenters.");
	}

}
