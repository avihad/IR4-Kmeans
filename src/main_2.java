import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;


public class main_2 {

	
	public static void brows_directory(File dir){
	    File[] files = dir.listFiles();
	    for(File file:files){
	    	if (!file.isDirectory()){
	    		String path = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("webkb") ).replace("\\","/");

	    		doc  tmp_doc = dictionary.nameTodoc.get(path);
	    		if (tmp_doc==null)
	    			continue;
	    		tmp_doc.parse_doc(file.getAbsolutePath());
	    	}
	    		
	       // System.out.println(file.getAbsolutePath());
	        if(file.listFiles() != null)
	        	brows_directory(file);        
	    }
	} 
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		


		
	
		
		precision_calculator.gold_standart_reader(new File("C:\\Users\\yaron\\Desktop\\docIDs.txt"));
		brows_directory(new File("C:\\Users\\yaron\\Desktop\\webkb"));
		
		ArrayList<sparse_vec> vectors= new ArrayList<sparse_vec> ();
		for (doc tmp_doc : dictionary.nameTodoc.values()){
			vectors.add(tmp_doc.create_tf_idf_vector());
		}
		k_mean local_kMean = new k_mean(vectors);
		local_kMean.calc_k_mean(7);
	}

}
