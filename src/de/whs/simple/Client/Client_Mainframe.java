package de.whs.simple.Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

public class Client_Mainframe extends JFrame implements KeyListener,ActionListener {
	private JTextField txtInput;
	private JTextArea txtrOutput;
	private JButton btnLogin;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Client_Mainframe frame = new Client_Mainframe();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public Client_Mainframe() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		getContentPane().setLayout(null);
		
		txtrOutput = new JTextArea();
		txtrOutput.setEditable(false);
		txtrOutput.setText("Output");
		txtrOutput.setBounds(15, 16, 548, 225);
		getContentPane().add(txtrOutput);
		
		txtInput = new JTextField();
		txtInput.setText("Input");
		txtInput.setBounds(15, 257, 548, 26);
		getContentPane().add(txtInput);
		txtInput.setColumns(10);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(15, 299, 115, 29);
		getContentPane().add(btnLogin);
		btnLogin.addActionListener(this);
		
		JButton btnSenden = new JButton("Senden");
		btnSenden.setBounds(448, 299, 115, 29);
		getContentPane().add(btnSenden);
		
		txtInput.addKeyListener(this);
		txtInput.requestFocus();
		
		
	}
	
	public void outline(String s){
		String zw = txtrOutput.getText();
		zw += s;
		txtrOutput.setText(zw);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == 10)
			try {
				
				Client.send(txtInput.getText());
			//	outline()
				txtInput.setText("");
				
			} catch (IOException e1) {
				
				outline("Fehler beim senden");
			}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnLogin){
			ProcessInput.login("/login test" + Math.random());
		}
		
	}
}
