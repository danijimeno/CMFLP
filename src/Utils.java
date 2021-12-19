import java.io.BufferedWriter;
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
	
	protected final static String ROUTE1 = "instance\\homogeneous_facilities\\large_C_over_F";
	protected final static String ROUTE2 = "instance\\homogeneous_facilities\\small_C_over_F";
	protected final static String OUTPUT_NAME_FILE = "salida.csv";
	
	public void createCSVFile(){
		FileWriter w = null;
		BufferedWriter bw = null;
		try {
			w = new FileWriter(OUTPUT_NAME_FILE);
			bw = new BufferedWriter(w);
			bw.write("Nombre de la instancia" + ";" + "Valor funci�n objetivo" + ';' + "Tiempo de ejecuci�n (ms)");
			bw.newLine();
		} catch (FileNotFoundException ex) {
			System.err.println("El fichero no se puede crear");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error al escribir en fichero");
		} finally {
	        try {
	            if (bw != null)
	                bw.close();
	            if (w != null)
	                w.close();
	        } catch (IOException ex) {
	        	System.err.println("ERROR al cerrar el fichero");
	            ex.printStackTrace();
	        }
	    }
	}
	
	public void addDataToCSVFile(String name, Solution solution) {
		FileWriter w = null;
		PrintWriter pw = null;
		try {
			w = new FileWriter(OUTPUT_NAME_FILE, true);
			pw = new PrintWriter(w);
			pw.print(name);
			pw.print(";");
			pw.printf("%.5f", solution.getTotalSum());
			pw.print(";");
			pw.printf("%.5f", solution.getTime());
			pw.println();
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
	        } catch (IOException ex2) {
	        	System.err.println("ERROR al cerrar el fichero");
	            ex2.printStackTrace();
	        }
	    }
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
		/*
		Instance instance1 = new Instance();
		instance1.readFile("fichero1.txt");
		System.out.println("V: " + instance1.getV());
		System.out.println("D: " + instance1.getD().length);
		System.out.println("W: " + instance1.getW().length);
		System.out.println("qCAP: " + instance1.getqCapacity().length);
		System.out.println("U: " + instance1.getU().length);
		System.out.println("q: " + instance1.getQ().length);
		
		
		ArrayList<Facility> facilities = instance1.getFacilities();
		ArrayList<Client> clientes = instance1.getClientsSortedDescByWeight();
		
		Solution initialSolution = new Solution(instance1);
		
		initialSolution.calculateSolution(instance1, clientes);
		
		Iterator<Client> iteratorClientsOrd = clientes.iterator();
		while(iteratorClientsOrd.hasNext()) {
			System.out.print(iteratorClientsOrd.next().getPoint() + " ");
		}
		System.out.println();
		
		
		System.out.println("Lista de facilities");
		System.out.println(facilities);
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
		
		//double summation = initialSolution.evaluateTheSolution(instance1, clientes);
		System.out.format("Total Sumatorio: %.5f ", initialSolution.getTotalSum());
		System.out.println();
		
		
		System.out.println("Array solution: " + initialSolution.getFacilities());
		
		LocalSearch ls = new LocalSearch();
		ls.solve(instance1, initialSolution);
		
		
		//initialSolution.getFacilities().get(0).getClients().get(1).deleteFacility();
		
		/*
		ArrayList<Integer> randomFacPoints = initialSolution.generateFacilitiesRandom(instance1, facilities);
		System.out.println("--SALIDA ARRAY RANDOM FAC---");
		for(Integer i : randomFacPoints) {
			System.out.println(i);
		}*/
		/*
		Solution randSolution = new Solution(instance1);

		randSolution.addRandomFacilitiesToOriginal(instance1);
		System.out.println("Facilities random asignadas a las originales :");
		System.out.println(randSolution.getFacilities());
		System.out.println(clientes);
		
		
		System.out.println("-----------EVALUACION RANDOM FAC--------------");
		long startTimeRan = System.nanoTime();
		randSolution.assignClients(instance1, clientes);
		long endTimeRan = System.nanoTime();
		long timeRan = endTimeRan - startTimeRan;

		double summationRandom = randSolution.evaluateTheSolution(instance1, clientes);
		System.out.println("Sumatorio Solucion random: " + summationRandom);
		System.out.println("Tiempo ejecuci�n random: " + timeRan/1e6);

		System.out.println(clientes);
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
		
		ArrayList<Instance> instances = new ArrayList<>();
		for(int i=0; i<filePaths.size(); i++) {
			Instance ins = new Instance();
			instances.add(ins);
		}
		System.out.println("Instancias: " + instances.size());
		
		u.createCSVFile();
		
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
			u.addDataToCSVFile(nameFile, solution);
			
			LocalSearch local = new LocalSearch();
			Solution localSol = local.solve(instance, solution);
			System.out.println("B�squeda local: " + localSol.getTotalSum());
			/*
			for(int j=0; j<100; j++) {
				//ArrayList<Client> cli = instance.getClientsSortedDescByWeight();
				Solution randomSolution = new Solution(instance);
				randomSolution.addRandomFacilitiesToOriginal(instance);
				randomSolution.calculateSolution(instance, clients);
				
				System.out.println("Tiempo ejecuci�n nanosegundos: " + randomSolution.getTime());
				System.out.println("Sumatorio: " + randomSolution.getTotalSum());
				
				String ranNameFile = "Ran" + j + nameFile;
				u.addDataToCSVFile(ranNameFile, randomSolution);
			}*/
		}
		
	}
}
