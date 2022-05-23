import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
	
	protected final static String ROUTE1 = "instance\\homogeneous_facilities\\large_C_over_F";
	protected final static String ROUTE2 = "instance\\homogeneous_facilities\\small_C_over_F";
	protected final static int NUMBER_RANDOM = 100;
	protected final static int NUMBER_GRASP = 100;
	protected final static int PERCENTAGE_CLOSEST_GRASP = 20;
	protected final static String OUTPUT_NAME_FILE = "salida-grlsts-test.csv";
	
	public void createCSVFile(){
		String fields = "F.O." + ';' + "Tiempo (s)" + ';' + "Dev (%)" + ';' + "#Best" + ';';
		FileWriter w = null;
		PrintWriter pw = null;
		try {
			w = new FileWriter(OUTPUT_NAME_FILE);
			pw = new PrintWriter(w);
			pw.println(';' + fields);
			pw.println("Constructivo");
			pw.println("B�squeda local");
			pw.println();
			pw.println();
			pw.println("" + ';' + "Constructivo" + ';'+ ';'+ ';'+ ';' + "B�squeda Local" + ';'+ ';'+ ';'+ ';' + "B�squeda Tabu");
			pw.print("Nombre de la instancia" + ";" + fields + fields + fields);
			pw.println("" + ';' + "Best");
			pw.flush();
		} catch (FileNotFoundException ex) {
			System.err.println("El fichero no se puede crear");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al escribir en fichero");
		} finally {
	        try {
	            if (w != null)
	                w.close();
	            if (pw != null)
	                pw.close();
	        } catch (IOException ex) {
	        	System.err.println("ERROR al cerrar el fichero");
	            ex.printStackTrace();
	        }
	    }
	}
	
	public void addDataToCSVFile(String name, Solution solution, Solution localSearchSol) {
		FileWriter w = null;
		PrintWriter pw = null;
		Solution bestSolution = solution.whichIsBetter(localSearchSol);
		try {
			w = new FileWriter(OUTPUT_NAME_FILE, true);
			pw = new PrintWriter(w);
			pw.print(name);
			pw.print(";");
			this.printMetrics(pw, solution, bestSolution);
			this.printMetrics(pw, localSearchSol, bestSolution);
			pw.print("");
			pw.print(";");
			pw.printf("%.5f", bestSolution.getTotalSum());
			pw.println();
			pw.flush();
		} catch (FileNotFoundException ex) {
			System.err.println("El fichero no se puede editar");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al escribir en fichero");
		} finally {
	        try {
	            if (w != null)
	                w.close();
	            if (pw != null)
	                pw.close();
	        } catch (IOException ex2) {
	        	System.err.println("ERROR al cerrar el fichero");
	            ex2.printStackTrace();
	        }
	    }
	}
	
	public void addDataToCSVFileOneSolution(String name, Solution solution) {
		FileWriter w = null;
		PrintWriter pw = null;
		try {
			w = new FileWriter(OUTPUT_NAME_FILE, true);
			pw = new PrintWriter(w);
			pw.print(name);
			pw.print(";");
			pw.printf("%.5f", solution.getTotalSum());
			pw.print(";");
			pw.printf("%.7f", solution.getTime()/1000); //ms->s
			pw.print(";");
			pw.print("");
			pw.print(";");
			pw.println();
			pw.flush();
		} catch (FileNotFoundException ex) {
			System.err.println("El fichero no se puede editar");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al escribir en fichero");
		} finally {
	        try {
	            if (w != null)
	                w.close();
	            if (pw != null)
	                pw.close();
	        } catch (IOException ex2) {
	        	System.err.println("ERROR al cerrar el fichero");
	            ex2.printStackTrace();
	        }
	    }
	}
	
	public void addDataToCSVFileThreeSolutions(String name, Solution solution, Solution localSearchSol, Solution tabuSearchSol) {
		FileWriter w = null;
		PrintWriter pw = null;
		Solution bestAuxSolution = solution.whichIsBetter(localSearchSol);
		Solution bestSolution = tabuSearchSol.whichIsBetter(bestAuxSolution);
		try {
			w = new FileWriter(OUTPUT_NAME_FILE, true);
			pw = new PrintWriter(w);
			pw.print(name);
			pw.print(";");
			this.printMetrics(pw, solution, bestSolution);
			this.printMetrics(pw, localSearchSol, bestSolution);
			this.printMetrics(pw, tabuSearchSol, bestSolution);
			pw.print("");
			pw.print(";");
			pw.printf("%.5f", bestSolution.getTotalSum());
			pw.println();
			pw.flush();
		} catch (FileNotFoundException ex) {
			System.err.println("El fichero no se puede editar");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al escribir en fichero");
		} finally {
	        try {
	            if (w != null)
	                w.close();
	            if (pw != null)
	                pw.close();
	        } catch (IOException ex2) {
	        	System.err.println("ERROR al cerrar el fichero");
	            ex2.printStackTrace();
	        }
	    }
	}
	
	public void printMetrics(PrintWriter pw, Solution solution, Solution bestSolution) {
		pw.printf("%.5f", solution.getTotalSum());
		pw.print(";");
		pw.printf("%.7f", solution.getTime()/1000); //ms->s
		pw.print(";");
		pw.printf("%.5f", solution.calculateDeviationFromTheBestSol(bestSolution));
		pw.print(";");
		pw.print(solution.isTheBest(bestSolution));
		pw.print(";");
	}
	
	public List<Path> listSourceFiles(String dir) throws IOException{
		List<Path> result = new ArrayList<Path>();
		try {
			DirectoryStream<Path> dirStream = Files.newDirectoryStream(Paths.get(dir), "*.{txt}");
			for (Path entry: dirStream) {
				result.add(entry);
			}
			//dirStream.forEach(result::add);
		} catch (DirectoryIteratorException e) {
			e.printStackTrace();
			throw e.getCause();
		}
		return result;
	}
	
	public Solution theBestSolutionOfAll(ArrayList<Solution> solutions) {
		/*
		double minimum = 1000000;
		Solution bestSolution = null;
		for(int i=0; i<solutions.size(); i++) {
			if(solutions.get(i).getTotalSum() < minimum) {
				minimum = solutions.get(i).getTotalSum();
				bestSolution = solutions.get(i);
			}
		}
		/*
		for(int i=0; i<solutions.size(); i++) {
			if(solutions.get(i).getTotalSum() == bestSolution.getTotalSum()) {
				System.out.println("RGBT " + i + "--" + bestSolution.getTotalSum());
			}
		}
		*/
		double minium = solutions.stream().mapToDouble(Solution::getTotalSum).min().getAsDouble();
		System.out.println("Minimo Ran: " + minium);
		Solution bestSolution = solutions.stream().filter(solu -> solu.getTotalSum() == minium)
				.collect(Collectors.toList()).get(0);
		//System.out.println("Min: " + bestSolution.getTotalSum());
		
		return bestSolution;
	}
	
	public double totalTimeOfAllSolutions(ArrayList<Solution> solutions) {
		return solutions.stream()
				.mapToDouble(Solution::getTime)
				.sum();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		Instance instance1 = new Instance();
		//instance1.readFile("fichero1.txt");
		instance1.readFile("pmed1v3.10.2.txt");
		System.out.println("V: " + instance1.getV());
		System.out.println("D: " + instance1.getD().length);
		System.out.println("W: " + instance1.getW().length);
		System.out.println("qCAP: " + instance1.getqCapacity().length);
		System.out.println("U: " + instance1.getU().length);
		System.out.println("q: " + instance1.getQ().length);
		
		ArrayList<Client> clientes = instance1.getClientsSortedDescByWeight();
		
		Solution initialSolution = new Solution(instance1);
		initialSolution.calculateSolution(instance1, clientes);
		
		clientes.forEach(client -> System.out.print(client.getPoint() + " "));
		System.out.println();
		
		System.out.println("Lista de facilities:");
		System.out.println(initialSolution.getFacilities());
		System.out.println(clientes);
		
		System.out.println("Despues de asignar: ");
		for (Facility facility : initialSolution.getFacilities()) {
			System.out.print(facility.getCurrentPoint() + "->" );
			for (Client client : facility.getClients()) {
				System.out.print(client.getPoint() + " ");
			}
			System.out.println();
		}
		for(Client client : clientes) {
			System.out.println(client.getPoint() + "->" + client.getFacility());
		}
		
		System.out.format("Total Sumatorio: %.5f ", initialSolution.getTotalSum());
		System.out.println();
		
		System.out.println("Array solution: " + initialSolution.getFacilities());
		/*
		LocalSearch ls = new LocalSearch();
		Solution localSolution = ls.calculateLocalSearch(instance1, initialSolution);
		System.out.println("B�sq Local: " + localSolution.getTotalSum());
		System.out.println("Tiempo B�sq Local: " + localSolution.getTime());
		*/
		/*
		TabuSearch tabu = new TabuSearch();
		tabu.solve(instance1, initialSolution);
		Solution tabuSolution = tabu.calculateTabuSearch(instance1, initialSolution);
		System.out.println("B�sq Tabu: " + tabuSolution.getTotalSum());
		System.out.println("Tiempo B�sq Tabu: " + tabuSolution.getTime());
		//Grasp grasp = new Grasp();
		//grasp.solve(instance1);
		//grasp.calculateGrasp(instance1, PERCENTAGE_CLOSEST_GRASP);

		/*
		Solution randSolution = new Solution(instance1);

		ArrayList<Client> clientes1 = instance1.getClientsSortedDescByWeight();
		randSolution.addRandomFacilitiesToOriginal(instance1);
		randSolution.calculateSolution(instance1, clientes1);
		System.out.println("Facilities random asignadas a las originales :");
		System.out.println(randSolution.getFacilities());
		System.out.println(clientes1);
		
		System.out.println("Sumatorio Solucion random: " + randSolution.getTotalSum());
		System.out.println("Tiempo ejecuci�n random: " + randSolution.getTime());
		*/
		
		
		Utils u = new Utils();
		List<Path> filePaths1 = new ArrayList<Path>();
		List<Path> filePaths2 = new ArrayList<Path>();
		try {
			filePaths1 = u.listSourceFiles(ROUTE1);
			filePaths2 = u.listSourceFiles(ROUTE2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		filePaths1.forEach(x -> System.out.println(x.toString()));
		System.out.println("--");
		filePaths2.forEach(x -> System.out.println(x.toString()));
		
		List<Path> filePaths = Stream.concat(filePaths1.stream(), filePaths2.stream()).collect(Collectors.toList());

		u.createCSVFile();
		
		ArrayList<Instance> instances = new ArrayList<>(); 
		for(int i=0; i<filePaths.size(); i++) { 
			Instance ins = new Instance(); 
			instances.add(ins); 
		} 
		/*
		ArrayList<Solution> solutions = new ArrayList<>();
		
		for(int i=0; i<instances.size(); i++) {
			Instance instance = instances.get(i);
			String pathFile = filePaths.get(i).toString();
			instance.readFile(pathFile);
			ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
			
			Solution solution = new Solution(instance);
			solution.calculateSolution(instance, clients);
			
			System.out.println("Tiempo ejecuci�n milisegundos: " + solution.getTime());
			System.out.println("Sumatorio: " + solution.getTotalSum());
			
			String nameFile = filePaths.get(i).getFileName().toString();
			
			solutions.add(solution);
			
			LocalSearch localSearch = new LocalSearch();
			Solution lsSolution = localSearch.calculateLocalSearch(instance, solution);
			
			TabuSearch tabuSearch = new TabuSearch();
			Solution tabuSolution = tabuSearch.calculateTabuSearch(instance, solution);	
			//u.addDataToCSVFile(nameFile, solution, tabuSolution);
			
			u.addDataToCSVFileThreeSolutions(nameFile, solution, lsSolution, tabuSolution);
			
		}
		
		for (int i = 0; i < instances.size(); i++) {
			Instance instance = instances.get(i);
			String pathFile = filePaths.get(i).toString();
			instance.readFile(pathFile);
			String nameFile = filePaths.get(i).getFileName().toString();
			
			ArrayList<Solution> randSolutions = new ArrayList<>();
			ArrayList<Solution> randLocalSearchSols = new ArrayList<>();
			ArrayList<Solution> randTabuSearchSols = new ArrayList<>();
			for (int j = 0; j < NUMBER_RANDOM; j++) {
				ArrayList<Client> clients1 = instance.getClientsSortedDescByWeight();
				Solution randomSolution = new Solution(instance);
				randomSolution.addRandomFacilitiesToOriginal(instance);
				randomSolution.calculateSolution(instance, clients1);

				randSolutions.add(randomSolution);

				LocalSearch localSearchRand = new LocalSearch();
				Solution lsRandSolution = localSearchRand.calculateLocalSearch(instance, randomSolution);
				randLocalSearchSols.add(lsRandSolution);

				TabuSearch tabuSearchRand = new TabuSearch();
				Solution tabusRandSol = tabuSearchRand.calculateTabuSearch(instance, randomSolution);
				randTabuSearchSols.add(tabusRandSol);

			}
			Solution bestRandSolution = u.theBestSolutionOfAll(randSolutions);
			double totalTimeRand = u.totalTimeOfAllSolutions(randSolutions);
			bestRandSolution.setTime(u.totalTimeOfAllSolutions(randSolutions));

			int indexBestRandSol = randSolutions.indexOf(bestRandSolution);
			System.out.println("Indice de la mejor solucion ALEATORIA " + indexBestRandSol);

			Solution bestLsRandSol = randLocalSearchSols.get(indexBestRandSol);
			bestLsRandSol.setTime(u.totalTimeOfAllSolutions(randLocalSearchSols));

			Solution bestTabuRandSol = randTabuSearchSols.get(indexBestRandSol);
			bestTabuRandSol.setTime(u.totalTimeOfAllSolutions(randTabuSearchSols));

			String ranNameFile = "Ran" + indexBestRandSol + nameFile;

			// u.addDataToCSVFile(ranNameFile, bestRandSolution, bestLsRandSol);
			u.addDataToCSVFileThreeSolutions(ranNameFile, bestRandSolution, bestLsRandSol, bestTabuRandSol);

		}
		*/
		Random random = new Random();
		double alpha = random.nextDouble();
		System.out.println("Valor de ALFA: " + alpha);
		
		for (int i = 0; i < instances.size(); i++) {
			Instance instance = instances.get(i);
			String pathFile = filePaths.get(i).toString();
			instance.readFile(pathFile);
			String nameFile = filePaths.get(i).getFileName().toString();

			ArrayList<Solution> graspSolutions = new ArrayList<>();
			ArrayList<Solution> graspLocalSols = new ArrayList<>();
			ArrayList<Solution> graspTabuSols = new ArrayList<>();
			
			for (int k = 0; k < NUMBER_GRASP; k++) {
				Grasp grasp = new Grasp(alpha);
				Solution graspSolution = grasp.calculateGrasp(instance, PERCENTAGE_CLOSEST_GRASP);

				graspSolutions.add(graspSolution);

				LocalSearch localSearchGrasp = new LocalSearch();
				Solution lsGraspSolution = localSearchGrasp.calculateLocalSearch(instance, graspSolution);
				graspLocalSols.add(lsGraspSolution);

				TabuSearch tabuSearchGrasp = new TabuSearch();
				Solution graspTabuSolution = tabuSearchGrasp.calculateTabuSearch(instance, graspSolution);
				graspTabuSols.add(graspTabuSolution);
			}
			Solution bestGraspSolution = u.theBestSolutionOfAll(graspSolutions);
			bestGraspSolution.setTime(u.totalTimeOfAllSolutions(graspSolutions));

			int indexBestGraspSol = graspSolutions.indexOf(bestGraspSolution);
			System.out.println("Indice de la mejor solucion GRASP " + indexBestGraspSol);

			Solution bestLsGraspSol = graspLocalSols.get(indexBestGraspSol);
			bestLsGraspSol.setTime(u.totalTimeOfAllSolutions(graspLocalSols));

			Solution bestTabuGraspSol = graspTabuSols.get(indexBestGraspSol);
			bestTabuGraspSol.setTime(u.totalTimeOfAllSolutions(graspTabuSols));

			String graspNameFile = "Grasp" + indexBestGraspSol + nameFile;

			// u.addDataToCSVFile(graspNameFile, bestGraspSolution, bestLsGraspSol);
			u.addDataToCSVFileThreeSolutions(graspNameFile, bestGraspSolution, bestLsGraspSol, bestTabuGraspSol);
		}
			/*
			for(int l=0; l<randSolutions.size(); l++) {
				Solution randSol = randSolutions.get(l);
				String ranNameFile = "RanTS" + l + nameFile;
				TabuSearch tabuSearchRand = new TabuSearch();
				Solution tabusRandSol = tabuSearchRand.calculateTabuSearch(instance, randSol);
				u.addDataToCSVFile(ranNameFile, randSol, tabusRandSol);
			}
			/*
			for(int m=0; m<graspSolutions.size(); m++) {
				Solution graspSol = graspSolutions.get(m);
				String graspNameFile = "GraspTS" + m + nameFile;
				TabuSearch tabuSearchGrasp = new TabuSearch();
				Solution tabuGraspSol = tabuSearchGrasp.calculateTabuSearch(instance, graspSol);
				
				u.addDataToCSVFile(graspNameFile, graspSol, tabuGraspSol);
			}
			*/
	}
}
