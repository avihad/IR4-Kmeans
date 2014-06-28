import java.util.List;

public class LineCalculator implements Runnable {

    private int	line;
    private int	range;
    private double[][] mat;
    private List<Doc>  docs;

    public LineCalculator(int line, int range, double[][] mat, List<Doc> docs) {
	super();
	this.line = line;
	this.range = range;
	this.mat = mat;
	this.docs = docs;
    }

    @Override
    public void run() {
	for (int r = this.line; r < this.line + this.range && r < this.mat.length; r++) {
	    for (int j = 0; j <= r; j++) {
		double tmp = this.docs.get(r).distance(this.docs.get(j));
		this.mat[r][j] = tmp;
	    }
	}
	System.out.println("finished line range: " + this.line + " - " + (this.line + this.range));
    }

}
