package pricequery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PriceQueryGui extends JFrame {
	private JButton getInfo = null;
	private Box megaBox = null;
	private StripedPanel backgroundPanel = null;
	private JTextField symbolInput = null;
	private JScrollPane scrollArea = null;
	private Box inputBox = null;
	private JTextArea outputArea = null;
	private final String[] formats = {"n", "p", "c1"};

	PriceQueryGui(){
		setLayout(new BorderLayout());

		getInfo = new JButton("Go");

		symbolInput = new JTextField("Symbol(s)");
		symbolInput.addFocusListener(new TextFieldFocusListener());
		symbolInput.setForeground(Color.gray);

		outputArea = new JTextArea(20,20);
		scrollArea = new JScrollPane(outputArea);
		inputBox = new Box(BoxLayout.X_AXIS);

		getInfo.addActionListener(new InputListener());
		symbolInput.addActionListener(new InputListener());

		outputArea.setEditable(false);

		backgroundPanel = new StripedPanel(StripedPanel.X_AXIS);

		megaBox = new Box(BoxLayout.Y_AXIS);

		inputBox.add(getInfo);
		inputBox.add(symbolInput);

		megaBox.add(inputBox);
		megaBox.add(scrollArea);

		backgroundPanel.add(megaBox);

		getContentPane().add(backgroundPanel);

		setMinimumSize(new Dimension(275,400));

		//setResizable(false);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		symbolInput.requestFocus();
	}

	class InputListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			backgroundPanel.genBackground();
			backgroundPanel.repaint();
			if(symbolInput.getText().equals("Symbol(s)")){
				return;
			}
			parseInput(symbolInput.getText());
			symbolInput.requestFocus();
		}
	}

	class TextFieldFocusListener implements FocusListener{
		@Override
		public void focusGained(FocusEvent arg0) {
			if(symbolInput.getText().equals("Symbol(s)")){
				symbolInput.setText("");
				symbolInput.setForeground(Color.black);
			}
		}
		@Override
		public void focusLost(FocusEvent arg0) {
			if(symbolInput.getText().length() == 0){
				symbolInput.setText("Symbol(s)");
				symbolInput.setForeground(Color.gray);
			}
		}
	}
	public void parseInput(String input){
		String[] symbols = null;
		List<String[]> res;

		long time = 0;

		symbols = input.split(",");
		

		try {
			time = System.currentTimeMillis();
			res = (new PriceQueryer(symbols, formats).getCsvResults());
			time = System.currentTimeMillis() - time;
			System.out.println("Query time: " + time);
			for(int i = 0; i < res.size(); i++){
				for(int j = 1; j < res.get(i).length; j++){
					outputArea.append(res.get(i)[0] + String.format(" (%s)", formats[j]) + ": " + res.get(i)[j] + "\r\n");
					outputArea.setCaretPosition(outputArea.getDocument().getLength());
				}
			}
		} catch (IOException e) {
			System.out.println("Somethin's up with your internet!");
		}
	}
}
