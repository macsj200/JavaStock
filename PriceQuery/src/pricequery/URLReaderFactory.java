package pricequery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLReaderFactory {
	public static BufferedReader newURLReader(String urlString)
			throws IOException {
		final URL service = new URL(urlString);
		final URLConnection con = service.openConnection();
		return new BufferedReader(new InputStreamReader(con.getInputStream()));
	}
}
