package arx;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 *
 * @author wesam.dawoud
 */
public class ARX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //The location of the training file
        CSVFileReader training = new CSVFileReader("training.csv");
        training.ReadFile();
        //The location of the validation file
        CSVFileReader running = new CSVFileReader("running.csv");
        running.ReadFile();
        
        
        //We ignore first column in the csv file which shows the sampling time
        Matrix Ms = new Matrix(training.getRowsNum(),training.getColsNum()-1);
        for(int row=0;row<training.getRowsNum();row++)
            for(int col=0;col<training.getColsNum()-1;col++)
                Ms.set(row, col, training.getValue(row, col+1));
        
        Matrix Mv = new Matrix(running.getRowsNum(),running.getColsNum()-1);
        for(int row=0; row < running.getRowsNum(); row++)
            for(int col=0; col< running.getColsNum()-1; col++)
                Mv.set(row, col, running.getValue(row, col+1));
        
        //Select the metric that is going to be modeled
        //modeledMetricIndex determines its index in training file, at this example we use CPU utilization!
        //Other metrics will be added later
        //18-load balancer, 19-web server, 20-app server, 21-db server, and 22-95th response time
        int modeledMetricIndex = 19;
        int modeledOut[] = {modeledMetricIndex};
        
        //Determine part of the training file to be considered
        //We ignore samples afterwor 120 because the system shows a saturation
        int numberOfSamples = 120;
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
        
        //Prepare some samples for validation
        //In your simulator you should expect a continues stream of these samples. It is the only input to the simulator!
        //These samples will be used to describe the CPU utilization of each VM instance in multi-tier system
        int samplingOut[] = {modeledMetricIndex};
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
        System.out.println("System parameters: ");
        x.print(2, 4);

        //Calculate predicted yk (see equation 8 at the paper)
        //Each row of Rv is considered as phi_k at equation 14 
        Matrix y_predicted = Rv.times(x);
        System.out.println("Predicted cpu utilization: ");
        y_predicted.print(2, 4);
        
        //Calculate the errors (i.e., the difference between the modeled and the measured values)
        //Matrix Residual = Rv.times(x).minus(yv);
        //Residual.print(2, 4);
        //double rnorm = Residual.normInf();
        //System.out.println("Normalized residulas: "+rnorm);
        
    }
    
    public static List<List<Double>> predictCPUUsage(String trainFile, String runFile) {
        List<List<Double>> output = new ArrayList<List<Double>>();
    	Matrix Ms = buildMatrix(trainFile);
    	Matrix Mv = buildMatrix(runFile);
        
        //Select the metric that is going to be modeled
        //modeledMetricIndex determines its index in training file, at this example we use CPU utilization!
        //Other metrics will be added later
        //18-load balancer, 19-web server, 20-app server, 21-db server, and 22-95th response time
        //int modeledMetricIndex = 19;
        int modeledMetricIndices[] = {18, 19, 20, 21};
	    for (int modeledMetrixIndex : modeledMetricIndices) {
	        int modeledOut[] = {modeledMetrixIndex};
	        
	        //Determine part of the training file to be considered
	        //We ignore samples afterwor 120 because the system shows a saturation
	        int numberOfSamples = 120;
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
	        
	        //Prepare some samples for validation
	        //In your simulator you should expect a continues stream of these samples. It is the only input to the simulator!
	        //These samples will be used to describe the CPU utilization of each VM instance in multi-tier system
	        int samplingOut[] = {modeledMetrixIndex};
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
