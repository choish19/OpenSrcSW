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

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 3주차 실습 코드
 * 
 * kkma 형태소 분석기를 이용하여 index.xml 파일을 생성하세요.
 * 
 * index.xml 파일 형식은 아래와 같습니다.
 * (키워드1):(키워드1에 대한 빈도수)#(키워드2):(키워드2에 대한 빈도수)#(키워드3):(키워드3에 대한 빈도수) ... 
 * e.g., 라면:13#밀가루:4#달걀:1 ...
 * 
 * input : collection.xml
 * output : index.xml 
 */

public class makeKeyword {

	private String input_file;
	private String output_flie = "./index.xml";
	
	public makeKeyword(String path) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		this.input_file = path;
		File file = new File(this.input_file);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document docs = docBuilder.parse(file);
		NodeList docList = docs.getElementsByTagName("doc"); //doc태그의 노드들 불러오기
		
		for(int i = 0; i < docList.getLength(); i++) {
			NodeList doc = docList.item(i).getChildNodes(); //i번째 doc태그의 자식 노드들 불러오기
			for(int j = 0; j < doc.getLength(); j++) {
				if(doc.item(j).getNodeName().equals("body")){ //자식 노드 중 body태그의 노드 찾기
					Node bodyNode = doc.item(j);
					KeywordExtractor ke = new KeywordExtractor();
					KeywordList kl = ke.extractKeyword(bodyNode.getTextContent(), true);
					String setText = ""; //body태그에 넣을 text
					for(int k = 0; k < kl.size(); k++) {
						Keyword kwrd = kl.get(k);
						setText += kwrd.getString() + ":" + kwrd.getCnt() + "#";
					}
					bodyNode.setTextContent(setText);
					break;
				}
			}
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		DOMSource source = new DOMSource(docs);
		StreamResult result = new StreamResult(new FileOutputStream(new File(this.output_flie)));
		
		transformer.transform(source, result);
	}

	public void convertXml() {
		System.out.println("3주차 실행완료");
	}

}
