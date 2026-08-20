package projekat;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class FajlErrorWindow extends Frame {
	
	ArrayList<String>greske;

	public FajlErrorWindow(ArrayList<String>greske) {
		this.greske=greske;
		setTitle("Error");
		setLocation(700,200);
		setResizable(true);
		setLayout(new GridLayout(greske.size(), 1));

		for (String s : this.greske) {
		    add(new Label(s));
		}
		setVisible(true);
		pack();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
	}
}
