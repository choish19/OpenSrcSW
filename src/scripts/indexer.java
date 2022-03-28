package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class indexer implements Serializable {
	private static final long serialVersionUID = 6813359272513434825L;
	private String input_file;
	private String output_flie = "./index.post";
	class IntDouble implements Serializable { //int, double형의 변수를 가지는 클래스
		private static final long serialVersionUID = -2680732787934146233L;
		int num;
		double w;
	}
	public indexer(String path) throws ParserConfigurationException, SAXException, IOException {
		this.input_file= path;
		File file = new File(this.input_file);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document docs = docBuilder.parse(file);
		NodeList docList = docs.getElementsByTagName("doc"); //doc태그의 노드들 불러오기
		HashMap<String, ArrayList<int[]>> keyMap = new HashMap<String,ArrayList<int[]>>();
		/*
		 * keyMap
		 * key: 키워드(string) 
		 * value: [[문서번호(int), 빈도수(int)],[문서번호, 빈도수],...]
		 */
		int docNum = docList.getLength(); //전체 문서의 수
		
		for(int i = 0; i < docNum; i++) {
			NodeList doc = docList.item(i).getChildNodes(); //i번째 doc태그의 자식 노드들 불러오기
			for(int j = 0; j < doc.getLength(); j++) {
				if(doc.item(j).getNodeName().equals("body")){ //자식 노드 중 body태그의 노드 찾기
					String[] keyWord = doc.item(j).getTextContent().split("#");
					for(int k = 0; k < keyWord.length; k++) {
						 String[] keySplit = keyWord[k].split(":"); //keySplit [0]은 키워드 [1]은 빈도수
						 int[] arr2 = new int[2];
						 arr2[0] = i; //문서번호
						 arr2[1] = Integer.parseInt(keySplit[1]); //빈도수
						 
						 if(keyMap.containsKey(keySplit[0])) { //이미 해당 key값이 있을 경우 배열만 추가
							 keyMap.get(keySplit[0]).add(arr2);
						 }
						 else { //없을 경우 ArrayList를 생성 후 추가
							 ArrayList<int[]> arr = new ArrayList<int[]>();
							 arr.add(arr2);
							 keyMap.put(keySplit[0], arr);
						 }
					}
					break;
				}
			}
		}
		/*
		 * keyMap
		 * key: 키워드(string) 
		 * value: [[문서번호(int), 빈도수(int)],[문서번호, 빈도수],...]
		 */
		Iterator<String> keys = keyMap.keySet().iterator();
		HashMap<String, ArrayList<IntDouble>> wMap = new HashMap<String, ArrayList<IntDouble>>();
		/*
		 * wMap
		 * key: 키워드(string)
		 * value: [[문서번호(int), 가중치(double)],[문서번호, 가중치],...]
		 */
		while(keys.hasNext()) { //keyMap을 순회
			String key = keys.next();
			int df = keyMap.get(key).size(); //키워드가 몇 개의 문서에서 등장하는지
			
			for(int i = 0; i < df; i++) {
				IntDouble intDouble = new IntDouble(); //문서 번호와 가중치를 담을 변수
				int tf = keyMap.get(key).get(i)[1]; //i번째 문서에서의 키워드의 빈도수
				double w = tf * Math.log((double)docNum/df); //가중치 계산
				w = Double.parseDouble(String.format("%.2f", w)); //소수점 셋째 자리에서 반올림
				intDouble.num = keyMap.get(key).get(i)[0]; //문서번호
				intDouble.w = w; //가중치
				if(wMap.containsKey(key)) { //해당 key값이 존재할 경우 ArrayList에 추가
					wMap.get(key).add(intDouble);
				}
				else { //없을 경우 ArrayList 생성 후 추가
					ArrayList<IntDouble> tmpArr = new ArrayList<IntDouble>();
					tmpArr.add(intDouble);
					wMap.put(key, tmpArr);
				}
				
			}
			
		}
		//wMap객체를 index.post파일로 저장
		FileOutputStream fileStream = new FileOutputStream(this.output_flie);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);
		
		objectOutputStream.writeObject(wMap);
		
		objectOutputStream.close();
		
		
	}
	
	
	public void convertXml() throws IOException, ClassNotFoundException{
		//index.post파일로부터 hashMap 객체를 읽어온 후 출력
		FileInputStream fileStream = new FileInputStream("./index.post");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		System.out.println("읽어온 객체의 type → " + object.getClass());
		
		HashMap hashMap = (HashMap) object;
		Iterator<String> it = hashMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			ArrayList<IntDouble> value = (ArrayList<IntDouble>)hashMap.get(key);
			
			System.out.print(key + " → ");
			for(int i = 0; i < value.size(); i++) {
				System.out.print(value.get(i).num + " " + value.get(i).w + " ");
			}
			System.out.println();
		}
		System.out.println("4주차 실행완료");
	}
}
