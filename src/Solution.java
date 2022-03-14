import java.util.ArrayList;
import java.util.Random;


public class Solution {
	
	private ArrayList<Facility> facilities;
	private double totalSum;
	private double time;
	
	public Solution(Instance instance) {
		this.facilities = instance.getFacilities();
		this.totalSum = 0;
		this.time = 0;
	}
	
	public Solution(Solution solution) {
		this.facilities = new ArrayList<Facility>();
		for(Facility fac: solution.getFacilities()) {
			this.facilities.add(new Facility(fac));
		}
		this.totalSum = solution.getTotalSum();
		this.time = solution.getTime();
	}

	public ArrayList<Facility> getFacilities() {
		return facilities;
	}

	public void setFacilities(ArrayList<Facility> facilities) {
		this.facilities = facilities;
	}
	
	public double getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(double totalSum) {
		this.totalSum = totalSum;
	}
	
	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}


	public void assignClients(Instance instance, ArrayList<Client> clientsOrdered) {
		int [][] d = instance.getD();
		int distance = 10000;
		int facility = 0;
		int partialCapFac = 0;
		int indexPartCap = 0;
		for (int i = 0; i < clientsOrdered.size(); i++) {
			int pointClient = clientsOrdered.get(i).getPoint();
			int indexClient = pointClient - 1;
			for (int j = 0; j < facilities.size(); j++) {
				int pointFacility = facilities.get(j).getCurrentPoint();
				int indexFacility = pointFacility - 1; // Resto uno para acceder al indice en la matriz de distancias ya que los puntos empiezan en 1 no en 0
				if ((d[indexClient][indexFacility] < distance)
						&& ((facilities.get(j).getPartialCapacity() + clientsOrdered.get(i).getQ()) <= facilities.get(j).getqCap())) {
					distance = d[indexClient][indexFacility];
					facility = pointFacility;
					partialCapFac = facilities.get(j).getPartialCapacity() + clientsOrdered.get(i).getQ();
					indexPartCap = j;
				}
			}
			if (!(facility == 0 && distance == 10000)) {
				facilities.get(indexPartCap).setPartialCapacity(partialCapFac);
				facilities.get(indexPartCap).addClient(clientsOrdered.get(i));
			}
			distance = 10000;
			facility = 0;
			partialCapFac = 0;
		}
	}
	
	public double evaluateTheSolution(Instance instance, ArrayList<Client> clientsOrdered) {
		int [][] d = instance.getD();
		double totalSum = 0;
		int partialSumFac = 0;
		double partialSumClients = 0;
		//1 part
		for(int j=0; j<facilities.size(); j++) {
			int originalPointFac = facilities.get(j).getOriginPoint();
			int originalIndexFac = originalPointFac - 1;
			int displacedPointFac = facilities.get(j).getCurrentPoint();
			int displacedIndexFac = displacedPointFac - 1;
			partialSumFac = facilities.get(j).getW()*d[originalIndexFac][displacedIndexFac];
			totalSum += partialSumFac;
		}
		//2 part
		for (int i = 0; i < clientsOrdered.size(); i++) {
			int pointClient = clientsOrdered.get(i).getPoint();
			int indexClient = pointClient - 1;
			if (clientsOrdered.get(i).getFacility() != null) {
				int pointFacility = clientsOrdered.get(i).getFacility().getCurrentPoint();
				int indexFacility = pointFacility - 1;
				partialSumClients = clientsOrdered.get(i).getU() * d[indexClient][indexFacility];
				totalSum += partialSumClients;
			}
		}
		return totalSum;
	}
	
	public ArrayList<Integer> generateFacilitiesRandom(Instance instance) {
		ArrayList<Integer> newFacilities = new ArrayList<Integer>();
		int newPoint = 0;
		Random random = new Random();  
		while((newFacilities.size() != facilities.size())){
			newPoint = (random.nextInt(instance.getV()) + 1);
			if(!newFacilities.contains(newPoint)) {
				newFacilities.add(newPoint);			
			}
		}
		return newFacilities;
	}
	
	public void addRandomFacilitiesToOriginal(Instance instance) {
		int point = 0;
		ArrayList<Integer> randomFacilitiesPoints = this.generateFacilitiesRandom(instance);
		for(int i=0; i<randomFacilitiesPoints.size(); i++) {
			point = randomFacilitiesPoints.get(i);
			facilities.get(i).setCurrentPoint(point);
			//se quitan los clientes asignados en la evaluación inicial para la posterior evaluación de las fac random
			facilities.get(i).deleteAllClients();
		}
	}
	
	public void calculateSolution(Instance instance, ArrayList<Client> clientsOrdered) {
		long startTime = System.nanoTime();
		this.assignClients(instance, clientsOrdered);
		double summation = this.evaluateTheSolution(instance, clientsOrdered);
		long endTime = System.nanoTime();
		long time = endTime - startTime;
		
		double executionTime = (double) time/1e6; //ns -> ms
		this.setTime(executionTime);
		this.setTotalSum(summation);
	}
	
	public double calculateDeviationFromTheBestSol(Solution bestSolution) {
		double best = bestSolution.getTotalSum();
		double objectiveFunction = this.getTotalSum();
		double dev = Math.abs((best - objectiveFunction)/best);
		return dev;
	}
	
	public Solution whichIsBetter(Solution solution) {
		return (this.getTotalSum() < solution.getTotalSum()) ? this : solution;
	}
	
	public int isTheBest(Solution bestSolution) {
		return (this == bestSolution) ? 1 : 0;
	}
	
	@Override
	public String toString() {
		return "Solution " + this.facilities.toString() + " " + this.totalSum + ", time=" + this.time;
	}
}
