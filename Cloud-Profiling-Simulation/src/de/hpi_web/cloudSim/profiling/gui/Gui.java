package de.hpi_web.cloudSim.profiling.gui;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.core.CloudSim;

import de.hpi_web.cloudSim.profiling.datacenter.ProfilingBroker;
import de.hpi_web.cloudSim.profiling.observer.Observable;
import de.hpi_web.cloudSim.profiling.observer.Observer;


public class Gui implements Observer {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JPanel panel;
	private JTextArea textArea_0;
	private JTextArea textArea_1;
	private JTextArea textArea_2;
	private StartAction start;
	private JTextField textField_2;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

	}

	/**
	 * Create the application.
	 */
	public Gui() {
		start = new StartAction(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 835, 555);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		
		JPanel panel_1 = new JPanel();
		
		JPanel panel_2 = new JPanel();
		
		JButton btnNewButton = new JButton("Set new values");
		btnNewButton.addActionListener(new SetValuesAction(this.start, this));
		
		textField = new JTextField();
		textField.setText("70");
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("30");
		textField_1.setColumns(10);
		
		JLabel lblCpuvmMaxTreshold = new JLabel("CPU/VM MAX threshold");
		
		JLabel lblCpuvmMixTreshold = new JLabel("CPU/VM MIN threshold");
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(this.start);
		
		JButton btnStop = new JButton("Stop");
		
		textField_2 = new JTextField();
		textField_2.setText("0.5");
		textField_2.setColumns(10);
		
		JLabel lblDelayAfterEach = new JLabel("Delay after each step [s]");
		
		JLabel lblTier = new JLabel("Tier 1");
		
		JLabel lblTier_1 = new JLabel("Tier 2");
		
		JLabel lblTier_2 = new JLabel("Tier 3");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(12)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblCpuvmMixTreshold)
								.addComponent(lblCpuvmMaxTreshold)
								.addComponent(lblDelayAfterEach))
							.addGap(4)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(btnNewButton))
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnStart)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(panel, GroupLayout.PREFERRED_SIZE, 269, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED, 12, Short.MAX_VALUE))
										.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
											.addComponent(lblTier)
											.addGap(14)))
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
											.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(6))
										.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
											.addComponent(lblTier_1)
											.addGap(14)))
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(lblTier_2)
											.addGap(12))
										.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(21))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(760, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblTier_2)
							.addComponent(lblTier_1)
							.addComponent(lblTier))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnStart)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnStop)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCpuvmMaxTreshold)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(12)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCpuvmMixTreshold)
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDelayAfterEach)
								.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(btnNewButton))
					.addContainerGap())
		);
		
		textArea_2 = new JTextArea();
		textArea_2.setRows(22);
		textArea_2.setEditable(false);
		textArea_2.setColumns(22);
		panel_2.add(textArea_2);
		
		textArea_1 = new JTextArea();
		textArea_1.setRows(22);
		textArea_1.setEditable(false);
		textArea_1.setColumns(22);
		panel_1.add(textArea_1);
		
		textArea_0 = new JTextArea();
		textArea_0.setColumns(22);
		textArea_0.setEditable(false);
		textArea_0.setRows(22);
		panel.add(textArea_0);
		frame.getContentPane().setLayout(groupLayout);
	}
	
	public JTextField getTextField_2() {
		return textField_2;
	}
	
	public JTextField getTextField_1() {
		return textField_1;
	}
	
	public JTextField getTextField() {
		return textField;
	}

	@Override
	public void refreshData(Observable subject) {
		ProfilingBroker broker = (ProfilingBroker) subject;
		// TODO: we need to decide which text area to use; this is just a fixed, quick and dirty solution
		JTextArea area = textArea_0;
		if (broker.getName() == "appBroker")
			area = textArea_1;
		if (broker.getName() == "dbBroker")
			area = textArea_2;
		
		String newText = "";
		
		for(Cloudlet cloudlet : broker.getCloudletSubmittedList()) {
			Double cpuUtil = new Double(cloudlet.getUtilizationModelCpu().getUtilization(CloudSim.clock()));
			Double memUtil = new Double(cloudlet.getUtilizationModelRam().getUtilization(CloudSim.clock()));
			int cpuShownUtil = cpuUtil.intValue();
			int memShownUtil = memUtil.intValue();
			int vmId = cloudlet.getVmId();
			int hostId = 0;
			
			hostId = broker.getVmForVmId(vmId).getHost().getId();
			
			newText += "VM: " + vmId + " at host " + hostId + "\r\n";
			newText += "-----------\r\n";
			newText += "CPU util at " + cpuShownUtil + "\r\n\r\n";
			newText += "Mem util at " + memShownUtil + "\r\n\r\n";
			
		}
		area.setText(newText);
		
		
	}
}
