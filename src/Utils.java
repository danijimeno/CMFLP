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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
	
	protected final static String ROUTE1 = "instance\\homogeneous_facilities\\large_C_over_F";
	protected final static String ROUTE2 = "instance\\homogeneous_facilities\\small_C_over_F";
	protected final static String OUTPUT_NAME_FILE = "salida-grasp.csv";
	protected final static int NUMBER_RANDOM = 100;
	protected final static int NUMBER_GRASP = 100;
	
	public void createCSVFile(){
		String fields = "F.O." + ';' + "Tiempo (s)" + ';' + "Dev (%)" + ';' + "#Best";
		FileWriter w = null;
		PrintWriter pw = null;
		try {
			w = new FileWriter(OUTPUT_NAME_FILE);
			pw = new PrintWriter(w);
			pw.println(';' + fields);
			pw.println("Constructivo");
			pw.println("Búsqueda local");
			pw.println();
			pw.println();
			pw.println("" + ';' + "Constructivo" + ';'+ ';'+ ';'+ ';' + "Búsqueda Local");
			pw.print("Nombre de la instancia" + ";" + fields + ';' + fields + ';');
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Instance instance1 = new Instance();
		instance1.readFile("fichero1.txt");
		//instance1.readFile("pmed1v3.10.2.txt");
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
		System.out.println("Búsq Local: " + localSolution.getTotalSum());
		System.out.println("Tiempo Búsq Local: " + localSolution.getTime());
		*/
		Grasp grasp = new Grasp();
		grasp.solve(instance1);
		//grasp.calculateGrasp(instance1);

		/*
		Solution randSolution = new Solution(instance1);

		ArrayList<Client> clientes1 = instance1.getClientsSortedDescByWeight();
		randSolution.addRandomFacilitiesToOriginal(instance1);
		randSolution.calculateSolution(instance1, clientes1);
		System.out.println("Facilities random asignadas a las originales :");
		System.out.println(randSolution.getFacilities());
		System.out.println(clientes1);
		
		System.out.println("Sumatorio Solucion random: " + randSolution.getTotalSum());
		System.out.println("Tiempo ejecución random: " + randSolution.getTime());
		*/
		
		/*
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
		
		for(int i=0; i<filePaths.size(); i++) {
			Instance instance = new Instance();
			String pathFile = filePaths.get(i).toString();
			instance.readFile(pathFile);
			ArrayList<Client> clients = instance.getClientsSortedDescByWeight();
			
			Solution solution = new Solution(instance);
			solution.calculateSolution(instance, clients);
			
			System.out.println("Tiempo ejecución milisegundos: " + solution.getTime());
			System.out.println("Sumatorio: " + solution.getTotalSum());
			
			String nameFile = filePaths.get(i).getFileName().toString();

			//LocalSearch localSearch = new LocalSearch();
			//Solution lsSolution = localSearch.calculateLocalSearch(instance, solution);
			//System.out.println("Búsqueda local: " + lsSolution.getTotalSum());
			
			//u.addDataToCSVFile(nameFile, solution, null);
			u.addDataToCSVFileOneSolution(nameFile, solution);
			
			for(int j=0; j<NUMBER_RANDOM; j++) {
				ArrayList<Client> clients1 = instance.getClientsSortedDescByWeight();
				Solution randomSolution = new Solution(instance);
				randomSolution.addRandomFacilitiesToOriginal(instance);
				randomSolution.calculateSolution(instance, clients1);
				
				//LocalSearch localSearchRand = new LocalSearch();
				//Solution lsRandSolution = localSearchRand.solve(instance, randomSolution);
				
				System.out.println("Tiempo ejecución milisegundos: " + randomSolution.getTime());
				System.out.println("Sumatorio: " + randomSolution.getTotalSum());
				
				String ranNameFile = "Ran" + j + nameFile;
				//u.addDataToCSVFile(ranNameFile, randomSolution, lsRandSolution);
				u.addDataToCSVFileOneSolution(ranNameFile, randomSolution);
				
			}
			for(int k=0; k<NUMBER_GRASP; k++) {
				ArrayList<Client> clients1 = instance.getClientsSortedDescByWeight();
				Grasp grasp = new Grasp();
				Solution graspSolution = grasp.calculateGrasp(instance);
				
				String graspNameFile = "Grasp" + k + nameFile;
				u.addDataToCSVFileOneSolution(graspNameFile, graspSolution);
			}
		}*/
		
	}
}
