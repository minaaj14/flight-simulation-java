package projekat;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Aerodrom{
	private static final double TRAJANJE_SLOTA = 10.0;

	private String kod;
	private String naziv;
	int x;
	int y;
	boolean vidljiv=true;

	private final Queue<Let> redZaPoletanje = new ConcurrentLinkedQueue<>();
	private double sledeciSlobodanTrenutak = 0;

	Aerodrom(String k,String n,int x, int y){
		kod=k;
		naziv=n;
		this.x=x;
		this.y=y;
		}
	int getX() {return x;}
	int getY() {return y;}
	String getNaziv() {return naziv;}
	String getKod() {return kod;}

	void prijaviZaPoletanje(Let let) {
		redZaPoletanje.add(let);
	}

	// Vraca true ako je let na redu (glava reda) i ako je slot slobodan;
	// u tom slucaju rezervise sledecih 10 simuliranih minuta za ovaj aerodrom.
	synchronized boolean pokusajPoleteti(Let let, double trenutnoVreme) {
		if (redZaPoletanje.peek() != let) return false;
		if (trenutnoVreme < sledeciSlobodanTrenutak) return false;
		redZaPoletanje.poll();
		sledeciSlobodanTrenutak = trenutnoVreme + TRAJANJE_SLOTA;
		return true;
	}

	synchronized void resetujSimulaciju() {
		redZaPoletanje.clear();
		sledeciSlobodanTrenutak = 0;
	}
	String ispisiPodatke() {
		StringBuilder sb=new StringBuilder();
		sb.append("Naziv: ").append(naziv).append(" kod: ").append(kod).append(" koordinate: ").append(x).append(", ").append(y);
		return sb.toString();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null)return false;
		if(this==obj)return true;
		if(!(obj instanceof Aerodrom))return false;
		Aerodrom novi=(Aerodrom)obj;
		return this.kod.equals(novi.kod);
	}
	
}



