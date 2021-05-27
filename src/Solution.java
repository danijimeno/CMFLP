import java.util.ArrayList;
import java.util.Iterator;

public class Solution {
	
	private ArrayList<Integer> facilities;
	private ArrayList<Integer> clients;
	private ArrayList<Integer> partialCapacities;
	
	public Solution() {
		this.facilities = new ArrayList<>();
		this.clients = new ArrayList<Integer>();
		this.partialCapacities = new ArrayList<Integer>();
	}
	
	public Solution(ArrayList<Integer> facilities) {
		this.facilities = facilities;
	}
	
	public ArrayList<Integer> getFacilities() {
		return facilities;
	}

	public void setFacilities(ArrayList<Integer> facilities) {
		this.facilities = facilities;
	}

	public ArrayList<Integer> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Integer> clients) {
		this.clients = clients;
	}

	public ArrayList<Integer> getPartialCapacities() {
		return partialCapacities;
	}

	public void setPartialCapacities(ArrayList<Integer> partialCapacities) {
		this.partialCapacities = partialCapacities;
	}

	public void generateFacilities(Instance instance) {
		int [] fac = instance.getW();
		for (int i=0; i<fac.length; i++) {
			if(fac[i] > 0) {
				int index = i + 1;
				facilities.add(index);
			}
		}
	}
	
	public void assignClients(Instance instance) {
		float [] clients = instance.getU();
		int [][] d = instance.getD();
		int distance = 10000;
		int facility = 0;
		int [] facilityCapabilities = instance.getqCapacity();
		int [] customerDemands = instance.getQ();
		int partialCapFac = 0;
		int indexPartCap = 0;
		inicializePartialCap();
		for(int i=0; i<clients.length; i++) {
			if(clients[i] > 0) {
				for(int j=0; j<this.facilities.size(); j++) {
				//Iterator<Integer> iteratorFacilities = facilities.iterator();
				//while(iteratorFacilities.hasNext()) {
					//int pointFacility = iteratorFacilities.next();
					//int indexFacility = pointFacility -1; //Resto uno para acceder al indice en la matriz de distancias ya que los puntos empiezan en 1 no en 0
					
					int pointFacility = this.facilities.get(j);
					int indexFacility = pointFacility - 1; //Resto uno para acceder al indice en la matriz de distancias ya que los puntos empiezan en 1 no en 0
					if((d[i][indexFacility] < distance) && ((this.partialCapacities.get(j) + customerDemands[i]) <= facilityCapabilities[indexFacility])) {
						distance = d[i][indexFacility];
						facility = pointFacility;
						partialCapFac = this.partialCapacities.get(j) + customerDemands[i];
						indexPartCap = j;
					} 
				}
				if(!(facility == 0 && distance == 10000)) {
					this.clients.add(facility);
					this.partialCapacities.set(indexPartCap, partialCapFac);
				} else {
					this.clients.add(facility);
				}
			}
			distance = 10000;
			facility = 0;
			partialCapFac = 0;
		}
	}
	
	//Creo e inicializo las capacidades parciales de cada facility a 0
	public void inicializePartialCap(){
		for (int i=0; i< this.facilities.size(); i++) {
			this.partialCapacities.add(0);
		}
	}
	
}
