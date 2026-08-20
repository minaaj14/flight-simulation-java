package projekat;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Timer;

public class Aplikacija extends Frame {
	
	private Button dodajLet=new Button("Dodaj let");
	private Button dodajAerodrom=new Button("Dodaj aerodrom");
	private InfoLetovi info=new InfoLetovi();
	private Simulacija sim=new Simulacija(info);
	Button tabelaButton=new Button("tabela");
	Button mapaButton=new Button("mapa");
	private Panel aerodromiPodaci;
	private Panel letoviPodaci;
	private Panel listaAerodroma;
	private Button ucitajCSV=new Button("ucitaj CSV");
	private Button ucitajJSON=new Button("ucitaj JSON");
	private Button snimiCSV=new Button("snimi CSV");
	private Button snimiJSON=new Button("snimi JSON");
	private CSVreader csvReader=new CSVreader(this);
	private JSONreader jsonReader=new JSONreader(this);
	private CSVWriter csvWriter=new CSVWriter(this);
	private JSONWriter jsonWriter=new JSONWriter(this);
	private long lastActionTime = System.currentTimeMillis();
	private Dialog warningDialog;
	private Label countdownLabel;
	private Timer idleTimer;
	private boolean timerPaused =false;
	private long pauseStart;
	private boolean pauzaOdSelekcije = false;
	private boolean pauzaOdSimulacije = false;
	private Button pokreniButton=new Button("Pokreni");
	private Button pauzirajButton=new Button("Pauziraj");
	private Button resetujButton=new Button("Resetuj");
	
	public Aplikacija() {
		setBounds(700,200,700,600);
		setResizable(false);
		populateWindow();
		startIdleTimer();
		setVisible(true);
		sim.setAp(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}
	

	public void populateWindow() {

	    setLayout(new BorderLayout(10, 10));

	    // TOP BAR
	    Panel northPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 10));

	    tabelaButton.setPreferredSize(new Dimension(140, 35));
	    mapaButton.setPreferredSize(new Dimension(140, 35));

	    northPanel.add(tabelaButton);
	    northPanel.add(mapaButton);

	    // CARD LAYOUT CENTER
	    CardLayout cl = new CardLayout();
	    Panel centerPanel = new Panel(cl);
	    centerPanel.setLayout(cl);

	    //  TABELA 
	    Panel tabela = new Panel(new BorderLayout(10, 10));

	    Panel dodavanje = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 10));
	    dodavanje.add(dodajLet);
	    dodavanje.add(dodajAerodrom);
	    dodavanje.add(ucitajCSV);
	    dodavanje.add(ucitajJSON);
	    dodavanje.add(snimiCSV);
	    dodavanje.add(snimiJSON);
	    tabela.add(dodavanje, BorderLayout.NORTH);

	    Panel centar = new Panel(new GridLayout(2, 1, 10, 10));

	    // AERODROMI
	    Panel aerodromiPanel = new Panel(new BorderLayout(5, 5));
	    aerodromiPanel.setBackground(Color.WHITE);

	    Label aerLabel = new Label("AERODROMI", Label.CENTER);
	    aerLabel.setFont(new Font("Arial", Font.BOLD, 14));

	    aerodromiPanel.add(aerLabel, BorderLayout.NORTH);

	    Panel aerodromiHeader = new Panel(new GridLayout(1, 4, 5, 5));
	    aerodromiHeader.add(new Label("Naziv"));
	    aerodromiHeader.add(new Label("Kod"));
	    aerodromiHeader.add(new Label("X"));
	    aerodromiHeader.add(new Label("Y"));

	    aerodromiPodaci = new Panel(new GridLayout(0, 4, 5, 5));

	    ScrollPane aerScroll = new ScrollPane();
	    aerScroll.add(aerodromiPodaci);
	   
	    aerodromiPanel.add(aerodromiHeader, BorderLayout.NORTH);
	    aerodromiPanel.add(aerScroll, BorderLayout.CENTER);

	    aerodromiPanel.setBackground(new Color(245, 245, 245));

	    // LETOVI
	    Panel letoviPanel = new Panel(new BorderLayout(5, 5));

	    Label letLabel = new Label("LETOVI", Label.CENTER);
	    letLabel.setFont(new Font("Arial", Font.BOLD, 14));

	    letoviPanel.add(letLabel, BorderLayout.NORTH);

	    Panel letoviHeader = new Panel(new GridLayout(1, 4, 5, 5));
	    letoviHeader.add(new Label("Kod1"));
	    letoviHeader.add(new Label("Kod2"));
	    letoviHeader.add(new Label("Polazak"));
	    letoviHeader.add(new Label("Trajanje"));

	    letoviPodaci = new Panel(new GridLayout(0, 4, 5, 5));
	    ScrollPane letScroll = new ScrollPane();
	    letScroll.add(letoviPodaci);
	    letoviPanel.add(letoviHeader, BorderLayout.NORTH);
	    letoviPanel.add(letScroll, BorderLayout.CENTER);

	    letoviPanel.setBackground(new Color(245, 245, 245));

	    centar.add(aerodromiPanel);
	    centar.add(letoviPanel);

	    tabela.add(centar, BorderLayout.CENTER);

	    // MAPA  
	    Panel mapaPanel = new Panel(new BorderLayout());
	    ScrollPane sp=new ScrollPane();
	    Panel southPanel=new Panel();
	    southPanel.add(pokreniButton);
	    southPanel.add(pauzirajButton);
	    southPanel.add(resetujButton);
	    listaAerodroma=new Panel(new GridLayout(0,1,15,8));
	    sp.add(listaAerodroma);
	    sp.setPreferredSize(new Dimension(280, 500));
	    
	    mapaPanel.add(sim, BorderLayout.CENTER);
	    mapaPanel.add(sp,BorderLayout.EAST);
	    mapaPanel.add(southPanel,BorderLayout.SOUTH);
	    
	    //dodavanje mape i tabele
	    centerPanel.add("tabela", tabela);
	    centerPanel.add("mapa", mapaPanel);

	
	    tabelaButton.addActionListener(e -> {
	    	cl.show(centerPanel, "tabela");
	    	 resetActivity();
	    });
	    mapaButton.addActionListener(e -> {
	    	cl.show(centerPanel, "mapa");
	    	 resetActivity();
	    });

	   
	    add(northPanel, BorderLayout.NORTH);
	    add(centerPanel, BorderLayout.CENTER);

	   setActions();
	}
	
	public void setActions() {
		
	    dodajLet.addActionListener(e ->{ 
	    	new DodavanjeLeta(info,this);
	    	 resetActivity();
	    	});
	    dodajAerodrom.addActionListener(e -> {
		    new DodavanjeAerodroma(info,this);
		    resetActivity();
	    });
	    ucitajCSV.addActionListener((ae)->{
	    	FileDialog fd=new FileDialog(this,"izaberi fajl",FileDialog.LOAD);
	    	fd.setFile("*.csv");
	    	fd.setDirectory(".");
	    	fd.setVisible(true);
	    	 if (fd.getFile() == null)return;

	    	File fajl = new File(fd.getDirectory(), fd.getFile());
			csvReader.ucitajFajl(fajl, info);
	    	 resetActivity();
	    });
	    ucitajJSON.addActionListener((ae)->{
	    	FileDialog fd=new FileDialog(this,"izaberi fajl",FileDialog.LOAD);
	    	fd.setFile("*.json");
	    	fd.setDirectory(".");
	    	fd.setVisible(true);
	    	if (fd.getFile() == null)return;
	    	File fajl = new File(fd.getDirectory(), fd.getFile());
	    	jsonReader.ucitajFajl(fajl, info);
	    	resetActivity();
	    });
	    snimiCSV.addActionListener((ae)->{
	    	FileDialog fd = new FileDialog(this, "Sačuvaj CSV", FileDialog.SAVE);
	        fd.setFile("*.csv");
	        fd.setDirectory(".");
	        fd.setVisible(true);

	        if (fd.getFile() == null) return;

	        File fajl = new File(fd.getDirectory(), fd.getFile());
	        csvWriter.sacuvajFajl(fajl, info);
	    });
	    
	    snimiJSON.addActionListener((ae)->{
	    	FileDialog fd = new FileDialog(this, "Sačuvaj JSON", FileDialog.SAVE);
    	    fd.setFile("*.json");
    	    fd.setDirectory(".");
    	    fd.setVisible(true);

    	    if (fd.getFile() == null) return;

    	    File fajl = new File(fd.getDirectory(), fd.getFile());
    	    jsonWriter.sacuvajFajl(fajl, info);
	    });

	    pokreniButton.addActionListener(e -> {
	    	if(!info.pokreniSimulaciju()) {
	    		new ErrorWindow(info.getErrorMessage());
	    	} else {
	    		postaviPauzuOdSimulacije(true);
	    	}
	    	resetActivity();
	    });
	    pauzirajButton.addActionListener(e -> {
	    	if(info.getSat().jePokrenut()) {
	    		if(!info.pauzirajSimulaciju()) {
	    			new ErrorWindow(info.getErrorMessage());
	    		} else {
	    			postaviPauzuOdSimulacije(false);
	    		}
	    	} else {
	    		if(!info.nastaviSimulaciju()) {
	    			new ErrorWindow(info.getErrorMessage());
	    		} else {
	    			postaviPauzuOdSimulacije(true);
	    		}
	    	}
	    	resetActivity();
	    });
	    resetujButton.addActionListener(e -> {
	    	info.resetujSimulaciju();
	    	postaviPauzuOdSimulacije(false);
	    	resetActivity();
	    });
	}
	
	
	
	public void dodajAerodromPodatke(String naziv,String kod,String x,String y) {
		
		aerodromiPodaci.add(new Label(naziv));
	    aerodromiPodaci.add(new Label(kod));
	    aerodromiPodaci.add(new Label(x));
	    aerodromiPodaci.add(new Label(y));

	    aerodromiPodaci.revalidate();
	    aerodromiPodaci.repaint();
	    
	    Panel red=new Panel(new BorderLayout());
    	Checkbox cb=new Checkbox("",true);
    	StringBuilder sb=new StringBuilder();
    	sb.append(kod).append(" - ").append(naziv).append("(").append(x).append(", ").append(y).append(")");
    	String opis=sb.toString();
    	red.add(new Label(opis),BorderLayout.CENTER);
    	red.add(cb,BorderLayout.EAST);
    	listaAerodroma.add(red);
    	
    	cb.addItemListener((e)-> {
    		Aerodrom a=info.pronadjiAerodrom(kod);
    		if(a==null)return;
    		if(e.getStateChange()==ItemEvent.SELECTED) {
    			info.promeniVidljivostAerodroma(a,true);
    		}
    		else {
    			info.promeniVidljivostAerodroma(a,false);
    		}
    		sim.prikaziSve();
    		resetActivity();  
    	});
    	
	    listaAerodroma.revalidate();
	    listaAerodroma.repaint();
	}
	
	public void dodajLetPodatke(String kod1, String kod2, String polazak, String trajanje) {
		
		letoviPodaci.add(new Label(kod1));
	    letoviPodaci.add(new Label(kod2));
	    letoviPodaci.add(new Label(polazak));
	    letoviPodaci.add(new Label(trajanje));

	    letoviPodaci.revalidate();
	    letoviPodaci.repaint();
	}
	
	void resetActivity() {
	    lastActionTime = System.currentTimeMillis();
	}
	
	private void startIdleTimer() {
	    idleTimer = new Timer(true);
	    idleTimer.scheduleAtFixedRate(new java.util.TimerTask() {
	        @Override
	        public void run() {
	        	
	        	if(timerPaused)return;
	        	
	            long idle = System.currentTimeMillis() - lastActionTime;

	            if (idle >= 55000 && idle < 60000) {

	                if (warningDialog == null || !warningDialog.isVisible()) {

	                    EventQueue.invokeLater(() -> {
	                        warningDialog = new Dialog(Aplikacija.this, "Upozorenje", false);
	                        warningDialog.setLayout(new FlowLayout());
	                        countdownLabel = new Label("5");
	                        Button continueBtn = new Button("Nastavi");
	                        continueBtn.addActionListener(e -> {
	                            resetActivity();
	                            warningDialog.dispose();
	                        });

	                        warningDialog.add(new Label("Aplikacija će se ugasiti za:"));
	                        warningDialog.add(countdownLabel);
	                        warningDialog.add(continueBtn);
	                        warningDialog.setSize(250, 150);
	                        warningDialog.setLocationRelativeTo(Aplikacija.this);
	                        warningDialog.setVisible(true);
	                        warningDialog.addWindowListener(new WindowAdapter() {
	                        	@Override
	                        	public void windowClosing(WindowEvent e) {
	                        	    resetActivity();
	                        	    warningDialog.dispose();
	                        	}
	                        });
	                    });
	                }

	                int secondsLeft = (int)(60 - idle / 1000);

	                EventQueue.invokeLater(() -> {
	                    if (countdownLabel != null) {
	                        countdownLabel.setText(String.valueOf(secondsLeft));
	                    }
	                });
	            }

	            if (idle >= 60000) {
	                System.exit(0);
	            }
	        }

	    }, 1000, 1000);
	}
	
	void pauseTimer(boolean b) {
		pauzaOdSelekcije = b;
		azurirajTimerPauzu();
	}

	void postaviPauzuOdSimulacije(boolean b) {
		pauzaOdSimulacije = b;
		azurirajTimerPauzu();
	}

	private void azurirajTimerPauzu() {
		boolean trebaPauza = pauzaOdSelekcije || pauzaOdSimulacije;
		if(trebaPauza == timerPaused) return;
		timerPaused = trebaPauza;
		if(timerPaused) {
			pauseStart=System.currentTimeMillis();
		}
		else {
			long pauseDuration = System.currentTimeMillis() - pauseStart;
			lastActionTime += pauseDuration;
		}
	}
	
	
	public static void main(String[] args) {
		new Aplikacija();
	}
}
