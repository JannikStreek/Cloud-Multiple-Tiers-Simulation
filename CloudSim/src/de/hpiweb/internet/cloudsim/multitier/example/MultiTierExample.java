package de.hpiweb.internet.cloudsim.multitier.example;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

public class MultiTierExample {
	
	static List<Vm> vmlist;
	
    /**
     * Creates main() to run this example.
     *
     * @param args the args
     */
    public static void main(String[] args) {

            Log.printLine("Starting CloudSimExample1...");

            try {
                    // First step: Initialize the CloudSim package. It should be called
                    // before creating any entities.
                    int num_user = 1; // number of cloud users
                    Calendar calendar = Calendar.getInstance();
                    boolean trace_flag = false; // mean trace events

                    // Initialize the CloudSim library
                    CloudSim.init(num_user, calendar, trace_flag);

                    // Second step: Create Datacenters
                    // Datacenters are the resource providers in CloudSim. We need at
                    // list one of them to run a CloudSim simulation
                    Datacenter datacenter0 = createDatacenter("Datacenter_0");

                    // Third step: Create Broker
                    DatacenterBroker broker = createBroker();
                    int brokerId = broker.getId();

                    // Fourth step: Create one virtual machine
                    vmlist = new ArrayList<Vm>();

                    // VM description
                    int vmid = 0;
                    int mips = 1000;
                    long size = 10000; // image size (MB)
                    int ram = 512; // vm memory (MB)
                    long bw = 1000;
                    int pesNumber = 1; // number of cpus
                    String vmm = "Xen"; // VMM name

                    // create VM
                    Vm vm = new Vm(vmid, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());

                    // add the VM to the vmList
                    vmlist.add(vm);

                    // submit vm list to the broker
                    broker.submitVmList(vmlist);

                    // Fifth step: Create one Cloudlet
                    cloudletList = new ArrayList<Cloudlet>();

                    // Cloudlet properties
                    int id = 0;
                    long length = 400000;
                    long fileSize = 300;
                    long outputSize = 300;
                    UtilizationModel utilizationModel = new UtilizationModelFull();

                    Cloudlet cloudlet = new Cloudlet(id, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
                    cloudlet.setUserId(brokerId);
                    cloudlet.setVmId(vmid);

                    // add the cloudlet to the list
                    cloudletList.add(cloudlet);

                    // submit cloudlet list to the broker
                    broker.submitCloudletList(cloudletList);

                    // Sixth step: Starts the simulation
                    CloudSim.startSimulation();

                    CloudSim.stopSimulation();

                    //Final step: Print results when simulation is over
                    List<Cloudlet> newList = broker.getCloudletReceivedList();
                    printCloudletList(newList);

                    // Print the debt of each user to each datacenter
                    datacenter0.printDebts();

                    Log.printLine("CloudSimExample1 finished!");
            } catch (Exception e) {
                    e.printStackTrace();
                    Log.printLine("Unwanted errors happen");
            }
    }

}
