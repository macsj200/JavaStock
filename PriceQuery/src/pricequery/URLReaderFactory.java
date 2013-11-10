package pricequery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLReaderFactory{
	public static BufferedReader newURLReader(String urlString) throws IOException{
		URL service = new URL(urlString);  
		URLConnection con = service.openConnection();  
		return new BufferedReader(new InputStreamReader(con.getInputStream())); 
	}
}
