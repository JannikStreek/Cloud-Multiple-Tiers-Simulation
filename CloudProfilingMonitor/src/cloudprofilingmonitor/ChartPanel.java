/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudprofilingmonitor;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author christoph
 */
public class ChartPanel {
    public final static String WEB_TIER = "Webserver";
    public final static String APP_TIER = "Application";
    public final static String DB_TIER = "Database";
    public final static String CPU = "CPU";
    public final static String MEMORY = "Memory";
    public final static String BANDWIDTH_IN = "Bandwidth I";
    public final static String BANDWIDTH_OUT = "Bandwidth O";
    public final static String HD_READ = "Disk Read";
    public final static String HD_WRITE = "Disk Write";
    public final static int TRACE_SIZE = 2000;
    private Chart cpuChart;
    private Chart memChart;
    private Chart bwInChart;
    private Chart bwOutChart;
    private Chart hdReadChart;
    private Chart hdWriteChart;
    
    private Chart selectedChart;
    private String selectedTier;
    private JPanel canvas;
    private Map<Integer, ITrace2D> traces;
    
    ChartPanel() {
        super();
        initializeCharts();
        traces = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            // when the limit is exceeded, real strange behavior happens
            // mb use other trace class?
            ITrace2D trace = new Trace2DLtd(TRACE_SIZE);
            selectedChart.addTrace(trace);
            traces.put(i, trace);
        }
    }
    
    private void initializeCharts() {
        cpuChart = new Chart();
        memChart = new Chart();
        bwInChart = new Chart();
        bwOutChart = new Chart();
        hdReadChart = new Chart();
        hdWriteChart = new Chart();
        selectedChart = cpuChart;
    }
    
    public void render(JPanel canvas) {
        this.canvas = canvas;
        render();
    }
    
    public void render() {
        canvas.removeAll();
        selectedChart.render(canvas, selectedTier);
    }
    
    public void selectTier(String tier) {
        selectedTier = tier;
    }
    public void selectCpuChart() {
        selectAndUpdate(cpuChart);
    }
    public void selectMemChart() {
        selectAndUpdate(memChart);
    }
    public void selectBwInChart() {
        selectAndUpdate(bwInChart);
    }
    public void selectBwOutChart() {
        selectAndUpdate(bwOutChart);
    }
    public void selectHdReadChart() {
        selectAndUpdate(hdReadChart);
    }
    public void selectHdWriteChart() {
        selectAndUpdate(hdWriteChart);
    }

    public void addCpuValue(String tier, Double cpuUtil) {
        cpuChart.addValue(tier, cpuUtil);
    }

    public void addMemValue(String tier, Double memUtil) {
        memChart.addValue(tier, memUtil);
    }

    public void addBwInValue(String tier, Double bwInUtil) {
        bwInChart.addValue(tier, bwInUtil);
    }

    public void addBwOutValue(String tier, Double bwOutUtil) {
        bwOutChart.addValue(tier, bwOutUtil);
    }

    public void addHdReadValue(String tier, Double hdReadUtil) {
        hdReadChart.addValue(tier, hdReadUtil);
    }

    public void addHdWriteValue(String tier, Double hdWriteUtil) {
        hdWriteChart.addValue(tier, hdWriteUtil);
    }

    private void selectAndUpdate(Chart chart) {
        if (selectedChart == chart)
            return;
        selectedChart = chart;
        render();
    }

    public void selectChart(String chartType) {
        switch (chartType) {
            case CPU:           selectCpuChart();       break;
            case MEMORY:        selectMemChart();       break;
            case BANDWIDTH_IN:  selectBwInChart();      break;
            case BANDWIDTH_OUT: selectCpuChart();       break;
            case HD_READ:       selectHdReadChart();    break;
            case HD_WRITE:      selectHdWriteChart();   break;
            default:            selectCpuChart();       break;
        }
    }
    
}