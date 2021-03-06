package pricequery;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.googlecode.jcsv.reader.internal.DefaultCSVEntryParser;

public class PriceQueryer {
	private BufferedReader URLReader = null;
	private String[] formats = null;
	private String[] symbols = {};
	private final String baseURL = "http://download.finance.yahoo.com/d/quotes.csv?%s";
	private java.util.List<String[]> csvResults = null;

	private HashMap<String, HashMap<String, String>> stockHash = null;

	public PriceQueryer(String[] symbols, String[] formats) throws IOException {
		this.symbols = symbols;
		this.formats = formats;

		stockHash = new HashMap<String, HashMap<String, String>>();

		URLReader = URLReaderFactory.newURLReader(makeQueryUrl());

		csvResults = new ArrayList<String[]>();
	}

	public java.util.List<String[]> getCsvResults() {
		readVals();
		return csvResults;
	}

	public HashMap<String, HashMap<String, String>> getStockHash() {
		readVals();
		return stockHash;
	}

	public void readVals() {
		final String line;
		try {
			final CSVReaderBuilder<String[]> CSVreaderbuilder = new CSVReaderBuilder<String[]>(
					URLReader).strategy(CSVStrategy.UK_DEFAULT).entryParser(
					new DefaultCSVEntryParser());
			final CSVReader<String[]> csvParser = CSVreaderbuilder.build();
			csvResults = csvParser.readAll();

			for (int i = 0; i < csvResults.size(); i++) {
				stockHash.put(symbols[i], new HashMap<String, String>());
				for (int j = 0; j < formats.length; j++) {
					stockHash.get(symbols[i]).put(formats[j],
							csvResults.get(i)[j]);
				}
			}
		} catch (final IOException e) {
			System.out.println("Sorry couldn't do it");
		}
	}

	public static String queryMaker(String[] symbols, String[] formats) {
		String query = "s=%s&f=%s";
		String format = "";

		String symbolsString = "";
		if (symbols.length == 1) {
			symbolsString = symbols[0];
		} else {
			for (int i = 0; i < symbols.length; i++) {
				symbolsString = symbolsString + symbols[i] + "+";
			}
		}

		for (int i = 0; i < formats.length; i++) {
			format = format + formats[i];
		}

		query = String.format(query, symbolsString, format);

		return query;
	}

	public String makeQueryUrl() {
		return String.format(baseURL, queryMaker(getSymbols(), formats));
	}

	public String[] getSymbols() {
		return symbols;
	}

	public void setSymbols(String[] symbols) {
		this.symbols = symbols;
	}
}
