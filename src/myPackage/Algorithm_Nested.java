package myPackage;

import java.util.ArrayList;

public class Algorithm_Nested {

	private ArrayList<Relation> R = new ArrayList<Relation>();

	public boolean isEntity(Node d) {
		if (d.getClass().getSimpleName().equalsIgnoreCase("Entity"))
			return true;
		else
			return false;
	}

	public boolean isAttribute(Node d) {
		if (d.getClass().getSimpleName().equalsIgnoreCase("Attribute"))
			return true;
		else
			return false;
	}

	public boolean isRelationship(Node d) {
		if (d.getClass().getSimpleName().equalsIgnoreCase("Relationship"))
			return true;
		else
			return false;
	}

	public ArrayList<Node> getAttributes(Node node) {
		if (node.getClass().getSimpleName().equalsIgnoreCase("Entity"))
			return ((Entity) node).getArrayList();
		else if (node.getClass().getSimpleName().equalsIgnoreCase("Relationship"))
			return ((Relationship) node).getArrayList();
		else
			return null;
	}

	public ArrayList<String> getKey(Node node) {
		ArrayList<String> key = new ArrayList<String>();
		if(isEntity(node)){
		for (Node tmpNode : ((Entity) node).getArrayList()) {
			if (isAttribute(tmpNode)) {
				Attribute attNode = (Attribute) tmpNode;
				if (attNode.getIsKey())
					key.add(attNode.name);
			}
		}
		}
		else{
			for (Node tmpNode : ((Relationship) node).getArrayList()) {
				if (isAttribute(tmpNode)) {
					Attribute attNode = (Attribute) tmpNode;
					if (attNode.getIsKey())
						key.add(attNode.name);
				}
			}
		}
		return key;
	}

	public ArrayList<String> get_NTS_Attribute(Node node) {
		ArrayList<String> Att = new ArrayList<String>();
		if (isEntity(node)) {
			for (Node tmpNode : ((Entity) node).getArrayList()) {
				if (isAttribute(tmpNode)) {
					Attribute attNode = (Attribute) tmpNode;
					if (!attNode.getIsKey()&&!attNode.getIsMulti()&&!attNode.isTemporal)
						Att.add(attNode.name);
				}
			}
		} else if(isRelationship(node)){
			for (Node tmpNode : ((Relationship) node).getArrayList()) {
				if (isAttribute(tmpNode)) {
					Attribute attNode = (Attribute) tmpNode;
					if (!attNode.getIsKey()&&!attNode.getIsMulti()&&!attNode.isTemporal)
						Att.add(attNode.name);
				}
			}
		}
		return Att;
	}

	public boolean hasTA(Node node)
	{
		boolean result=false;
		if (isEntity(node)) {
			for (Node tmpNode : ((Entity) node).getArrayList()) {
				if (isAttribute(tmpNode)) {
					Attribute attNode = (Attribute) tmpNode;
					if (attNode.isTemporal){
						result=true;
						break;
					}
				}
			}
		} else if(isRelationship(node)){
			for (Node tmpNode : ((Relationship) node).getArrayList()) {
				if (isAttribute(tmpNode)) {
					Attribute attNode = (Attribute) tmpNode;
					if (attNode.isTemporal){
						result=true;
						break;
					}
				}
			}
		}
		return result;
	}
	public ArrayList<Relation> makingNested(ArrayList<Node> D) {
		if (D != null && D.size() != 0) {
			for (Node node : D) {
				if (isEntity(node)) {
					Relation relation = new Relation();
					relation.setName(node.name);
					if (node.isTemporal) {					
						relation.setName(node.name);
						relation.setKeyLists(getKey(node));
						relation.setAttLists(get_NTS_Attribute(node));//<K U S>
						relation.addAttLists("Start");
						relation.addAttLists("End");
						
					}
					else if(hasTA(node))
					{
						relation.setName(node.name);
						relation.setKeyLists(getKey(node));
					}
					else
					{
						relation.setKeyLists(getKey(node));
						relation.setAttLists(get_NTS_Attribute(node));//<K U S>
						
					}
					for(Node tmpNode:((Entity)node).getArrayList())
					{
						if(isAttribute(tmpNode))
						{
							Attribute attNode = (Attribute)tmpNode;
							if(!attNode.isTemporal&&attNode.getIsMulti())
							{	
								Relation nestedRel = new Relation();
								nestedRel.setName(attNode.getName()+"_m");
								nestedRel.addKeyLists(attNode.name);
								relation.addNestedLists(nestedRel);
							}
						}
					}
					for(Node tmpNode2:((Entity)node).getArrayList())
					{
						if(isAttribute(tmpNode2))
						{
							Attribute attNode = (Attribute)tmpNode2;
							Relation nestedRel2 = new Relation();
							nestedRel2.setName(attNode.getName()+"_N");
							if(attNode.isTemporal)
							{	
								nestedRel2.addKeyLists(attNode.name+"_Start");
								nestedRel2.addAttLists(attNode.name+"_End");
								
								if(attNode.getTypeOfMulti()==0||attNode.getTypeOfMulti()==2)
								{
									if(!attNode.getIsKey())
									nestedRel2.addAttLists(attNode.name);
									
								}
								else{
									if(!attNode.getIsKey())
									nestedRel2.addKeyLists(attNode.name);
								}
								relation.addNestedLists(nestedRel2);
							}
							
						}
					}
					R.add(relation);
				}
				else if(isRelationship(node))
				{
					Relation relation1 = new Relation();
					ArrayList<String> relationshipKeys=new ArrayList<String>();//p
					relation1.setName(node.name);
					
					Relationship relNode=(Relationship)node;		
					
					for(Node tmpNode:relNode.getArrayList())
					{	
						if(isEntity(tmpNode))
						{
							for(Node tmpNode2:getAttributes(tmpNode))
							{
								if(isAttribute(tmpNode2))
								{
									Attribute attNode2 = (Attribute)tmpNode2;
									if(attNode2.getIsKey()){
										relationshipKeys.add(attNode2.name);
									}
								}
							}
						}
						else if(isRelationship(tmpNode))
						{
							for(Node tmpNode2:getAttributes(tmpNode))
							{
								if(isEntity(tmpNode2))
								{
									for(Node tmpNode3:getAttributes(tmpNode2))
									{	
										if(isAttribute(tmpNode3))
										{
											Attribute attNode3 = (Attribute)tmpNode3;
											if(attNode3.getIsKey()){
												relationshipKeys.add(attNode3.name);
											}
										}
									}
								}
							}
						}
					}
					if(relNode.isTemporal)
					{
						relation1.setKeyLists(relationshipKeys);
						Relation g1 = new Relation();
						g1.setName(relation1.getName()+"Group");
						g1.addKeyLists("Start");
						
						g1.setAttLists(get_NTS_Attribute(relNode));
						g1.addAttLists("End");
						
						
						for(Node tmpNode:relNode.getArrayList())
						{
							if(isAttribute(tmpNode))
							{
								Attribute attNode = (Attribute)tmpNode;
								if(!attNode.isTemporal&&attNode.getIsMulti())
								{	
									Relation g2=new Relation();
									g2.setName(g1.getName()+"-nested");
									g2.addKeyLists(attNode.name);
									g1.addNestedLists(g2);
								}
							}
						}
						relation1.addNestedLists(g1);
					}
					else if(hasTA(relNode))
					{
						relation1.setAttLists(relationshipKeys);
						relation1.setKeyLists(getKey(relNode));
					}
					else
					{
						ArrayList<String> rkey=getKey(node);
						for(int i=0;i<relationshipKeys.size();i++)
						{
							rkey.add(relationshipKeys.get(i));
						}
						relation1.setKeyLists(rkey);
						relation1.setAttLists(get_NTS_Attribute(node));//<K U S>
					}
					for(Node tmpNode:((Relationship)node).getArrayList())
					{
						if(isAttribute(tmpNode))
						{
							Attribute attNode = (Attribute)tmpNode;
							if(!attNode.isTemporal&&attNode.getIsMulti()&&!node.isTemporal)
							{	
								relation1.addAttLists(attNode.name);
							}
						}
					}
					for(Node tmpNode3:((Relationship)node).getArrayList())
					{
						if(isAttribute(tmpNode3))
						{
							Attribute attNode = (Attribute)tmpNode3;
							Relation g1 = new Relation();
							g1.setName(attNode.name+" Group");
							if(attNode.isTemporal)
							{	
								g1.addKeyLists(attNode.name+"_Start");
								g1.addAttLists(attNode.name+"_End");
								if(attNode.getTypeOfMulti()==0||attNode.getTypeOfMulti()==2)
								{
									if(!attNode.getIsKey())
									g1.addAttLists(attNode.name);
								}
								else{
									if(!attNode.getIsKey())
									g1.addKeyLists(attNode.name);
								}
								relation1.addNestedLists(g1);
							}
							
						}
					}
					R.add(relation1);
				}
				
			}
		}
		return R;

	}

}
