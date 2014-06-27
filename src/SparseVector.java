import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SparseVector {
    private int			    index  = 0;
    private ConcurrentMap<Integer, Double> vector = new ConcurrentHashMap<Integer, Double>();

    boolean add(Double e) {
	if (e != 0) {
	    this.vector.put(this.index, e);
	}
	this.index++;
	return true;
    }

    double get(int e) {
	if (e < this.index) {
	    if (!this.vector.containsKey(e)) {
		return 0;
	    } else {
		return this.vector.get(e);
	    }
	}
	return 0;

    }

    boolean set(Integer location, Double e) {
	if (e == 0) {
	    this.vector.remove(location);
	} else {
	    this.vector.put(location, e);
	}
	return true;
    }

    int size() {
	return this.index;
    }

    public ConcurrentMap<Integer, Double> getVector() {
	return this.vector;
    }

}
