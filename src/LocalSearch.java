import java.util.ArrayList;

public class LocalSearch {

	public Solution solve(Instance instance, Solution sol) {
		long startTime = System.nanoTime();
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
			/*
			for(int k=0; k<bestAuxSolution.getFacilities().size(); k++) {
				auxSolution.getFacilities().get(k).deleteAllClients();
				auxSolution.getFacilities().remove(k);
				Facility fac = bestAuxSolution.getFacilities().get(k);
				auxSolution.getFacilities().add(k, new Facility(fac));
			}*/
			auxSolution = new Solution(bestAuxSolution);
			
		}
		long endTime = System.nanoTime();
		long time = endTime - startTime;
		
		double executionTime = (double) time/1e6; //ns -> ms
		System.out.println("Tiempo Busq Local: " + executionTime);
		bestAuxSolution.setTime(executionTime);
		//System.out.println("Tiempo SOL devuelta " + bestAuxSolution.getTime());
		return bestAuxSolution;
	}
}
