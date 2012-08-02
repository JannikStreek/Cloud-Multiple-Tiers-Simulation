/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hpi_web.cloudSim.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author christoph
 */
public class ResourceModelCollection {
    Map<String, ResourceModel> modelMap;
    
    public ResourceModelCollection() {
        modelMap = new HashMap<>();
        modelMap.put(StringConstants.Resource.CPU, new ResourceModel());
        modelMap.put(StringConstants.Resource.MEMORY, new ResourceModel());
        modelMap.put(StringConstants.Resource.BANDWIDTH_IN, new ResourceModel());
        modelMap.put(StringConstants.Resource.BANDWIDTH_OUT, new ResourceModel());
        modelMap.put(StringConstants.Resource.HD_READ, new ResourceModel());
        modelMap.put(StringConstants.Resource.HD_WRITE, new ResourceModel());
    }
    
    public void put(String tier, ResourceModel model) {
        modelMap.put(tier, model);
    }
    
    public ResourceModel get(String tier) {
        return modelMap.get(tier);
    }
}
