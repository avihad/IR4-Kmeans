package idc.algorithms;
import idc.datastructs.Dictionary;
import idc.datastructs.Doc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

// class for calc precisions
public class PrecisionCalculator {

    // init - read file and fill map
    public static void goldStandartReader(File truthFilePath) {

	// File truth = truth_input;

	BufferedReader in = null;
	Doc tmpDoc;

	// reading the truth file
	try {
	    in = new BufferedReader(new FileReader(truthFilePath));
	}
	catch (final FileNotFoundException e) {
	    e.printStackTrace();
	}
	int docId;
	String docName;
	int cluster;

	// reading the truth format and fill the map
	try {
	    while (in.ready()) {
		final String s = in.readLine();
		final Scanner s2 = new Scanner(s);
		docId = Integer.parseInt(s2.next());
		// s2.next(); // read the 0
		docName = (s2.next()).replaceAll(":", "_");
		cluster = Integer.parseInt(s2.next());
		s2.close();

		tmpDoc = new Doc(docName, docId, cluster);
		Dictionary.nameTodoc2.put(docName, tmpDoc);
		Dictionary.docToname2.put(tmpDoc, docName);

	    }
	}

	catch (final Exception e) {
	    e.printStackTrace();
	}

    }

}
