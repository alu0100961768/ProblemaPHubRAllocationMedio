
public class MatrizTabu {

	public int[][] matriz_;
	
	public MatrizTabu() {
		
	}
	
	public MatrizTabu(int pnucleos, int iteraciones) {
		matriz_= new int[pnucleos][iteraciones];
		for (int x = 0; x < matriz_.length;x++) {
			for (int y = 0; y < matriz_[x].length; y++) {
				 matriz_[x][y] = -1;
			}   
		}
	}
	
	public void introducirElemento(int nucleoIndex, int valorNucleo) {
		for (int y = 0; y < matriz_[nucleoIndex].length-1; y++) {
			matriz_[nucleoIndex][y]= matriz_[nucleoIndex][y+1];
		}
		matriz_[nucleoIndex][matriz_[nucleoIndex].length-1]= valorNucleo;
	}
	
	public void iterarNucleo(int nucleoIndex) {
		for (int y = 0; y < matriz_[nucleoIndex].length-1; y++) {
			matriz_[nucleoIndex][y]= matriz_[nucleoIndex][y+1];
		}
		matriz_[nucleoIndex][matriz_[nucleoIndex].length-1]= -1;
	}
	
	public void iterarTodo() {
		for (int x = 0; x < matriz_.length;x++) {
			iterarNucleo(x);
		}
	}
	
	public boolean isLocked(int nucleoIndex, int valorNucleo) {
		for (int y = 0; y < matriz_[nucleoIndex].length; y++) {
			if(matriz_[nucleoIndex][y]== valorNucleo) {
				return true;
			}
		}
		return false;
	}
	
}
