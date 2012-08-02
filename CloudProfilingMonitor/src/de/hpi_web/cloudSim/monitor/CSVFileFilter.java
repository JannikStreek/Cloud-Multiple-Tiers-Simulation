/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hpi_web.cloudSim.monitor;

import java.io.File;

/**
 *
 * @author christoph
 */
public class CSVFileFilter extends javax.swing.filechooser.FileFilter {
    @Override
    public boolean accept(File file) {
        return file.isDirectory() || file.getAbsolutePath().endsWith(".csv");
    }
    @Override
    public String getDescription() {
        return "CSV (*.csv)";
    }
}
