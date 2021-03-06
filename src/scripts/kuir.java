package scripts;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class kuir {

	public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, SAXException, ClassNotFoundException {
		
		String command = args[0];   
		String path = args[1];

		if(command.equals("-c")) {
			makeCollection collection = new makeCollection(path);
			collection.makeXml();
		}
		else if(command.equals("-k")) {
			makeKeyword keyword = new makeKeyword(path);
			keyword.convertXml();
		}
		else if(command.equals("-i")) {
			indexer indexer = new indexer(path);
			indexer.convertXml();
		}
		else if(command.equals("-s")){
			String command2 = args[2];
			String query = args[3];
			searcher searcher = new searcher(command2, path, query);
			searcher.searchPost();
		}
	}
}
