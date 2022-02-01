import java.util.ArrayList;
import java.util.List;

public class Facility {
	
	private int w; //weight
	private int qCap; //capacities
	private int originPoint;
	private int currentPoint; //punto actual
	private int partialCapacity;
	private List<Client> clients;
	
	public Facility(int w, int q, int point) {
		this.w = w;
		this.qCap = q;
		this.originPoint = point;
		this.currentPoint = point;
		this.partialCapacity = 0;
		this.clients = new ArrayList<Client>();
	}
	
	public Facility(Facility fac) {
		this.w = fac.getW();
		this.qCap = fac.qCap;
		this.originPoint = fac.getOriginPoint();
		this.currentPoint = fac.getCurrentPoint();
		this.partialCapacity = fac.getPartialCapacity();
		this.clients = new ArrayList<Client>();
		fac.getClients().forEach(client -> addClient(new Client(client)));
		//fac.getClients().stream().forEach(client -> addClient(new Client (client.getU(), client.getQ(), client.getPoint())));
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

	public int getCurrentPoint() {
		return currentPoint;
	}

	public void setCurrentPoint(int currentPoint) {
		this.currentPoint = currentPoint;
	}

	public int getPartialCapacity() {
		return partialCapacity;
	}

	public void setPartialCapacity(int partialCapacity) {
		this.partialCapacity = partialCapacity;
	}
	
	public List<Client> getClients() {
		return clients;
	}
	

	public void addClient(Client client) {
		client.setFacility(this);
		clients.add(client);
	}
	
	public void deleteAllClients() {
		//clients.stream().forEach(client -> client.deleteFacility())
		clients.stream().forEach(Client::deleteFacility);
		clients.clear();
		this.setPartialCapacity(0);
	}

	@Override
	public String toString() {
		return "Facility [originPoint=" + this.originPoint + ", currentPoint=" + this.currentPoint + ", w=" + this.w + ", qCap=" + this.qCap 
				+ ", partialCapacity=" + this.partialCapacity + "]";
	}
	


}
