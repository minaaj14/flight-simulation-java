package projekat;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;


public class Simulacija extends Canvas {
	
	private InfoLetovi info;
	private Aerodrom selected;
	private boolean blink = false;
	Timer timer = new Timer(true);  //true-ne sprecava gasenje programa
	private Aplikacija ap;

	public Simulacija(InfoLetovi info) {
		this.info = info;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for(Aerodrom a : info.getAerodromi()) {
					int px = mapX(a.getX());
					int py = mapY(a.getY());
					if(Math.abs(e.getX() - px) < 6 && Math.abs(e.getY() - py) < 6){
						if(selected == a) {
							selected = null;
							ap.pauseTimer(false);
						} else {
							selected = a;
							ap.pauseTimer(true);
						}
						return;
					}
				}
			}
		});
		
		timer.scheduleAtFixedRate(new TimerTask() {
			private int brojac = 0;
			@Override
			public void run() {
				brojac++;
				if(brojac >= 2) { 
					blink = !blink;
					brojac = 0;
				}
				
				EventQueue.invokeLater(() -> prikaziSve());
			}
		}, 0, 200); 
	}
	
	void prikaziSve() {
		if (!this.isDisplayable() || !this.isVisible()) {
	        return;
	    }
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(2); 
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		String status = "";

		if (info.simulacijaJePokrenuta() && !info.getSat().jePokrenut()) {
		    status = " (pauzirano)";
		}
		double simVreme = info.getSat().trenutnoVreme();
		int ukupnoMinuta = ((int) simVreme) % (24 * 60);
		String vremeTekst = String.format("Vreme: %02d:%02d%s", ukupnoMinuta / 60, ukupnoMinuta % 60, status);    
		g.setColor(Color.DARK_GRAY);
		g.fillRect(5, 5, 190, 20);
		g.setColor(Color.WHITE);
		g.drawString(vremeTekst, 10, 20);

		for(Aerodrom a : info.getAerodromi()){
			if(!a.vidljiv)continue;
			int px = mapX(a.getX());
			int py = mapY(a.getY());
			if(selected == a) {
				g.setColor(blink ? Color.RED : Color.GRAY);
			} else {
				g.setColor(Color.GRAY);
			}
			g.fillRect(px - 5, py - 5, 10, 10);
			g.setColor(Color.BLACK);
			g.drawString(a.getKod(), px + 8, py);
		}
		
		g.setColor(Color.BLUE);
		for(Let let : info.getAktivniLetovi()) {
			int ax = mapX(let.trenutnoX(simVreme));
			int ay = mapY(let.trenutnoY(simVreme));
			g.fillOval(ax - 4, ay - 4, 8, 8);
		}

		g.dispose();
		bs.show(); // Prikazuje sve odjednom na ekran, bez treperenja
	}
	
	@Override
	public void update(Graphics g) {
		// Prazno - sprečava AWT da briše ekran automatski i pravi flickering
	}
	
	@Override
	public void paint(Graphics g) {
		prikaziSve(); 
	}

	void setAp(Aplikacija a) {
		ap = a;
	}
	
	private int mapX(double x){ return (int)((x + 180) / 360 * getWidth()); }
	private int mapY(double y){ return (int)((90 - y) / 180 * getHeight()); }
}
