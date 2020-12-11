package com.app.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI implements ActionListener{
	
	private int count = 0;
	private JFrame frame;
	private JPanel panel;
	
	public GUI() {
		
		// Frame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Gestionale");
		frame.pack();
		frame.setSize(640, 170);
		
		// Panel
		panel = new JPanel();
		//panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(null); // new GridLayout(0, 1)
		
		// Intro Label
		JLabel introLabel = new JLabel("Cosa si desidera visualizzare o modificare?");
		introLabel.setBounds(0, 0, 640, 50);
		
		// Buttons
		JButton clientButton = new JButton("Clienti");
		clientButton.setBounds(0, 50, 213, 50);
		clientButton.addActionListener(this);
		
		JButton itemButton = new JButton("Componenti");
		itemButton.setBounds(214, 50, 213, 50);
		itemButton.addActionListener(this);
		
		JButton orderButton = new JButton("Ordini");
		orderButton.setBounds(428, 50, 213, 50);
		orderButton.addActionListener(this);
		
		// Notice Label
		JLabel noticeLabel = new JLabel("Software by Antignani-De Martino-Sbrighi. Released under the GNU GPL Licence v3.0.");
		noticeLabel.setBounds(0, 100, 640, 20);
		
		panel.add(introLabel);
		panel.add(clientButton);
		panel.add(itemButton);
		panel.add(orderButton);
		panel.add(noticeLabel);
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args) {
		// Il costruttore costruisce tutta la GUI
		new GUI();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO - Capisci quale dei tre bottoni è stato premuto e 
	}
	
}
