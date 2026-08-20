package projekat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InfoLetovi {

	private ArrayList<Aerodrom>aerodromi=new ArrayList<>();
	private ArrayList<Let>letovi=new ArrayList<>();
	private String errorMessage;
	private CopyOnWriteArrayList<Let> aktivniLetovi = new CopyOnWriteArrayList<>();
	private boolean pauzirano=false;
	private boolean simulacijaPostoji=false;
	private final SimulacioniSat sat = new SimulacioniSat();

	public void dodajAerodrom(Aerodrom a) {
		aerodromi.add(a);
	}
	public void dodajLet(Let l) {
		letovi.add(l);
	}

	public ArrayList<Aerodrom>getAerodromi(){return aerodromi;}
	public ArrayList<Let>getLetovi(){return letovi;}

	public SimulacioniSat getSat() { return sat; }

	public void dodajAktivniLet(Let l) {
        aktivniLetovi.add(l);
    }

    public void ukloniAktivniLet(Let l) {
        aktivniLetovi.remove(l);
    }

    public List<Let> getAktivniLetovi() { return aktivniLetovi; }

    public synchronized void setPauzirano(boolean p) {
        this.pauzirano = p;
        if (!pauzirano) {
            notifyAll();
        }
    }

    public synchronized void proveriPauzu() throws InterruptedException {
        while (pauzirano) {
            wait();
        }
    }

    public boolean pokreniSimulaciju() {
        if (sat.jePokrenut()) {
            errorMessage = "Greska: Simulacija je vec pokrenuta";
            return false;
        }
        if (letovi.isEmpty()) {
            errorMessage = "Greska: Nema unetih letova za simulaciju";
            return false;
        }
        for (Aerodrom a : aerodromi) {
            a.resetujSimulaciju();
        }
        aktivniLetovi.clear();
        setPauzirano(false);
        sat.pokreni();
        for (Let l : letovi) {
            Thread nit = new Thread(l);
            l.pripremiZaSimulaciju(this, nit);
            nit.start();
        }
        simulacijaPostoji=true;
        return true;
    }

    public boolean pauzirajSimulaciju() {
        if (!sat.jePokrenut()) {
            errorMessage = "Greska: Simulacija nije pokrenuta";
            return false;
        }
        sat.pauziraj();
        setPauzirano(true);
        return true;
    }

    public boolean nastaviSimulaciju() {
        if (sat.jePokrenut()) {
            errorMessage = "Greska: Simulacija vec radi";
            return false;
        }
        if (!simulacijaPostoji) {
            errorMessage = "Greska: Simulacija nije pokrenuta";
            return false;
        }
        sat.nastavi();
        setPauzirano(false);
        return true;
    }

    public void resetujSimulaciju() {
        for (Let l : letovi) {
            Thread nit = l.getNit();
            if (nit != null) {
                nit.interrupt();
            }
        }
        setPauzirano(false);
        sat.resetuj();
        aktivniLetovi.clear();
        for (Aerodrom a : aerodromi) {
            a.resetujSimulaciju();
        }
        simulacijaPostoji=false;
    }
	
	boolean validateLet(String s1,String s2, String s3, String s4) {
		
		if(s1.isEmpty()||s2.isEmpty()||s3.isEmpty()||s4.isEmpty()) {
			errorMessage="Greska: Nisu uneti svi podaci";
			return false;
		}
		if(pronadjiAerodrom(s1)==null) {
			errorMessage="Greska: Nepostojeci kod aerodroma";
			return false;
		}
		if(pronadjiAerodrom(s2)==null) {
			errorMessage="Greska: Nepostojeci kod aerodroma";
			return false;
		}
		
		if(s1.equals(s2)) {
			errorMessage="Greska: Potrebno je uneti razlicite aerodrome";
			return false;
		}
		if(!proveriKod(s1) || !proveriKod(s2)) {
			errorMessage="Greska: Neispravan kod";
		}
		if(!s3.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
			errorMessage="Greska: Neispravno uneto vreme";
			return false;
		}
		if(!s4.matches("\\d+")) {
			errorMessage="Greska: Neispravno uneto trajanje leta";
			return false;
		}
		
		Aerodrom a1=pronadjiAerodrom(s1);
		Aerodrom a2=pronadjiAerodrom(s2);
		if(postojiLet(new Let(a1,a2,DodavanjeLeta.parsirajVreme(s3),Integer.parseInt(s4)))){
			errorMessage="Greska: Let vec postoji";
			return false;
		}
		return true;
	}
	
	boolean validateAerodrom(String naziv, String kod, String x, String y) {
		if(naziv.isEmpty()||kod.isEmpty()||x.isEmpty()||y.isEmpty()) {
			errorMessage="Greska: Nisu uneti svi podaci";
			return false;
		}
		for(Aerodrom a:aerodromi) {
			if(a.getKod().equals(kod)) {
				errorMessage="Greska: Kod mora biti jedinstven";
				return false;
			}
		}
		if(!proveriKod(kod)) {
			errorMessage="Greska: Neispravan kod";
			return false;
		}
		try {
			int broj1=Integer.parseInt(x);
			int broj2=Integer.parseInt(y);
			if(!(broj1>=-180 && broj1<=180)) {
				errorMessage="Greska: X koordinata nije u opsegu";
				return false;
			}
			if(!(broj2>=-90 && broj2<=90)) {
				errorMessage="Greska: Y koordinata nije u opsegu";
				return false;
			}
		}catch(NumberFormatException e) {
			errorMessage="Greska: Koordinate treba da budu date kao celi brojevi";
			return false;
		}
		
		return true;
	}
	
	void restartError() {errorMessage="";}
	String getErrorMessage() {return errorMessage;}
	
	boolean proveriKod(String kod) {
		if(kod.length()!=3)return false;
		for(char c:kod.toCharArray()) {
			if(!(Character.isLetter(c)&& Character.isUpperCase(c)))return false;
		}
		return true;
	}
	
	boolean postojiLet(Let let) {
		for(Let l:letovi) {
			if(l.equals(let))return true;
		}
		return false;
	}
	Aerodrom pronadjiAerodrom(String kod) {
		for(Aerodrom a:aerodromi) {
			if(a.getKod().equals(kod))return a;
		}
		return null;
	}
	void promeniVidljivostAerodroma(Aerodrom a,boolean b) {a.vidljiv=b;}
	boolean simulacijaJePokrenuta() {return simulacijaPostoji;}
	
	
}
