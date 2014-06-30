package idc.datastructs;
import java.util.HashMap;

public class Term {

    private HashMap<Integer, Integer> docFrequency = new HashMap<Integer, Integer>();
    private String		    term;

    public Term(String word) {
	this.term = word;
    }

    public double dF() {
	return (double) Dictionary.nameTodoc.size() / this.docFrequency.size();
    }

    public double tf_DF(int docId) {
	if (!this.docFrequency.containsKey(docId)) {
	    return 0;
	}
	return this.dF() * this.docFrequency.get(docId);
    }

    public void incrementDocFreq(int docId) {
	Integer docInstances = this.docFrequency.get(docId);
	docInstances = docInstances == null ? 1 : docInstances++;
	this.docFrequency.put(docId, docInstances);
    }
}
