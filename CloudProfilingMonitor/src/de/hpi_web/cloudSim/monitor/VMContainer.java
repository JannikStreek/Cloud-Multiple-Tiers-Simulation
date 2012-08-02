/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hpi_web.cloudSim.monitor;

import de.hpi_web.cloudSim.profiling.utilization.UtilizationThreshold;
import java.awt.Color;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author christoph
 */
public class VMContainer extends JPanel {
    private JLabel cpuUtilText, cpuUtilValue;
    private JLabel memUtilText, memUtilValue;
    private JLabel bwInUtilText, bwInUtilValue;
    private JLabel bwOutUtilText, bwOutUtilValue;
    private JLabel hdReadUtilText, hdReadUtilValue;
    private JLabel hdWriteUtilText, hdWriteUtilValue;
    
    private DecimalFormat df;
    
    
    VMContainer() {
        super();
        initializeLabels();
        initializeLayout();
        setSize(200, 150);
        setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        df = new DecimalFormat("#.##");
        df.setMinimumFractionDigits(2);
        df.setMinimumIntegerDigits(2);
    }
    
    public void setCpuUtil(Double value, UtilizationThreshold threshold) {
        Color fgColor = getColorForValue(value, threshold);
        cpuUtilValue.setText(df.format(value) + "%");
        cpuUtilValue.setForeground(fgColor);
    }
    public void setMemUtil(Double value, UtilizationThreshold threshold) {
        Color fgColor = getColorForValue(value, threshold);
        memUtilValue.setText(df.format(value) + "%");
        memUtilValue.setForeground(fgColor);
    }
//    public void setBwUtil(Double value, UtilizationThreshold threshold) {
//        Color fgColor = getColorForValue(value, threshold);
//        bwInUtilValue.setText(df.format(value) + "%");
//        bwInUtilValue.setForeground(fgColor);
//    }

    void setBwInUtil(Double value, UtilizationThreshold threshold) {
        Color fgColor = getColorForValue(value, threshold);
        bwInUtilValue.setText(df.format(value) + "%");
        bwInUtilValue.setForeground(fgColor);
    }

    void setBwOutUtil(Double value, UtilizationThreshold threshold) {
        Color fgColor = getColorForValue(value, threshold);
        bwOutUtilValue.setText(df.format(value) + "%");
        bwOutUtilValue.setForeground(fgColor);
    }

    void setHdReadUtil(Double value, UtilizationThreshold threshold) {
        Color fgColor = getColorForValue(value, threshold);
        hdReadUtilValue.setText(df.format(value) + "%");
        hdReadUtilValue.setForeground(fgColor);
    }

    void setHdWriteUtil(Double value, UtilizationThreshold threshold) {
        Color fgColor = getColorForValue(value, threshold);
        hdWriteUtilValue.setText(df.format(value) + "%");
        hdWriteUtilValue.setForeground(fgColor);
    }
    
    private Color getColorForValue(double value, UtilizationThreshold threshold) {
        if (!threshold.isValid())
            return Color.BLACK;
        double lower = threshold.getLower();
        double upper = threshold.getUpper();
        
        // blue = very close to or below lower threshold
        if (value <= lower*1.05)
            return Color.BLUE;
        // green = close to lower threshold
        if (value <= lower*1.20)
            return Color.GREEN;
        // black = normal
        if (value < upper*0.80)
            return Color.BLACK;
        // orange = close to upper threshold
        if (value < upper*0.95)
            return Color.ORANGE;
        // red = very close to or above upper threshold
        return Color.RED;
    }
    private void initializeLabels() {
        cpuUtilText = new javax.swing.JLabel();
        memUtilText = new javax.swing.JLabel();
        bwInUtilText = new javax.swing.JLabel();
        bwOutUtilText = new javax.swing.JLabel();
        hdReadUtilText = new javax.swing.JLabel();
        hdWriteUtilText = new javax.swing.JLabel();
        
        cpuUtilValue = new javax.swing.JLabel();
        memUtilValue = new javax.swing.JLabel();
        bwInUtilValue = new javax.swing.JLabel();
        bwOutUtilValue = new javax.swing.JLabel();
        hdReadUtilValue = new javax.swing.JLabel();
        hdWriteUtilValue = new javax.swing.JLabel();
        
        cpuUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        cpuUtilText.setText("CPU:");

        memUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        memUtilText.setText("RAM:");

        bwInUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        bwInUtilText.setText("BW I:");

        bwOutUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        bwOutUtilText.setText("BW O:");
        
        hdReadUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        hdReadUtilText.setText("HD R:");
        
        hdWriteUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        hdWriteUtilText.setText("HD W:");
        

        cpuUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        cpuUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cpuUtilValue.setText("0%");

        memUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        memUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        memUtilValue.setText("0%");

        bwInUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        bwInUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        bwInUtilValue.setText("0%");

        bwOutUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        bwOutUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        bwOutUtilValue.setText("0%");

        hdReadUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        hdReadUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        hdReadUtilValue.setText("0%");

        hdWriteUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        hdWriteUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        hdWriteUtilValue.setText("0%");
        
    }
    
    private void initializeLayout() {
        
        javax.swing.GroupLayout VMPanelLayout = new javax.swing.GroupLayout(this);
        setLayout(VMPanelLayout);
        VMPanelLayout.setHorizontalGroup(
            VMPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VMPanelLayout.createSequentialGroup()
                .addGroup(VMPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cpuUtilText)  //23
                    .addComponent(memUtilText)  //24
                    .addComponent(bwInUtilText)  //25
                    .addComponent(bwOutUtilText)  //25
                    .addComponent(hdReadUtilText)  //25
                    .addComponent(hdWriteUtilText))  //25
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                .addGroup(VMPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cpuUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)    //26
                    .addComponent(memUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)    //27
                    .addComponent(bwInUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)    //28
                    .addComponent(bwOutUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hdReadUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(hdWriteUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        VMPanelLayout.setVerticalGroup(
            VMPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VMPanelLayout.createSequentialGroup()
                .addComponent(cpuUtilText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memUtilText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bwInUtilText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bwOutUtilText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hdReadUtilText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hdWriteUtilText))
            .addGroup(VMPanelLayout.createSequentialGroup()
                .addComponent(cpuUtilValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memUtilValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bwInUtilValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bwOutUtilValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hdReadUtilValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hdWriteUtilValue))
        );
    }
}
