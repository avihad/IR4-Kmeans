import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;

//import org.tartarus.snowball.ext.PorterStemmer;

public class Doc {

    private List<Term> termList = new ArrayList<Term>();
    private String     document;
    private int	docId;
    private int	docCluster;
    private int	docGoldStadartCluster;

    public Doc(String document, int docId, int docGoldStadartCluster) {
	super();
	this.document = document;
	this.docId = docId;
	this.docGoldStadartCluster = docGoldStadartCluster;
    }

    public void parseDoc(String fileLocation) {

	InputStream inputFile;
	try {
	    inputFile = new FileInputStream(fileLocation);

	    final BodyContentHandler handler = new BodyContentHandler(10000000);
	    final Metadata metadata = new Metadata();

	    new HtmlParser().parse(inputFile, handler, metadata, new ParseContext());
	    final String plainText = handler.toString();

	    this.parseText(plainText);
	}
	catch (final Exception e) {
	    e.printStackTrace();
	}

    }

    public void parseText(String file) {
	final String[] words = file.split("\\s");

	for (int i = 0; i < words.length; i++) {
	    words[i] = words[i].replaceAll("[^A-Za-z ]", "").toLowerCase();
	}
	Term tmpTerm;
	for (final String word : words) {
	    if ((word.length() <= 3) || (word.length() >= 11)) {
		continue;
	    }
	    if (isStopWord(word)) {
		continue;
	    }

	    if (!Dictionary.TermList.containsKey(word)) {
		tmpTerm = new Term(word);
		Dictionary.TermList.put(word, tmpTerm);

	    }
	    this.termList.add(Dictionary.TermList.get(word));
	    Dictionary.TermList.get(word).incrementDocFreq(this.docId);

	}

    }

    /*
     * private String stemmer(String word){ PorterStemmer obj = new PorterStemmer(); obj.setCurrent(word);
     * obj.stem(); return obj.getCurrent().toLowerCase(); }
     */
    public SparseVector createTfIdfVector() {
	final SparseVector vec = new SparseVector();
	for (final Term tmpTerm : Dictionary.TermList.values()) {
	    vec.add(tmpTerm.tf_DF(this.docId));
	}
	return vec;
    }

    public static boolean isStopWord(String word) {
	if (word.equals("a") || word.equals("an") || word.equals("and") || word.equals("are")
		|| word.equals("as") || word.equals("at") || word.equals("be") || word.equals("but")
		|| word.equals("by") || word.equals("for") || word.equals("if") || word.equals("in")
		|| word.equals("into") || word.equals("is") || word.equals("it") || word.equals("no")
		|| word.equals("not") || word.equals("of") || word.equals("on") || word.equals("or")
		|| word.equals("such") || word.equals("that") || word.equals("the") || word.equals("their")
		|| word.equals("then") || word.equals("there") || word.equals("these") || word.equals("they")
		|| word.equals("this") || word.equals("to") || word.equals("was") || word.equals("will")
		|| word.equals("withword.equals(")) {
	    return true;
	} else {
	    return false;
	}
    }

    public List<Term> getTermList() {
	return this.termList;
    }

    public void setTermList(List<Term> termList) {
	this.termList = termList;
    }

    public String getDocument() {
	return this.document;
    }

    public void setDocument(String document) {
	this.document = document;
    }

    public int getDocId() {
	return this.docId;
    }

    public void setDocId(int docId) {
	this.docId = docId;
    }

    public int getDocCluster() {
	return this.docCluster;
    }

    public void setDocCluster(int docCluster) {
	this.docCluster = docCluster;
    }

    public int getDocGoldStadartCluster() {
	return this.docGoldStadartCluster;
    }

    public void setDocGoldStadartCluster(int docGoldStadartCluster) {
	this.docGoldStadartCluster = docGoldStadartCluster;
    }

}
