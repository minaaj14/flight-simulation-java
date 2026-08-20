package projekat;

public class SimulacioniSat {

	private static final double SIM_MINUTA_PO_SEKUNDI = 10.0;

	private boolean pokrenut = false;
	private long realniPocetak;
	private double akumuliranoVreme = 0;

	public synchronized void pokreni() {
		akumuliranoVreme = 0;
		realniPocetak = System.nanoTime();
		pokrenut = true;
	}

	public synchronized void nastavi() {
		realniPocetak = System.nanoTime();
		pokrenut = true;
	}

	public synchronized void pauziraj() {
		if (pokrenut) {
			akumuliranoVreme = trenutnoVreme();
			pokrenut = false;
		}
	}

	public synchronized void resetuj() {
		pokrenut = false;
		akumuliranoVreme = 0;
	}

	public synchronized double trenutnoVreme() {
		if (!pokrenut) return akumuliranoVreme;
		double proteklihSekundi = (System.nanoTime() - realniPocetak) / 1_000_000_000.0;
		return akumuliranoVreme + proteklihSekundi * SIM_MINUTA_PO_SEKUNDI;
	}

	public synchronized boolean jePokrenut() {
		return pokrenut;
	}
}