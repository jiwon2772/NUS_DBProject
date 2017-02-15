package myPackage;



public class Attribute extends Node{



	private Boolean isKey=false;

	private Boolean isMulti=false;

	

	public Attribute(String name, boolean isKey, boolean isMulti) {

		// TODO Auto-generated constructor stub

		this.name=name;

		this.isTemporal=false;

		this.isKey=isKey;

		this.isMulti=isMulti;

	}

	public Attribute(String name) {

		this.name=name;

	}

	public Boolean getIsKey() {

		return isKey;

	}

	public void setIsKey(Boolean isKey) {

		this.isKey = isKey;

	}

	public Boolean getIsMulti() {

		return isMulti;

	}

	public void setIsMulti(Boolean isMulti) {

		this.isMulti = isMulti;

	}

	

	

	

}