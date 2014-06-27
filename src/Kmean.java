import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Kmean {

    private double[][]	  distMap;
    private List<SparseVector>  vectors;
    private List<Integer>       centroids  = new ArrayList<Integer>();
    private List<List<Integer>> membership = new ArrayList<List<Integer>>();

    public Kmean(List<SparseVector> vectors) throws InterruptedException {
	super();
	this.vectors = vectors;
	this.calcDistMat(vectors);
    }

    public void calcKmean(int k) {

	Double tmp;
	for (int i = 0; i < k; i++) {
	    tmp = Math.random() * this.vectors.size();
	    while (this.centroids.contains(tmp)) {
		tmp = Math.random() * this.vectors.size();
	    }
	    this.centroids.add(tmp.intValue());
	}
	for (int i = 0; i < k; i++) {
	    this.membership.add(new ArrayList<Integer>());
	}

	int move = this.vectors.size();
	while (move > (0.1 * this.vectors.size())) {

	    move = recalcMembersip();
	    recalcCentroids();

	    System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance()
		    .getTime()));
	}
    }

    public static double dist(SparseVector a, SparseVector b) {
	double sum = 0;
	final Set<Integer> set = new HashSet<Integer>(a.getVector().keySet());
	set.addAll(b.getVector().keySet());
	for (final Integer i : set) {
	    sum += Math.pow(a.get(i) - b.get(i), 2);
	}
	return Math.sqrt(sum);
    }

    public void calcDistMat(List<SparseVector> vectors) throws InterruptedException {
	final ExecutorService executor = Executors.newFixedThreadPool(8);

	final int size = vectors.size();
	final double[][] mat = new double[size][size];
	for (int i = 0; i < size; i++) {
	    final Runnable worker = new LineCalculator(i, mat, vectors);
	    executor.execute(worker);
	    // for (int j=0;j<=i;j++)
	    // mat[i][j]=dist(vectors.get(i),vectors.get(j));
	}
	executor.shutdown();
	while (!executor.isTerminated()) {
	    Thread.sleep(10000);
	}

	System.out.println("Finished all threads");

	for (int i = 0; i < size; i++) {
	    for (int j = i; j < size; j++) {
		mat[i][j] = mat[j][i];
	    }
	}

	this.distMap = mat;
    }

    public void add(SparseVector a, SparseVector b) {
	final Set<Integer> set = a.getVector().keySet();
	set.addAll(b.getVector().keySet());
	for (final Integer i : set) {
	    a.set(i, a.get(i) + b.get(i));
	}

    }

    public void div(SparseVector a, double b) {
	final Set<Integer> set = a.getVector().keySet();
	for (final Integer i : set) {
	    a.set(i, a.get(i) / b);
	}

    }

    public int recalcMembersip() {
	int tmp;
	int change = 0;
	final List<List<Integer>> NewMembership = new ArrayList<List<Integer>>();
	for (int i = 0; i < this.membership.size(); i++) {
	    NewMembership.add(new ArrayList<Integer>());
	}

	for (int i = 0; i < this.vectors.size(); i++) {
	    tmp = findClosestCentroid(i);
	    if (!this.membership.get(tmp).contains(i)) {
		change++;
	    }
	    NewMembership.get(tmp).add(i);

	}
	this.membership = NewMembership;
	return change;

    }

    public void recalcCentroids() {

	for (int i = 0; i < this.membership.size(); i++) {

	    double dist = Integer.MAX_VALUE;
	    for (int j = 0; j < this.vectors.size(); j++) {
		double tmp_dist = 0;

		for (int z = 0; z < this.membership.get(i).size(); z++) {
		    tmp_dist += this.distMap[this.membership.get(i).get(z)][j];
		}
		if (tmp_dist < dist) {
		    dist = tmp_dist;
		    this.centroids.set(i, j);
		}

	    }
	}

    }

    public int findClosestCentroid(int j) {
	double dist = Double.MAX_VALUE;
	int ans = 0;
	double tmp_dist;
	for (int i = 0; i < this.centroids.size(); i++) {
	    tmp_dist = this.distMap[j][this.centroids.get(i)];
	    if (tmp_dist < dist) {
		dist = tmp_dist;
		ans = i;
	    }

	}
	return ans;
    }

}
