Cloud-Multiple-Tiers-Simulation
===============================
This project extends / changes CloudSim, a cloud simulator tool for cloud environments, to simulate models for dynamic scalability. The models are build on real usage data of applications. For more information please have a look at the paper "Dynamic scalability and contention prediction at public infrastructure using Internet application profiling" by Wesam Dawoud, Ibrahim Takouna and Christoph Meinel.

Developed by Christoph Müller and Jannik Streek at Hasso Plattner Institute in Potsdam under supervision of Wesam Dawoud.

Getting started: Using the provided simulator as a jar
============
The most convenient way to get started, is to just use the provided jar. To do so, change to the folder where you checked out the project, and start the jar via: java -jar xy.jar
A window will appear. Now you can adjust your settings, put certain models inside the simulation etc.
Hit the start button, to finally start the simulation. Via the graph tab, you can get a live feedback of your provided models.

Installation with an IDE
============
After you have cloned the project via git, you can work with it in your editor of choice. We propose to use either eclipse or netbeans.
For this project we used netbeans for the GUI, so if you want to change the GUI, using netbeans would be a good idea.

Used java sdk: 1.7

Using the project in Eclipse
==========
From Eclipse choose New Project > New Java Project and select the folder where you cloned the project. Note that you have to uncheck the checkbox 'use default location'. Select both possible projects which you can add. The overall project consists of two projects, one responsible for the backend and one responsible for the gui.

Now you need to add the dependencies for the projects, because we don't use maven/other build tools. To do so, select the projects, go to properties > java build path and add all the dependencies form the dependencies folder you just cloned.
For the monitoring project it is also important to add the second project to the build path.

To start the simulation, just start the Gui.java in the monitoring project as a java application.

Using the project in Netbeans
==========


Fixed Bugs in CloudSim
============
During our work with cloudsim, we discovered at least these three bugs:

Datacenter: processCloudletResume => forgot to subtract actual time during scheduling
Datacenter: processCloudletResume + processCloudletPause => getVm(vmId, userId) wrong parameter order

We fixed these bugs in the cloudsim version which is provided in this repository.

