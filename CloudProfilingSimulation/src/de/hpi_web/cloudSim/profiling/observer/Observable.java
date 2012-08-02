package de.hpi_web.cloudSim.profiling.observer;

public interface Observable {
	
	  public void notifyObservers();

	  public void register(Observer obs);

	  public void unRegister(Observer obs);

}
