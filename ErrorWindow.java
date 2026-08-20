package projekat;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ErrorWindow extends Frame {

	ErrorWindow(String message){
		setTitle("Error");
		setBounds(900,400,400,300);
		add(new Label(message),BorderLayout.CENTER);
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

