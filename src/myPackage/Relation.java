package myPackage;

import java.util.ArrayList;

public class Relation {

	private String name;
	private ArrayList<String> keyLists= new ArrayList<String>();
	private ArrayList<String> attLists= new ArrayList<String>();
	private ArrayList<Relation> nestedGroup = new ArrayList<Relation>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getKeyLists() {
		return keyLists;
	}
	public void setKeyLists(ArrayList<String> lists) {
		this.keyLists=lists;
	}
	public void addKeyLists(String keyLists) {
		this.keyLists.add(keyLists);
	}
	public void subKeyLists(String keyLists) {
		this.keyLists.remove(keyLists);
	}
	
	public void setAttLists(ArrayList<String> lists)
	{
		this.attLists=lists;
	}
	public ArrayList<String> getAttLists() {
		return attLists;
	}
	
	public void addAttLists(String attLists) {
		this.attLists.add(attLists);
	}
	public void subAttLists(String keyLists) {
		this.attLists.remove(keyLists);
	}
	
	public ArrayList<Relation> getNestedGroup() {
		return nestedGroup;
	}
	public void setNestedGroup(ArrayList<Relation> nestedGroup) {
		this.nestedGroup = nestedGroup;
	}
	public void addNestedLists(Relation attLists) {
		this.nestedGroup.add(attLists);
	}
}
