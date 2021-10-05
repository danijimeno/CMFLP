import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Solution {
	
	private ArrayList<Integer> pointsFacilities;
	private ArrayList<Integer> facilitiesAssignedtoClients;
	private ArrayList<Facility> randomFacilities;
	
	public Solution() {
		this.pointsFacilities = new ArrayList<Integer>();
		this.facilitiesAssignedtoClients = new ArrayList<Integer>();
		this.randomFacilities = new ArrayList<Facility>();
	}
	
	public Solution(ArrayList<Integer> pointsFacilities) {
		this.pointsFacilities = pointsFacilities;
	}
	
	public ArrayList<Integer> getPointsFacilities() {
		return pointsFacilities;
	}

	public void setPointsFacilities(ArrayList<Integer> pointsFacilities) {
		this.pointsFacilities = pointsFacilities;
	}
	
	public ArrayList<Integer> getFacilitiesAssignedtoClients() {
		return facilitiesAssignedtoClients;
	}

	public void setFacilitiesAssignedtoClients(ArrayList<Integer> facilitiesAssignedtoClients) {
		this.facilitiesAssignedtoClients = facilitiesAssignedtoClients;
	}

	public ArrayList<Facility> getRandomFacilities() {
		return randomFacilities;
	}

	public void setRandomFacilities(ArrayList<Facility> randomFacilities) {
		this.randomFacilities = randomFacilities;
	}

	public void generateFacilities(Instance instance) {
		int [] fac = instance.getW();
		for (int i=0; i<fac.length; i++) {
			if(fac[i] > 0) {
				int index = i + 1;
				pointsFacilities.add(index);
			}
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
		while((newFacilities.size() != this.pointsFacilities.size())){
			newPoint = (random.nextInt(instance.getV()) + 1);
			if(!newFacilities.contains(newPoint)) {
				newFacilities.add(newPoint);
				System.out.println(newPoint);
			}
		}
		return newFacilities;
	}
	
	public void addRandomFacilitiesToOriginal (ArrayList<Integer> randomFacilitiesPoints, ArrayList<Facility> facilities) {
		int point;
		for(int i=0; i<randomFacilitiesPoints.size(); i++) {
			point = randomFacilitiesPoints.get(i);
			facilities.get(i).setNewPoint(point);
			randomFacilities.add(new Facility(facilities.get(i).getW(), facilities.get(i).getqCap(), point));
		}
	}
	
	public void changeOriginalFacToRandomOnes (Instance instance, ArrayList<Integer> facilitiesRandom) {
		int[] wAux = instance.getW();
		int[] qAux = instance.getqCapacity();
		for(int i=0; i< facilitiesRandom.size(); i++) {
			int pointFac = this.pointsFacilities.get(i);
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
