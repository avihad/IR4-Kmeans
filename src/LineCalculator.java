import java.util.List;

public class LineCalculator implements Runnable {

    private int	      line;
    private double[][]       mat;
    private List<SparseVector> vectors;

    public LineCalculator(int line, double[][] mat, List<SparseVector> vectors) {
	super();
	this.line = line;
	this.mat = mat;
	this.vectors = vectors;
    }

    @Override
    public void run() {
	final int i = this.line;
	for (int j = 0; j <= i; j++) {
	    this.mat[i][j] = Kmean.dist(this.vectors.get(i), this.vectors.get(j));
	}
	System.out.println("finished line: " + i);
    }

}
