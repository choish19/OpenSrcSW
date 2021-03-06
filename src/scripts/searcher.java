package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import scripts.indexer.IntDouble;

public class searcher {
	private String input_file;
	private String query;
	private String command;

	public searcher(String command, String path, String query) throws ClassNotFoundException, IOException, ParserConfigurationException, SAXException {
		this.input_file = path;
		this.query = query;
		this.command = command;
		if(command.equals("-q")) {
			this.CalcSim();
		}
	}
	public void CalcSim() throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
		FileInputStream fileStream = new FileInputStream(this.input_file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		File file = new File("./index.xml");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document docs = docBuilder.parse(file);
		NodeList docList = docs.getElementsByTagName("doc");
		int docNum = docList.getLength(); //문서 개수
		
		HashMap<String,ArrayList<IntDouble>> keyMap = (HashMap<String,ArrayList<IntDouble>>) object;
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(this.query, true);
		double[] innerArray = new double[docNum]; //내적 배열
		double[] qSize = new double[docNum]; //query 벡터 크기 배열
		double[] idSize = new double[docNum]; //키워드 벡터 크기 배열
		double[] simArray = new double[docNum]; //유사도 배열 
		//simArray의 index는 문서번호를 나타냄.
		/*
		 * keyMap의 구조
		 * key: (String)키워드
		 * value: (ArrayList)[(IntDouble)[문서번호, 가중치],[문서번호, 가중치],...]
		 */
		//IntDouble { int num(문서번호), double w(가중치) }
		innerArray = InnerProduct();
		for(int k = 0; k < kl.size(); k++) {
			Keyword kwrd = kl.get(k);
			String key = kwrd.getString();
			int w = kwrd.getCnt();
			for(int i = 0; i < docNum; i++) {
				qSize[i] += w * w; //query의 가중치 제곱 계산.
				if(keyMap.containsKey(key)) {
					for(int j = 0; j < keyMap.get(key).size(); j++) { //해당 키워드를 포함한 문서 수
						if(keyMap.get(key).get(j).num == i) { //문서번호가 같다면
							idSize[i] += keyMap.get(key).get(j).w * keyMap.get(key).get(j).w; //해당 id문서의 가중치 제곱 계산
							//keyMap에 키워드의 가중치가 0인 문서 번호는 포함되어있지 않으므로 계산에서 제외됨. qSize의 가중치 제곱 계산 부분이 바깥에 있는 이유
							break;
						}
					}
				}
			}
		}
		for(int i = 0; i < docNum; i++) {
			simArray[i] = innerArray[i] / (Math.sqrt(qSize[i]) * Math.sqrt(idSize[i])); //코사인 유사도 계산
		}
		double[] q = {0, 0, 0}; //유사도가 큰 순서의 유사도를 담기 위한 배열
		int[] idx = {-1, -1, -1}; //유사도가 큰 순서의 index를 담기 위한 배열
		for(int i = 0; i < docNum; i++) { //작은 index부터 비교하므로 유사도가 같은 경우는 가장 작은 index가 할당된다.
			if(simArray[i] > q[0]) { //유사도가 가장 클 경우, 1->2, 0->1 값을 미룬 뒤, 0번째에 값을 할당
				q[2] = q[1];
				q[1] = q[0];
				idx[2] = idx[1];
				idx[1] = idx[0];
				q[0] = simArray[i];
				idx[0] = i;
			}
			else if(simArray[i] > q[1]) { //두번째로 클 경우, 1->2 값을 미룬 뒤, 1번째에 값을 할당
				q[2] = q[1];
				idx[2] = idx[1];
				q[1] = simArray[i];
				idx[1] = i;
			}
			else if(simArray[i] > q[2]) { //세번째로 클 경우, 2번째에 값을 할당
				q[2] = simArray[i];
				idx[2] = i;
			}
		}
		if(idx[0] == -1) { //유사도가 모두 0인 경우
			System.out.println("검색된 문서가 없습니다.");
			return;
		}
		NodeList titleList = docs.getElementsByTagName("title");
		for(int i = 0; i < 3; i++) {
			if(idx[i] == -1) break;
			String title = titleList.item(idx[i]).getTextContent();
			System.out.println("-" + title + "\t\t유사도: " + String.format("%.2f", simArray[idx[i]]));
		}
	}
	public double[] InnerProduct() throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
		FileInputStream fileStream = new FileInputStream(this.input_file);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		File file = new File("./index.xml");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document docs = docBuilder.parse(file);
		NodeList docList = docs.getElementsByTagName("doc");
		int docNum = docList.getLength(); //문서 개수
		
		HashMap<String,ArrayList<IntDouble>> keyMap = (HashMap<String,ArrayList<IntDouble>>) object;
		KeywordExtractor ke = new KeywordExtractor();
		KeywordList kl = ke.extractKeyword(this.query, true);
		double[] qArray = new double[docNum]; //유사도 배열
		//qArray의 index는 문서번호를 나타냄.
		/*
		 * keyMap의 구조
		 * key: (String)키워드
		 * value: (ArrayList)[(IntDouble)[문서번호, 가중치],[문서번호, 가중치],...]
		 */
		//IntDouble { int num(문서번호), double w(가중치) }
		for(int k = 0; k < kl.size(); k++) {
			Keyword kwrd = kl.get(k);
			String key = kwrd.getString();
			int w = kwrd.getCnt();
			for(int i = 0; i < docNum; i++) {
				if(keyMap.containsKey(key)) {
					for(int j = 0; j < keyMap.get(key).size(); j++) {
						if(keyMap.get(key).get(j).num == i) { //문서번호가 같다면
							qArray[i] += w * keyMap.get(key).get(j).w; //해당 문서의 유사도 계산
							break;
						}
					}
				}
			}
		}
		return qArray;
	}
	public void searchPost() {
		System.out.println("5주차 실행완료");
	}

}
