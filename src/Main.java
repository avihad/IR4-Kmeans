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

		final Doc tmpDoc = Dictionary.nameTodoc.get(path);
		if (tmpDoc == null) {
		    continue;
		}
		tmpDoc.parseDoc(file.getAbsolutePath());
	    }

	    // System.out.println(file.getAbsolutePath());
	    if (file.listFiles() != null) {
		browesDirectory(file);
	    }
	}
    }

    public static void main(String[] args) throws InterruptedException {
	long timeDelta = System.currentTimeMillis();
	PrecisionCalculator.goldStandartReader(new File("docIDs.txt"));
	System.out.println("Gold Standart Reader running time: "
		+ ((System.currentTimeMillis() - timeDelta) / 1000.0));

	timeDelta = System.currentTimeMillis();
	browesDirectory(new File("C:\\Devel\\work\\webkb"));
	System.out.println("BrowseDirectory running time: "
		+ ((System.currentTimeMillis() - timeDelta) / 1000.0));

	final List<SparseVector> vectors = new ArrayList<SparseVector>();
	for (final Doc tmpDoc : Dictionary.nameTodoc.values()) {
	    vectors.add(tmpDoc.createTfIdfVector());
	}
	final Kmean localKMean = new Kmean(vectors);

	timeDelta = System.currentTimeMillis();
	localKMean.calcKmean(7);
	System.out.println("Kmeans running time: " + ((System.currentTimeMillis() - timeDelta) / 1000.0));

    }

}
