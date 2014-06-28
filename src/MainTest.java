import java.util.ArrayList;
import java.util.List;

public class MainTest {

    public static void main(String[] args) throws InterruptedException {
	// TODO Auto-generated method stub
	final List<SparseVector> vectors = new ArrayList<SparseVector>();
	SparseVector tmp;
	tmp = new SparseVector();
	tmp.add(1.0);
	tmp.add(1.0);
	vectors.add(tmp);
	tmp = new SparseVector();
	tmp.add(1.0);
	tmp.add(1.1);
	vectors.add(tmp);
	tmp = new SparseVector();
	tmp.add(1.1);
	tmp.add(1.0);
	vectors.add(tmp);

	tmp = new SparseVector();
	tmp.add(3.0);
	tmp.add(3.0);
	vectors.add(tmp);
	tmp = new SparseVector();
	tmp.add(3.0);
	tmp.add(3.1);
	vectors.add(tmp);
	tmp = new SparseVector();
	tmp.add(3.1);
	tmp.add(3.0);
	vectors.add(tmp);

	tmp = new SparseVector();
	tmp.add(30.0);
	tmp.add(30.0);
	vectors.add(tmp);
	tmp = new SparseVector();
	tmp.add(30.0);
	tmp.add(30.1);
	vectors.add(tmp);
	tmp = new SparseVector();
	tmp.add(30.1);
	tmp.add(30.0);

	vectors.add(tmp);
	// final Kmean localKMean = new Kmean(vectors);
	// localKMean.calcKmean(3);

    }

}
