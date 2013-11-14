package pricequery;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class StockPanel extends JPanel implements GuiWritable{
	private String writeString = "";
	
	public StockPanel(){
		setPreferredSize(new Dimension(10, getFontMetrics(getFont()).getHeight()));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawString(writeString, 0, getFontMetrics(getFont()).getHeight() / 2 + 2);
	}

	@Override
	public void write(String s) {
		writeString = s;
		repaint();
	}
}
