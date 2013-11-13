package pricequery;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;

public class PriceQueryer {
	private BufferedReader URLReader = null;
	private String [] formats = null;
	private String[] symbols = {};
	private final String baseURL = "http://download.finance.yahoo.com/d/quotes.csv?%s";
	private java.util.List<String[]> csvResults = null;

	public PriceQueryer(String[] symbols, String[] formats) throws IOException{
		this.symbols = symbols;
		this.formats = formats;
		URLReader = URLReaderFactory.newURLReader(makeQueryUrl());

		csvResults = new ArrayList<String[]>();
	}

	public java.util.List<String[]> getCsvResults(){
		(new Thread(new Runnable(){
			public void run(){
				readVals();
			}
		})).start();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {}
		return csvResults;
	}

	public void readVals(){
		String line;
		try {
			CSVReaderBuilder<String[]> CSVreaderbuilder = (new CSVReaderBuilder<String[]>(URLReader))
					.strategy(CSVStrategy.UK_DEFAULT).entryParser(new DefaultCSVEntryParser());
			CSVReader<String[]> csvParser = CSVreaderbuilder.build();
			csvResults = csvParser.readAll();
		} catch (IOException e) {
			System.out.println("Sorry couldn't do it");
		}
	}

	public static String queryMaker(String[] symbols, String [] formats){
		String query = "s=%s&f=%s";
		String format = "";

		String symbolsString = "";
		if(symbols.length == 1){
			symbolsString = symbols[0];
		}
		else{
			for(int i = 0; i < symbols.length; i++){
				symbolsString = symbolsString + symbols[i] + "+";
			}
		}

		for(int i = 0; i < formats.length; i++){
			format = format + formats[i];
		}

		query = String.format(query, symbolsString, format);

		return query; 
	}

	public String makeQueryUrl(){
		return String.format(baseURL, queryMaker(getSymbols(), formats));
	}

	public String[] getSymbols() {
		return symbols;
	}

	public void setSymbols(String[] symbols) {
		this.symbols = symbols;
	}
}
