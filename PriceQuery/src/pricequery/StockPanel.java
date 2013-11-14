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
		
		setPreferredSize(new Dimension(10, getFontMetrics(getFont()).getHeight()));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawString(symbol + ": " + price, 0, getFontMetrics(getFont()).getHeight() / 2 + 2);
	}
	
	public void updatePrice(String price){
		this.price = price;
		repaint();
	}
}
