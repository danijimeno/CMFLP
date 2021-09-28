
public class Facility {
	
	private int w; //weight
	private int qCap; //capacities
	private int originPoint;
	private int newPoint;
	
	public Facility(int w, int q, int point) {
		this.w = w;
		this.qCap = q;
		this.originPoint = point;
		this.newPoint = -1;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getqCap() {
		return qCap;
	}

	public void setqCap(int qCap) {
		this.qCap = qCap;
	}

	public int getPoint() {
		return originPoint;
	}

	public void setPoint(int point) {
		this.originPoint = point;
	}

	@Override
	public String toString() {
		return "Facility [w=" + this.w + ", qCap=" + this.qCap + ", originPoint=" + this.originPoint + ", newPoint=" + this.newPoint + "]";
	}
	


}
