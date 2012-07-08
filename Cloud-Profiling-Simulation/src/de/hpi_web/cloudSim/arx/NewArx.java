package de.hpi_web.cloudSim.arx;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 *
 * @author wesam.dawoud
 */
public class NewArx {
	
	private static CSVFileReader training;
	private static CSVFileReader running;
	public static int RUNNING_VALUES = 370;

	public static void init(String trainFile, String runFile) {
        //The location of the training file
        training = new CSVFileReader(trainFile);
        training.ReadFile();
        //The location of the validation file
        running = new CSVFileReader(runFile);
        running.ReadFile();
	}
	
    //Select the metric that is going to be modeled
    //modeledMetricIndex determines its index in training file, at this example we use CPU utilization!
    //Other metrics will be added later
    //web cpu - 28
    //web mem - 29
    //web disk read - 30
    //web disk write - 31
    //web network in - 32
    //web network out - 33
    //app cpu - 35
    //app mem - 36
    //app disk read - 37
    //app disk write - 38
    //app network in - 39
    //app network out - 40
    //db cpu - 42
    //db mem - 43
    //db disk read - 44
    //db disk write - 45
    //db network in - 46
    //db network out - 47
	
	public static List<List<Double>> predictWebTierUtil() {
		int modeledMetricIndices[] = {28, 29, 30, 31, 32, 33};
		return getResultList(modeledMetricIndices);
	}
	
	public static List<List<Double>> predictAppTierUtil() {
		int modeledMetricIndices[] = {35,36,37,38,39,40};
		return getResultList(modeledMetricIndices);
	}
	
	public static List<List<Double>> predictDbTierUtil() {
		int modeledMetricIndices[] = {42, 43, 44, 45,46,47};
		return getResultList(modeledMetricIndices);
	}
	
	
	public static List<List<Double>> predictCPUUsage() {
		int modeledMetricIndices[] = {21, 28, 35, 42};
		return getResultList(modeledMetricIndices);
	}
	

	
	private static List<List<Double>> getResultList(int modeledMetricIndices[]) {
		List<List<Double>> resultList = new ArrayList<List<Double>>();
		for(int value : modeledMetricIndices) {
			System.out.println("starting");
			resultList.add(getUsage(value));
		}
		System.out.println(resultList);
		return resultList;
	}
	
	public static List<List<Double>> predictMemoryUsage() {
        int modeledMetricIndices[] = {22, 29, 36, 43};
        return getResultList(modeledMetricIndices);
	}
	
	public static List<List<Double>> predictDiskReadUsage() {
        int modeledMetricIndices[] = {23, 30, 37, 44};
        return getResultList(modeledMetricIndices);
	}
	
	public static List<List<Double>> predictDiskWriteUsage() {
        int modeledMetricIndices[] = {24, 31, 38, 45};
        return getResultList(modeledMetricIndices);
	}
	
	public static List<List<Double>> predictNetworkIncomingUsage() {
        int modeledMetricIndices[] = {25, 32, 39, 46};
        return getResultList(modeledMetricIndices);
	}
	
	public static List<List<Double>> predictNetworkOutgoingUsage() {
        int modeledMetricIndices[] = {26, 33, 40, 47};
        return getResultList(modeledMetricIndices);
	}


    private static List<Double> getUsage(int modeledMetricIndex) {
    	List<Double> result = new ArrayList<Double>();
    	//We ignore first column in the csv file which shows the sampling time
        Matrix Ms = new Matrix(training.getRowsNum(),training.getColsNum()-1);
        for(int row=0;row<training.getRowsNum();row++)
            for(int col=0;col<training.getColsNum()-1;col++)
                Ms.set(row, col, training.getValue(row, col+1));
        
        Matrix Mv = new Matrix(running.getRowsNum(),running.getColsNum()-1);
        for(int row=0; row < running.getRowsNum(); row++)
            for(int col=0; col< running.getColsNum()-1; col++)
                Mv.set(row, col, running.getValue(row, col+1));
        
        
        int modeledOut[] = {modeledMetricIndex};
        
        //Determine part of the training file to be considered
        //We ignore samples aftrwor 100 because the system shows a saturation
        int numberOfSamples = 100;
        Matrix ys = Ms.getMatrix(0,numberOfSamples,modeledOut);
        
        //List the inputs you are going to consider for modeling
        //Note: We have 18 type of requets in our case
        int modeledInput[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
        Matrix us = Ms.getMatrix(0, numberOfSamples, modeledInput);
        
        //Generate the matrix that will be solved
        Matrix R = new Matrix(ys.getRowDimension(),us.getColumnDimension()+1);
        for(int i=0;i<R.getRowDimension()-1;i++)
            R.set(i+1, 0, ys.get(i, 0));
        
        R.setMatrix(0, us.getRowDimension()-1, 1, us.getColumnDimension(), us);
        Matrix x;
        
        try {
        	x = R.solve(ys);
        }catch(RuntimeException e){
        	for(int i = 0; i < 370; i++) {
        		result.add(0.0);
        	}
        	return result;
        	
        }
        System.out.println("System parameters, (theta) at equation 5: ");
        x.print(2, 4);
        
        //Prepare some samples for validation
        //In your simulator you should expect a continues stream of these samples. It is the only input to the simulator!
        //These samples will be used to describe the CPU utilization of each VM instance in multi-tier system
        //int samplingOut[] = {modeledMetricIndex};
        //Matrix yv = Mv.getMatrix(0, numberOfValidationSamples, samplingOut);
        
        int validationInput[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
        Matrix uv = Mv.getMatrix(0, RUNNING_VALUES, validationInput);
        
        //Assume the last measured value of the modeled metric to be zero
        double yk_minus_1 = 0.0;
        Matrix input_row = new Matrix(1,validationInput.length+1);
        
        

        
        //System.out.println(input_row.getColumnDimension());
        //input_row.print(2, 4);
        
        
        for(int j=0; j<RUNNING_VALUES; j++)
        {
            for(int i=1; i<validationInput.length+1; i++)
                input_row.set(0, i, uv.get(j, i-1));
            
            input_row.set(0, 0, yk_minus_1*(-1.0));
                        
//            System.out.print("\u03D5"+"k(at equation 6):");
            input_row.print(2, 4);
            
            //It is equation 7 at paper!
            Matrix yk = input_row.times(x);
//            System.out.print("yk (Predicted y at equation 7):");
            yk.print(2, 4);
            
            yk_minus_1 = yk.get(0, 0);
            
            if(0.0 > yk_minus_1) {
            	yk_minus_1 = 0.0;
            }
            result.add(yk_minus_1);

        }
        System.out.println(result);
        return result;
    }
}
