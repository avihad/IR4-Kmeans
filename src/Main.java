import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void browesDirectory(File dir) {
	final File[] files = dir.listFiles();
	for (final File file : files) {
	    if (!file.isDirectory()) {
		final String path = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("webkb"))
			.replace("\\", "/");

		final Doc tmpDoc = Dictionary.nameTodoc2.get(path);
		if (tmpDoc == null) {
		    continue;
		}
		tmpDoc.parseDoc(file.getAbsolutePath());
		Dictionary.nameTodoc.put(tmpDoc.getDocumentName(), tmpDoc);
		Dictionary.docToname.put(tmpDoc, tmpDoc.getDocumentName());
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

	browesDirectory(new File("C:\\Devel\\work\\testkb\\webkb"));
	// browesDirectory(new File("C:\\Devel\\work\\webkb"));

	System.out.println("BrowseDirectory running time: "
		+ ((System.currentTimeMillis() - timeDelta) / 1000.0));

	final ExecutorService threadPool = Executors.newFixedThreadPool(4);

	timeDelta = System.currentTimeMillis();
	for (final Doc myDoc : Dictionary.nameTodoc.values()) {
	    threadPool.execute(new Runnable() {

		@Override
		public void run() {
		    myDoc.createTfIdfVector();
		}
	    });
	}
	threadPool.shutdown();
	threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

	System.out.println("Initiate sparse vectors running time: "
		+ ((System.currentTimeMillis() - timeDelta) / 1000.0));

	timeDelta = System.currentTimeMillis();
	final Kmean localKMean = new KmeanPlus(new ArrayList<Doc>(Dictionary.nameTodoc.values()));
	System.out.println("Kmeans constructor running time: "
		+ ((System.currentTimeMillis() - timeDelta) / 1000.0));

	timeDelta = System.currentTimeMillis();
	localKMean.calcKmean(7);
	System.out.println("Kmeans running time: " + ((System.currentTimeMillis() - timeDelta) / 1000.0));

	timeDelta = System.currentTimeMillis();
	localKMean.calcPurity();
	System.out.println("purity calculation running time: "
		+ ((System.currentTimeMillis() - timeDelta) / 1000.0));

	localKMean.printPurity();

    }

}
