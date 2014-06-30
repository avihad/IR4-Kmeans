package idc.algorithms;

import idc.datastructs.Doc;
import idc.datastructs.LineCalculator;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private double		randIndex;

    private boolean	       advanceMod;

    public Kmean(List<Doc> docs, boolean advanceMod) throws InterruptedException {
	super();
	this.docs = docs;
	this.calcDistMat(docs);
	this.advanceMod = advanceMod;

    }

    public void calcKmean(int k) {

	initCentroids(k);

	for (int i = 0; i < k; i++) {
	    this.membership.add(new ArrayList<Integer>());
	}

	int move = this.docs.size();
	while (move > (0.05 * this.docs.size())) {

	    move = recalcMembersip();
	    recalcCentroids();

	    System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance()
		    .getTime()));
	}

	// Set the centrid output into the Doc instance
	for (int centroIndex = 0; centroIndex < this.membership.size(); centroIndex++) {
	    final Integer docCluster = this.centroids.get(centroIndex);
	    final List<Integer> clusterMembers = this.membership.get(centroIndex);
	    for (int centoMemIndex = 0; centoMemIndex < clusterMembers.size(); centoMemIndex++) {
		this.docs.get(clusterMembers.get(centoMemIndex)).setDocCluster(docCluster);
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

    public void calcRandIndex() {
	final Map<Integer, Integer> docIndexToGoldCluster = new HashMap<Integer, Integer>();
	final Map<Integer, Integer> docIndexToRegularCluster = new HashMap<Integer, Integer>();

	for (int i = 0; i < this.docs.size(); i++) {
	    final int goldStadartClusterNum = this.docs.get(i).getDocGoldStadartCluster();
	    docIndexToGoldCluster.put(i, goldStadartClusterNum);
	}

	for (int i = 0; i < this.docs.size(); i++) {
	    final int regularClusterNum = this.docs.get(i).getDocCluster();
	    docIndexToRegularCluster.put(i, regularClusterNum);
	}

	double a = 0, b = 0, c = 0, d = 0;

	for (int i = 0; i < this.docs.size(); i++) {
	    for (int j = i; j < this.docs.size(); j++) {
		final Integer goldClusterFirst = docIndexToGoldCluster.get(i);
		final Integer goldClusterSecond = docIndexToGoldCluster.get(j);

		final Integer regularClusterFirst = docIndexToRegularCluster.get(i);
		final Integer regularClusterSecond = docIndexToRegularCluster.get(j);

		if (goldClusterFirst == goldClusterSecond) {
		    if (regularClusterFirst == regularClusterSecond) {
			a++;
		    } else {
			c++;
		    }
		} else {
		    if (regularClusterFirst == regularClusterSecond) {
			d++;
		    } else {
			b++;
		    }
		}

	    }
	}

	this.randIndex = (a + b) / (a + b + c + d);

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

	int countZeros = 0;
	for (int i = 0; i < mat.length; i++) {
	    for (int j = 0; j < mat[i].length; j++) {
		if (mat[i][j] == 0) {
		    countZeros++;
		}
	    }
	}
	System.out.println("Num of zeros: " + countZeros);
    }

    public int recalcMembersip() {
	final int maxCentroidSize = new Double(this.docs.size() / 1.2).intValue();
	final Set<Integer> bestCentroids = new HashSet<Integer>();
	int bestCentroid;
	final List<List<Integer>> newMembership = new ArrayList<List<Integer>>();
	boolean toContinue = true;
	int change = 0;
	while (toContinue) {
	    change = 0;
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
	    toContinue = false;
	    for (int i = 0; i < this.membership.size() && this.advanceMod; i++) {
		if (this.membership.get(i).size() > maxCentroidSize) {
		    if (bestCentroids.size() == this.centroids.size()) {
			toContinue = false;
			continue;
		    }
		    final Random rand = new Random();
		    bestCentroids.add(this.centroids.get(i));
		    this.centroids.set(i, new Double(this.docs.size() * rand.nextDouble()).intValue());

		    toContinue = true;
		    newMembership.clear();
		    if (bestCentroids.size() == this.centroids.size()) {
			this.centroids = new ArrayList<Integer>(bestCentroids);
		    }
		}
	    }
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

    public void printPurityAndRandIndex() {

	for (int i = 0; i < this.purity.size(); i++) {
	    double sum = 0;
	    final List<Integer> clusterIMembers = this.membership.get(i);
	    for (final Integer docIndex : clusterIMembers) {
		sum += this.distMap[docIndex][this.centroids.get(i)];

	    }
	    final Doc docCluster = this.docs.get(this.centroids.get(i));
	    System.out.println("Purity Centroid " + i + ": " + this.purity.get(i) + " cluster size: "
		    + clusterIMembers.size() + " average distance: " + (sum / clusterIMembers.size())
		    + " cluserDocId: " + docCluster.getDocId() + " clustDocName: "
		    + docCluster.getDocumentName() + " clustDocGold: "
		    + docCluster.getDocGoldStadartCluster());

	}

	System.out.println("Rand Index: " + this.randIndex);

    }

    public void writeOutputFile(String filePath) throws IOException {
	final List<Doc> docsToOutput = new ArrayList<Doc>(this.docs);
	Collections.sort(docsToOutput, new Comparator<Doc>() {

	    @Override
	    public int compare(Doc o1, Doc o2) {
		return Integer.compare(o1.getDocId(), o2.getDocId());
	    }
	});

	final StringBuilder sb = new StringBuilder();
	for (final Doc doc : docsToOutput) {
	    sb.append(doc.getDocId());
	    sb.append(", ");
	    sb.append(this.centroids.indexOf(doc.getDocCluster()));
	    sb.append("\n");
	}

	DataOutputStream out = null;
	try {
	    final FileOutputStream fio = new FileOutputStream(filePath);
	    out = new DataOutputStream(fio);
	    out.write(sb.toString().getBytes("UTF-8"));
	    out.flush();
	}
	finally {
	    if (out != null) {
		out.close();
	    }
	}
    }

}
