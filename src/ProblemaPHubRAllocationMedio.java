import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;


/**Programa que pretende permitir resolver el problema p-hub r-allocation aplicando diferentes algoritmos constructivos y b�squedas por entornos.
 * Tambien implementa la solucion GRASP.
 * @author Germ�n Alfonso Teixid� - alu0100961768
 */
public class ProblemaPHubRAllocationMedio {

	public Vector<Punto> puntos;   		// Guarda los puntos del mapa.
	public Vector<Integer> nucleos;	 	// Recuerda en que posici�n del vector de puntos est�n guardado cada nucleo. EJ: [4,6,9] Significa que puntos[4/6/9] ser� un n�cleo

	public int rDistribuciones;			// Numero de n�cleos a los que un nodo puede estar conectado.
	public int pNucleos; 				// N�mero de n�cleos existentes en el problema.
	public int nPuntos;					// Numero de puntos existentes en el problema
	
	public float costeEntradaHub= 1;
	public float costeHubHub= 1;
	public float costeHubSalida= 1;
	
	//Constructores
	public ProblemaPHubRAllocationMedio() {			//Problema standar
		this.puntos= new Vector<Punto>();
		this.nucleos= new Vector<Integer>();	
		
		this.rDistribuciones= 2;
		this.pNucleos= 5;
		this.nPuntos= 30;
	}
	
	/** Constructor de la clase ProblemaPHubRAllocationMedio. 
	 * @param r (Distribuciones) N�mero de nucleos a los que un nodo puede conectarse en el problema.
	 * @param p (N�cleos) Numero de nodos del problema que ser�n considerados huds/nucleos/nexos/etc.
	 * @param n (N�mero de puntos) Numero de puntos que contiene el problema.
	 * @param puntos Contiene la lista de puntos del problema.
	 * @param nucleos Contiene los indexs de los puntos que son nucleos en puntos.
	 */
	public ProblemaPHubRAllocationMedio(Vector<Punto> puntos, Vector<Integer> nucleos, int r, int p, int n) {
		this.puntos= puntos;
		this.nucleos= nucleos;	
		this.rDistribuciones= r;
		this.pNucleos= p;
		this.nPuntos= n;
	}
	
	/** Constructor de la clase ProblemaPHubRAllocationMedio. 
	 * @param r (Distribuciones) N�mero de nucleos a los que un nodo puede conectarse en el problema.
	 * @param p (N�cleos) Numero de nodos del problema que ser�n considerados huds/nucleos/nexos/etc.
	 * @param n (N�mero de puntos) Numero de puntos que contiene el problema.
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
	 * Funciona como un subsituto de puntos.contains(), pero que comprueba si un obejto similar existe o no en el vector (fijandose �nicamente en las coordenadas x e y de los puntos), aunque no sea el objeto exacto en cuestion.
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
	
	/** Usando esta funci�n, generamos p N�cleos aleatorios y asignamos a cada punto sus n�cleos correspondientes.
	 *  Debemos tener una instancia del problema para que funcione.
	 */
	public void generarNucleosAleatorios() {
		// Asignamos p n�cleos aleatorios a nuestros puntos.
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
		asignarNucleos();
	}
	
	/** Dado el problema actual, asigna los nodos correspondientes a cada punto.
	 */
	public void asignarNucleos() {
		
		for (Punto puntoi : getPuntos()) {
			puntoi.setNucleosAsignados(new Vector<Integer>());		//Nos aseguramos de que cada punto no tenga asignado ningun N�cleo de antemano.
			Vector<Float> minimosValue= new Vector<Float>();		//Caclucalmos la distancia de cada punto a cada posible nodo.
			Vector<Integer> minimosIndex= new Vector<Integer>();
			for (Integer nodeIndex : getNucleos()) {
				minimosValue.add(puntoi.distanciaA(getPuntos().get(nodeIndex)));
				minimosIndex.add(nodeIndex);
			}
			sort(minimosValue, minimosIndex);						//Asignamos los r nucleos m�s cercanos a cada punto.
			if(minimosIndex.size()> getrDistribuciones()) {
				for(int i=0; i< getrDistribuciones(); i++) {			// TODO: Solucionar que hayan m�s distribuciones que nucleos
					puntoi.getNucleosAsignados().add(minimosIndex.get(i));
				}
			}
			else {
				for(int i=0; i< minimosIndex.size(); i++) {			// TODO: Solucionar que hayan m�s distribuciones que nucleos
					puntoi.getNucleosAsignados().add(minimosIndex.get(i));
				}
			}
		}
	}
	
	/** M�todo auxiliar que se encarga de ordenar 2 vectores:
	 *  @param minimosValue Contiene la distancia de los nodos al punto
	 *  @param minimosIndex Contiene a que nodo corresponde cada distancia<p>
	 *  El m�todo ordenar� ambos vectores aplicando insertion sort a <b>minimosValue</b> y cambiando los valores de  <b>minimosIndex</b> de la misma forma que cambia los valores de <b>minimosValue</b>.
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

	/**Calcula el coste m�nimo para ir del punto <b>inicio</b> al punto <b>destino</b> dados los nodos intermedios de cada punto.
	 * @param inicio Punto de inicio del reccorrido.
	 * @param destino Punto final del recorrido.
	 * @return float con el coste m�nimo de <b>inicio</b> a <b>destino</b>.
	 */
	public float calcularCosteMinimo(Punto inicio, Punto destino) {
		float distanciaMinima= Float.POSITIVE_INFINITY;
		//int nexoInicio= inicio.getNucleosAsignados().get(0);
		//int nexoDestino= destino.getNucleosAsignados().get(0);
		//Encuentra la mejor ruta (i->k->l->j) probando para cada k y cada l.
		if(inicio.equals(destino)) {
			distanciaMinima= 0;
			return distanciaMinima;
		}
		else {
			for (Integer nucleoInicio : inicio.getNucleosAsignados()) {
				for (Integer nucleoDestino : destino.getNucleosAsignados()) {
					// TODO: A�adir pesos a los desplazamientos de nodo a nucleo, de nucleo a nucleo y de nucleo a nodo.
					float aux= (costeEntradaHub*inicio.distanciaA(puntos.get(nucleoInicio))) + (costeHubHub*puntos.get(nucleoInicio).distanciaA(puntos.get(nucleoDestino))) + (costeHubSalida*puntos.get(nucleoDestino).distanciaA(destino));
					if(aux< distanciaMinima) {
						distanciaMinima= aux;
						//nexoInicio= nucleoInicio;
						//nexoDestino= nucleoDestino;
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

	/** Resuelve la instancia actual aplicandole GRASP.
	 */
	public void resolverInstanciaGrasp() {
		// Creamos todos los puntos del problema y los distribuimos sobre un grid cuadrada de tama�o 0 a 100. 
//		for(int i= 0; i< this.getnPuntos(); i++) {							//Los puntos que generemos estar�n entre las coordenadas x/y [0-100]
//			Punto aux= new Punto(ThreadLocalRandom.current().nextInt(0, 100+ 1), ThreadLocalRandom.current().nextInt(0, 100+ 1), false);
//			if(puntosContiene(aux)) {
//				i--;
//			}
//			else {
//				getPuntos().add(aux);
//			}
//		}
		
		for(int i=0; i<getpNucleos(); i++) {
			generarNuevoHubGrasp();
		}
		busquedaLocalGreed();
	}

	/** Siguiendo el proceso de inicializaci�n de GRASP, genera un hub de forma pseudo-aleatoria entre las 3 mejores hubs que podr�an incorporarse a la actual instancia.
	 */
	public void generarNuevoHubGrasp(){
		
		Vector<Integer> posiblesNucleos= new Vector<Integer>();
		Vector<Float> costePosiblesNucleos= new Vector<Float>();
		
		for(int i=0; i< getPuntos().size(); i++) {								// Para cada punto
			if(!getPuntos().get(i).isEsNucleo()) {								// Que no sea ya un nucleo
				
				Vector<Punto> puntosActuales= new Vector<Punto>();
				puntosActuales= deepCopyVPuntos(getPuntos());
				
				Vector<Integer> nucleosActuales= new Vector<Integer>();
				nucleosActuales= deepCopyVNucleos(getNucleos());
				
				nucleosActuales.add(i);
				
				//Calculamos su coste al insertarlo en el problema
				ProblemaPHubRAllocationMedio Subproblema= new ProblemaPHubRAllocationMedio(puntosActuales, nucleosActuales, getrDistribuciones(), getpNucleos(), getnPuntos());
				Subproblema.asignarNucleos();
				float coste= Subproblema.resolverInstancia();
			
				posiblesNucleos.add(i);
				costePosiblesNucleos.add(coste);
				
			}
		}
		sort(costePosiblesNucleos, posiblesNucleos);
		int r= ThreadLocalRandom.current().nextInt(0, 2+ 1);
		getNucleos().add(posiblesNucleos.get(r));
		asignarNucleos();
	}
	
	/** Encuentra la soluci�n optima local de la instancia. Algoritmo greed.
	 */
	public void busquedaLocalGreed() {	
		boolean haCambiado= false;
		for(int i=0; i< getNucleos().size(); i++) {
			Vector<Punto> puntosActuales= new Vector<Punto>();
			puntosActuales= deepCopyVPuntos(getPuntos());
			Vector<Integer> nucleosActuales= new Vector<Integer>();
			nucleosActuales= deepCopyVNucleos(getNucleos());
			for(int punt=0; punt< getPuntos().size(); punt++) {
				//System.out.println("test");
				if(getPuntos().equals(puntosActuales))
					System.out.println("OH NO");
				
				nucleosActuales.set(i, punt);
				ProblemaPHubRAllocationMedio Subproblema= new ProblemaPHubRAllocationMedio(puntosActuales, nucleosActuales, getrDistribuciones(), getpNucleos(), getnPuntos());
				Subproblema.asignarNucleos();
				
				if(Subproblema.resolverInstancia()< resolverInstancia()) {
					getNucleos().set(i, punt);
					asignarNucleos();
					haCambiado= true;
				}
			}

			if(i+1< getNucleos().size() && haCambiado) {
				busquedaLocalGreed();
			}
		}
	}
	
	/** Encuentra la soluci�n optima local de la instancia. Algoritmo ansioso.
	 */
	public void busquedaLocalAnsiosa() {	
		for(int i=0; i< getNucleos().size(); i++) {
			Vector<Punto> puntosActuales= new Vector<Punto>();
			puntosActuales= deepCopyVPuntos(getPuntos());	
			Vector<Integer> nucleosActuales= new Vector<Integer>();
			nucleosActuales= deepCopyVNucleos(getNucleos());

			for(int punt=0; punt< getPuntos().size(); punt++) {
				//System.out.println("test");
				if(getPuntos().equals(puntosActuales))
					System.out.println("OH NO");
				
				nucleosActuales.set(i, punt);
				ProblemaPHubRAllocationMedio Subproblema= new ProblemaPHubRAllocationMedio(puntosActuales, nucleosActuales, getrDistribuciones(), getpNucleos(), getnPuntos());
				Subproblema.asignarNucleos();
				
				if(Subproblema.resolverInstancia()< resolverInstancia()) {
					getNucleos().set(i, punt);
					asignarNucleos();
				}
			}
		}
	}
	
	/** Generamos un problema a partir de los nPuntos asignados. <p>
	 *  El problema se representar� en un vector de puntos "<b>puntos</b>", que almacenar� los diferentes puntos (nodos) a unir.<br>
	 *  Los puntos contienen su coordenada X, su coordenada Y, y si son o no n�cleos.<br>
	 *  El vector de Integers "<b>nucleos</b>" contiene una lista de posiciones de "<b>puntos</b>" con la direcci�n de cada n�cleo dentro de <b>puntos</b>.
	 */
	public void generarProblemaAleatorio() {
		// Creamos todos los puntos del problema y los distribuimos sobre un grid cuadrada de tama�o 0 a 100. 
		for(int i= 0; i< this.getnPuntos(); i++) {							//Los puntos que generemos estar�n entre las coordenadas x/y [0-100]
			Punto aux= new Punto(ThreadLocalRandom.current().nextInt(0, 100+ 1), ThreadLocalRandom.current().nextInt(0, 100+ 1), false);
			if(puntosContiene(aux)) {
				i--;
			}
			else {
				getPuntos().add(aux);
			}
		}
	}
	
	static public Vector<Punto> deepCopyVPuntos(Vector<Punto> oldObj) {
		Vector<Punto> nuevoVector= new Vector<Punto>();
		for(Punto punto: oldObj) {
			Punto nuevoPunto= new Punto(punto.getCordX(), punto.getCordY(), false);
			nuevoVector.add(nuevoPunto);
		}
		return nuevoVector;
	}
	
	static public Vector<Integer> deepCopyVNucleos(Vector<Integer> oldObj) {
		Vector<Integer> nuevoVector= new Vector<Integer>();
		for(Integer inte: oldObj) {
			Integer nuevoInt= inte;
			nuevoVector.add(nuevoInt);
		}
		return nuevoVector;
	}
		
 	public static void main(String[] args) {
 		//Problema simple
 		ProblemaPHubRAllocationMedio ProblemaInicial= new ProblemaPHubRAllocationMedio();		//Genera un problema aleatorio
 		ProblemaInicial.generarProblemaAleatorio();												//Genera los puntos del problema, sin asignar hubs (de eso se encargan lo algoritoms de resoluci�n)
 		Vector<Punto> puntosProblema;
 		//ProblemaPHubRAllocationMedio(Vector<Punto> puntos, Vector<Integer> nucleos, int r, int p, int n)
 		
 		//GRASP:
 		System.out.println("Soluci�n GRASP: ");
 		puntosProblema= deepCopyVPuntos(ProblemaInicial.getPuntos());
 		ProblemaPHubRAllocationMedio GRASP= new ProblemaPHubRAllocationMedio(puntosProblema, new Vector<Integer>(), ProblemaInicial.getrDistribuciones(), ProblemaInicial.getpNucleos(), ProblemaInicial.getnPuntos());
 		GRASP.resolverInstanciaGrasp();
		System.out.println(GRASP.resolverInstancia());
		
//		//Multiarranque:
//		System.out.println("Soluci�n Multiarranque: ");
// 		puntosProblema= deepCopy(ProblemaInicial.getPuntos());
// 		ProblemaPHubRAllocationMedio MultiArranque= new ProblemaPHubRAllocationMedio(puntosProblema, new Vector<Integer>(), ProblemaInicial.getrDistribuciones(), ProblemaInicial.getpNucleos(), ProblemaInicial.getnPuntos());
// 		MultiArranque.resolverInstanciaMultiarranque();
	}

}



/*
 * LEGADO:
 * 
//////////////////////////////////////////////////////
 * //MULTIARRANQUE
//////////////////////////////////////////////////////

	//Esto no deber�a ser una duncion del objeto problema, deber�a estar en otro archivo o ser su propio main, pero lo hago aqui para trabajar con todo en el mismo archivo. Luego lo "vuelvo bonito".
//	public void resolverInstanciaMultiarranque() {
//		
//		Vector<Punto> puntosActuales= new Vector<Punto>();
//		puntosActuales= deepCopy(getPuntos());	
//		
//		ProblemaPHubRAllocationMedio solucionOptima= new ProblemaPHubRAllocationMedio(puntosActuales, new Vector<Integer>(), getrDistribuciones(), getpNucleos(), getnPuntos());
//		solucionOptima.resolverInstanciaGrasp();
//		int iteracion= 0;
//		int iteracionSinCambios= 0;
//		
//		boolean condicionDeParada= false;
//		
//		while(!condicionDeParada) {
//			
//			Vector<Punto> puntosActual= new Vector<Punto>();
//			puntosActual= deepCopy(getPuntos());	
//			
//			ProblemaPHubRAllocationMedio solucionActual= new ProblemaPHubRAllocationMedio(puntosActual, new Vector<Integer>(), getrDistribuciones(), getpNucleos(), getnPuntos());
//			solucionActual.resolverInstanciaGrasp();
//
//			if(solucionActual.resolverInstancia() < solucionOptima.resolverInstancia()) {
//				solucionOptima= solucionActual;
//			}
//			else {
//				iteracionSinCambios++;
//			}
//			iteracion++;
//			if(iteracion> getnPuntos() && iteracionSinCambios> getnPuntos()/getpNucleos()) {
//				condicionDeParada=true;
//			}
//			//condicionDeParada=true;
//		}
//		System.out.println(solucionOptima.resolverInstancia());
//	} 

 * 
 * 
 */
