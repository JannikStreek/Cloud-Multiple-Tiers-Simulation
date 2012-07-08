/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudprofilingmonitor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author christoph
 */
public class ResourceModel {
    List<Double> values;
    Boolean isActive;
    
    ResourceModel() {
        super();
        this.values = new ArrayList<>();
        this.isActive = true;
    }
    
    ResourceModel(List<Double> values) {
        super();
        this.values = values;
        this.isActive = true;
    }
    
    public void setActive(Boolean active) {
        this.isActive = active;
    }
    
    public Boolean isActive() {
        return isActive;
    }
    public List<Double> toList() {
        return values;
    }
    
    public void setValuesFromString (String text) {
        values = fromString(text).toList();
    }
    
    @Override
    public String toString() {
        String s = new String();
        for (Double value : values) {
            s = s.concat(value.toString());
            s = s.concat(";");
        }
        return s;
    }
    
    public static ResourceModel fromString(String text) {
        String[] split = text.split(";");
        List<Double> modelValues = new ArrayList<>();
        for (String part : split)
            modelValues.add(Double.parseDouble(part));
        return new ResourceModel(modelValues);
    }
}
