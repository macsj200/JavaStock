package pricequery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PriceQueryGui extends JFrame {
	private JButton getInfo = null;
	private JPanel megaPanel = null;
	private JTextField symbolInput = null;
	private JScrollPane scrollArea = null;
	private JTextArea outputArea = null;
	private final String[] formats = {"n", "p", "c1"};

	PriceQueryGui(){
		setLayout(new BorderLayout());

		getInfo = new JButton("Go");
		symbolInput = new JTextField(10);
		outputArea = new JTextArea(20,20);
		scrollArea = new JScrollPane(outputArea);

		getInfo.addActionListener(new InputListener());
		symbolInput.addActionListener(new InputListener());
		
		outputArea.setEditable(false);

		megaPanel = new JPanel();
		megaPanel.setLayout(new FlowLayout());
		
		megaPanel.add(getInfo);
		megaPanel.add(symbolInput);
		megaPanel.add(scrollArea);
		
		getContentPane().add(megaPanel);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	class InputListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){
			parseInput(symbolInput.getText());
		}
	}

	public void parseInput(String input){
		String[] symbols = null;
		List<String[]> res;

		long time = 0;
		
		Color randomColor = null;

		symbols = input.split(" |,");

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
		
		randomColor = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
		megaPanel.setBackground(randomColor);
	}
}
