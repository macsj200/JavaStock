package pricequery;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextOutputArea extends Box implements GuiWritable{
	private JTextArea outputArea = null;
	private JScrollPane scrollPane = null;
	
	public TextOutputArea(int rows, int columns){
		super(BoxLayout.X_AXIS);
		outputArea = new JTextArea(rows, columns);
		outputArea.setEditable(false);
		scrollPane = new JScrollPane(outputArea);
		
		add(scrollPane);
	}

	@Override
	public void write(String s) {
		outputArea.append(s);
		outputArea.setCaretPosition(outputArea.getDocument().getLength());
	}
}
