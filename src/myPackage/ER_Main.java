package myPackage;



import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Iterator;



import org.json.simple.parser.*;

import org.json.simple.JSONArray;

import org.json.simple.JSONObject;



public class ER_Main {



//	public static void main(String[] args) {

//		// TODO Auto-generated method stub

//		JSONParser parser = new JSONParser();

//		JSONObject obj = null;

//		JSONObject obj2 = null;

//

//		try {

//			obj = (JSONObject) parser

//					.parse(new FileReader("C:/Users/Jayden/workspace/DBProject/src/myPackage/ERdiagram1.json"));

//			obj2 = (JSONObject) parser

//					.parse(new FileReader("C:/Users/Jayden/workspace/DBProject/src/myPackage/temporal.json"));

//		} catch (FileNotFoundException e) {

//			// TODO Auto-generated catch block

//			e.printStackTrace();

//		} catch (IOException e) {

//			// TODO Auto-generated catch block

//			e.printStackTrace();

//		} catch (org.json.simple.parser.ParseException e) {

//			// TODO Auto-generated catch block

//			e.printStackTrace();

//		}

//		ArrayList<String> temp = new ArrayList<>();

//

//		JSONArray tempList = (JSONArray) obj2.get("temporal");

//		Iterator<String> iterator = tempList.iterator();

//		while (iterator.hasNext()) {

//			String tmp = iterator.next();

//			temp.add(tmp); // Save array

//		}

//

//		ArrayList<Node> er1 = new ER_Maker().getER();

//		ArrayList<Node> er2 = new ER_Maker().getER(obj);

//		SearchHelper sh = new SearchHelper();

//		for (int i = 0; i < temp.size(); i++) {

//			sh.annotateTemporal(er2, temp.get(i));

//		}

//		sh.showAttribute(er2);

//	}



}