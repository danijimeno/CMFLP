
public class Client implements Comparable<Client> {

	private float u; //weight
	private int q;	//demand
	private int point;	//location
	private Facility facility;
	
	public Client(float u, int q, int point) {
		this.u = u;
		this.q = q;
		this.point = point;
	}
	
	public Client(Client c) {
		this.u = c.getU();
		this.q = c.getQ();
		this.point = c.getPoint();
	}


	public float getU() {
		return u;
	}

	public void setU(float u) {
		this.u = u;
	}

	public int getQ() {
		return q;
	}

	public void setQ(int q) {
		this.q = q;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
	
	public Facility getFacility() {
		return facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

	@Override
	public String toString() {
		return "Client [p=" + this.point + ", u=" + this.u + ", q=" + this.q + ", Fac: " + this.facility + "]" + "\n";
	}

	@Override
	public int compareTo(Client c) {
		if(this.u > c.u) {
			return 1;
		}else if (this.u == c.u) {
			return 0;
		}
		return -1;
	}
	
	public void deleteFacility() {
		this.facility = null;
	}
	
	
	
}
