import java.util.ArrayList;

public class LocalSearch {

	public Solution solve(Instance instance, Solution sol) {
		boolean facilityContainsThisPoint = false;
		Solution auxSolution = new Solution(sol);
		Solution bestAuxSolution = new Solution(sol);
		
		ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
		int numPosition = instance.getV();
		ArrayList<Facility> facilities = auxSolution.getFacilities();
		
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
						//desmarco los clientes asignados para poder calcular con el nuevo punto
						for(Facility fac: auxSolution.getFacilities()) {
							fac.deleteAllClients();
						}
						//calcular solucion con el nuevo punto en la facility
						auxSolution.calculateSolution(instance, clients);

						//comparar soluciones
						if(auxSolution.getTotalSum() < bestAuxSolution.getTotalSum()) {
							/*
							System.out.println("La solución es mejor en el punto: " + newPoint);
							System.out.println("Aux SUM: " + auxSolution.getTotalSum());
							System.out.println("Best SUM: " + bestAuxSolution.getTotalSum());
							*/
							bestAuxSolution = new Solution(auxSolution);
							break;
							//hay que actualizar el valor encontrado
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
		executionTime = executionTime + timeSol; //
		localSolution.setTime(executionTime);
		
		return localSolution;
	}
}
