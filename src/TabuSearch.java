import java.util.Vector;

public class TabuSearch {

	static ProblemaPHubRAllocationMedio copiarProblema(ProblemaPHubRAllocationMedio problemaACopiar) {
		
		Vector<Punto> puntosActuales= new Vector<Punto>();
		puntosActuales= problemaACopiar.deepCopyVPuntos(problemaACopiar.getPuntos());
		Vector<Integer> nucleosActuales= new Vector<Integer>();
		nucleosActuales= problemaACopiar.deepCopyVNucleos(problemaACopiar.getNucleos());
		
		return new ProblemaPHubRAllocationMedio(puntosActuales, nucleosActuales, problemaACopiar.getrDistribuciones(), problemaACopiar.getpNucleos(), problemaACopiar.getnPuntos());
		
	}
	
	
	
	
	public static void main(String[] args) {
		
		ProblemaPHubRAllocationMedio problemaActual= new ProblemaPHubRAllocationMedio();
		problemaActual.generarProblemaAleatorio();
		
		ProblemaPHubRAllocationMedio mejorSolucion= copiarProblema(problemaActual);
		mejorSolucion.asignarNucleos();
		
		int iteracionesMax= problemaActual.getnPuntos()/4;
		MatrizTabu matrizTabu= new MatrizTabu(problemaActual.getpNucleos(), iteracionesMax);
		
		problemaActual.generarProblemaAleatorio();
		problemaActual.resolverInstanciaGrasp();
		
		for(int i=0; i< problemaActual.getNucleos().size(); i++) {
			matrizTabu.introducirElemento(i, problemaActual.getNucleos().get(i));
		}
		
		int iteracionActual=0;
		boolean parada= false;
		
		while(!parada) {

			problemaActual.localTabu(matrizTabu);
			if(problemaActual.resolverInstancia()< mejorSolucion.resolverInstancia()) {
				mejorSolucion= copiarProblema(problemaActual);
				mejorSolucion.asignarNucleos();
			}
			
			for(int i=0; i< problemaActual.getNucleos().size(); i++) {
				matrizTabu.introducirElemento(i, problemaActual.getNucleos().get(i));
			}
			iteracionActual++;
			System.out.println(iteracionActual);
			//condicionesdeparada
			if(iteracionActual> iteracionesMax) {
				parada= true;
			}
			
		}
		
		problemaActual.busquedaLocalGreed();
		mejorSolucion.busquedaLocalGreed();
		
		if(problemaActual.resolverInstancia()< mejorSolucion.resolverInstancia()) {
			mejorSolucion= copiarProblema(problemaActual);
			mejorSolucion.asignarNucleos();
		}
		
		System.out.println(mejorSolucion.resolverInstancia());
		
		
		
		
		
		
		
		
		
		
	}
}
