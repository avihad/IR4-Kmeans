import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	    // TODO Auto-generated catch block
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
		/*
		 * if (!dictionary.nameTodoc.containsKey(doc_name)){ System.out.println("doc " + doc_name +
		 * " is not found"); continue; }
		 */
		tmpDoc = new Doc(docName, docId, cluster);
		// tmpDoc = dictionary.nameTodoc.get(doc_name);
		Dictionary.nameTodoc2.put(docName, tmpDoc);
		Dictionary.docToname2.put(tmpDoc, docName);

	    }
	}
	catch (final NumberFormatException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
    // for debuging - print the truth values

}
