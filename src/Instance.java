import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;


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
	
	public ArrayList<Client> getClientsSortedDescByWeight(){
		ArrayList<Client> clients = new ArrayList<Client>();
		for(int i=0; i<this.u.length; i++) {
			if((this.u[i] > 0) && (this.q[i] > 0)) {
				clients.add(new Client(this.u[i], this.q[i], i+1));
			}
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

}
