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

public class DodavanjeLeta extends Frame {
	
	private TextField aerodrom1=new TextField(5);
	private TextField aerodrom2=new TextField(5);
	private TextField vreme=new TextField(5);
	private TextField trajanje=new TextField(5);
	private Button submit=new Button("submit");
	private InfoLetovi info;
	private Aplikacija aplikacija;
	
	public DodavanjeLeta(InfoLetovi i,Aplikacija a) {
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
		p.add(new Label("Aerodrom 1: "));
		p.add(aerodrom1);
		p.add(new Label("Aerodrom 2: "));
		p.add(aerodrom2);
		p.add(new Label("Vreme poletanja: "));
		p.add(vreme);
		p.add(new Label("Trajanje leta: "));
		p.add(trajanje);
		p.add(submit);
		add(p,BorderLayout.CENTER);
		submit.addActionListener((ae)->{
			aplikacija.resetActivity();
			if(!info.validateLet(aerodrom1.getText(),aerodrom2.getText(),vreme.getText(),trajanje.getText())) {
				new ErrorWindow(info.getErrorMessage());
			}
			else {
				Aerodrom a1=info.pronadjiAerodrom(aerodrom1.getText());
				Aerodrom a2=info.pronadjiAerodrom(aerodrom2.getText());
				Let novi=new Let(a1,a2,parsirajVreme(vreme.getText()),Integer.parseInt(trajanje.getText()));
				info.dodajLet(novi);
				aplikacija.dodajLetPodatke(aerodrom1.getText(), aerodrom2.getText(),vreme.getText(),trajanje.getText());
				dispose();
	
			}
		});
		TextListener tl = e -> aplikacija.resetActivity();
		aerodrom1.addTextListener(tl);
		aerodrom2.addTextListener(tl);
		vreme.addTextListener(tl);
		trajanje.addTextListener(tl);
		
	}
	
	static int parsirajVreme(String vreme) {
		String[] parts = vreme.split(":");
		int hours = Integer.parseInt(parts[0]);
		int minutes = Integer.parseInt(parts[1]);

		int totalMinutes = hours * 60 + minutes;
		return totalMinutes;
	}
}
