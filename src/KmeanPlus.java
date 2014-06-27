import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class KmeanPlus extends Kmean {

    public KmeanPlus(List<SparseVector> vectors) throws InterruptedException {
	super(vectors);
    }

    @Override
    protected void initCentroids(int k) {

	final int size = this.vectors.size();
	final Random rand = new Random();
	final Double firstCendroid = rand.nextDouble() * size;
	this.centroids.add(firstCendroid.intValue());
	Map<Integer, Double> distances;
	Double distanceSum;
	for (int i = 0; i < k - 1; i++) {
	    distances = new HashMap<Integer, Double>();
	    distanceSum = 0.0;
	    for (int j = 0; j < this.vectors.size(); j++) {
		double termDistanceSqure = super.findClosestCentroidDistance(j);
		termDistanceSqure = termDistanceSqure * termDistanceSqure;
		distances.put(j, termDistanceSqure);
		distanceSum += termDistanceSqure;
	    }

	    final List<Map.Entry<Integer, Double>> distancesList = new LinkedList<Map.Entry<Integer, Double>>(
		    distances.entrySet());

	    for (final Map.Entry<Integer, Double> docIdTodistance : distancesList) {
		docIdTodistance.setValue(docIdTodistance.getValue() / distanceSum);
	    }

	    Collections.sort(distancesList, new Comparator<Map.Entry<Integer, Double>>() {
		@Override
		public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
		    return (o1.getValue()).compareTo(o2.getValue());
		}
	    });

	    final List<Double> probDistList = new ArrayList<Double>();
	    for (int j = 1; j < distancesList.size(); j++) {
		final Entry<Integer, Double> docIdTodistance = distancesList.get(j);
		final Entry<Integer, Double> lastDocIdTodistance = distancesList.get(j - 1);
		probDistList.add(docIdTodistance.getKey(),
			docIdTodistance.getValue() + lastDocIdTodistance.getValue());
	    }

	    int clusterIndex = Collections.binarySearch(probDistList, rand.nextDouble());
	    clusterIndex = clusterIndex == probDistList.size() ? probDistList.size() - 1 : clusterIndex;
	    clusterIndex = clusterIndex > 0 ? clusterIndex : Math.abs(clusterIndex) - 1;

	    this.centroids.add(clusterIndex);

	}

    }
}
