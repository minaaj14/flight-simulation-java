package projekat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DodavanjeAerodroma extends Frame {
	
	private TextField naziv=new TextField(10);
	private TextField kod=new TextField(10);
	private TextField x=new TextField(10);
	private TextField y=new TextField(10);
	private Button submit=new Button("submit");
	private InfoLetovi info;
	private Aplikacija aplikacija;

	public DodavanjeAerodroma(InfoLetovi i,Aplikacija a) {
		info=i;
		aplikacija=a;
		i.restartError();
		populateWindow();
		setVisible(true);
		setBounds(700,200,400,300);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

	private void populateWindow() {
		Panel p=new Panel(new GridLayout(0,1));
		p.add(new Label("Naziv aerodroma: "));
		p.add(naziv);
		p.add(new Label("Kod: "));
		p.add(kod);
		p.add(new Label("X koordinata: "));
		p.add(x);
		p.add(new Label("Y koordinata: "));
		p.add(y);
		p.add(submit);
		add(p,BorderLayout.CENTER);
		submit.addActionListener((ae)->{
			aplikacija.resetActivity();
			if(!info.validateAerodrom(naziv.getText(),kod.getText(),x.getText(),y.getText())) {
				new ErrorWindow(info.getErrorMessage());
			}
			else{
				info.dodajAerodrom(new Aerodrom(kod.getText(),naziv.getText(),Integer.parseInt(x.getText()),Integer.parseInt(y.getText())));
				aplikacija.dodajAerodromPodatke(naziv.getText(),kod.getText(),x.getText(),y.getText());
				dispose();
			}
		});
		
		TextListener tl = e -> aplikacija.resetActivity();

		naziv.addTextListener(tl);
		kod.addTextListener(tl);
		x.addTextListener(tl);
		y.addTextListener(tl);
		
	}
	
}
