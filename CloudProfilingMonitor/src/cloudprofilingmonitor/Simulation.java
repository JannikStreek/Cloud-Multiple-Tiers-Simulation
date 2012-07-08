/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudprofilingmonitor;

import de.hpi_web.cloudSim.profiling.builders.DatacenterBuilder;
import de.hpi_web.cloudSim.profiling.builders.VmBuilder;
import de.hpi_web.cloudSim.profiling.example.NewCloudProfiler;
import de.hpi_web.cloudSim.profiling.observer.Observer;
import de.hpi_web.cloudSim.profiling.utilization.UtilizationThreshold;
import java.io.File;
import java.util.Map;

/**
 *
 * @author christoph
 */
public class Simulation extends Thread {
    private Boolean stopped = false;
    private Observer observer;
    private File running, training;
    private double delay = SimulationDefaults.STEP_DELAY;
    private UtilizationThreshold cpuThreshold;
    private UtilizationThreshold memThreshold;
    private UtilizationThreshold bwInThreshold;
    private UtilizationThreshold bwOutThreshold;
    private UtilizationThreshold hdReadThreshold;
    private UtilizationThreshold hdWriteThreshold;
    private DatacenterBuilder dcBuilder;
    private VmBuilder vmBuilder;
    private Map<String, ResourceModelCollection> models;

    
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
        NewCloudProfiler.start(
                observer, 
                delay, 
                training.getAbsolutePath(), 
                running.getAbsolutePath(), 
                cpuThreshold, 
                memThreshold,
                bwInThreshold,
                bwOutThreshold,
		hdReadThreshold,
		hdWriteThreshold,
		dcBuilder,
		vmBuilder);
//        CloudProfiler.start(
//                observer, 
//                delay, 
//                training.getAbsolutePath(), 
//                running.getAbsolutePath(), 
//                cpuThreshold, 
//                memThreshold,
//                bwInThreshold,
//                bwOutThreshold);
    }
    
    public void stopped(Boolean stopped) {
        // TODO: stopping not implemented yet
        this.stopped = stopped;
    }
    
    public void setModels(Map<String, ResourceModelCollection> models) {
        this.models = models;
    }
    
    public Map<String, ResourceModelCollection> getModels() {
        return models;
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

    
    public DatacenterBuilder getDcBuilder() {
        return dcBuilder;
    }

    public void setDcBuilder(DatacenterBuilder dcBuilder) {
        this.dcBuilder = dcBuilder;
    }

    public UtilizationThreshold getHdReadThreshold() {
        return hdReadThreshold;
    }

    public void setHdReadThreshold(UtilizationThreshold hdReadThreshold) {
        this.hdReadThreshold = hdReadThreshold;
    }

    public UtilizationThreshold getHdWriteThreshold() {
        return hdWriteThreshold;
    }

    public void setHdWriteThreshold(UtilizationThreshold hdWriteThreshold) {
        this.hdWriteThreshold = hdWriteThreshold;
    }

    public VmBuilder getVmBuilder() {
        return vmBuilder;
    }

    public void setVmBuilder(VmBuilder vmBuilder) {
        this.vmBuilder = vmBuilder;
    }
    private void initializeThresholds() {
        cpuThreshold =      new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_CPU, SimulationDefaults.MIN_THRESHOLD_CPU);
        memThreshold =      new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_MEM, SimulationDefaults.MIN_THRESHOLD_MEM);
        bwInThreshold =     new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_BW, SimulationDefaults.MIN_THRESHOLD_BW);
        bwOutThreshold =    new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_BW, SimulationDefaults.MIN_THRESHOLD_BW);
        hdReadThreshold =   new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_HDD_READ, SimulationDefaults.MIN_THRESHOLD_HDD_READ);
        hdWriteThreshold =  new UtilizationThreshold(SimulationDefaults.MAX_THRESHOLD_HDD_WRITE, SimulationDefaults.MIN_THRESHOLD_HDD_WRITE);
    }
}
