package scripts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 2주차 실습 코드
 * 
 * 주어진 5개의 html 문서를 전처리하여 하나의 xml 파일을 생성하세요. 
 * 
 * input : data 폴더의 html 파일들
 * output : collection.xml 
 */


public class makeCollection {
	
	private String data_path;
	private String output_flie = "./collection.xml";
	
	public static File[] makeFileList(String path) {
		File dir = new File(path);
		return dir.listFiles();
	}
	
	public makeCollection(String path) throws ParserConfigurationException, IOException, TransformerException {
		this.data_path = path;
		File[] file = makeFileList(this.data_path); //
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element docs = doc.createElement("docs");
		doc.appendChild(docs);
		for(int i = 0; i < file.length; i++) { //파일의 수만큼 for문 반복
			org.jsoup.nodes.Document html = Jsoup.parse(file[i],"UTF-8");
			String titleData = html.title();
			String bodyData = html.body().text();
			
			Element doc_id = doc.createElement("doc");
			docs.appendChild(doc_id);
			String to = Integer.toString(i);
			doc_id.setAttribute("id", to);
			
			
			Element title = doc.createElement("title");
			title.appendChild(doc.createTextNode(titleData));
			doc_id.appendChild(title);
			
			
			Element body = doc.createElement("body");
			body.appendChild(doc.createTextNode(bodyData));
			doc_id.appendChild(body);
		}
		
		
		
		
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new FileOutputStream(new File(this.output_flie)));
		
		transformer.transform(source, result);
	}
	
	public void makeXml(){
		System.out.println("2주차 실행완료");
	}
	
}
