Cloud-Multiple-Tiers-Simulation
===============================
This project extends / changes CloudSim, a cloud simulator tool for cloud environments, to simulate models for dynamic scalability. The models are build on real usage data of applications. For more information please have a look at the paper "Dynamic scalability and contention prediction at public infrastructure using Internet application profiling" by Wesam Dawoud, Ibrahim Takouna and Christoph Meinel.

The project is released under GPL license.

Developed by Christoph Müller and Jannik Streek at Hasso Plattner Institute in Potsdam under supervision of Wesam Dawoud.

Getting started: Using the provided simulator as a jar
-------------
The most convenient way to get started, is to just use the provided jar in the main folder. To do so, change to the folder where you checked out the project, and start the jar via: java -jar CloudMultiSim.jar
A window will appear. Now you can adjust your settings, put certain models inside the simulation etc.
Hit the start button, to finally start the simulation. Via the graph tab, you can get a live feedback of your provided models.

Installation with an IDE
-------------
After you have cloned the project via git, you can work with it in your editor of choice. We propose to use either eclipse or netbeans.
For this project we used netbeans for the GUI, so if you want to change the GUI, using netbeans would be a good idea.

Used java sdk: 1.7

Using the project in Eclipse
-------------
From Eclipse choose Import Project > Select the Project inside the cloned folder.

To start the simulation, just start the Gui.java in the monitoring project as a java application.

Using the project in Netbeans
-------------
Netbeans is the tool of choice for editing the GUI. The CloudProfilingMonitor/ folder contains the project files for Netbeans. Simply chose File > Open Project from the Netbeans menu and select the folder. 
If you want to edit the rest of the code (which was managed using Eclipse) we suggest to use the import function for Eclipse projects built into Netbeans. Go to File > Import Project > Eclipse Project and follow the wizard.

The main class to start the simulator is Gui.java in the monitoring project.

Using the Model Tab
-------------
To add new Models, select a Tier, click 'Edit' on the resource you are about to edit and paste your model inside the field. Click 'submit' to save your entries. Some examples of models can be found below.

Example Models
-------------
CPU Web Tier: 0.0119;0.039;0.0668;0.0288;0.0133;0.0604;-0.029;0.0384;0.0625;0.0741;-0.0367;0.0826;0.0124;-0.0184;0.0366;0.0139;0.1013;0.0296;0.0431

Memory Web Tier: 0.893;70.7097;-100.5962;69.3366;-522.9268;1217.8132;-219.092;152.5894;243.5844;-1191.8981;76.0063;148.5534;-50.5976;995.324;521.8842;-549.1095;39.1384;-159.4872;119.2077

CPU App Tier: -0.0289;0.0337;0.046;0.029;0.0092;0.0284;0.0374;-0.0389;0.057;0.0033;0.0176;0.0476;0.0262;-0.0054;0.0314;0.0218;0.0339;0.0268;0.0271


Fixed Bugs in CloudSim
-------------
During our work with cloudsim, we discovered at least these three bugs:

Datacenter: processCloudletResume => forgot to subtract actual time during scheduling

Datacenter: processCloudletResume + processCloudletPause => getVm(vmId, userId) wrong parameter order

We fixed these bugs in the cloudsim version which is provided in this repository.

