import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;


public class Instance {
	
	private int v; 		//number of vertices
	private int [][] d;	//the distance between i and j
	private int [] w; 	//is the facility locations and weights
	private int [] qCapacity; //is the facility locations and capacities
	private float [] u; 	//is the client locations and weights
	private int [] q; 	//is the client locations and demands
	
	public Instance() {
		this.v = 0;
		this.d = null;
		this.w = null;
		this.qCapacity = null;
		this.u = null;
		this.q = null;
	}

	public int getV() {
		return v;
	}
	public int[][] getD() {
		return d;
	}
	public int[] getW() {
		return w;
	}
	public int[] getqCapacity() {
		return qCapacity;
	}
	public float[] getU() {
		return u;
	}
	public int[] getQ() {
		return q;
	}
	public void setV(int v) {
		this.v = v;
	}
	public void setD(int[][] d) {
		this.d = d;
	}
	public void setW(int[] w) {
		this.w = w;
	}
	public void setqCapacity(int[] qCapacity) {
		this.qCapacity = qCapacity;
	}
	public void setU(float[] u) {
		this.u = u;
	}
	public void setQ(int[] q) {
		this.q = q;
	}
	
	public void readFile(String name) {
		try {
			File file = new File(name);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			String line = null;
			String [] lineAux = null;
			line = br.readLine();
			this.setV(Integer.parseInt(line));
			
			this.d = new int [this.v][this.v];
			this.w = new int [this.v];
			this.qCapacity = new int [this.v];
			this.u = new float [this.v];
			this.q = new int [this.v];
			
			line = br.readLine(); //line Empty
			for(int i=0; i<this.v; i++) {
				line = br.readLine();
				lineAux = line.split("\t");
				for(int j=0; j<lineAux.length; j++) {
					this.d[i][j] = Integer.parseInt(lineAux[j]);
				}
			}
			line = br.readLine(); //line Empty
			line = br.readLine();
			lineAux = line.split("\t");
			for(int i=0; i<lineAux.length; i++) {
				this.w[i] = Integer.parseInt(lineAux[i]);
			}
			line = br.readLine(); //line Empty
			line = br.readLine();
			lineAux = line.split("\t");
			for(int j=0; j<lineAux.length; j++) {
				this.qCapacity[j] = Integer.parseInt(lineAux[j]);
			}
			line = br.readLine(); //line Empty
			line = br.readLine();
			lineAux = line.split("\t");
			for(int i=0; i<lineAux.length; i++) {
				this.u[i] = Float.parseFloat(lineAux[i]);
			}
			line = br.readLine(); //line Empty
			line = br.readLine();
			lineAux = line.split("\t");
			for(int j=0; j<lineAux.length; j++) {
				this.q[j] = Integer.parseInt(lineAux[j]);
			}
			
			fr.close();
			br.close();
		}catch (Exception e) {
			System.out.println("Error al leer fichero");
			e.printStackTrace();
		}
	}
	
	public List<Client> getClientsSortedDescByWeight(){
		List<Client> clients = new ArrayList<Client>();
		for(int i=0; i<this.u.length; i++) {
			clients.add(new Client(this.u[i], this.q[i], i+1));
		}
		Collections.sort(clients, Collections.reverseOrder()); //to sort them in descending order
		return clients;
	}
	
	public ArrayList<Facility> getFacilities(){
		ArrayList<Facility> facilities = new ArrayList<Facility>();
		for(int i=0; i<this.w.length; i++) {
			if((this.w[i] > 0) && (this.qCapacity[i] > 0)){
				facilities.add(new Facility(this.w[i], this.qCapacity[i], i+1));
			}
		}
		return facilities;
	}
	
	public void createCSVFile(){
		FileWriter w = null;
		BufferedWriter bw = null;
		try {
			w = new FileWriter("salida.csv");
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
			w = new FileWriter("salida.csv", true);
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
	        } catch (IOException ex) {
	        	System.err.println("ERROR al cerrar el fichero");
	            ex.printStackTrace();
	        }
	    }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Instance instance = new Instance();
		//instance.readFile("pmed1v3.10.2.txt");
		instance.readFile("fichero1.txt");
		System.out.println("V: " + instance.getV());
		System.out.println("D: " + instance.getD().length);
		System.out.println("W: " + instance.getW().length);
		System.out.println("qCAP: " + instance.getqCapacity().length);
		System.out.println("U: " + instance.getU().length);
		System.out.println("q: " + instance.getQ().length);
		
		Solution solution = new Solution();
		solution.generateFacilities(instance);
		System.out.println("Facilities: " + solution.getPointsFacilities().size());
		Iterator<Integer> iteratorFacilities = solution.getPointsFacilities().iterator();
		while(iteratorFacilities.hasNext()) {
			Integer pointFac = iteratorFacilities.next();
			System.out.print(pointFac + " ");
		}
		System.out.println();
		
		ArrayList<Facility> facilities = instance.getFacilities();
		long startTime = System.nanoTime();
		long startMil = System.currentTimeMillis();
		solution.assignClientsOrdered(instance, facilities);
		long endTime = System.nanoTime();
		long endMil = System.currentTimeMillis();
		long time = endTime - startTime;
		long mil = endMil - startMil;
		System.out.println("Tiempo ejecución nanosegundos: " + time/1e6);
		System.out.println("Tiempo ejecución milisegundos: " + mil);
		System.out.println("Clients: " + solution.getFacilitiesAssignedtoClients().size());
		
		Iterator<Client> iteratorClientsOrd = instance.getClientsSortedDescByWeight().iterator();
		while(iteratorClientsOrd.hasNext()) {
			System.out.print(iteratorClientsOrd.next().getPoint() + " ");
		}
		System.out.println();
		
		Iterator<Integer> iteratorClients = solution.getFacilitiesAssignedtoClients().iterator();
		while(iteratorClients.hasNext()) {
			Integer pointClient = iteratorClients.next();
			System.out.print(pointClient + " ");
		}
		System.out.println();
		
		System.out.println("Nueva manera de guardar las facilities con la clase Facility");
		System.out.println(facilities);
		
		List<Client> clientes = instance.getClientsSortedDescByWeight();
		System.out.println(clientes);
		
		float summation = solution.evaluateTheSolution(instance, facilities);
		
		System.out.println("Sumatorio parte clientes (parte 2): " + summation);
		instance.createCSVFile();
		instance.addDataToCSVFile("fichero1.txt", summation, time);
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
		/*
		ArrayList<Integer> randomFacPoints = solution.generateFacilitiesRandom(instance);
		System.out.println("--SALIDA ARRAY RANDOM FAC---");
		for(Integer i : randomFacPoints) {
			System.out.println(i);
		}

		solution.addRandomFacilitiesToOriginal(randomFacPoints, facilities);
		System.out.println("Facilities random asignadas a las originales :");
		System.out.println(facilities);
		
		System.out.println("-----------EVALUACION RANDOM FAC--------------");
		solution.getFacilitiesAssignedtoClients().clear();
		long startTimeRan = System.nanoTime();
		solution.assignClientsOrdered(instance, solution.getRandomFacilities());
		long endTimeRan = System.nanoTime();
		long timeRan = endTimeRan - startTimeRan;

		Iterator<Integer> iteratorClients1 = solution.getFacilitiesAssignedtoClients().iterator();
		while(iteratorClients1.hasNext()) {
			Integer pointClient = iteratorClients1.next();
			System.out.print(pointClient + " ");
		}
		System.out.println();
		float summationRandom = solution.evaluateTheSolution(instance, solution.getRandomFacilities());
		System.out.println("Sumatorio Solucion random: " + summationRandom);
		System.out.println("Tiempo ejecución random: " + timeRan/1e6);

		System.out.println("Random FAC" + solution.getRandomFacilities());
		
		instance.addDataToCSVFile("Random", summationRandom, timeRan);
		*/
		/*
		solution.changeOriginalFacToRandomOnes(instance, randomFacilities);
		
		for(int i=0; i<instance.getW().length; i++) {
			System.out.print(instance.getW()[i] + " ");
		}
		System.out.println();
		for(int i=0; i<instance.getqCapacity().length; i++) {
			System.out.print(instance.getqCapacity()[i] + " ");
		}
		System.out.println();
		

		solution.getFacilitiesAssignedtoClients().clear();
		solution.getPartialCapacities().clear();
		
		solution.assignClientsOrdered(instance, randomFacilities);
		System.out.println("Clients: " + solution.getFacilitiesAssignedtoClients().size());
		
		Iterator<Client> iteratorClientsOrd1 = instance.getClientsSortedDescByWeight().iterator();
		while(iteratorClientsOrd1.hasNext()) {
			System.out.print(iteratorClientsOrd1.next().getPoint() + " ");
		}
		System.out.println();
		
		Iterator<Integer> iteratorClients1 = solution.getFacilitiesAssignedtoClients().iterator();
		while(iteratorClients1.hasNext()) {
			Integer pointClient = iteratorClients1.next();
			System.out.print(pointClient + " ");
		}
		System.out.println();
		
		System.out.println("Partial Capacities: " + solution.getPartialCapacities().size());
		Iterator<Integer> iteratorPartCap1 = solution.getPartialCapacities().iterator();
		while(iteratorPartCap1.hasNext()) {
			Integer cap = iteratorPartCap1.next();
			System.out.print(cap + " ");
		}
		System.out.println();
		*/
	}

}
