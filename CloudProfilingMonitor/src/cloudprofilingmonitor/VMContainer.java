/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudprofilingmonitor;

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
    private JLabel bwUtilText, bwUtilValue;
    
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
    
    public void setCpuUtil(Double value) {
        cpuUtilValue.setText(df.format(value) + "%");
    }
    public void setMemUtil(Double value) {
        memUtilValue.setText(value.toString() + "%");
    }
    public void setBwUtil(Double value) {
        bwUtilValue.setText(value.toString() + "%");
    }
    
    private void initializeLabels() {
        cpuUtilText = new javax.swing.JLabel();
        memUtilText = new javax.swing.JLabel();
        bwUtilText = new javax.swing.JLabel();
        cpuUtilValue = new javax.swing.JLabel();
        memUtilValue = new javax.swing.JLabel();
        bwUtilValue = new javax.swing.JLabel();
        
        cpuUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        cpuUtilText.setText("CPU:");

        memUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        memUtilText.setText("RAM:");

        bwUtilText.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        bwUtilText.setText("BW:");

        cpuUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        cpuUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cpuUtilValue.setText("0%");

        memUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        memUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        memUtilValue.setText("0%");

        bwUtilValue.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        bwUtilValue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        bwUtilValue.setText("0%");
        
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
                    .addComponent(bwUtilText))  //25
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
                .addGroup(VMPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cpuUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)    //26
                    .addComponent(memUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)    //27
                    .addComponent(bwUtilValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))    //28
                .addContainerGap())
        );
        VMPanelLayout.setVerticalGroup(
            VMPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(VMPanelLayout.createSequentialGroup()
                .addComponent(cpuUtilText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memUtilText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bwUtilText))
            .addGroup(VMPanelLayout.createSequentialGroup()
                .addComponent(cpuUtilValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(memUtilValue)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bwUtilValue))
        );
    }
}
