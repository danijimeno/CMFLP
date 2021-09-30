
public class Facility {
	
	private int w; //weight
	private int qCap; //capacities
	private int originPoint;
	private int newPoint;
	private int partialCapacity;
	
	public Facility(int w, int q, int point) {
		this.w = w;
		this.qCap = q;
		this.originPoint = point;
		this.newPoint = -1;
		this.partialCapacity = 0;
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

	public int getOriginPoint() {
		return originPoint;
	}

	public void setOriginPoint(int originPoint) {
		this.originPoint = originPoint;
	}

	public int getNewPoint() {
		return newPoint;
	}

	public void setNewPoint(int newPoint) {
		this.newPoint = newPoint;
	}

	public int getPartialCapacity() {
		return partialCapacity;
	}

	public void setPartialCapacity(int partialCapacity) {
		this.partialCapacity = partialCapacity;
	}

	@Override
	public String toString() {
		return "Facility [w=" + this.w + ", qCap=" + this.qCap + ", originPoint=" + this.originPoint + ", newPoint=" + this.newPoint
				+ ", partialCapacity=" + this.partialCapacity + "]";
	}
	


}
