import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

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
			bw.write("Nombre de la instancia" + ";" + "Valor función objetivo" + ';' + "Tiempo de ejecución (ms)");
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
	
	public void addDataToCSVFile(String name, float evaluationValue, long executionTime) {
		FileWriter w = null;
		PrintWriter pw = null;
		try {
			w = new FileWriter(OUTPUT_NAME_FILE, true);
			pw = new PrintWriter(w);
			pw.print(name);
			pw.print(";");
			pw.printf("%.5f", evaluationValue);
			pw.print(";");
			Double time = (double)(executionTime/1e6);
			pw.printf("%.5f", time);
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Instance instance = new Instance();
		instance.readFile("fichero1.txt");
		System.out.println("V: " + instance.getV());
		System.out.println("D: " + instance.getD().length);
		System.out.println("W: " + instance.getW().length);
		System.out.println("qCAP: " + instance.getqCapacity().length);
		System.out.println("U: " + instance.getU().length);
		System.out.println("q: " + instance.getQ().length);
		
		Solution solution = new Solution();
		
		ArrayList<Facility> facilities = instance.getFacilities();
		ArrayList<Client> clientes = instance.getClientsSortedDescByWeight();
		
		long startTime = System.nanoTime();
		long startMil = System.currentTimeMillis();
		solution.assignClientsOrdered(instance, facilities,clientes);
		long endTime = System.nanoTime();
		long endMil = System.currentTimeMillis();
		long time = endTime - startTime;
		long mil = endMil - startMil;
		System.out.println("Tiempo ejecución nanosegundos: " + time/1e6);
		System.out.println("Tiempo ejecución milisegundos: " + mil);
		
		Iterator<Client> iteratorClientsOrd = clientes.iterator();
		while(iteratorClientsOrd.hasNext()) {
			System.out.print(iteratorClientsOrd.next().getPoint() + " ");
		}
		System.out.println();

		
		System.out.println("Lista de facilities");
		System.out.println(facilities);
		
		System.out.println(clientes);
		
		System.out.println("Despues de asignar: ");
		for (Facility facility : facilities) {
			System.out.print(facility.getCurrentPoint() + "->" );
			for (Client client : facility.getClients()) {
				System.out.print(client.getPoint() + " ");
			}
			System.out.println();
		}
		
		for(Client client : clientes) {
			System.out.println(client.getPoint() + "->" + client.getFacility());
		}
		
		double summation = solution.evaluateTheSolution(instance, facilities, clientes);
		System.out.format("Total Sumatorio: %.5f ", summation);
		System.out.println();
		

		/*
		ArrayList<Integer> randomFacPoints = solution.generateFacilitiesRandom(instance, facilities);
		System.out.println("--SALIDA ARRAY RANDOM FAC---");
		for(Integer i : randomFacPoints) {
			System.out.println(i);
		}

		solution.addRandomFacilitiesToOriginal(randomFacPoints, facilities);
		System.out.println("Facilities random asignadas a las originales :");
		System.out.println(facilities);
		System.out.println(clientes);
		
		
		System.out.println("-----------EVALUACION RANDOM FAC--------------");
		long startTimeRan = System.nanoTime();
		solution.assignClientsOrdered(instance, facilities, clientes);
		long endTimeRan = System.nanoTime();
		long timeRan = endTimeRan - startTimeRan;

		double summationRandom = solution.evaluateTheSolution(instance, facilities, clientes);
		System.out.println("Sumatorio Solucion random: " + summationRandom);
		System.out.println("Tiempo ejecución random: " + timeRan/1e6);

		System.out.println(clientes);
		*/
		//instance.addDataToCSVFile("Random", summationRandom, timeRan);
		
				
		
		/*
		instance.createCSVFile();
		instance.addDataToCSVFile("fichero1.txt", summation, time);
		*/
		
		/*
		try {		
			/*
			Stream<Path> walk = Files.walk(Paths.get("C:\\Users\\Dani\\Documents\\Eclipse\\CMFLP\\instance\\homo"));
			walk.filter(file ->!Files.isDirectory(file))
			.map(Path::getFileName)
			.forEach(System.out::println);
			*/
			/*
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(ROUTE1), String.format("*.%s", "txt"));
			ArrayList<Path> archivos = new ArrayList<>();
			stream.forEach(archivos::add);
			archivos.forEach(x -> System.out.println(x.getFileName()));

			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*
		ArrayList<Path> fiche = new ArrayList<>();
        try {
			Files.list(Paths.get("")).filter(Files::isRegularFile).forEach(x -> fiche.add(x.getFileName()));
			DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(""));
			for (Path p: stream) {
				if(p.toFile().isDirectory()) {
					System.out.println("DIR: " + p.getFileName());			
				}else {
					System.out.println("FICH: " + p.getFileName());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        System.out.println("----");
        System.out.println(fiche); 
        */
	}

}
