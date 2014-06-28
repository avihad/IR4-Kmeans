import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class KmeanPlus extends Kmean {

    public KmeanPlus(List<Doc> docs) throws InterruptedException {
	super(docs);
    }

    @Override
    protected void initCentroids(int k) {

	final int size = this.docs.size();
	final Random rand = new Random();
	final Double firstCendroid = rand.nextDouble() * size;
	this.centroids.add(firstCendroid.intValue());
	Map<Integer, Double> distances;
	Double distanceSum;
	for (int i = 0; i < k - 1; i++) {
	    distances = new HashMap<Integer, Double>();
	    distanceSum = 0.0;
	    for (int j = 0; j < this.docs.size(); j++) {
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

	    final Map<Integer, Double> probDistMap = new LinkedHashMap<Integer, Double>();
	    Double sum = distancesList.get(0).getValue();
	    for (int j = 1; j < distancesList.size(); j++) {
		final Entry<Integer, Double> docIdTodistance = distancesList.get(j);
		probDistMap.put(docIdTodistance.getKey(), docIdTodistance.getValue() + sum);
		sum += docIdTodistance.getValue();
	    }

	    final List<Entry<Integer, Double>> probDistList = new ArrayList<Entry<Integer, Double>>(
		    probDistMap.entrySet());
	    Collections.sort(probDistList, new Comparator<Entry<Integer, Double>>() {

		@Override
		public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
		    return o1.getValue().compareTo(o2.getValue());
		}
	    });

	    final double randNum = rand.nextDouble();
	    int clusterIndex = probDistList.get(0).getKey();
	    for (int j = 1; j < probDistList.size(); j++) {
		if (probDistList.get(j - 1).getValue() < randNum && probDistList.get(j).getValue() >= randNum) {
		    clusterIndex = probDistList.get(j).getKey();
		}
	    }

	    this.centroids.add(clusterIndex);

	    /*
	     * int clusterIndex = rand.nextDouble(); clusterIndex = clusterIndex == probDistMap.size() ?
	     * probDistMap.size() - 1 : clusterIndex; clusterIndex = clusterIndex > 0 ? clusterIndex :
	     * Math.abs(clusterIndex) - 1;
	     * 
	     * clusterIndex = new LinkedList<Entry<Integer, Double>>(probDistMap.entrySet()).get(clusterIndex)
	     * .getKey();
	     * 
	     * this.centroids.add(clusterIndex);
	     */
	}

    }
}
