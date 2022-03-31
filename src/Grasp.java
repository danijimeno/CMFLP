import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Grasp {
	
	final double ALFA = 0.5;

	public Solution solve(Instance instance, int distancePercentage) {
		int[][] d = instance.getD();
		ArrayList<Solution> s = new ArrayList<Solution>();
		Solution result = new Solution(instance);
		result.getFacilities().clear();

		ArrayList<Facility> facilities = instance.getFacilities();
		double minimum = 100000.0;
		double maximum = 0;
		double mu = 0;
		ArrayList<Facility> facsSelectedCL = new ArrayList<Facility>();
		ArrayList<Solution> candidatesList = new ArrayList<Solution>();
		
		Solution auxSolution = new Solution(instance);
		Solution otherSolution = new Solution(auxSolution);
		int numPositions = instance.getV();

		int closestNumber = (int) Math.round(numPositions * distancePercentage / 100);

		for(int i=0; i<auxSolution.getFacilities().size(); i++) {
			ArrayList<Integer> lista = new ArrayList<Integer>();
			int pointFacility = auxSolution.getFacilities().get(i).getCurrentPoint();
			int indexFacility = pointFacility - 1;
			//the list is created with the distances from the facility to each of the clients
			for(int j=0; j<numPositions; j++) {
				lista.add(d[indexFacility][j]);
			}
			//select the number of clients closest to the installation according to the distance percentage defined
			for(int k=0; k<closestNumber; k++) {
				//int min = lista.stream().min(Comparator.naturalOrder()).get();
				int min = 100000;
				for(Integer in: lista) {
					if(in != null) {
						if(in < min) {
							min = in;
						}
					}
				}
				int indexNewPos = lista.indexOf(min);
				int pointNewPos = indexNewPos + 1;
				lista.set(indexNewPos, null);
				
				ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
				auxSolution.getFacilities().get(i).setCurrentPoint(pointNewPos);
				// calculating the solution with the new facility point
				auxSolution.calculateSolution(instance, clients);
				for (int l = 0; l < auxSolution.getFacilities().size(); l++) {
					if (auxSolution.getFacilities().get(l) != auxSolution.getFacilities().get(i)) {
						auxSolution.getFacilities().get(l).setOriginPoint(0);
					}
				}
				min = 100000;
				candidatesList.add(auxSolution);
				auxSolution = new Solution(otherSolution);
			}
			auxSolution = new Solution(otherSolution);
		}

		Solution solution = this.selectRandomCandidate(candidatesList);
		s.add(solution);
		candidatesList.remove(solution);

		this.saveSelectedCandidate(solution, facsSelectedCL);
		this.removeCandidatesBasedSelectedCand(facsSelectedCL, candidatesList);
		this.updateCandidates(facsSelectedCL.get(0), candidatesList);

		
		//while(resul.getFacilities().size() != facilities.size()) {
		while(s.size() != facilities.size()) {
			//minimum = candidates.stream().mapToDouble(Solution::getTotalSum).min().getAsDouble();
			//maximum = candidates.stream().mapToDouble(Solution::getTotalSum).max().getAsDouble();
			
			for(int i=0; i<candidatesList.size(); i++) {
				Solution auxSolCand = new Solution(candidatesList.get(i));
				for(int j=0; j<auxSolCand.getFacilities().size(); j++) {
					Facility auxFac = auxSolCand.getFacilities().get(j);
					if(auxFac.getOriginPoint() == 0) {
						auxFac.setOriginPoint(auxFac.getCurrentPoint());
					}
				}
				auxSolCand.getFacilities().forEach(Facility::deleteAllClients);

				ArrayList<Client> clientes = instance.getClientsSortedDescByWeight();
				auxSolCand.calculateSolution(instance, clientes);

				candidatesList.get(i).setTotalSum(auxSolCand.getTotalSum());
				candidatesList.get(i).setTime(auxSolCand.getTime());
				/*
				for(int k=0; k<candidates.get(i).getFacilities().size(); k++) {
					Facility facility = candidates.get(i).getFacilities().get(k);
					if(facility.getOriginPoint() == 0) {
						auxSolCand.getFacilities().get(k).setOriginPoint(0);
					}
				}
				candidates.set(i, auxSolCand);
				*/			
				
				if(candidatesList.get(i).getTotalSum() < minimum) {
					minimum = candidatesList.get(i).getTotalSum();
				}
				if(maximum < candidatesList.get(i).getTotalSum()) {
					maximum = candidatesList.get(i).getTotalSum();
				}
			}
			
			mu = minimum + ALFA * (maximum - minimum);
			
			ArrayList<Solution> restrictedCandidatesList = new ArrayList<Solution>();
			for(int i=0; i<candidatesList.size(); i++) {
				if(candidatesList.get(i).getTotalSum() <= mu) {
					restrictedCandidatesList.add(candidatesList.get(i));
				}
			}
			
			solution = this.selectRandomCandidate(restrictedCandidatesList);
			s.add(solution);
			candidatesList.remove(solution);

			this.saveSelectedCandidate(solution, facsSelectedCL);
			this.removeCandidatesBasedSelectedCand(facsSelectedCL, candidatesList);
			this.updateCandidates(facsSelectedCL.get(facsSelectedCL.size()-1), candidatesList);
			
			minimum = 100000.0;
			maximum = 0;
		}
		
		s.forEach(sol -> sol.getFacilities().forEach(Facility::deleteAllClients));

		for(int i=0; i<facsSelectedCL.size(); i++) {
			result.getFacilities().add(facsSelectedCL.get(i));
		}
		result.calculateSolution(instance, instance.getClientsSortedDescByWeight());

		return result;
	}
	
	public Solution selectRandomCandidate(ArrayList<Solution> candidates) {
		Random random = new Random();
		int index = random.nextInt(candidates.size());
		Solution solution = candidates.get(index);
		return solution;
	}
	
	public Solution calculateGrasp(Instance instance, int distancePercentage) {
		long startTime = System.nanoTime();
		Solution graspSolution = this.solve(instance, distancePercentage);
		long endTime = System.nanoTime();
		long time = endTime - startTime;
		double executionTime = (double) time / 1e6; // ns -> ms
		//System.out.println("Dentro GRASP execution: " + executionTime);
		double constructionTime = graspSolution.getTime();
		//System.out.println("Dentro GRASP construction: " + constructionTime);
		graspSolution.setTime(executionTime + constructionTime);

		return graspSolution;
	}
	
	public void updateCandidates(Facility facility, ArrayList<Solution> candidates) {
		int pointOrigin = facility.getOriginPoint();
		for(int i=0; i<candidates.size(); i++) {
			for(int j=0; j<candidates.get(i).getFacilities().size(); j++) {
				if(candidates.get(i).getFacilities().get(j).getCurrentPoint() == pointOrigin 
						&& candidates.get(i).getFacilities().get(j).getOriginPoint() == 0) {
					candidates.get(i).getFacilities().set(j, facility);
				}
			}
		}
	}
	
	public void saveSelectedCandidate(Solution solution, ArrayList<Facility> selectedCand) {
		Iterator<Facility> itSolution = solution.getFacilities().iterator();
		while(itSolution.hasNext()) {
			Facility facility = itSolution.next();
			if(facility.getOriginPoint() == 0) {
				itSolution.remove();
			} else if (!selectedCand.contains(facility)) {
				selectedCand.add(facility);
			} 
		}
	}
	
	// remove candidates based on the selected candidate
	public void removeCandidatesBasedSelectedCand(ArrayList<Facility> selectedCand, ArrayList<Solution> candidates) {
		Facility fac = selectedCand.get(selectedCand.size() - 1);
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
