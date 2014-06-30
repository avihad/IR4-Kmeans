package idc.datastructs;

import java.util.HashMap;
import java.util.Map;

public class SparseVector {
    private int		  index  = 0;
    private Map<Integer, Double> vector = new HashMap<Integer, Double>();

    public boolean add(Double e) {
	if (e != 0) {
	    this.vector.put(this.index, e);
	}
	this.index++;
	return true;
    }

    public double get(int e) {
	if (e < this.index) {
	    if (!this.vector.containsKey(e)) {
		return 0;
	    } else {
		return this.vector.get(e);
	    }
	}
	return 0;

    }

    public boolean set(Integer location, Double e) {
	if (e == 0) {
	    this.vector.remove(location);
	} else {
	    this.vector.put(location, e);
	}
	return true;
    }

    public int size() {
	return this.index;
    }

    public Map<Integer, Double> getVector() {
	return this.vector;
    }

}
