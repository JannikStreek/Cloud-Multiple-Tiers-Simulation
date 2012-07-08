package de.hpi_web.cloudSim.profiling.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.hpi_web.cloudSim.profiling.CloudProfiler;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilizationThreshold;

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
				UtilizationThreshold cpu = new UtilizationThreshold(upperThreshold, lowerThreshold);
				UtilizationThreshold allOthers = new UtilizationThreshold(5000000, 0);
				CloudProfiler.start(observer, delay, "training.csv", "running.csv", cpu, allOthers, allOthers, allOthers);
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
