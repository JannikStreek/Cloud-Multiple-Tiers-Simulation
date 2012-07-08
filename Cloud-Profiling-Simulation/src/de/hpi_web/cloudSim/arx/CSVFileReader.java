package de.hpi_web.cloudSim.arx;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

public class CSVFileReader {
    String fileName;
    String newline;
    ArrayList <Vector>storeValues = new ArrayList<Vector>();
        
public CSVFileReader(String FileName)
    {
            this.fileName=FileName;
    }

public void ReadFile()
        {
            try {
                    //storeValues.clear();//just in case this is the second call of the ReadFile Method./
                    BufferedReader br = new BufferedReader( new FileReader(fileName));
                    StringTokenizer st = null;
                    int lineNumber = 0;
                    
                    while( (newline = br.readLine()) != null)
                    {
                        lineNumber++;
                        //System.out.println(newline);
                        //storeValues.add(fileName);
                        //break comma separated line using ","
                        st = new StringTokenizer(newline, ",");
                        Vector currenline = new Vector();
                        
                        while(st.hasMoreTokens())
                            {
                                /*System.out.println("Line # " + lineNumber +
                                ", Token # " + tokenNumber
                                + ", Token : "+ st.nextToken());
                                 * */
                                currenline.add(st.nextToken());
                                 
                            }
                        storeValues.add(currenline);
                    }
                } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        }
        }
    //mutators and accesors
    public void setFileName(String newFileName)
        {
            this.fileName=newFileName;
        }
    public String getFileName()
        {
            return fileName;
        }
    public ArrayList getFileValues()
        {
            return this.storeValues;
        }
    public void displayArrayList()
        {
            for(int x=0;x<this.storeValues.size();x++)
                {
                    for(int y=0;y<this.storeValues.get(0).size();y++)
                    {
                        System.out.print(storeValues.get(x).get(y)+",");
                    }
                    System.out.println();
                }
        }
    public double getValue(int x, int y)
    {
        double return_value = 0.0;
        if(storeValues.size() > x && storeValues.get(0).size() > y)
        {
                return_value = Double.parseDouble((String)storeValues.get(x).get(y));
        }
        else
        {
            System.out.println("Out of range indexing");
        }
        return return_value;
    }
    
    public int getRowsNum()
    {
        return storeValues.size();
    }
    
    public int getColsNum()
    {
        return storeValues.get(0).size();
    }
}