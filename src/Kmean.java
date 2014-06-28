import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Kmean {

    protected double[][]	  distMap;
    protected List<Doc>	   docs;
    protected List<Integer>       centroids  = new ArrayList<Integer>();
    protected List<List<Integer>> membership = new ArrayList<List<Integer>>();
    protected List<Double>	purity     = new ArrayList<Double>();

    public Kmean(List<Doc> docs) throws InterruptedException {
	super();
	this.docs = docs;
	this.calcDistMat(docs);
    }

    public void calcKmean(int k) {

	initCentroids(k);

	for (int i = 0; i < k; i++) {
	    this.membership.add(new ArrayList<Integer>());
	}

	int move = this.docs.size();
	while (move > (0.1 * this.docs.size())) {

	    move = recalcMembersip();
	    recalcCentroids();

	    System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance()
		    .getTime()));
	}

	// Set the centrid output into the Doc instance
	for (int centroIndex = 0; centroIndex < this.membership.size(); centroIndex++) {
	    final Integer docCluster = this.centroids.get(centroIndex);
	    for (int centoMemIndex = 0; centoMemIndex < this.membership.get(centroIndex).size(); centoMemIndex++) {
		this.docs.get(centoMemIndex).setDocCluster(docCluster);
	    }
	}

    }

    public void calcPurity() {

	Map<Integer, Integer> dominatesClassCount;
	for (int centroidIndex = 0; centroidIndex < this.membership.size(); centroidIndex++) {
	    dominatesClassCount = new HashMap<Integer, Integer>();
	    final List<Integer> currentCentride = this.membership.get(centroidIndex);
	    for (int membersIndex = 0; membersIndex < currentCentride.size(); membersIndex++) {
		final int docGoldStadartCluster = this.docs.get(currentCentride.get(membersIndex))
			.getDocGoldStadartCluster();
		Integer domCount = dominatesClassCount.get(docGoldStadartCluster);
		domCount = domCount == null ? 1 : domCount++;
		dominatesClassCount.put(docGoldStadartCluster, domCount);
	    }
	    Double centroidePurity = 0.0;
	    if (!currentCentride.isEmpty()) {
		centroidePurity = Collections.max(dominatesClassCount.values())
			/ (currentCentride.size() * 1.0);
	    }
	    this.purity.add(centroidePurity);
	}

    }

    protected void initCentroids(int k) {
	Double tmp;
	for (int i = 0; i < k; i++) {
	    tmp = Math.random() * this.docs.size();
	    while (this.centroids.contains(tmp.intValue())) {
		tmp = Math.random() * this.docs.size();
	    }
	    this.centroids.add(tmp.intValue());
	}
    }

    public void calcDistMat(List<Doc> docs) throws InterruptedException {
	final ExecutorService executor = Executors.newFixedThreadPool(4);

	final int size = docs.size();
	final double[][] mat = new double[size][size];
	final int calculationRange = 500;
	for (int i = 0; i < size; i = i + calculationRange) {
	    final Runnable worker = new LineCalculator(i, calculationRange, mat, docs);
	    executor.execute(worker);
	}
	executor.shutdown();
	executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

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
	int bestCentroid;
	int change = 0;
	final List<List<Integer>> newMembership = new ArrayList<List<Integer>>();
	for (int i = 0; i < this.membership.size(); i++) {
	    newMembership.add(new ArrayList<Integer>());
	}

	for (int i = 0; i < this.docs.size(); i++) {
	    bestCentroid = findClosestCentroid(i);
	    if (!this.membership.get(bestCentroid).contains(i)) {
		change++;
	    }
	    newMembership.get(bestCentroid).add(i);

	}
	this.membership = newMembership;
	return change;

    }

    public void recalcCentroids() {

	for (int centIndex = 0; centIndex < this.membership.size(); centIndex++) {

	    double bestDistance = Integer.MAX_VALUE;
	    for (int centroCandidateIndex = 0; centroCandidateIndex < this.docs.size(); centroCandidateIndex++) {
		double distance = 0;

		final List<Integer> centroidIMembers = this.membership.get(centIndex);
		for (int i = 0; i < centroidIMembers.size(); i++) {
		    distance += this.distMap[centroidIMembers.get(i)][centroCandidateIndex];
		}
		if (distance < bestDistance) {
		    bestDistance = distance;
		    this.centroids.set(centIndex, centroCandidateIndex);
		}

	    }
	}

    }

    public int findClosestCentroid(int j) {
	double lowestDistance = Double.MAX_VALUE;
	int ans = 0;
	double distance;
	for (int i = 0; i < this.centroids.size(); i++) {
	    distance = this.distMap[j][this.centroids.get(i)];
	    if (distance < lowestDistance) {
		lowestDistance = distance;
		ans = i;
	    }

	}
	return ans;
    }

    public double findClosestCentroidDistance(int j) {
	double lowestDistance = Double.MAX_VALUE;
	double distance;
	for (int i = 0; i < this.centroids.size(); i++) {
	    distance = this.distMap[j][this.centroids.get(i)];
	    if (distance < lowestDistance) {
		lowestDistance = distance;
	    }

	}
	return lowestDistance;
    }

    public void printPurity() {

	for (int i = 0; i < this.purity.size(); i++) {
	    System.out.println("Purity Centroid " + i + ": " + this.purity.get(i));
	}

    }

}
