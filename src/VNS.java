import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class VNS {

	ProblemaPHubRAllocationMedio phub_;
	
	public VNS() {
		phub_=new ProblemaPHubRAllocationMedio();
		phub_.generarProblemaAleatorio();
	}
	
	public VNS(ProblemaPHubRAllocationMedio phub) {
		phub_=phub;
	}
	
	public void execute() {
		int neighbourhood=1;
		int max_neighbourhood=Math.min(phub_.getpNucleos(),phub_.getnPuntos()-phub_.getpNucleos());
		phub_.busquedaLocalGreed();
		while(neighbourhood<=max_neighbourhood) {
			ProblemaPHubRAllocationMedio ph=changeSol(neighbourhood);
			
			if(ph.resolverInstancia()<phub_.resolverInstancia()) {
				phub_=ph;
				neighbourhood=0;
			}
			else {
				neighbourhood++;
			}
		}
	}
	
	public ProblemaPHubRAllocationMedio changeSol(int n) {
		ProblemaPHubRAllocationMedio ph= clonaproblema();
		Vector<Integer> newhubs=new Vector<Integer>();
		for(int i=0;i<n;i++) {
			int aux= ThreadLocalRandom.current().nextInt(0, phub_.getnPuntos());
			if(phub_.getNucleos().contains(aux) || newhubs.contains(aux)) {
				i--;
			}
			else {
				newhubs.add(aux);
			}
		}
		
		for(int i=0;i<n;i++) {
			int aux= ThreadLocalRandom.current().nextInt(0, phub_.getpNucleos());
			if(newhubs.contains(phub_.nucleos.get(aux))) {
				i--;
			}
			else {
				phub_.puntos.get(phub_.nucleos.get(aux)).esNucleo=false;
				phub_.puntos.get(newhubs.get(i)).esNucleo=true;
				phub_.nucleos.set(aux,newhubs.get(i));
			}
		}
		phub_.nucleos.sort(null);
		return ph;
	}
	
	public ProblemaPHubRAllocationMedio clonaproblema() {
		ProblemaPHubRAllocationMedio ph= new ProblemaPHubRAllocationMedio((Vector<Punto>)phub_.puntos.clone(),(Vector<Integer>)phub_.nucleos.clone(),phub_.getrDistribuciones(),phub_.getpNucleos(),phub_.getnPuntos());
		return ph;
	}
}
