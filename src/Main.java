import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void browesDirectory(File dir) {
	final File[] files = dir.listFiles();
	for (final File file : files) {
	    if (!file.isDirectory()) {
		final String path = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("webkb"))
			.replace("\\", "/");

		final Doc tmp_doc = Dictionary.nameTodoc.get(path);
		if (tmp_doc == null) {
		    continue;
		}
		tmp_doc.parseDoc(file.getAbsolutePath());
	    }

	    // System.out.println(file.getAbsolutePath());
	    if (file.listFiles() != null) {
		browesDirectory(file);
	    }
	}
    }

    public static void main(String[] args) throws InterruptedException {
	PrecisionCalculator.goldStandartReader(new File("C:\\Users\\yaron\\Desktop\\docIDs.txt"));
	browesDirectory(new File("C:\\Users\\yaron\\Desktop\\webkb"));

	final List<SparseVector> vectors = new ArrayList<SparseVector>();
	for (final Doc tmpDoc : Dictionary.nameTodoc.values()) {
	    vectors.add(tmpDoc.createTfIdfVector());
	}
	final Kmean localKMean = new Kmean(vectors);
	localKMean.calcKmean(7);
    }

}
