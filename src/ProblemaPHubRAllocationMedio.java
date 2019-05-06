import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;


/**Programa que pretende permitir resolver el problema p-hub r-allocation aplicando diferentes algoritmos constructivos y búsquedas por entornos.
 * @author Germán Alfonso Teixidó - alu0100961768
 */
public class ProblemaPHubRAllocationMedio {

	public Vector<Punto> puntos;   		// Guarda los puntos del mapa.
	public Vector<Integer> nucleos;	 	// Recuerda en que posición del vector de puntos están guardado cada nucleo. EJ: [4,6,9] Significa que puntos[4/6/9] será un núcleo

	public int rDistribuciones;			// Numero de núcleos a los que un nodo puede estar conectado.
	public int pNucleos; 				// Número de núcleos existentes en el problema.
	public int nPuntos;					// Numero de puntos existentes en el problema
	
	//Constructores
	public ProblemaPHubRAllocationMedio() {			//Problema standar
		this.puntos= new Vector<Punto>();
		this.nucleos= new Vector<Integer>();	
		
		this.rDistribuciones= 2;
		this.pNucleos= 3;
		this.nPuntos= 10;
	}
	
	/** Constructor de la clase ProblemaPHubRAllocationMedio. 
	 * @param r (Distribuciones) Número de nucleos a los que un nodo puede conectarse en el problema.
	 * @param p (Núcleos) Numero de nodos del problema que serán considerados huds/nucleos/nexos/etc.
	 * @param n (Número de puntos) Numero de puntos que contiene el problema.
	 */
	public ProblemaPHubRAllocationMedio(int r, int p, int n) {
		this.puntos= new Vector<Punto>();
		this.nucleos= new Vector<Integer>();	
		this.rDistribuciones= r;
		this.pNucleos= p;
		this.nPuntos= n;
	}
	
	//Getters y setters
	public Vector<Punto> getPuntos() {
		return puntos;
	}
	public void setPuntos(Vector<Punto> puntos) {
		this.puntos = puntos;
	}
	public Vector<Integer> getNucleos() {
		return nucleos;
	}
	public void setNucleos(Vector<Integer> hubs) {
		this.nucleos = hubs;
	}
	public int getrDistribuciones() {
		return rDistribuciones;
	}
	public void setrDistribuciones(int rDistribuciones) {
		this.rDistribuciones = rDistribuciones;
	}
	public int getpNucleos() {
		return pNucleos;
	}
	public void setpNucleos(int pNucleos) {
		this.pNucleos = pNucleos;
	}
	public int getnPuntos() {
		return nPuntos;
	}
	public void setnPuntos(int nNodes) {
		this.nPuntos = nNodes;
	}

	// Funciones de la clase:
	
	/**Comprueba si el paramentro "punto" existe en el Vector de Punto "puntos" de la clase ProblemaPHubRAllocationMedio.
	 * Funciona como un subsituto de puntos.contains(), pero que comprueba que un obejto similar exista o no en el vector (fijandose únicamente en las coordenadas x e y de los puntos), aunque no sea la misma
	 * @param punto Punto a buscar en "<b>puntos</b>".
	 * @return <b>true</b> en caso de que "punto" exista en "puntos", <b>false</b> en caso contrario.
	 */
	public boolean puntosContiene(Punto punto) {
		for (Punto puntoi : getPuntos()) {
			if(puntoi.equals(punto))
				return true;
		}
		return false;
	}
	
	/** Generamos el problema a partir de las rDistribuciones, pNucleos y nPuntos asignados. <p>
	 *  El problema se representará en un vector de puntos "<b>puntos</b>", que almacenará los diferentes puntos (nodos) a unir.<br>
	 *  Los puntos contienen su coordenada X, su coordenada Y, y si son o no núcleos.<br>
	 *  El vector de Integers "<b>nucleos</b>" contiene una lista de posiciones de "<b>puntos</b>" con la dirección de cada núcleo dentro de <b>puntos</b>. 
	 */
	public void generateProblem() {
		// Creamos todos los puntos del problema y los distribuimos sobre un grid cuadrada de tamaño 0 a 100. 
		for(int i= 0; i< this.getnPuntos(); i++) {							//Los puntos que generemos estarán entre las coordenadas x/y [0-100]
			Punto aux= new Punto(ThreadLocalRandom.current().nextInt(0, 100+ 1), ThreadLocalRandom.current().nextInt(0, 100+ 1), false);
			if(puntosContiene(aux)) {
				i--;
			}
			else {
				getPuntos().add(aux);
			}
		}
		// Asignamos p nucleos diferentes a nuestros puntos.
		for(int i= 0; i< this.getpNucleos(); i++) {							
			int aux= ThreadLocalRandom.current().nextInt(0, getnPuntos());
			if(getNucleos().contains(aux)) {
				i--;
			}
			else {
				getNucleos().add(aux);
				getPuntos().get(aux).setEsNucleo(true);
			}
		}
		asignarNodos();
	}
	
	/** Dado el problema actual, asigna los nodos correspondientes a cada punto.
	 * @return
	 */
	public void asignarNodos() {
		
		for (Punto puntoi : getPuntos()) {
			Vector<Float> minimosValue= new Vector<Float>();
			Vector<Integer> minimosIndex= new Vector<Integer>();
			for (Integer nodeIndex : getNucleos()) {
				minimosValue.add(puntoi.distanciaA(getPuntos().get(nodeIndex)));
				minimosIndex.add(nodeIndex);
			}
			sort(minimosValue, minimosIndex);
			for(int i=0; i< getrDistribuciones(); i++) {
				puntoi.getNucleosAsignados().add(minimosIndex.get(i));
			}
		}
	}
	
	
	/**Método auxiliar de la función <b>asignarNodos()</b> que ordenará 2 vectores:
	 * @param minimosValue Contiene la distancia de los nodos al punto
	 * @param minimosIndex Contiene a que nodo corresponde cada distancia<p>
	 * El método ordenará ambos vectores aplicando insertion sort a <b>minimosValue</b> y cambiando los valores de  <b>minimosIndex</b> de la misma forma que cambia los valores de <b>minimosValue</b>.
	 */
	private void sort(Vector<Float> minimosValue, Vector<Integer> minimosIndex) {
		int n= minimosValue.size();
		for(int i= 1; i< n; i++) {
			float key= minimosValue.get(i);
			int kaux= minimosIndex.get(i);
			int j= i-1;
			
			while(j>=0 && minimosValue.get(j) > key) {
				minimosValue.set(j+1, minimosValue.get(j));
				minimosIndex.set(j+1, minimosIndex.get(j));
				j= j-1;
			}
			minimosValue.set(j+1, key);
			minimosIndex.set(j+1, kaux);
		}
	}

	/**Calcula el coste mínimo para ir del punto <b>inicio</b> al punto <b>destino</b> dados los nodos intermedios de cada punto.
	 * @param inicio Punto de inicio del reccorrido.
	 * @param destino Punto final del recorrido.
	 * @return float con el coste mínimo de <b>inicio</b> a <b>destino</b>.
	 */
	public float calcularCosteMinimo(Punto inicio, Punto destino) {
		float distanciaMinima= Float.POSITIVE_INFINITY;
		int nexoi= inicio.getNucleosAsignados().get(0);
		int nexod= destino.getNucleosAsignados().get(0);
		//Encuentra la mejor ruta (i->k->l->j) probando para cada k y cada l.
		if(inicio.equals(destino)) {
			distanciaMinima= 0;
			return distanciaMinima;
		}
		else {
			for (Integer nucleoi : inicio.getNucleosAsignados()) {
				for (Integer nucleod : inicio.getNucleosAsignados()) {
					// TODO: Añadir pesos a los desplazamientos de nodo a nucleo, de nucleo a nucleo y de nucleo a nodo.
					float aux= inicio.distanciaA(puntos.get(nucleoi)) + puntos.get(nucleoi).distanciaA(puntos.get(nucleod)) + puntos.get(nucleod).distanciaA(destino);
					if(aux< distanciaMinima) {
						distanciaMinima= aux;
						nexoi= nucleoi;
						nexod= nucleod;
					}
				}
			}
		return distanciaMinima;
		}
	}
	
	/** Devuelve el coste medio de la instancia actual del problema.
	 * @return media
	 */
	public float resolverInstancia() {
		float media= 0;
		int contador= 0;
		
		for (Punto inicio : puntos) {
			for (Punto destino : puntos) {
				media= media+calcularCosteMinimo(inicio, destino);
				contador++;
			}
		}
		media= media/contador;
		
		return media;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProblemaPHubRAllocationMedio PHRA= new ProblemaPHubRAllocationMedio();
		PHRA.generateProblem();
		System.out.print(PHRA.resolverInstancia());
	}

}
