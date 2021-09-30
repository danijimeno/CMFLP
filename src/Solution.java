import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Solution {
	
	private ArrayList<Integer> facilities;
	private ArrayList<Integer> facilitiesAssignedtoClients;
	private ArrayList<Integer> partialCapacities;
	
	public Solution() {
		this.facilities = new ArrayList<Integer>();
		this.facilitiesAssignedtoClients = new ArrayList<Integer>();
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
	
	public ArrayList<Integer> getFacilitiesAssignedtoClients() {
		return facilitiesAssignedtoClients;
	}

	public void setFacilitiesAssignedtoClients(ArrayList<Integer> facilitiesAssignedtoClients) {
		this.facilitiesAssignedtoClients = facilitiesAssignedtoClients;
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
					this.facilitiesAssignedtoClients.add(facility);
					this.partialCapacities.set(indexPartCap, partialCapFac);
				} else {
					this.facilitiesAssignedtoClients.add(facility);
				}
			}
			distance = 10000;
			facility = 0;
			partialCapFac = 0;
		}
	}
	
	public void assignClientsOrdered(Instance instance, ArrayList<Facility> facilities) {
		int [][] d = instance.getD();
		int distance = 10000;
		int facility = 0;
		List<Client> clientsOrdered = instance.getClientsSortedDescByWeight();
		int partialCapFac = 0;
		int indexPartCap = 0;
		for(int i=0; i<clientsOrdered.size(); i++) {
			if(clientsOrdered.get(i).getU() > 0) {
				int pointClient = clientsOrdered.get(i).getPoint();
				int indexClient = pointClient - 1;
				for(int j=0; j<facilities.size(); j++) {
					int pointFacility = facilities.get(j).getOriginPoint();
					int indexFacility = pointFacility - 1; //Resto uno para acceder al indice en la matriz de distancias ya que los puntos empiezan en 1 no en 0
					if((d[indexClient][indexFacility] < distance) && ((facilities.get(j).getPartialCapacity() + clientsOrdered.get(i).getQ()) <= facilities.get(j).getqCap())) {
						distance = d[indexClient][indexFacility];
						facility = pointFacility;
						partialCapFac = facilities.get(j).getPartialCapacity() + clientsOrdered.get(i).getQ();
						indexPartCap = j;
					} 
				}
				if(!(facility == 0 && distance == 10000)) {
					this.facilitiesAssignedtoClients.add(facility);
					facilities.get(indexPartCap).setPartialCapacity(partialCapFac);
				} else {
					this.facilitiesAssignedtoClients.add(facility);
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
	
	public float evaluateTheSolution(Instance instance, ArrayList<Facility> facilities) {
		int [][] d = instance.getD();
		float totalSum = 0;
		int partialSumFac = 0;
		float partialSumClients = 0;
		//1 part

		//2 part
		List<Client> clientsOrdered = instance.getClientsSortedDescByWeight();
		for(int i=0; i<clientsOrdered.size(); i++) {
			if(clientsOrdered.get(i).getU() > 0) {
				int pointClient = clientsOrdered.get(i).getPoint();
				int indexClient = pointClient - 1;
				if(this.facilitiesAssignedtoClients.get(i) > 0) {
					int pointFacility = this.facilitiesAssignedtoClients.get(i);
					int indexFacility = pointFacility - 1;
					partialSumClients = clientsOrdered.get(i).getU() * d[indexClient][indexFacility];
					totalSum += partialSumClients;
				}
			}
		}
		return totalSum;
	}
	
	public ArrayList<Integer> generateFacilitiesRandom(Instance instance) {
		ArrayList<Integer> newFacilities = new ArrayList<Integer>();
		int newPoint;
		Random random = new Random();  
		while((newFacilities.size() != this.facilities.size())){
			newPoint = (random.nextInt(instance.getV()) + 1);
			if(!newFacilities.contains(newPoint)) {
				newFacilities.add(newPoint);
				System.out.println(newPoint);
			}
		}
		return newFacilities;
	}
	
	public void changeOriginalFacToRandomOnes (Instance instance, ArrayList<Integer> facilitiesRandom) {
		int[] wAux = instance.getW();
		int[] qAux = instance.getqCapacity();
		for(int i=0; i< facilitiesRandom.size(); i++) {
			int pointFac = this.facilities.get(i);
			int indexFac = pointFac - 1;
			int indexFacRan = (facilitiesRandom.get(i) - 1);
			if(indexFac != indexFacRan) {
				wAux[indexFac] = 0;
				qAux[indexFacRan] = qAux[indexFac];
				wAux[indexFacRan] = 1;
				qAux[indexFac] = 0;
			}
		}
		instance.setW(wAux);
		instance.setqCapacity(qAux);
	}
	
}
