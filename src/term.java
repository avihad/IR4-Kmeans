import java.util.HashMap;


public class term {

	HashMap<Integer, Integer> doc_frequency = new HashMap<Integer, Integer>();
	String Term;
	
	public  double DF (){
		return (double)dictionary.nameTodoc.size()/doc_frequency.size();
	}
	public  double tf_DF (int doc_id){
		if (!doc_frequency.containsKey(doc_id))
			return 0;
		return this.DF()*doc_frequency.get(doc_id);
	}
	
	
	public void incrementDoc_freq(int docId){
		if (!doc_frequency.containsKey(docId))
			doc_frequency.put(docId, 1);
		else
			doc_frequency.put(docId, doc_frequency.get(docId)+1);
	}
}
