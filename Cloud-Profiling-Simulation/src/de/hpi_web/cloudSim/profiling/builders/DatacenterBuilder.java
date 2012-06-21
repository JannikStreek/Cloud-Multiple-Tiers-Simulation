package de.hpi_web.cloudSim.profiling.builders;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import de.hpi_web.cloudSim.profiling.datacenter.FixedDatacenter;

public class DatacenterBuilder {

	public String name;
	public DatacenterCharacteristics characteristics;
	public VmAllocationPolicy vmAllocationPolicy;
	public List<Storage> storageList;
	public double lastProcessTime;
	
	DatacenterBuilder(String name) {
		this.name = name;
	}
	
	public DatacenterBuilder setCharacteristics(DatacenterCharacteristics c) {
		this.characteristics = c;
		return this;
	}
	
	public FixedDatacenter build() throws Exception {
		return new FixedDatacenter(this);
	}

}
