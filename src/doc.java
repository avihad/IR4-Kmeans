import java.util.ArrayList;
import java.io.*;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.analysis.snowball.*;
import org.apache.lucene.util.*;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.tartarus.snowball.ext.PorterStemmer;
import org.xml.sax.SAXException;



public class doc {


	ArrayList<term> termList = new ArrayList<term> ();
	String document;
	int docId;
	int doc_cluster;
	int doc_gold_stadart_cluster;



	public void parse_doc(String file_location){


		InputStream inputFile;
		try {
			inputFile = new FileInputStream(file_location);

			BodyContentHandler handler =  new BodyContentHandler(10000000);
			Metadata metadata = new Metadata();

			new HtmlParser().parse(inputFile, (org.xml.sax.ContentHandler) handler, metadata, new ParseContext());
			String plainText = handler.toString();

			//System.out.println(plainText);

			this.parse_text(plainText);
		} 
		catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TikaException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	}

	public void parse_text(String file){
		String[] words = file.split("\\s");

		for (int i=0;i<words.length;i++){
			words[i] = words[i].replaceAll("[^A-Za-z ]", "").toLowerCase();
		}
		term tmp_term;
		for (String word : words){
			if ((word.length()<=3) || (word.length()>=11))
				continue;
			if (is_a_stop_word(word))
				continue;

			if (!dictionary.TermList.containsKey(word)){
				tmp_term = new term();
				tmp_term.Term = word;
				dictionary.TermList.put(word, tmp_term);

			}
			this.termList.add(dictionary.TermList.get(word));
			dictionary.TermList.get(word).incrementDoc_freq(docId);

		}

	}


	private String stemmer(String word){
		PorterStemmer obj = new PorterStemmer();
		obj.setCurrent(word);
		obj.stem();
		return obj.getCurrent().toLowerCase();
	}

	public sparse_vec create_tf_idf_vector(){
		sparse_vec vec = new sparse_vec ();
		for( term tmp_term: dictionary.TermList.values())
		{
			vec.add(tmp_term.tf_DF(this.docId));
		}
		return vec;
	}

	public static boolean is_a_stop_word (String word){
		if (word.equals("a")|| word.equals("an")|| word.equals("and")|| word.equals("are")|| word.equals("as")|| word.equals("at")|| word.equals("be")|| word.equals("but")|| word.equals("by")||
				word.equals("for")|| word.equals("if")|| word.equals("in")|| word.equals("into")|| word.equals("is")|| word.equals("it")||
				word.equals("no")|| word.equals("not")|| word.equals("of")|| word.equals("on")|| word.equals("or")|| word.equals("such")||
				word.equals("that")|| word.equals("the")|| word.equals("their")|| word.equals("then")|| word.equals("there")|| word.equals("these")||
				word.equals("they")|| word.equals("this")|| word.equals("to")|| word.equals("was")|| word.equals("will")|| word.equals("withword.equals("))
			return true;
		else
			return false;
	}
}
