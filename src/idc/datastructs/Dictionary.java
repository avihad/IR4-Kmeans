package idc.datastructs;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {

    public static Map<String, Term> TermList   = new HashMap<String, Term>();
    public static Map<String, Doc>  nameTodoc  = new HashMap<String, Doc>();
    public static Map<Doc, String>  docToname  = new HashMap<Doc, String>();
    public static Map<String, Doc>  nameTodoc2 = new HashMap<String, Doc>();
    public static Map<Doc, String>  docToname2 = new HashMap<Doc, String>();

}
