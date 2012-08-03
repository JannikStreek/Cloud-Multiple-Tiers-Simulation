/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hpi_web.cloudSim.monitor;

import de.hpi_web.cloudSim.model.StringConstants;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;

/**
 *
 * @author christoph
 */
public class Chart extends Chart2D {
    Map<String, List<Double>> valuesMap;
    Map<String, ITrace2D> tracesMap;
    String selectedTier;
    
    Chart() {
        valuesMap = new HashMap<>();
        valuesMap.put(StringConstants.Tier.WEB, new ArrayList<Double>());
        valuesMap.put(StringConstants.Tier.APP, new ArrayList<Double>());
        valuesMap.put(StringConstants.Tier.DB, new ArrayList<Double>());
        
        tracesMap = new HashMap<>();
        tracesMap.put(StringConstants.Tier.WEB, newTrace(ChartPanel.TRACE_SIZE, Color.red));
        tracesMap.put(StringConstants.Tier.APP, newTrace(ChartPanel.TRACE_SIZE, Color.green));
        tracesMap.put(StringConstants.Tier.DB, newTrace(ChartPanel.TRACE_SIZE, Color.blue));
        
        for (String tier : tracesMap.keySet()) {
            ITrace2D trace = tracesMap.get(tier);
            trace.setVisible(false);
            trace.setName(tier);
            this.addTrace(trace);
        }
    }
    
    void addValue(String tier, Double value) {
        valuesMap.get(tier).add(value);
        ITrace2D trace = tracesMap.get(tier);
        trace.addPoint(trace.getSize(), value);
    }
    
    void render(JComponent target, String tier) {
        //this.removeAllTraces();
        setVisibilityForTraces(false);
        if (tracesMap.keySet().contains(tier)) {
            renderTrace(tier, tracesMap.get(tier));
        } else {
            renderAllTraces();
        }
        target.add(this);
    }
    
    void renderAllTraces() {
        for (String key : tracesMap.keySet()) {
            renderTrace(key, tracesMap.get(key));
        }
    }
    
    void renderTrace(String tier, ITrace2D trace) {
        trace.setVisible(true);
    }

    private void setVisibilityForTraces(boolean visible) {
        for (ITrace2D trace : tracesMap.values())
            trace.setVisible(visible);
    }

    private ITrace2D newTrace(int size, Color color) {
        ITrace2D trace = new Trace2DLtd(size);
        trace.setColor(color);
        return trace;
    }
    
    
}
