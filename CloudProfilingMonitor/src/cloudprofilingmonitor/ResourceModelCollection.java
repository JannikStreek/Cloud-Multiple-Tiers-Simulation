/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudprofilingmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author christoph
 */
public class ResourceModelCollection {
    Map<String, ResourceModel> modelMap;
    
    ResourceModelCollection() {
        modelMap = new HashMap<>();
        modelMap.put(ChartPanel.CPU, new ResourceModel());
        modelMap.put(ChartPanel.MEMORY, new ResourceModel());
        modelMap.put(ChartPanel.BANDWIDTH_IN, new ResourceModel());
        modelMap.put(ChartPanel.BANDWIDTH_OUT, new ResourceModel());
        modelMap.put(ChartPanel.HD_READ, new ResourceModel());
        modelMap.put(ChartPanel.HD_WRITE, new ResourceModel());
    }
    
    public void put(String tier, ResourceModel model) {
        modelMap.put(tier, model);
    }
    
    public ResourceModel get(String tier) {
        return modelMap.get(tier);
    }
}
