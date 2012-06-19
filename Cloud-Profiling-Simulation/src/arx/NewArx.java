package arx;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 *
 * @author wesam.dawoud
 */
public class NewArx {
	
	private static Matrix Ms;
	private static Matrix Mv;

	public static void init(String trainFile, String runFile) {
    	Ms = buildMatrix(trainFile);
    	Mv = buildMatrix(runFile);
	}
	
	public static List<List<Double>> predictCPUUsage() {
        int modeledMetricIndices[] = {21, 28, 35, 42};
        int modeledMetricIndicesRunning[] = {18, 19, 20, 21};
		return getUsage(0.01,modeledMetricIndices,modeledMetricIndicesRunning);
	}
	
	public static List<List<Double>> predictMemoryUsage() {
        int modeledMetricIndices[] = {22, 29, 36, 43};
        int modeledMetricIndicesRunning[] = {18, 19, 20, 21};
		return getUsage(1,modeledMetricIndices,modeledMetricIndicesRunning);
	}
	
	public static List<List<Double>> predictDiskReadUsage() {
        int modeledMetricIndices[] = {23, 30, 37, 44};
        int modeledMetricIndicesRunning[] = {18, 19, 20, 21};
		return getUsage(1,modeledMetricIndices,modeledMetricIndicesRunning);
	}
	
	public static List<List<Double>> predictDiskWriteUsage() {
        int modeledMetricIndices[] = {24, 31, 38, 45};
        int modeledMetricIndicesRunning[] = {18, 19, 20, 21};
		return getUsage(1,modeledMetricIndices,modeledMetricIndicesRunning);
	}
	
	public static List<List<Double>> predictNetworkIncomingUsage() {
        int modeledMetricIndices[] = {25, 32, 39, 46};
        int modeledMetricIndicesRunning[] = {18, 19, 20, 21};
		return getUsage(1,modeledMetricIndices,modeledMetricIndicesRunning);
	}
	
	public static List<List<Double>> predictNetworkOutgoingUsage() {
        int modeledMetricIndices[] = {26, 33, 40, 47};
        int modeledMetricIndicesRunning[] = {18, 19, 20, 21};
		return getUsage(1,modeledMetricIndices,modeledMetricIndicesRunning);
	}
	

    private static List<List<Double>> getUsage(double factor,int modeledMetricIndices[],int modeledMetricIndicesRunning[]) {
        List<List<Double>> output = new ArrayList<List<Double>>();

        
        //Select the metric that is going to be modeled
        //modeledMetricIndex determines its index in training file, at this example we use CPU utilization!
    	
    	// cpu, mem, disc read, disc write, network received, network transmitted
    	// 22 = load balancer
    	// 29 = webserver
    	// 36 = app
    	// 43 = db

        int n = 0;
	    for (int modeledMetrixIndex : modeledMetricIndices) {
	        int modeledOut[] = {modeledMetrixIndex};

	        //Determine part of the training file to be considered
	        //We ignore samples afterwor 100 because the system shows a saturation
	        int numberOfSamples = 100;
	        Matrix ys = Ms.getMatrix(0,numberOfSamples,modeledOut);
	        ys = ys.times(factor);
	        
	        //List the inputs you are going to consider for modeling
	        //Note: We have 18 type of requets in our case
	        int modeledInput[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
	        Matrix us = Ms.getMatrix(0, numberOfSamples, modeledInput);
	        
	        //Generate the matrix that will be solved
	        Matrix R = new Matrix(ys.getRowDimension(),us.getColumnDimension()+1);
	        for(int i=0;i<R.getRowDimension()-1;i++) {
	            R.set(i+1, 0, ys.get(i, 0));
	        }
	        
	        R.setMatrix(0, us.getRowDimension()-1, 1, us.getColumnDimension(), us);
	        
	        //Prepare some samples for validation
	        //In your simulator you should expect a continues stream of these samples. It is the only input to the simulator!
	        //These samples will be used to describe the CPU utilization of each VM instance in multi-tier system
	        int samplingOut[] = {modeledMetricIndicesRunning[n]};
	        int numberOfValidationSamples = 370;
	        Matrix yv = Mv.getMatrix(0, numberOfValidationSamples, samplingOut);
	        
	        int validationInput[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
	        Matrix uv = Mv.getMatrix(0, numberOfValidationSamples, validationInput);
	        
	    
	        Matrix Rv = new Matrix(yv.getRowDimension(),uv.getColumnDimension()+1);
	        for(int i=0;i<Rv.getRowDimension()-1;i++)
	            Rv.set(i+1, 0, yv.get(i, 0));
	        
	        
	        //you can scale the samples rate according to number of replicas
	        //For instance if we have two replicas (e.g., two web servers) then replace uv by uv.times(0.5)
	        Rv.setMatrix(0, uv.getRowDimension()-1, 1, uv.getColumnDimension(), uv);
	
	        
	        //The following simple example solves a 3x3 linear system Ax=b and computes the norm of the residual. 
	        //double[][] array = {{1.,2.,3.},{4.,5.,6.},{7.,8.,10.}};
	        //Matrix A = new Matrix(array);
	        //Matrix b = Matrix.random(3,1);
	        //Matrix x = A.solve(b);
	        //Matrix Residual = A.times(x).minus(b);
	        //double rnorm = Residual.normInf();
	        //x array is theta at equation 13
	        Matrix x = R.solve(ys);
	        //System.out.println("System parameters: ");
	        //x.print(2, 4);
	
	        //Calculate predicted yk (see equation 8 at the paper)
	        //Each row of Rv is considered as phi_k at equation 14 
	        Matrix y_predicted = Rv.times(x);
	        //System.out.println("Predicted cpu utilization: ");
	        //y_predicted.print(2, 4);
	        double[][] array = y_predicted.getArray();
	        List<Double> cpuUtil = new ArrayList<Double>();
	        for (double[] element : array)
	        	cpuUtil.add(element[0]);

        	output.add(cpuUtil);
	        //Calculate the errors (i.e., the difference between the modeled and the measured values)
	        //Matrix Residual = Rv.times(x).minus(yv);
	        //Residual.print(2, 4);
	        //double rnorm = Residual.normInf();
	        //System.out.println("Normalized residulas: "+rnorm);
        	n++;
	    }
        return output;
    }

	private static Matrix buildMatrix(String file) {
        CSVFileReader reader = new CSVFileReader(file);
        reader.ReadFile();
        //We ignore first column in the csv file which shows the sampling time
        Matrix M = new Matrix(reader.getRowsNum(),reader.getColsNum()-1);
        for(int row=0;row<reader.getRowsNum();row++)
            for(int col=0;col<reader.getColsNum()-1;col++)
                M.set(row, col, reader.getValue(row, col+1));
        return M;
	}
}
