package de.hpi_web.cloudSim.profiling.datacenter;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

public class ProfilingCloudlet extends Cloudlet {
	
	private UtilizationModel utilizationModelDiskRead;
	private UtilizationModel utilizationModelDiskWrite;
	private UtilizationModel utilizationModelBwOut;

	public ProfilingCloudlet(int cloudletId, long cloudletLength,
			int pesNumber, long cloudletFileSize, long cloudletOutputSize,
			UtilizationModel utilizationModelCpu,
			UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelDiskRead,
			UtilizationModel utilizationModelDiskWrite,
			UtilizationModel utilizationModelBw,
			UtilizationModel utilizationModelBwOut
) {
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize,
				cloudletOutputSize, utilizationModelCpu, utilizationModelRam,
				utilizationModelBw);
		
		this.utilizationModelDiskRead = utilizationModelDiskRead;
		this.utilizationModelDiskWrite = utilizationModelDiskWrite;
		this.utilizationModelBwOut = utilizationModelBwOut;
		
	}
	
	public UtilizationModel getUtilizationModelDiskRead() {
		return utilizationModelDiskRead;
	}

	public void setUtilizationModelDiskRead(
			UtilizationModel utilizationModelDiskRead) {
		this.utilizationModelDiskRead = utilizationModelDiskRead;
	}

	public UtilizationModel getUtilizationModelDiskWrite() {
		return utilizationModelDiskWrite;
	}

	public void setUtilizationModelDiskWrite(
			UtilizationModel utilizationModelDiskWrite) {
		this.utilizationModelDiskWrite = utilizationModelDiskWrite;
	}

	public UtilizationModel getUtilizationModelBwOut() {
		return utilizationModelBwOut;
	}

	public void setUtilizationModelBwOut(UtilizationModel utilizationModelBwOut) {
		this.utilizationModelBwOut = utilizationModelBwOut;
	}
	
	public double getUtilizationOfBwOut(final double time) {
		return getUtilizationModelBwOut().getUtilization(time);
	}
	
	public double getUtilizationOfDiskRead(final double time) {
		return getUtilizationModelDiskRead().getUtilization(time);
	}
	
	public double getUtilizationOfDiskWrite(final double time) {
		return getUtilizationModelDiskWrite().getUtilization(time);
	}

}
