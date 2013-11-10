package pricequery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PriceQuery {

	public static void main(String[] args) {
		String input = "";

		String[] symbols = null;
		String[] formats = {"n", "p", "c1"};
		List<String[]> res;

		Scanner scanner = new Scanner(System.in);

		greet();
		
		long time = 0;

		while(true){
			System.out.print("Type a symbol:  ");
			input = scanner.nextLine();

			if(input.equalsIgnoreCase("quit")){
				break;
			}

			symbols = input.split(" |,");

			try {
				time = System.currentTimeMillis();
				res = (new PriceQueryer(symbols, formats).getCsvResults());
				time = System.currentTimeMillis() - time;
				System.out.println("Query time: " + time);
				for(int i = 0; i < res.size(); i++){
					for(int j = 1; j < res.get(i).length; j++){
						System.out.println(res.get(i)[0] + String.format(" (%s)", formats[j]) + ": " + res.get(i)[j]);
					}
				}
			} catch (IOException e) {
				System.out.println("Somethin's up with your internet!");
			}
		}

		System.out.println("Goodbye!");
	}

	public static void greet(){
		System.out.println("Welcome to Max's ticker!");
		System.out.println("Type quit to quit");
	}

}
