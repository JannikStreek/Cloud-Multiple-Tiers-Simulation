package de.hpi_web.cloudSim.profiling.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

import arx.ARX;

import de.hpi_web.cloudSim.multitier.staticTier.VmFactory;
import de.hpi_web.cloudSim.profiling.datacenter.DatacenterBuilder;
import de.hpi_web.cloudSim.profiling.datacenter.ProfilingBroker;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilManager;
import de.hpi_web.cloudSim.profiling.example.SimpleExample;

public class StartAction implements ActionListener{
	
	private Observer observer;
	private double delay = 0.5;
	private int upperThreshold = 70;
	private int lowerThreshold = 30;

	public StartAction(Observer observer) {
		super();
		this.observer = observer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		class Start extends Thread {
			
			@Override
			public void run() {
				SimpleExample.start(observer, delay, upperThreshold, lowerThreshold);
			}
		}
		
		Thread thread = new Start();
		thread.start();
		
	}
	
	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}
	
	public int getUpperThreshold() {
		return upperThreshold;
	}

	public void setUpperThreshold(int upperThreshold) {
		this.upperThreshold = upperThreshold;
	}

	public int getLowerThreshold() {
		return lowerThreshold;
	}

	public void setLowerThreshold(int lowerThreshold) {
		this.lowerThreshold = lowerThreshold;
	}


}
