package idc;
import idc.algorithms.Kmean;
import idc.algorithms.KmeanPlus;
import idc.algorithms.PrecisionCalculator;
import idc.datastructs.Dictionary;
import idc.datastructs.Doc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
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

	    if (file.listFiles() != null) {
		browesDirectory(file);
	    }
	}
    }

    public static void main(String[] args) throws InterruptedException, Exception {

	// Read property file
	if (args.length < 0) {
	    System.out.println("Please enter property file path");
	    System.exit(-1);
	}

	final InputStream fis = new FileInputStream(args[0]);
	final Properties prop = new Properties();
	prop.load(fis);

	final String outputFile = prop.getProperty("outputFile");
	final Integer clusterAmount = Integer.parseInt(prop.getProperty("k"));
	final String algoName = prop.getProperty("retrivalAlgorithm");

	final long timeDelta = System.currentTimeMillis();
	PrecisionCalculator.goldStandartReader(new File("docIDs.txt"));
	System.out.println("Gold Standart Reader running time: "
		+ ((System.currentTimeMillis() - timeDelta) / 1000.0));

	browesDirectory(new File("C:\\Devel\\work\\webkb"));

	final ExecutorService threadPool = Executors.newFixedThreadPool(4);

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
	Kmean localKMean;
	if (algoName.equals("basic")) {
	    localKMean = new Kmean(new ArrayList<Doc>(Dictionary.nameTodoc.values()), Boolean.FALSE);
	} else if (algoName.equals("basic++")) {
	    localKMean = new KmeanPlus(new ArrayList<Doc>(Dictionary.nameTodoc.values()), Boolean.FALSE);
	} else { // as default set the advance algorithm
	    localKMean = new KmeanPlus(new ArrayList<Doc>(Dictionary.nameTodoc.values()), Boolean.TRUE);

	}

	localKMean.calcKmean(clusterAmount);

	localKMean.calcPurity();
	localKMean.calcRandIndex();

	localKMean.printPurityAndRandIndex();

	localKMean.writeOutputFile(outputFile);

    }
}
