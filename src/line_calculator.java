import java.util.ArrayList;


public class line_calculator  implements Runnable{

	
	int line;
	double [][] mat; 
	ArrayList<sparse_vec> vectors;
	public line_calculator(int line,double [][] mat,ArrayList<sparse_vec> vectors) {
		super();
		this.line = line;
		this.mat =  mat;
		this.vectors = vectors;
	}
	@Override
	public void run() {
		int i=line;
		for (int j=0;j<=i;j++)
			mat[i][j]=k_mean.dist(vectors.get(i),vectors.get(j));
		System.out.println("finished line: "+ i);
	}
	

}
