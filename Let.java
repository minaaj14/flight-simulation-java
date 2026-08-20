package projekat;

public class Let implements Runnable{
	Aerodrom a1;
	Aerodrom a2;
	int vremePoletanja;
	int trajanje;
	private InfoLetovi info;
	private Thread nit;
	private volatile double stvarnoVremePoletanja = -1;

	Let(Aerodrom a,Aerodrom b, int v, int t){
		a1=a;
		a2=b;
		vremePoletanja=v;
		trajanje=t;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj==null)return false;
		if(this==obj)return true;
		if(!(obj instanceof Let))return false;
		Let novi=(Let)obj;
		if(a1.equals(novi.a1) && a2.equals(novi.a2) && vremePoletanja==novi.vremePoletanja && trajanje==novi.trajanje)return true;
		return false;
	}

	void pripremiZaSimulaciju(InfoLetovi info, Thread nit) {
		this.info = info;
		this.nit = nit;
		this.stvarnoVremePoletanja = -1;
	}

	Thread getNit() { return nit; }

	boolean jeULetu() { return stvarnoVremePoletanja >= 0; }

	@Override
	public void run() {
		try {
			SimulacioniSat sat = info.getSat();
			cekajDoVremena(vremePoletanja, sat);
			a1.prijaviZaPoletanje(this);
			
			while (!a1.pokusajPoleteti(this, sat.trenutnoVreme())) {
				info.proveriPauzu();
				Thread.sleep(100);
			}
			stvarnoVremePoletanja = sat.trenutnoVreme();
			info.dodajAktivniLet(this);

			cekajDoVremena(stvarnoVremePoletanja + trajanje, sat);
			info.ukloniAktivniLet(this);
		} catch (InterruptedException e) {
			// Simulacija je pauzirana/resetovana - nit se tiho zavrsava.
		}
	}

	private void cekajDoVremena(double ciljnoVreme, SimulacioniSat sat) throws InterruptedException {
		while (sat.trenutnoVreme() < ciljnoVreme) {
			info.proveriPauzu();
			Thread.sleep(100);
		}
	}

	double trenutnoX(double simVreme) {
		return a1.getX() + (a2.getX() - a1.getX()) * progres(simVreme);
	}

	double trenutnoY(double simVreme) {
		return a1.getY() + (a2.getY() - a1.getY()) * progres(simVreme);
	}

	private double progres(double simVreme) {
		double t = (simVreme - stvarnoVremePoletanja) / trajanje;
		if (t < 0) t = 0;
		if (t > 1) t = 1;
		return t;
	}
}
