import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.document.Document;

// class for calc precisions
public class precision_calculator {


	//init - read file and fill map
	public static void gold_standart_reader(File truth) {
		
		//File truth = truth_input;
	

		BufferedReader in=null;
		doc tmp_doc;

		// reading the truth file
		try {
			in = new BufferedReader(new FileReader(truth));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int  doc_num;
		String doc_name;
		int cluster ;
		
		// reading the truth format and fill the map
		try {
			while (in.ready()) {
				String s = in.readLine();
				Scanner s2 = new Scanner(s);
				doc_num = Integer.parseInt(s2.next()); 
				//s2.next(); // read the 0 
				doc_name = (s2.next()).replaceAll(":", "_");
				cluster = Integer.parseInt(s2.next());
				s2.close();
			/*
				if (!dictionary.nameTodoc.containsKey(doc_name)){
					System.out.println("doc " + doc_name + " is not found");
					continue;
				}
				*/
				tmp_doc = new doc();
				tmp_doc.document= doc_name;
				//tmp_doc = dictionary.nameTodoc.get(doc_name);
				tmp_doc.docId = doc_num;
				tmp_doc.doc_gold_stadart_cluster = cluster;
				dictionary.nameTodoc.put(doc_name, tmp_doc);
				dictionary.docToname.put(tmp_doc, doc_name);
				
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// for debuging - print the truth values



}
