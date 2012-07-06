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
import java.util.Map;
import java.util.Random;
import javax.swing.JPanel;

/**
 *
 * @author christoph
 */
public class Chart extends Chart2D {
    JPanel canvas;
    Map<Integer, ITrace2D> traces;
    
    Chart() {
        super();
        traces = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            ITrace2D trace = new Trace2DLtd(2000);      // when the limit is exceeded, real strange behavior happens, mb use other trace
            this.addTrace(trace);
            traces.put(i, trace);
        }
    }
    
    void render(int tier, JPanel canvas) {
        this.canvas = canvas;

        // Create a chart:  
//        Chart2D chart = new Chart2D();
//        // Create an ITrace: 
//        ITrace2D trace = new Trace2DLtd(200);        // max. of 200 values, ring buffer
//        trace.setColor(Color.RED);
//        // Add the trace to the chart. This has to be done before adding points (deadlock prevention): 
//        ITrace2D trace = traces.get(tier);
//        chart.addTrace(trace);
        canvas.add(this);
    }
    
    void addValue(int tier, double value) {
        ITrace2D trace = traces.get(tier);
        trace.addPoint(trace.getSize(), value);
    }
    
}
