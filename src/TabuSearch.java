import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TabuSearch {	
	
	private final int MAX_ITERATIONS = 3;

	public Solution solve(Instance instance, Solution solution) {
		boolean facilityContainsThisPoint = false;
		Solution auxSolution = new Solution(solution);
		Solution bestAuxSolution = new Solution(solution);
	
		int numPosition = instance.getV();
		//ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
		int numFacilities = solution.getFacilities().size();
		int queueSize = (int) Math.round(numFacilities * 0.3);
		System.out.println("Tamaño de la cola Tabu " + queueSize);
		int iteration = 0;
		Queue<Facility> cola = new LinkedList<Facility>();
		boolean foundBetterSolution = false;
		
		while(iteration != MAX_ITERATIONS) {
			Solution bestOfWorstsSolution = new Solution(instance);
			bestOfWorstsSolution.setTotalSum(100000);
			for(int i=0; i<auxSolution.getFacilities().size(); i++) {
				Facility facility = auxSolution.getFacilities().get(i);
				int pointFacility = auxSolution.getFacilities().get(i).getCurrentPoint();
				int indexFacility = pointFacility - 1;
				if (this.containsFacility(cola, facility)) {
					continue;
				}
				for(int j=0; j<numPosition; j++) {
					if(indexFacility != j) {
						int newPoint = j + 1;
						System.out.println("Punto: " + newPoint);
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
							System.out.println("Facility a probar");
							System.out.println(auxSolution.getFacilities().get(i));
							//I uncheck the assigned clients to be able to perform the calculation with the new point
							for(Facility fac: auxSolution.getFacilities()) {
								fac.deleteAllClients();
							}
							//calculating the solution with the new facility point
							ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
							auxSolution.calculateSolution(instance, clients);
							
							System.out.println("AUX Sol: " + auxSolution.getTotalSum());
							System.out.println("BEST Sol: " + bestAuxSolution.getTotalSum());
							//compare solutions
							if(auxSolution.getTotalSum() < bestAuxSolution.getTotalSum()) {
								System.out.println("AUX Sol: " + auxSolution.getTotalSum());
								System.out.println("BEST Sol: " + bestAuxSolution.getTotalSum());
								bestAuxSolution = new Solution(auxSolution);
								Facility facility2 = auxSolution.getFacilities().get(i);
								System.out.println("Facility " + facility2);
								if(cola.size()==queueSize) {
									cola.remove();
								}
								cola.add(auxSolution.getFacilities().get(i));
								System.out.println("Cola ");
								System.out.println(cola);
								foundBetterSolution = true;
								break;
							} 
							else {
								if(auxSolution.getTotalSum() < bestOfWorstsSolution.getTotalSum()) {
									System.out.println("Worst Sol: " + bestOfWorstsSolution.getTotalSum());
									bestOfWorstsSolution = new Solution(auxSolution);
									Facility facility2 = auxSolution.getFacilities().get(i);
									System.out.println("---La mejor de las peores---");
									System.out.println("sol " + bestOfWorstsSolution);
									/*
									if(cola.size()==1) {
										cola.remove();
									}
									cola.add(auxSolution.getFacilities().get(i));
									*/
									System.out.println("Cola ");
									System.out.println(cola);
									System.out.println("-----");
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
		
		System.out.println("Resultado ");
		System.out.println(bestAuxSolution);
		//cambiar por auxSolution para contemplar el caso de la mejor de las peores
		System.out.println("Salida ");
		System.out.println(auxSolution);
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
