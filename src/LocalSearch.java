import java.util.ArrayList;

public class LocalSearch {

	public Solution solve(Instance instance, Solution sol) {
		boolean facilityContainsThisPoint = false;
		Solution auxSolution = new Solution(sol);
		Solution bestAuxSolution = new Solution(sol);
		
		ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
		int numPosition = instance.getV();
		
		for(int i=0; i<sol.getFacilities().size(); i++) {
			int pointFacility = auxSolution.getFacilities().get(i).getCurrentPoint();
			int indexFacility = pointFacility - 1;
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
						auxSolution.calculateSolution(instance, clients);

						//compare solutions
						if(auxSolution.getTotalSum() < bestAuxSolution.getTotalSum()) {
							bestAuxSolution = new Solution(auxSolution);
							break;
						}
					}
				}
			}
			//Termina de recorrer todos los puntos esa facility
			//Ver si actuliza siendo mejor o sino volver a dejar la fac donde estaba
			auxSolution = new Solution(bestAuxSolution);
		}
		return bestAuxSolution;
	}
	
	public Solution calculateLocalSearch(Instance instance, Solution solution) {
		long startTime = System.nanoTime();
		Solution localSolution = this.solve(instance, solution);
		long endTime = System.nanoTime();
		long time = endTime - startTime;
		double executionTime = (double) time/1e6; //ns -> ms
		System.out.println("Dentro de calculate LS: " + executionTime);
		double timeSol = solution.getTime();
		//total time of the local search is the sum of the constructive plus the time taken for the local search
		executionTime = executionTime + timeSol;
		localSolution.setTime(executionTime);
		
		return localSolution;
	}
}
