import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;


public class Main {
	
	public interface IGrafo {

		public boolean esVacio();
		
		void borrarArista(int a, int b);

		//Conecta el vetice A con el B
		public  void agregarConexion(int vertexA, int vertexB);
		
		//Agrega peso a la arista que une A con B
		public void setPesoArista(int vertA, int vertB, int peso);
		 
		//Devuelve el peso de la arista que une A con B
		public int getPesoArista(int vertA, int vertB);
		 
		//Devuelve la cantidad de vertices existentes en el grafo
		public int getCantVetices();
		 
		//Devuelve todos los vetices conectados con v
		public List<Integer> getVerticesConectadosA(int v);
		 
		//Devuelve un lista de pares, que representa las aristas del grafo 
		//public List<Pair> getAristas();
			
		
	}
	

	
	public class Dijkstra {
		private Integer pesos[]; //Pesos del vertice raiz a  U
		private List previos[]; //Vertice desde donde se accedio a U
		private final int RAIZ;  //Vertice de partida

		public Dijkstra(IGrafo g , int raiz){
			pesos = new Integer[g.getCantVetices()];
			previos = new ArrayList[g.getCantVetices()];
			RAIZ  = raiz;
		}
		
		public void dijkstra(IGrafo g){
			int r = RAIZ;
			
			//Arreglo booleando para marcar los verices visitados
			boolean visitado[] = new boolean[g.getCantVetices()];
			
			for(int  i=0 ; i < g.getCantVetices() ; i++ ){
				//Todos los pesos son inicilizadas con valor Infinito
				pesos[i]= Integer.MAX_VALUE;
				//Todos los vertices previos son -1 para representar que no tiene conexión con U
				previos[i] = new ArrayList<Integer>();	
				//Aun no visito a nadie, por lo que todos son false.
				visitado[i]=false;
			}
			
			//El peso al vertice de partida es siempre 0.
			pesos[r] = 0;
			
			//Necesita una Cola de Prioridad para elegir el siguiente mas cercano.
			Queue<ParVerticePeso> pq = new PriorityQueue<ParVerticePeso>();
			
			//Agrega el par <Vertice, peso> a la cola 
			pq.add(new ParVerticePeso(r,pesos[r]));
			
			//Mientras que la cola de prioridad no sea vacia
			while(! pq.isEmpty()){
				//Saca el elementos U mas pequeño
				ParVerticePeso pvp = pq.poll();
				int u = pvp.getVertice();
				//Lo marca como visitado
				visitado[u]=true;
				
				//Obtiene los vertices adyacentes (vecinos) de U
				List<Integer> vecinos = g.getVerticesConectadosA(u);
				
				//Revisa todos los vecinos
				for(int v: vecinos){
					//Si no fué visitado
					if(!visitado[v]){
						
						//Relaja: Actualiza el peso de la arist tomando como punto
						//intermedio el vertice actual U
						if( pesos[v] >= pesos[u] + g.getPesoArista(u,v) ){
							
							//Actualizo vertice desde donde se llego a V.
							if(pesos[u] + g.getPesoArista(u,v) == pesos[v]){
								//Si el vertice actual u llega a v con el mismo costo que 
								//otro vertice, entonces lo agrego.
								previos[v].add(u);
							}
							else{
								//Si ahora se llega con mejor costo, limpio los 
								// vertices previos a V, porque solo quiero el mejor
								previos[v].clear();
								previos[v].add(u);
							}
							
							//La distancia es menor, la actualizo
							pesos[v] = pesos[u] + g.getPesoArista(u,v);

							//Coloco este nuevo vertice V en la cola de prioridad
							pq.add(new ParVerticePeso(v,pesos[v]));
						}
					}
					
						
				}
			}
		}
		
		public Integer[] getPesos(){
			return pesos;
		}
		
		public List[] getPrevios(){
			return previos;
		}
		
	}
	
	
	public class ParVerticePeso implements Comparable{
		private int vertice; //
	    private int peso; //

	    public ParVerticePeso(int vert, int peso) {
	        this.vertice = vert;
	        this.peso = peso;
	    }

	    public void setVertice(int first) {
	        this.vertice = first;
	    }

	    public void setPeso(int second) {
	        this.peso = second;
	    }

	    public int getVertice() {
	        return vertice;
	    }

	    public int getPeso() {
	        return peso;
	    }

		@Override
		public int compareTo(Object o) {
			if(o == null) return 1;
			if(!( o instanceof ParVerticePeso)) return 1;
			ParVerticePeso pvp =(ParVerticePeso) o;
			
			return this.peso - pvp.getPeso() ;
		}
	}	
	
	
	public class GrafoConLista implements IGrafo{

		private int iVertices;
	    private List<Integer>[] adyacencia;
	    private int[][] matAdy; //Para los pesos de las aristas
	    //private List<Pair> aristas;   
	    
	    
	    //Inicializa un grafo vacio pasandole el numero de vertices
	    public GrafoConLista(int cantVertices){
	    	
	    	iVertices = cantVertices;
	        adyacencia =  new List[this.iVertices];	//La lista de adyacencia con n nodos
	        matAdy = new int[this.iVertices][this.iVertices]; //El peso de las aristas
	        //aristas = new ArrayList<Pair>();
	     
	        for (int i = 0; i < this.iVertices; i++){ //Inicializa cada elemento de la lista
	        	//Un arrayList de Enteros en cada nodo. 
	            adyacencia[i] = new ArrayList<Integer>();
	            for (int j = 0; j < this.iVertices; j++){
	            	//Inicializo todas las aristas en MAX_VALUE
	            	//necesario pues se conoce aun su peso. 
	            	matAdy[i][j] = Integer.MAX_VALUE; 
	            }
	        }
	    }
		
	    
		@Override
		public boolean esVacio() {
			return this.iVertices==0;
		}

		@Override
		public void agregarConexion(int vertexA, int vertexB) {
		     adyacencia[vertexA].add(vertexB);
		     //Pair<Integer,Integer> arista = new Pair<Integer,Integer>(vertexA, vertexB);
		     //aristas.add(arista);
		}

		@Override
		public void setPesoArista(int vertA, int vertB, int peso) {
			matAdy[vertA][vertB] = peso;
			
		}

		@Override
		public int getPesoArista(int vertA, int vertB) {
	    	return matAdy[vertA][vertB];
		}

		@Override
		public int getCantVetices() {
			return this.iVertices;
		}

		@Override
		public List<Integer> getVerticesConectadosA(int v) {
			return adyacencia[v];
		}

		/*
		 * Deprecated
		@Override
		public List<Pair> getAristas() {
			return aristas;
		}
		*/
		
		@Override
		public void borrarArista(int a , int b){
			if(adyacencia[a].size() > 0){

				adyacencia[a].remove(new Integer(b));
				// OBS:  si se hace  adyacencia[a].remove(b) remueve la posicion, no el elemento
				// cuyo valor es b. Por eso crea un objeto Integer con el valor de b.
			}
			/*
			 * Deprecated: Remover de un arrayList es muy costoso y no estamos usando este atributo 
			 * de la clase
		    Pair<Integer,Integer> arista = new Pair<Integer,Integer>(a, b);
		    aristas.remove(arista);
		    */	
		}
		
		
		public void borrarVertice(int v){
			adyacencia[v].clear();
			
		}
	}
	
	
	public static void main(String args[]) {
		
		Scanner s = new Scanner(System.in);

		Main asp= new Main();
		
		List<Integer> respuestas= new LinkedList<Integer>();
		
		while(true){
			int cantVert = s.nextInt();     //Cantidad de vertices del grafo
			int cantAristas = s.nextInt();  //Cantidad de aristas de la entrada
		
			if(cantVert == 0 && cantAristas == 0) break;
				
			int origen = s.nextInt(); 
			int destino = s.nextInt();
						
			GrafoConLista g = asp.new GrafoConLista(cantVert);
			
			//Lectura de las aristas y los pesos
			for(int  i = 0 ; i < cantAristas ; i++){
				int vpadre = s.nextInt();
				int vhijo = s.nextInt();
				int peso = s.nextInt();
				g.agregarConexion(vpadre, vhijo);
				g.setPesoArista(vpadre, vhijo, peso);
			}
			
			Dijkstra d = asp.new Dijkstra(g, origen);
			
			d.dijkstra(g);
			
			
			ArrayList[] previos = (ArrayList[]) d.getPrevios(); 
			
			ArrayList<Integer> auxDestinos= new ArrayList<Integer>();
			auxDestinos.add(destino); 
			
			while(!auxDestinos.isEmpty()){
				int vDestino = auxDestinos.remove(0);
				ArrayList<Integer> auxPrev = previos[vDestino];
				auxDestinos.addAll(auxPrev);
				for(int v: auxPrev){
					g.borrarArista(v, vDestino);
				}
				
			}
			
			Dijkstra d2 = asp.new Dijkstra(g, origen);
			
			d2.dijkstra(g);
			
			Integer[] pesos =  d2.getPesos();
			int nuevoCosto = pesos[destino];
			if(nuevoCosto != Integer.MAX_VALUE){
				respuestas.add(nuevoCosto);
				//System.out.println(nuevoCosto);
			}
			else{
				respuestas.add(-1);
				//System.out.println(-1);

			}
		}
		
		for(int r: respuestas){
			System.out.println(r);
		}
		
		s.close();
		
	}

}
	
