package myPackage;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ValidER {

	private boolean isValid = false;
	private String jsonString = "hollo";
	private JSONObject json = null;

	private HashMap<String, String> keyText = new HashMap<String, String>();
	private HashMap<String, String[]> nodeLink = new HashMap<String, String[]>();

	public JSONArray nodeArr = new JSONArray();
	public JSONArray linkArr = new JSONArray();

	public ValidER(String jsonString) {
		// TODO Auto-generated constructor stub
		this.jsonString = jsonString;
	}

	public void makeJson() {
		JSONParser parser = new JSONParser();
		try {
			json = ((JSONObject) parser.parse(jsonString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nodeArr = (JSONArray) json.get("nodeDataArray");
		linkArr = (JSONArray) json.get("linkDataArray");
		// JSONArray

	}

	public String makeErrorMsg() {
		String errorMsg = "";
		makeJson();

		for (int i = 0; i < nodeArr.size(); i++) {
			JSONObject node = ((JSONObject) nodeArr.get(i));
			
			if (node.get("text").toString().trim().equals("")) {
				errorMsg += "Please input node's name<br>";
			} // 노드들에 이름을 안썻을 때 에러메시지 추가 <통과

			keyText.put(node.get("key").toString(), node.get("text").toString());
		}
		// linkData traverse for getting lot of information

		for (int i = 0; i < linkArr.size(); i++) {
			JSONObject link = ((JSONObject) linkArr.get(i));// Link array들중 한줄 씩
			String linkType = link.get("type").toString().trim(); // 얻어 오는 것
			if (link.get("to") == null)
				errorMsg += "No link (to)!<br>";
			if (link.get("from") == null)
				errorMsg += "No link (from)!<br>";
			if ((link.get("multi") == null || link.get("multi").toString().trim().equals("")) && (linkType.equals("r")))
				errorMsg += "No input (Mulit or 1)!<br>";
			else { // 멀티값이 필요한경우인데도 불구하고 멀티값이 없으면 < 통과
				if (linkType.equals("r")) {
					if (!(link.get("multi").toString().trim().equalsIgnoreCase("m")
							|| link.get("multi").toString().trim().equalsIgnoreCase("1"))) {
						errorMsg += "Incorrect multi : " + keyText.get(link.get("from").toString()) + " ~ "
								+ keyText.get(link.get("to").toString()) + "<br>";
					}
				}
			}
		}

		JSONObject fromNode = null;
		JSONObject linkNode = null;
		JSONObject toNode = null;

		String from = "";
		String fromType = "";

		String linkFrom = "";
		String linkTo = "";
		String linkType = "";
		String linkMulti = "";
		String attrType = "";

		String to = "";
		String toType = "";

		int cnt = 0;
		boolean hasKey = false;
		boolean validCondition = true;
		
		if ((nodeArr.size() == 0)) {
			errorMsg += "No node";
			if ((linkArr.size() == 0))
				errorMsg = "No node and No link";
		} else if ((linkArr.size() == 0)) {
			errorMsg += "No link";
		} else {
			for (int f = 0; f < nodeArr.size(); f++)// from노드를 위한 포문
			{
				fromNode = (JSONObject) nodeArr.get(f);
				from = fromNode.get("key").toString().trim();
				fromType = fromNode.get("type").toString().trim();
				for (int l = 0; l < linkArr.size(); l++) {
					linkNode = (JSONObject) linkArr.get(l);
					linkType = linkNode.get("type").toString().trim();
					if(linkNode.get("attriType")!=null)
						attrType = linkNode.get("attriType").toString().trim();

					if (linkNode.get("to") == null) {
						validCondition=false;
					} else
						linkTo = linkNode.get("to").toString().trim();

					if (linkNode.get("from") == null) {
						validCondition=false;
					} else
						linkFrom = linkNode.get("from").toString().trim();

					if (linkType.equals("r")) {
						if (linkNode.get("multi") == null || linkNode.get("multi").toString().trim().equals("")) 
							validCondition=false;
					}

					if (validCondition&&linkFrom.equals(from))// 링크가 from이랑 같을때
					{
						for (int t = 0; t < nodeArr.size(); t++) {
							toNode = (JSONObject) nodeArr.get(t);
							to = toNode.get("key").toString().trim();
							toType = toNode.get("type").toString().trim();

							if (linkTo.equals(to))// 링크의 to가 같을때
							{
								cnt++;
								if (fromType.equalsIgnoreCase("e") && toType.equalsIgnoreCase("a")) {
									if (attrType.equals("k"))
										hasKey = true;// key 값이 있는 지 없는 지 확인
									else if (linkType.equals("r"))// 
										errorMsg += "Not proper link " + fromNode.get("text").toString() + " - "+ toNode.get("text") + "<br>";
								} else if (fromType.equalsIgnoreCase("a") && toType.equalsIgnoreCase("e")//from = attribute, to = entity
										&& (linkType.equalsIgnoreCase("a") || linkType.equalsIgnoreCase("m"))) {
									errorMsg += "Please Change direction " + fromNode.get("text").toString() + "->"+ toNode.get("text").toString() + "<br>";
								} else if (fromType.equalsIgnoreCase("a") && toType.equalsIgnoreCase("a"))
									errorMsg += "Please remove link " + fromNode.get("text").toString() + " - "+ toNode.get("text").toString() + "<br>";
								  else if (fromType.equalsIgnoreCase("e") && toType.equalsIgnoreCase("e"))
									errorMsg += "Please remove link " + fromNode.get("text").toString() + " - "+ toNode.get("text").toString() + "<br>";
								  else if((fromType.equalsIgnoreCase("e")&&toType.equalsIgnoreCase("r"))||(fromType.equalsIgnoreCase("r")&&toType.equalsIgnoreCase("e"))){
									if(!linkType.equals("r"))
									{
										errorMsg+="Please link correctly "+keyText.get(from)+" - "+keyText.get(to)+"<br>";
									}
								  }
								  else if(fromType.equalsIgnoreCase("r")&&toType.equalsIgnoreCase("a"))
								  {
									  if(linkType.equalsIgnoreCase("r"))
										  errorMsg += "Not proper link " + fromNode.get("text").toString() + " - "+ toNode.get("text") + "<br>";
								  }
								  else if(fromType.equalsIgnoreCase("a")&&toType.equalsIgnoreCase("r"))
								  {
									  if(linkType.equalsIgnoreCase("r"))
										  errorMsg += "Not proper link " + fromNode.get("text").toString() + " - "+ toNode.get("text") + "<br>";
									  else if(!linkType.equalsIgnoreCase("k"))
										  errorMsg +="Please Change direction " + fromNode.get("text").toString() + "->"+ toNode.get("text").toString() + "<br>";
								  }
							}
						}
					}
				}
				if (hasKey == false && fromType.equals("E"))
					errorMsg += "No KEY " + fromNode.get("text").toString() + "<br>";
				else if (hasKey == true)
					hasKey = false;
				/*
				 * JSONObject fromNode = (JSONObject) nodeArr.get(j); if
				 * (link.get("from") != null) { if
				 * (link.get("from").toString().trim().equals(fromNode.get("key"
				 * ). toString().trim())) {// from
				 * 
				 * from = fromNode.get("key").toString().trim(); String fromType
				 * = fromNode.get("type").toString().trim(); for (int k = 0; k <
				 * nodeArr.size(); k++) {// to노드 일치 // 여부 확인 // attribute먼저 포문
				 * JSONObject toNode = (JSONObject) nodeArr.get(k); if
				 * (link.get("to").toString().trim().equals(toNode.get("key").
				 * toString().trim())) { String to =
				 * toNode.get("key").toString().trim(); String toType =
				 * toNode.get("type").toString().trim(); if
				 * (fromType.equals("E") && toType.equals("A")) { cnt++; if
				 * (linkType.equalsIgnoreCase("k")) hasKey=true;
				 * 
				 * } else if (fromType.equals("A") && toType.equals("E")) { if
				 * (link.get("type").toString().trim().equals("a")) errorMsg +=
				 * "Please Change direction " + fromNode.get("text").toString()
				 * + "->" + toNode.get("text").toString() + "<br>"; // >통과 } } } }
				 * }
				 */
			}
		}
		return errorMsg;

	}
}
