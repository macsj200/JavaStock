package pricequery;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class StockPanel extends JPanel{
	private String symbol = null;
	private String price = null;
	
	public StockPanel(String symbol, String price){
		this.symbol = symbol;
		this.price = price;
		
		setMinimumSize(new Dimension(10, 100));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawString(symbol, 0, getFontMetrics(getFont()).getHeight() / 2);
	}
}
