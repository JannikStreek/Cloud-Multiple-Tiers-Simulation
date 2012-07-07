/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudprofilingmonitor;

import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import java.util.ArrayList;

/**
 *
 * @author christoph
 */
public class TracingValueList<Double> extends ArrayList<Double> {
    public final static int TRACE_SIZE = 2000;
    
    ITrace2D getTrace() {
        ITrace2D trace = new Trace2DLtd(TRACE_SIZE);
        int i = 0;
        for (Object value : this) {
            trace.addPoint(i, (double) value);
            i++;
        }
        return trace;
    }
}
