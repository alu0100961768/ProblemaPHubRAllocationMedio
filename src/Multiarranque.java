import java.util.Vector;

/** Algoritmo multiarranque para solucionar el Problema PHubs RAllocators.
 * @author Germ�n Alfonso Teixid� - alu0100961768
 */
public class Multiarranque {

	public static ProblemaPHubRAllocationMedio problemaOriginal;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		problemaOriginal= new ProblemaPHubRAllocationMedio();
		problemaOriginal.generarProblemaAleatorio();	
		resolverInstanciaMultiarranque();
	}
	
	public static void resolverInstanciaMultiarranque() {
		Vector<Punto> puntosActuales= new Vector<Punto>();
		puntosActuales= problemaOriginal.deepCopyVPuntos(problemaOriginal.getPuntos());	
		
		ProblemaPHubRAllocationMedio solucionOptima= new ProblemaPHubRAllocationMedio(puntosActuales, new Vector<Integer>(), problemaOriginal.getrDistribuciones(), problemaOriginal.getpNucleos(), problemaOriginal.getnPuntos());
		solucionOptima.resolverInstanciaGrasp();
		int iteracion= 0;
		int iteracionSinCambios= 0;
		
		boolean condicionDeParada= false;
		
		while(!condicionDeParada) {
			
			Vector<Punto> puntosActual= new Vector<Punto>();
			puntosActual= problemaOriginal.deepCopyVPuntos(problemaOriginal.getPuntos());	
			
			ProblemaPHubRAllocationMedio solucionActual= new ProblemaPHubRAllocationMedio(puntosActual, new Vector<Integer>(), problemaOriginal.getrDistribuciones(), problemaOriginal.getpNucleos(), problemaOriginal.getnPuntos());
			solucionActual.resolverInstanciaGrasp();

			if(solucionActual.resolverInstancia() < solucionOptima.resolverInstancia()) {
				solucionOptima= solucionActual;
			}
			else {
				iteracionSinCambios++;
			}
			iteracion++;
			if(iteracion> problemaOriginal.getnPuntos() && iteracionSinCambios> problemaOriginal.getnPuntos()/problemaOriginal.getpNucleos()) {
				condicionDeParada=true;
			}
		}
		System.out.println(solucionOptima.resolverInstancia());
	}
	
}
