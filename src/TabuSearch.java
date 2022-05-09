import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TabuSearch {	
	
	private final int MAX_ITERATIONS = 3;
	private final double PERCENTAGE_NODES_CHOOSE = 0.3;

	public Solution solve(Instance instance, Solution solution) {
		boolean facilityContainsThisPoint = false;
		Solution auxSolution = new Solution(solution);
		Solution bestAuxSolution = new Solution(solution);
	
		int numPosition = instance.getV();
		//ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
		int numFacilities = solution.getFacilities().size();
		int queueSize = (int) Math.round(numFacilities * PERCENTAGE_NODES_CHOOSE);
		int iteration = 0;
		Queue<Facility> tabuQueue = new LinkedList<Facility>();
		boolean foundBetterSolution = false;
		
		while(iteration != MAX_ITERATIONS) {
			Solution bestOfWorstsSolution = new Solution(instance);
			bestOfWorstsSolution.setTotalSum(100000);
			for(int i=0; i<auxSolution.getFacilities().size(); i++) {
				Facility facility = auxSolution.getFacilities().get(i);
				int pointFacility = auxSolution.getFacilities().get(i).getCurrentPoint();
				int indexFacility = pointFacility - 1;
				if (this.containsFacility(tabuQueue, facility)) {
					continue;
				}
				for(int j=0; j<numPosition; j++) {
					if(indexFacility != j) {
						int newPoint = j + 1;
						for(int k=0; k<auxSolution.getFacilities().size(); k++) {
							if(auxSolution.getFacilities().get(k).getCurrentPoint() == newPoint) {
								facilityContainsThisPoint = true;
								break;
							}
						}
						if(facilityContainsThisPoint) {
							facilityContainsThisPoint = false;
							continue;
						}else {
							auxSolution.getFacilities().get(i).setCurrentPoint(newPoint);
							//I uncheck the assigned clients to be able to perform the calculation with the new point
							for(Facility fac: auxSolution.getFacilities()) {
								fac.deleteAllClients();
							}
							//calculating the solution with the new facility point
							ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
							auxSolution.calculateSolution(instance, clients);

							//compare solutions
							if(auxSolution.getTotalSum() < bestAuxSolution.getTotalSum()) {
								bestAuxSolution = new Solution(auxSolution);
								Facility facility2 = auxSolution.getFacilities().get(i);
								if(tabuQueue.size()==queueSize) {
									tabuQueue.remove();
								}
								tabuQueue.add(auxSolution.getFacilities().get(i));
								foundBetterSolution = true;
								break;
							} 
							else {
								if(auxSolution.getTotalSum() < bestOfWorstsSolution.getTotalSum()) {
									bestOfWorstsSolution = new Solution(auxSolution);
								}
							}
						}
					}
				}
				//Termina de recorrer todos los puntos esa facility
				//Ver si actuliza siendo mejor o sino volver a dejar la fac donde estaba
				
				auxSolution = new Solution(bestAuxSolution);
				//auxSolution = new Solution(bestOfWorstsSolution);
				//bestAuxSolution = new Solution(bestOfWorstsSolution);
				/*
				if(foundBetterSolution) {
					auxSolution = new Solution(bestAuxSolution);
				} else {
					auxSolution = new Solution(bestOfWorstsSolution);
					iteration++;
				}*/
			}
			if(foundBetterSolution) {
				iteration = 0;
			} else {
				auxSolution = new Solution(bestOfWorstsSolution);
				iteration++;
			}
			foundBetterSolution = false;
		}
		
		//cambiar por auxSolution para contemplar el caso de la mejor de las peores

		return auxSolution;
	}
	
	public boolean containsFacility(Queue<Facility> cola, Facility facility) {
		Iterator<Facility> itCola = cola.iterator();
		while(itCola.hasNext()) {
			Facility fac = itCola.next();
			if(fac.getOriginPoint() == facility.getOriginPoint()) {
				return true;
			}
		}
		return false;
	}
	
	public Solution calculateTabuSearch(Instance instance, Solution solution) {
		long startTime = System.nanoTime();
		Solution tabuSolution = this.solve(instance, solution);
		long endTime = System.nanoTime();
		long time = endTime - startTime;
		double executionTime = (double) time/1e6; //ns -> ms
		//System.out.println("Dentro de calculate TS: " + executionTime);
		double timeSol = solution.getTime();
		//total time of the local search is the sum of the constructive plus the time taken for the local search
		executionTime = executionTime + timeSol;
		tabuSolution.setTime(executionTime);
		
		return tabuSolution;
	}
}
