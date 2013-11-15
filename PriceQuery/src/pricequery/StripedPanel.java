package pricequery;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class StripedPanel extends JPanel {
	private static final int nStripes = 100;

	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;

	private final int axis;

	Color[] bgColors = null;

	public StripedPanel(int axis) {
		this.axis = axis;
		genBackground();
	}

	public StripedPanel() {
		this(X_AXIS);
	}

	public void genBackground() {
		bgColors = new Color[nStripes];
		for (int i = 0; i < bgColors.length; i++) {
			bgColors[i] = randomColor();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2d = (Graphics2D) g;

		for (int i = 0; i < nStripes; i++) {
			g2d.setColor(bgColors[i]);

			if (axis == Y_AXIS) {
				g2d.fill(new Rectangle2D.Double(getWidth() / (float) nStripes
						* i, 0, getWidth() / (float) nStripes, getHeight()));
			} else if (axis == X_AXIS) {
				g2d.fill(new Rectangle2D.Double(0, getHeight()
						/ (float) nStripes * i, getWidth(), getHeight()
						/ (float) nStripes));
			}
		}
	}

	public Color randomColor() {
		return new Color((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255));
	}
}
