
public class Client implements Comparable<Client> {

	private float u; //weight
	private int q;	//demand
	private int point;	//location
	
	public Client(float u, int q, int point) {
		this.u = u;
		this.q = q;
		this.point = point;
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

	@Override
	public String toString() {
		return "Client [u=" + this.u + ", q=" + this.q + ", point=" + this.point + "]";
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
	
	
	
}
