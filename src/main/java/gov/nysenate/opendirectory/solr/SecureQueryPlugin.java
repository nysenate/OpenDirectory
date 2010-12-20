package gov.nysenate.opendirectory.solr;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.LuceneQParserPlugin;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SolrQueryParser;


public class SecureQueryPlugin extends LuceneQParserPlugin {
	public static String NAME = "secure";

	@SuppressWarnings("unchecked")
	public void init(NamedList args) {}

	public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
		return new SecureQueryParser(qstr, localParams, params, req); 
	}
} 

class SecureQueryParser extends QParser {
	String sortStr;
	SolrQueryParser lparser;
	
	public final String DEFAULT_CREDENTIAL = "public";
	
	public SecureQueryParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
		super(qstr, localParams, params, req);
	}

	public static String secureQuery(String query,String credential) {
		String newQuery = "";
		TreeSet<String> basics = new TreeSet<String>(Arrays.asList("uid","otype","firstName","lastName","fullName"));
		Pattern p = Pattern.compile("(.*?)(\\w+):((?:\\(.+?\\))|(?:\\[.+?\\])|\\w+|(?:\"[\\w ]+\"))([\\) ]*)?");
		Matcher m = p.matcher(query);
		while(m.find()) {
			if(basics.contains(m.group(2)))
				newQuery += m.group(1)+m.group(2)+":"+m.group(3);
			else
				newQuery += m.group(1)+"("+m.group(2)+":"+m.group(3)+" AND "+m.group(2)+"_access:("+credential+") )"+m.group(4);
		}
		System.out.println("Transformed Query: "+newQuery);
		return newQuery;
	}
	
	public Query parse() throws ParseException {
		
		String credential = (this.localParams.get("credential")!=null) ? this.localParams.get("credential") : DEFAULT_CREDENTIAL;
		String qstr = getString(); 
		
		if (qstr == null) return null;
		
		String defaultField = getParam(CommonParams.DF);
		if (defaultField==null)
			defaultField = getReq().getSchema().getDefaultSearchFieldName();
		
		lparser = new SolrQueryParser(this, defaultField);
		
		// I need the Lucene package to make this work
		// import org.apache.lucene.queryParser.QueryParser; 
		// lparser.setDefaultOperator(QueryParsing.getQueryParserDefaultOperator(getReq().getSchema(),getParam(QueryParsing.OP)));
		
		return lparser.parse(secureQuery(qstr,credential));
	}
	
	public String[] getDefaultHighlightFields() {
		return lparser == null ? new String[]{} : new String[]{lparser.getField()};
	}
}