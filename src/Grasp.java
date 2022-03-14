import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
		//ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
		double minimum = 100000.0;
		double maximum = 0;
		double mu = 0;
		
		Solution auxSolution = new Solution(instance);
		Solution otherSolution = new Solution(auxSolution);
		int numPositions = instance.getV();
		
		ArrayList<Facility> facilitiesSel = new ArrayList<Facility>();
		
		ArrayList<Solution> candidates = new ArrayList<Solution>();
		
		for (int i = 0; i < facilities.size(); i++) {
			int pointFacility = auxSolution.getFacilities().get(i).getCurrentPoint();
			int indexFacility = pointFacility - 1;
			for (int j = 0; j < numPositions; j++) {
				ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
				int newPoint = j + 1;
				auxSolution.getFacilities().get(i).setCurrentPoint(newPoint);
				// I uncheck the assigned clients to be able to perform the calculation with the
				// new point
				for (Facility fac : auxSolution.getFacilities()) {
					fac.deleteAllClients();
				}
				// calculating the solution with the new facility point
				auxSolution.calculateSolution(instance, clients);

				System.out.printf(auxSolution.getFacilities().get(i) + ";" + newPoint + ";" + "%.5f %n",
						auxSolution.getTotalSum());

				for (int k = 0; k < auxSolution.getFacilities().size(); k++) {
					if (auxSolution.getFacilities().get(k) != auxSolution.getFacilities().get(i)) {
						auxSolution.getFacilities().get(k).setOriginPoint(0);
					}
				}
				candidates.add(auxSolution);
				auxSolution = new Solution(otherSolution);
			}
			System.out.println("***");
		}
		auxSolution = new Solution(otherSolution);
		
		System.out.println("Lista Candidatos " + candidates.size());
		this.imprimirLista(candidates);


			
		
		int index = this.selectRandomItem(candidates);
		Solution solution = candidates.get(index);
		s.add(solution);
		candidates.remove(index);
		System.out.println("Lista Candidatos " + candidates.size());

		System.out.println("SOLUCION " + solution);

		this.facSeleccionada(solution, facilitiesSel);
		System.out.println("SOLUCION " + solution);
		System.out.println("ARRAY SELECCIONADAS");
		System.out.println(facilitiesSel);
		
		this.borrarSel(facilitiesSel, candidates);
		
		System.out.println("DESPUES DE ELIMINAR SELECCIONADOS");
		this.imprimirLista(candidates);
		

		this.updateCandidates(facilitiesSel.get(0), candidates);
		
		System.out.println("DESPUES DE ACTUALIZAAARRRR");
		this.imprimirLista(candidates);
		
		//while(resul.getFacilities().size() != facilities.size()) {
		while(s.size() != facilities.size()) {
			//minimum = candidates.stream().mapToDouble(Solution::getTotalSum).min().getAsDouble();
			//maximum = candidates.stream().mapToDouble(Solution::getTotalSum).max().getAsDouble();
			
			
			for(int i=0; i<candidates.size(); i++) {
				Solution auxSolCand = new Solution(candidates.get(i));
				System.out.println("ANTES MOD SOLUCION " + auxSolCand);
				for(int j=0; j<auxSolCand.getFacilities().size(); j++) {
					Facility auxFac = auxSolCand.getFacilities().get(j);
					if(auxFac.getOriginPoint() == 0) {
						auxFac.setOriginPoint(auxFac.getCurrentPoint());
					}
				}
				auxSolCand.getFacilities().forEach(Facility::deleteAllClients);
				System.out.println("DESPU MOD SOLUCION " + auxSolCand);
				ArrayList<Client> clientes = instance.getClientsSortedDescByWeight();
				auxSolCand.calculateSolution(instance, clientes);
				System.out.println("     " + auxSolCand);
				
				candidates.get(i).setTotalSum(auxSolCand.getTotalSum());
				candidates.get(i).setTime(auxSolCand.getTime());
				
				/*
				for(int k=0; k<candidates.get(i).getFacilities().size(); k++) {
					Facility facility = candidates.get(i).getFacilities().get(k);
					if(facility.getOriginPoint() == 0) {
						auxSolCand.getFacilities().get(k).setOriginPoint(0);
					}
				}
				candidates.set(i, auxSolCand);
				*/			
				
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
					System.out.println("Cand " + candidates.get(i));
				}
			}
			

			int index1 = this.selectRandomItem(restrictedCandidatesList);
			
			Solution solution1 = restrictedCandidatesList.get(index1);
			s.add(solution1);
			candidates.remove(solution1);
			
			System.out.println("RCL");
			this.imprimirLista(restrictedCandidatesList);
					
			
			System.out.println("SOLUCION " + solution1);

			
			this.facSeleccionada(solution1, facilitiesSel);
			System.out.println("SOLUCION ACT " + solution1);
			System.out.println("ARRAY SELECCIONADAS");
			System.out.println(facilitiesSel);
			
			this.borrarSel(facilitiesSel, candidates);
			
			System.out.println("DESPUES DE ELIMINAR SELECCIONADOS RCL");
			this.imprimirLista(candidates);
			
			this.updateCandidates(facilitiesSel.get(facilitiesSel.size()-1), candidates);
			
			System.out.println("DESPUES DE ACTUALIZAAARRRR");
			this.imprimirLista(candidates);
					

			minimum = 100000.0;
			maximum = 0;
		}
		
		s.forEach(sol -> sol.getFacilities().forEach(Facility::deleteAllClients));

		s.forEach(sol -> sol.getFacilities().removeIf(fac2 -> fac2.getOriginPoint() == 0));
		
		System.out.println("SOLUCIONN");
		for(int i=0; i<s.size(); i++) {
			for(int j=0; j<s.get(i).getFacilities().size(); j++) {
				System.out.println(s.get(i).getFacilities().get(j));
				System.out.println(s.get(i).getFacilities().get(j).getClients());
			}
		}
		
		for(int i=0; i<facilitiesSel.size(); i++) {
			resul.getFacilities().add(facilitiesSel.get(i));
		}
		resul.calculateSolution(instance, instance.getClientsSortedDescByWeight());
		
		System.out.println("RESULTADO" + resul);

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
	
	
	public void updateCandidates(Facility facility, ArrayList<Solution> candidates) {
		int pointOrigin = facility.getOriginPoint();
		int currentPoint = facility.getCurrentPoint();
		for(int i=0; i<candidates.size(); i++) {
			for(int j=0; j<candidates.get(i).getFacilities().size(); j++) {
				if(candidates.get(i).getFacilities().get(j).getCurrentPoint() == pointOrigin 
						&& candidates.get(i).getFacilities().get(j).getOriginPoint() == 0) {
					//candidates.get(i).getFacilities().get(j).setOriginPoint(pointOrigin);
					//candidates.get(i).getFacilities().get(j).setCurrentPoint(currentPoint);
					candidates.get(i).getFacilities().set(j, facility);
				}
			}
		}
	}
	
	public void facSeleccionada(Solution solution, ArrayList<Facility> seleccionados) {
		Iterator<Facility> itSolution = solution.getFacilities().iterator();
		while(itSolution.hasNext()) {
			Facility facility = itSolution.next();
			if(facility.getOriginPoint() == 0) {
				itSolution.remove();
			} else if (!seleccionados.contains(facility)) {
				seleccionados.add(facility);
			} 
		}
	}
	
	public void borrarSel(ArrayList<Facility> seleccionadas, ArrayList<Solution> candidates) {
		Facility fac = seleccionadas.get(seleccionadas.size() - 1);
		int originPointFac = fac.getOriginPoint();
		int currentPointFac = fac.getCurrentPoint();
		Iterator<Solution> iteratorCand = candidates.iterator();
		while(iteratorCand.hasNext()) {
			Solution sol = iteratorCand.next();
			for(int i=0; i<sol.getFacilities().size(); i++) {
				if(sol.getFacilities().get(i).getOriginPoint() == originPointFac) {
					iteratorCand.remove();
				}
				if((sol.getFacilities().get(i).getCurrentPoint() == currentPointFac) && (sol.getFacilities().get(i).getOriginPoint() != 0)){
					iteratorCand.remove();
				}
			}
		}
	}
}
