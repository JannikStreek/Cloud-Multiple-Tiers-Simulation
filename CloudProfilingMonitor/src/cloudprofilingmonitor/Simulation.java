/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudprofilingmonitor;

import de.hpi_web.cloudSim.profiling.example.CloudProfiler;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilizationThreshold;
import java.io.File;

/**
 *
 * @author christoph
 */
public class Simulation extends Thread {
    private Boolean stopped = false;
    private Observer observer;
    private File running, training;
    private double delay = SimulationDefaults.STEP_DELAY;

    public UtilizationThreshold getBwInThreshold() {
        return bwInThreshold;
    }

    public void setBwInThreshold(UtilizationThreshold bwThreshold) {
        this.bwInThreshold = bwThreshold;
    }

    public UtilizationThreshold getBwOutThreshold() {
        return bwOutThreshold;
    }

    public void setBwOutThreshold(UtilizationThreshold bwThreshold) {
        this.bwOutThreshold = bwThreshold;
    }

    public UtilizationThreshold getCpuThreshold() {
        return cpuThreshold;
    }

    public void setCpuThreshold(UtilizationThreshold cpuThreshold) {
        this.cpuThreshold = cpuThreshold;
    }

    public UtilizationThreshold getMemThreshold() {
        return memThreshold;
    }

    public void setMemThreshold(UtilizationThreshold memThreshold) {
        this.memThreshold = memThreshold;
    }
    private UtilizationThreshold cpuThreshold;
    private UtilizationThreshold memThreshold;
    private UtilizationThreshold bwInThreshold;
    private UtilizationThreshold bwOutThreshold;

    Simulation(Observer observer, File training, File running) {
        super();
        this.observer = observer;
        this.running = running;
        this.training = training;
        initializeThresholds();
    }

    @Override
    public void run() {
        // TODO: how to safely stop and resume the thread?
        CloudProfiler.start(
                observer, 
                delay, 
                training.getAbsolutePath(), 
                running.getAbsolutePath(), 
                cpuThreshold, 
                memThreshold,
                bwInThreshold,
                bwOutThreshold);
    }
    
    public void stopped(Boolean stopped) {
        // TODO: stopping not implemented yet
        this.stopped = stopped;
    }
    
    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
    
    public File getRunning() {
        return running;
    }

    public void setRunning(File running) {
        this.running = running;
    }

    public File getTraining() {
        return training;
    }

    public void setTraining(File training) {
        this.training = training;
    }

    private void initializeThresholds() {
        cpuThreshold = new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_CPU, SimulationDefaults.MIN_THRESHOLD_CPU);
        memThreshold = new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_MEM, SimulationDefaults.MIN_THRESHOLD_MEM);
        bwInThreshold = new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_BW, SimulationDefaults.MIN_THRESHOLD_BW);
        bwOutThreshold = new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_BW, SimulationDefaults.MIN_THRESHOLD_BW);
    }
}
