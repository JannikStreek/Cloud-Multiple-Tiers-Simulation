/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudprofilingmonitor;

import de.hpi_web.cloudSim.profiling.example.SimpleExample;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import java.io.File;

/**
 *
 * @author christoph
 */
public class Simulation extends Thread {
    private Observer observer;
    private File running, training;
    private double delay = 0.5;
    private int upperThreshold = 70;
    private int lowerThreshold = 30;

    Simulation(Observer observer, File training, File running) {
        super();
        this.observer = observer;
        this.running = running;
        this.training = training;
    }

    @Override
    public void run() {

        SimpleExample.start(observer, delay, upperThreshold, lowerThreshold);
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
