package myPackage;

import java.util.ArrayList;

import org.json.simple.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ER_Maker {

	private ArrayList<Node> ER = new ArrayList<Node>();
	private ArrayList<Attribute> attArr = new ArrayList<Attribute>();
	private JSONObject json = null;

	public ArrayList<Node> getER(String txt) {
		JSONParser parser = new JSONParser();
		try {
			json = (JSONObject) parser.parse(txt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray nodeArr = (JSONArray) json.get("nodeDataArray");
		JSONArray linkArr = (JSONArray) json.get("linkDataArray");
		// JSONArray

		for (int i = 0; i < linkArr.size(); i++) {
			//System.out.println(((JSONObject) (linkArr.get(i))).toString());
			//System.out.println((nodeArr.get(i).toString()));
		}

		// first, make instance for entity node and relationship node
		for (int i = 0; i < nodeArr.size(); i++) {

			
			if (((JSONObject) nodeArr.get(i)).get("type").toString().compareTo("E") == 0) {
				Entity temp = new Entity(((JSONObject) nodeArr.get(i)).get("text").toString());
				temp.setKey(Integer.parseInt(((JSONObject) nodeArr.get(i)).get("key").toString()));
				if (((JSONObject) nodeArr.get(i)).get("isTemp").toString().compareTo("true") == 0)
					temp.setIsTemporal(true);
				ER.add(temp);
			} else if (((JSONObject) nodeArr.get(i)).get("type").toString().compareTo("R") == 0) {
				Relationship temp = new Relationship(((JSONObject) nodeArr.get(i)).get("text").toString());
				temp.setKey(Integer.parseInt(((JSONObject) nodeArr.get(i)).get("key").toString()));
				if (((JSONObject) nodeArr.get(i)).get("isTemp").toString().compareTo("true") == 0)
					temp.setIsTemporal(true);
				ER.add(temp);
			} else if (((JSONObject) nodeArr.get(i)).get("type").toString().compareTo("A") == 0) {
				Attribute temp = new Attribute(((JSONObject) nodeArr.get(i)).get("text").toString());
				temp.setKey(Integer.parseInt(((JSONObject) nodeArr.get(i)).get("key").toString()));
				if (((JSONObject) nodeArr.get(i)).get("isTemp").toString().compareTo("true") == 0)
					temp.setIsTemporal(true);
				attArr.add(temp);
			} else {
				// exception
			}
		}
		// linkData traverse for getting lot of information
		for (int i = 0; i < linkArr.size(); i++) {
			Node from = null, to = null;
			for (int j = 0; j < ER.size(); j++) {
				// System.out.println(ER.get(j).getClass().getName());
				if ((ER.get(j)).getKey() == Integer.parseInt(((JSONObject) linkArr.get(i)).get("from").toString()))
					from = ER.get(j);
				if ((ER.get(j)).getKey() == Integer.parseInt(((JSONObject) linkArr.get(i)).get("to").toString()))
					to = ER.get(j);
			}
			for (int j=0; j < attArr.size(); j++) {
				if ((attArr.get(j)).getKey() == Integer.parseInt(((JSONObject) linkArr.get(i)).get("from").toString()))
					from = attArr.get(j);
				if ((attArr.get(j)).getKey() == Integer.parseInt(((JSONObject) linkArr.get(i)).get("to").toString()))
					to = attArr.get(j);
			}
			// We knew that which node is from and which node is to
			if (from != null && to != null) {
				// 'from' save 'to'
				if (from.getClass().getName().compareTo("myPackage.Relationship") == 0) { // relationship type
					((Relationship) from).addList(to);
					if ( (((JSONObject) linkArr.get(i)).get("type").toString()).compareTo("m") == 0) { //the attribute is multi-valued
						((Attribute)to).setIsMulti(true);
					}
				}
				else if(from.getClass().getName().compareTo("myPackage.Entity") == 0) {
					((Entity) from).addList(to);
					//add attribute
					if ( (((JSONObject) linkArr.get(i)).get("type").toString()).compareTo("m") == 0) { //the attribute is multi-valued
						((Attribute)to).setIsMulti(true);
					}
					else if( (((JSONObject) linkArr.get(i)).get("type").toString()).compareTo("k") == 0 ) {
						((Attribute)to).setIsKey(true);
					}
				}
				else {
					//error
				}
				// 'to' save 'from'
				if (to.getClass().getName().compareTo("myPackage.Relationship") == 0) {
					((Relationship) to).addList(from);
				}
				else if(to.getClass().getName().compareTo("myPackage.Entity") == 0) {
					((Entity) to).addList(from);
				}
				else {
					//error
				}
			}
		}

		return ER;
	}

	public ArrayList<Node> getER(org.json.simple.JSONObject obj) {

		// first, make instance for entity node and relationship node
		for (Object key : obj.keySet()) {
			// based on you key types
			String keyStr = (String) key;
			Object keyvalue = obj.get(keyStr);

			// Print key and value
			// System.out.println("key: " + keyStr + " value: " + keyvalue);

			// Whether it is entity type or relationship type.
			JSONObject now = (JSONObject) keyvalue;
			// System.out.println(now.get("type"));

			if (now.get("type").toString().compareTo("entity") == 0) {
				Entity temp = new Entity(keyStr);
				ER.add(temp);
			} else if (now.get("type").toString().compareTo("relationship") == 0) {
				Relationship temp = new Relationship(keyStr);
				ER.add(temp);
			} else {
				// invalid input of type
			}
		}
		// Second, add nodes's information such as attribute, etc.
		for (int i = 0; i < ER.size(); i++) {
			// find node's name
			Node temp = ER.get(i);

			JSONObject info = (JSONObject) obj.get(temp.name);

			// add relations
			JSONArray rel_arr = (JSONArray) info.get("relation");
			for (int j = 0; j < rel_arr.size(); j++) {
				int count = 0;
				for (int k = 0; k < ER.size(); k++) {
					if (rel_arr.get(j).toString().compareTo(ER.get(k).name) == 0)
						break;
					count++;
				}
				if (info.get("type").toString().compareTo("entity") == 0)
					(((Entity) temp).getArrayList()).add(ER.get(count));
				else if (info.get("type").toString().compareTo("relationship") == 0)
					(((Relationship) temp).getArrayList()).add(ER.get(count));
			}
			// add attributes
			JSONArray attr_arr = (JSONArray) info.get("attr");
			if (attr_arr != null) {
				for (int j = 0; j < attr_arr.size(); j++) {
					if (info.get("type").toString().compareTo("entity") == 0)
						(((Entity) temp).getArrayList()).add(new Attribute(attr_arr.get(j).toString(), false, false));
					else if (info.get("type").toString().compareTo("relationship") == 0)
						(((Relationship) temp).getArrayList())
								.add(new Attribute(attr_arr.get(j).toString(), false, false));
				}
			}
			// add info about key for Entity type
			JSONArray key_arr = (JSONArray) info.get("key");
			if (key_arr != null) {
				for (int j = 0; j < key_arr.size(); j++) {
					for (int k = 0; k < ((Entity) temp).getArrayList().size(); k++) {
						if (((Entity) temp).getArrayList().get(k).name.compareTo(key_arr.get(j).toString()) == 0) {
							((Attribute) (((Entity) temp).getArrayList().get(k))).setIsKey(true);
							// System.out.println("name : " +
							// key_arr.get(j).toString() + "result : " +
							// ((Attribute)temp.lists.get(k)).getIsKey() );
							break;
						}
					}
				}
			}
			// add info about whether it is one-to-one or one-to-many or
			// many-to-many for relationship type
			JSONArray multi_arr = (JSONArray) info.get("multi");
			if (multi_arr != null) {
				for (int j = 0; j < multi_arr.size(); j++) {
					((Relationship) temp).addList_Multi(j, (multi_arr.get(j).toString()));
				}
				// System.out.println(((Relationship)temp).getName() + " : " +
				// ((Relationship)temp).getArrayListMulti().size());
			}
			// add info about whether it is multi-valued attribute or not for
			// entity type
			JSONArray multiV_arr = (JSONArray) info.get("multiValued");
			if (multiV_arr != null) {
				for (int j = 0; j < multiV_arr.size(); j++) {
					for (int k = 0; k < ((Entity) temp).getArrayList().size(); k++) {
						if (((Entity) temp).getArrayList().get(k).name.compareTo(multiV_arr.get(j).toString()) == 0) {
							((Attribute) (((Entity) temp).getArrayList().get(k))).setIsMulti(true);
							// System.out.println("name : " +
							// multiV_arr.get(j).toString() + "result : " +
							// ((Attribute)(((Entity)temp).getArrayList().get(k))).getIsMulti()
							// );
							break;
						}
					}
				}
			}
		}
		return ER;
	}

	public ArrayList<Node> getER() {
		Entity employee = new Entity("Employee");
		Entity department = new Entity("Department");
		Entity project = new Entity("Project");
		Entity supplier = new Entity("Supplier");
		Entity part = new Entity("Part");

		Relationship join = new Relationship("Join");
		Relationship workFor = new Relationship("WorkFor");
		Relationship sp = new Relationship("SP");
		Relationship spj = new Relationship("SPJ");

		employee.addList(0, new Attribute("E#", true, false));// name,key,multi;
		employee.addList(1, new Attribute("Name", false, false));
		employee.addList(2, new Attribute("Salary", false, false));
		employee.addList(3, new Attribute("Hobby", false, true));
		// Employee Entity 积己

		department.addList(0, new Attribute("D#", true, false));
		department.addList(1, new Attribute("Dname", false, false));
		// Department Entity 积己

		project.addList(0, new Attribute("J#", true, false));
		project.addList(1, new Attribute("Title", false, false));
		project.addList(2, new Attribute("Budget", false, false));
		// Project Entity 积己

		supplier.addList(0, new Attribute("S#", true, false));
		supplier.addList(1, new Attribute("Sname", false, false));
		supplier.addList(2, new Attribute("Address", false, false));
		// Supplier Entity 积己

		part.addList(0, new Attribute("P#", true, false));
		part.addList(1, new Attribute("Pname", false, false));
		// Part Entity 积己

		join.addList(0, employee);
		join.addList(1, department);
		join.addList(2, new Attribute("Position", false, false));
		// attribute客 entity甸 眠啊(Join)

		join.addList_Multi(0, true);// true绰 钢萍
		join.addList_Multi(1, false);
		// Join Relationship 积己

		join.addList(0, employee);
		join.addList(1, project);
		// attribute客 entity甸 眠啊(WorkFor)

		join.addList_Multi(0, true);// true绰 钢萍
		join.addList_Multi(1, true);
		// WorkFor Relationship 积己

		spj.addList(0, project);
		spj.addList(1, sp);
		spj.addList(2, new Attribute("Quantity", false, false));
		// attribute客 entity甸 眠啊(spj)

		spj.addList_Multi(0, true);// true绰 钢萍
		spj.addList_Multi(1, true);
		// SPJ Relationship 积己

		sp.addList(0, spj);
		sp.addList(1, supplier);
		sp.addList(2, part);
		sp.addList(3, new Attribute("Price", false, false));
		// attribute客 entity甸 眠啊(sp)

		sp.addList_Multi(0, true);// true绰 钢萍
		sp.addList_Multi(1, true);
		sp.addList_Multi(2, true);
		// SPJ Relationship 积己

		ER.add(employee);
		ER.add(department);
		ER.add(project);
		ER.add(supplier);
		ER.add(part);

		ER.add(join);
		ER.add(workFor);
		ER.add(sp);
		ER.add(spj);

		return ER;
	}
}
