import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Grasp {
	
	final double ALFA = 0.5;

	public Solution solve(Instance instance) {
		int[][] d = instance.getD();
		ArrayList<Solution> s = new ArrayList<Solution>();
		Solution resul = new Solution(instance);
		System.out.println(resul.getFacilities().size());
		resul.getFacilities().clear();
		System.out.println(resul.getFacilities().size());
		ArrayList<Facility> facilities = instance.getFacilities();
		ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
		double minimum = 100000.0;
		double maximum = 0;
		double mu = 0;
		
		boolean facilityContainsThisPoint = false;
		Solution auxSolution = new Solution(instance);
		Solution otherSolution = new Solution(auxSolution);
		int numPositions = instance.getV();
		
		ArrayList<Solution> candidates = new ArrayList<Solution>();
		
		for(int i=0; i<facilities.size(); i++) {
			int pointFacility = auxSolution.getFacilities().get(i).getCurrentPoint();
			int indexFacility = pointFacility - 1;
			for(int j=0; j<numPositions; j++) {
				// if(indexFacility != j) {
				int newPoint = j + 1;
				/*
				for (int k = 0; k < auxSolution.getFacilities().size(); k++) {
					if (auxSolution.getFacilities().get(k).getCurrentPoint() == newPoint) {
						facilityContainsThisPoint = true;
						break;
					}
				}*/
				if (facilityContainsThisPoint) {
					facilityContainsThisPoint = false;
					continue;
				} else {
					auxSolution.getFacilities().get(i).setCurrentPoint(newPoint);
					// I uncheck the assigned clients to be able to perform the calculation with the
					// new point
					for (Facility fac : auxSolution.getFacilities()) {
						fac.deleteAllClients();
					}
					// calculating the solution with the new facility point
					auxSolution.calculateSolution(instance, clients);

					//System.out.printf(auxSolution.getFacilities().get(i) + ";" + newPoint + ";" + "%.5f %n", auxSolution.getTotalSum());
					
					for(int k=0; k<auxSolution.getFacilities().size(); k++) {
						if(auxSolution.getFacilities().get(k) != auxSolution.getFacilities().get(i)) {
							auxSolution.getFacilities().get(k).setOriginPoint(0);
							auxSolution.getFacilities().set(k, null);
							
						}
					}
					/*
					Facility seleccionada = auxSolution.getFacilities().get(i);
					auxSolution.getFacilities().removeIf(facility -> facility != seleccionada);
					*/
					
					candidates.add(auxSolution);
				}
				// }
				auxSolution = new Solution(otherSolution);
			}
			//System.out.println("***");
		}
		auxSolution = new Solution(otherSolution);
		
		//IMPRSystem.out.println("Lista Candidatos " + candidates.size());
		//IMPRthis.imprimirLista(candidates);

		
		candidates.forEach(sol -> sol.getFacilities().removeIf(facility -> facility == null));
		
		
		int index = this.selectRandomItem(candidates);
		Solution solution = candidates.get(index);
		solution.getFacilities().get(0).deleteAllClients();
		resul.getFacilities().add(solution.getFacilities().get(0));
		//s.add(solution);
		candidates.remove(index);
		//IMPRSystem.out.println("Lista Candidatos " + candidates.size());
		//IMPRthis.imprimirLista(candidates);

		this.deleteMovesCandidateSelected(solution, candidates);
		
		
		//IMPRSystem.out.println("ANTES DE ELIMINAR VACIOSSSSSSSSSSSSSSSSSSs");
		//IMPRthis.imprimirLista(candidates);
		
		this.removeEmptys(candidates);
		
		while(resul.getFacilities().size() != facilities.size()) {
			//minimum = candidates.stream().mapToDouble(Solution::getTotalSum).min().getAsDouble();
			//maximum = candidates.stream().mapToDouble(Solution::getTotalSum).max().getAsDouble();
			for(int i=0; i<candidates.size(); i++) {
				if(candidates.get(i).getTotalSum() < minimum) {
					minimum = candidates.get(i).getTotalSum();
				}
				if(maximum < candidates.get(i).getTotalSum()) {
					maximum = candidates.get(i).getTotalSum();
				}
			}

			System.out.println("MINIMO " + minimum);
			System.out.println("MAXIMO " + maximum);
			
			mu = minimum + ALFA * (maximum - minimum);

			System.out.println("MU: " + mu);
			
			ArrayList<Solution> restrictedCandidatesList = new ArrayList<Solution>();
			
			for(int i=0; i<candidates.size(); i++) {
				if(candidates.get(i).getTotalSum() <= mu) {
					restrictedCandidatesList.add(candidates.get(i));
					//IMPRSystem.out.println("Cand " + candidates.get(i).getTotalSum());
					//IMPRSystem.out.println("Cand " + candidates.get(i).getFacilities().get(0));
				}
			}
			

			int index1 = this.selectRandomItem(restrictedCandidatesList);
			
			Solution solution1 = restrictedCandidatesList.get(index1);
			solution1.getFacilities().get(0).deleteAllClients();
			resul.getFacilities().add(solution1.getFacilities().get(0));
			//s.add(solution1);
			candidates.remove(solution1);
			
			//IMPRSystem.out.println("RCL");
			//IMPRthis.imprimirLista(restrictedCandidatesList);
		
			this.deleteMovesCandidateSelected(solution1, candidates);
			this.removeEmptys(candidates);
			
					
			//IMPRSystem.out.println("FINAAAAAAAAAAAALLLLLLLLLLL");
			//IMPRthis.imprimirLista(candidates);
			/*
			System.out.println("SOLUCIONN");
			for(int i=0; i<s.size(); i++) {
				for(int j=0; j<s.get(i).getFacilities().size(); j++) {
					System.out.println(s.get(i).getFacilities().get(j));
					System.out.println(s.get(i).getFacilities().get(j).getClients());
				}
			}*/
			minimum = 100000.0;
			maximum = 0;
		}
		/*
		for(int i=0; i<s.size(); i++) {
			s.get(i).getFacilities().get(0).deleteAllClients();
		}
		for(int i=1; i<s.size(); i++) {
			s.get(0).getFacilities().add(s.get(i).getFacilities().get(0));
			s.get(i).getFacilities().remove(0);
		}
		ArrayList<Facility> listaSol = new ArrayList<Facility>();
		
		System.out.println("SOLUCIONN");
		for(int i=0; i<s.size(); i++) {
			for(int j=0; j<s.get(i).getFacilities().size(); j++) {
				System.out.println(s.get(i).getFacilities().get(j));
				System.out.println(s.get(i).getFacilities().get(j).getClients());
			}
		}
		*/
		//Solution resultado = s.get(0);
		//IMPRSystem.out.println(resul.getFacilities());
		//IMPRSystem.out.println(resul.getTotalSum());
		//IMPRSystem.out.println(resul.getTime());
		//resultado.setTime(0);
		//resultado.setTotalSum(0);
		
		ArrayList<Client> clients1 = instance.getClientsSortedDescByWeight();
		//IMPRSystem.out.println(clients1);
		resul.calculateSolution(instance, clients1);
		System.out.println("RESULTADO");
		System.out.println(resul.getFacilities());
		System.out.println(resul.getTotalSum());
		System.out.println(resul.getTime());
		//IMPRSystem.out.println(clients1);
		/*
		LocalSearch localSearch = new LocalSearch();
		Solution lsSolution = localSearch.calculateLocalSearch(instance, resultado);
		System.out.println("LOCAL SEARCH");
		System.out.println(lsSolution.getFacilities());
		System.out.println(lsSolution.getTotalSum());
		System.out.println(lsSolution.getTime());
		*/
		return resul;
	}
	
	public int selectRandomItem(ArrayList<Solution> candidates) {
		Random random = new Random();
		int index = random.nextInt(candidates.size());
		System.out.println("Indice aleatorio: " + index);
		return index;
	}
	
	public void imprimirLista(ArrayList<Solution> lista) {
		int n=0;
		for(Solution sol: lista) {
			System.out.print(n + " ");
			System.out.println(sol.getFacilities());
			System.out.println(sol.getTotalSum());
			System.out.println("--------------");
			n++;
		}
	}
	
	public void deleteMovesCandidateSelected(Solution solution, ArrayList<Solution> candidates){
		for(int i=0; i<solution.getFacilities().size(); i++) {
			Facility facility = solution.getFacilities().get(i);
			//if (facility.getOriginPoint() != 0 && facility.getCurrentPoint() != 0) {
			if(facility != null) {
				int facOriginPoint = facility.getOriginPoint();
				int facCurrentPoint = facility.getCurrentPoint();
				candidates.forEach(sol -> sol.getFacilities().removeIf(fac2 -> fac2.getOriginPoint() == facOriginPoint || fac2.getCurrentPoint() == facCurrentPoint));
			}
		}
	}
	
	public void removeEmptys(ArrayList<Solution> candidates) {
		int n=0;
		while(n != candidates.size()) {
			if(candidates.get(n).getFacilities().isEmpty()) {
				candidates.remove(n);
			} else {
				n++;
			}
		}
	}
	
	public Solution calculateGrasp(Instance instance) {
		long startTime = System.nanoTime();
		Solution GraspSolution = this.solve(instance);
		long endTime = System.nanoTime();
		long time = endTime - startTime;
		double executionTime = (double) time/1e6; //ns -> ms
		System.out.println("Dentro de calculate GRASP: " + executionTime);

		GraspSolution.setTime(executionTime);
		
		return GraspSolution;
	}
	
}
