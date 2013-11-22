package pricequery;

import java.io.IOException;
import java.util.HashMap;

import javax.swing.SwingUtilities;

public class FixedUpdater implements Runnable {
	private PriceQueryGui priceQueryGui = null;

	public FixedUpdater(PriceQueryGui priceQueryGui){
		this.priceQueryGui = priceQueryGui;
	}

	@Override
	public void run() {

		while(true){
			for (int i = 0; i < priceQueryGui.getSavedStocksPanels().length; i++) {
				try {
					SwingUtilities.invokeLater(new AsyncUpdaterRunnable (new PriceQueryer(
							priceQueryGui.getSavedStocksPanels()[i].getSingletonSymbol(),
							priceQueryGui.getMiniFormats()).getStockHash(), priceQueryGui.getSavedStocksPanels()[i]));
				} catch (IOException e) {
					
				}
			}
		}
	}

	class AsyncUpdaterRunnable implements Runnable{
		public AsyncUpdaterRunnable(HashMap<String, HashMap<String, String>> hash, GuiWritable writer){
			try {
				priceQueryGui.writeResults(hash, writer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void run(){

		}
	}

}
