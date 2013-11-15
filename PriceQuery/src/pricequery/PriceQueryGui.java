package pricequery;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
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
	private final String[] formats = { "n", "p", "c1" };
	private final String[] miniFormats = { "p" };
	private String[] savedStocksSymbols = { "aapl", "goog" };
	private File configFile = null;
	private FileReader configFileReader = null;
	private final FileWriter configFileWriter = null;
	private BufferedWriter configFileBufferedWriter = null;
	private CSVReader<String[]> csvParser = null;
	private CSVReaderBuilder<String[]> CSVreaderbuilder = null;
	private List<String[]> temp = null;
	private String userConfigFilePath = null;

	PriceQueryGui() {
		setLayout(new BorderLayout());
		userConfigFilePath = System.getProperty("user.home").concat(
				"/Desktop/config.csv");

		configFile = new File(userConfigFilePath);

		try {
			configFileReader = new FileReader(configFile);
			CSVreaderbuilder = new CSVReaderBuilder<String[]>(configFileReader)
					.strategy(CSVStrategy.UK_DEFAULT).entryParser(
							new DefaultCSVEntryParser());
		} catch (final FileNotFoundException e2) {
			try {
				configFileBufferedWriter = new BufferedWriter(new FileWriter(
						userConfigFilePath));
				for (int x = 0; x < savedStocksSymbols.length; x++) {
					if (x == savedStocksSymbols.length - 1) {
						configFileBufferedWriter.write(savedStocksSymbols[x]);
					} else {
						configFileBufferedWriter.write(savedStocksSymbols[x]
								.concat(","));

					}
				}
				configFileBufferedWriter.flush();
				CSVreaderbuilder = new CSVReaderBuilder<String[]>(
						new FileReader(userConfigFilePath)).strategy(
						CSVStrategy.UK_DEFAULT).entryParser(
						new DefaultCSVEntryParser());
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		csvParser = CSVreaderbuilder.build();

		try {
			temp = csvParser.readAll();
		} catch (final IOException e1) {
			e1.printStackTrace();
		} finally {
			if (configFileReader != null) {
				try {
					configFileReader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			if (configFileWriter != null) {
				try {
					configFileWriter.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			if (configFileBufferedWriter != null) {
				try {
					configFileBufferedWriter.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}

			if (csvParser != null) {
				try {
					csvParser.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

		savedStocksSymbols = temp.get(0);

		savedStocksBox = new Box(BoxLayout.X_AXIS);

		savedStocksPanels = new StockPanel[savedStocksSymbols.length];

		for (int i = 0; i < savedStocksSymbols.length; i++) {
			savedStocksPanels[i] = new StockPanel(savedStocksSymbols[i]);

			try {
				writeResults(
						new PriceQueryer(
								savedStocksPanels[i].getSingletonSymbol(),
								miniFormats).getStockHash(),
						savedStocksPanels[i]);
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			savedStocksBox.add(savedStocksPanels[i]);
		}

		getInfo = new JButton("Go");

		symbolInput = new JTextField("Symbol(s)");
		symbolInput.addFocusListener(new TextFieldFocusListener());
		symbolInput.setForeground(Color.gray);

		textOutput = new TextOutputArea(20, 20);
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

		megaBox.setPreferredSize(new Dimension(300, 400));

		backgroundPanel.add(megaBox);

		getContentPane().add(backgroundPanel);

		setMinimumSize(new Dimension(300, 400));

		// setResizable(false);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		symbolInput.requestFocus();
	}

	class InputListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			backgroundPanel.genBackground();
			backgroundPanel.repaint();
			if (symbolInput.getText().equals("Symbol(s)")) {
				return;
			}
			parseInput(symbolInput.getText());
			symbolInput.requestFocus();
		}
	}

	class TextFieldFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent arg0) {
			if (symbolInput.getText().equals("Symbol(s)")) {
				symbolInput.setText("");
				symbolInput.setForeground(Color.black);
			}
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			if (symbolInput.getText().length() == 0) {
				symbolInput.setText("Symbol(s)");
				symbolInput.setForeground(Color.gray);
			}
		}
	}

	public String sanitize(String s) {
		return s.replaceAll(" ", "");
	}

	public void parseInput(String input) {
		String[] symbols = null;

		input = sanitize(input);

		symbols = input.split(",");

		try {
			writeResults(new PriceQueryer(symbols, formats).getStockHash(),
					textOutput);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeResults(
			HashMap<String, HashMap<String, String>> stocksMap,
			GuiWritable writer) throws IOException {
		for (final Entry<String, HashMap<String, String>> entry : stocksMap
				.entrySet()) {
			final String key = entry.getKey();
			final HashMap<String, String> val = entry.getValue();
			for (final Entry<String, String> secentry : val.entrySet()) {
				writer.write(key + String.format(" (%s)", secentry.getKey())
						+ ": " + secentry.getValue() + "\r\n");
			}
		}
	}
}
