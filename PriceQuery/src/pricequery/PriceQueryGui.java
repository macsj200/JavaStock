package pricequery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;

public class PriceQueryGui extends JFrame {
	private JButton getInfo = null;
	private Box megaBox = null;
	private StripedPanel backgroundPanel = null;
	private JTextField symbolInput = null;
	private Box inputBox = null;
	private Box savedStocksBox = null;
	private StockPanel[] savedStocksPanels = null;
	private TextOutputArea textOutput = null;
	private final String[] formats = {"n", "p", "c1"};
	private final String[] miniFormats = {"p"};
	private String[] savedStocksSymbols = {"aapl", "goog"};

	private List<String[]> res;
	private String userConfigFilePath = null;

	PriceQueryGui(){
		setLayout(new BorderLayout());
		CSVReaderBuilder<String[]> CSVreaderbuilder = null;
		List<String[]> temp = null;
		userConfigFilePath = System.getProperty("user.home").concat("/Desktop/config.csv");
		try {
			CSVreaderbuilder = (new CSVReaderBuilder<String[]>(new FileReader(userConfigFilePath))
					.strategy(CSVStrategy.UK_DEFAULT).entryParser(new DefaultCSVEntryParser()));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block

			try {

				BufferedWriter writer = new BufferedWriter(new FileWriter(userConfigFilePath));
				for (int x = 0; x < savedStocksSymbols.length; x++) {
					if (x == savedStocksSymbols.length-1){
						writer.write(savedStocksSymbols[x]);
					} else {
						writer.write(savedStocksSymbols[x].concat(","));

					}
				}
				writer.flush();
				CSVreaderbuilder = (new CSVReaderBuilder<String[]>(new FileReader(userConfigFilePath))
						.strategy(CSVStrategy.UK_DEFAULT).entryParser(new DefaultCSVEntryParser()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		CSVReader<String[]> csvParser = CSVreaderbuilder.build();
		try {
			temp = csvParser.readAll();
		} catch (IOException e1) {
			// TODO Auto-generated catch block

		}
		savedStocksSymbols = temp.get(0);
		System.out.println(temp.get(0)[1]);

		savedStocksBox = new Box(BoxLayout.X_AXIS);

		savedStocksPanels = new StockPanel[savedStocksSymbols.length];



		for(int i = 0; i < savedStocksSymbols.length; i++){
			savedStocksPanels[i] = new StockPanel(savedStocksSymbols[i]);

			try {
				writeResults((new PriceQueryer(savedStocksPanels[i].getSingletonSymbol(), 
						miniFormats).getStockHash()), savedStocksPanels[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			savedStocksBox.add(savedStocksPanels[i]);
		}


		getInfo = new JButton("Go");

		symbolInput = new JTextField("Symbol(s)");
		symbolInput.addFocusListener(new TextFieldFocusListener());
		symbolInput.setForeground(Color.gray);

		textOutput = new TextOutputArea(20,20);
		inputBox = new Box(BoxLayout.X_AXIS);

		getInfo.addActionListener(new InputListener());
		symbolInput.addActionListener(new InputListener());

		backgroundPanel = new StripedPanel(StripedPanel.X_AXIS);

		megaBox = new Box(BoxLayout.Y_AXIS);

		inputBox.add(getInfo);
		inputBox.add(symbolInput);

		megaBox.add(savedStocksBox);

		megaBox.add(inputBox);
		megaBox.add(textOutput);

		megaBox.setPreferredSize(new Dimension(300,400));

		backgroundPanel.add(megaBox);

		getContentPane().add(backgroundPanel);

		setMinimumSize(new Dimension(300,400));

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

	public String sanitize(String s){
		return s.replaceAll(" ", "");
	}

	public void parseInput(String input){
		String[] symbols = null;

		input = sanitize(input);

		symbols = input.split(",");

		try {
			writeResults((new PriceQueryer(symbols, formats).getStockHash()), textOutput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeResults(HashMap<String, HashMap<String,String>> stocksMap, GuiWritable writer) throws IOException{
		for(Entry<String, HashMap<String, String>> entry : stocksMap.entrySet()){
			String key = entry.getKey();
			HashMap<String, String> val = entry.getValue();
			for(Entry<String, String> secentry : val.entrySet()){
				writer.write(key + String.format(" (%s)", secentry.getKey()) + ": " + secentry.getValue() + "\r\n");
			}
		}
	}
}
