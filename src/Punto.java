import java.util.Vector;

public class Punto {
	
	public float cordX;
	public float cordY;
	
	public boolean esNucleo;
	public Vector<Integer> nucleosAsignados;
	
	public Punto() {
		setCordX(-1);
		setCordY(-1);
		setEsNucleo(false);
		this.nucleosAsignados= new Vector<Integer>();
	}
	
	public Punto(float cordX, float cordY, boolean esPuente) {
		setCordX(cordX);
		setCordY(cordY);
		setEsNucleo(esPuente);
		this.nucleosAsignados= new Vector<Integer>();
	}

	public float getCordX() {
		return cordX;
	}
	public void setCordX(float cordX) {
		this.cordX = cordX;
	}
	public float getCordY() {
		return cordY;
	}
	public void setCordY(float cordY) {
		this.cordY = cordY;
	}
	public boolean isEsNucleo() {
		return esNucleo;
	}
	public void setEsNucleo(boolean esPuente) {
		this.esNucleo = esPuente;
	}
	public Vector<Integer> getNucleosAsignados() {
		return nucleosAsignados;
	}
	public void setNucleosAsignados(Vector<Integer> nucleoAsignado) {
		this.nucleosAsignados = nucleoAsignado;
	}

	public boolean equals(Punto punto) {
		if(punto.getCordX()== this.getCordX() && punto.getCordY()== this.getCordY())
			return true;
		else
			return false;
	}
	
	/**Devuelve la distancia en línea recta de este punto al punto de destino.<br>
	 * Se resuelve mediante pitágoras.
	 * @param destino
	 * @return
	 */
	public float distanciaA(Punto destino) {
		float qx= (float)Math.pow(this.getCordX()- destino.getCordX(), 2); //Cuadrado del cateto del eje X.              	     P2
		float qy= (float)Math.pow(this.getCordY()- destino.getCordY(), 2); //Cuadrado del cateto del eje Y.	 (distancia)		/ | (eje y)
		float distancia= (float)Math.sqrt(qx+qy);						   //Hipotenusa= Distancia				      		  P1--|  
		return distancia;												   //										         (eje x)
	}
	
	public void volverNucleo(boolean esHub) {
		setEsNucleo(esHub);
	}
}
