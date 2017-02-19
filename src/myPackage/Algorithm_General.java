package myPackage;

import java.util.ArrayList;
import java.util.Iterator;

public class Algorithm_General {

   private ArrayList<Relation> R=new ArrayList<Relation>();
   
   public boolean isEntity(Node d)
   {
      if(d.getClass().getSimpleName().equalsIgnoreCase("Entity"))
         return true;
      else
         return false;
   }
   public boolean isAttribute(Node d)
   {
      if(d.getClass().getSimpleName().equalsIgnoreCase("Attribute"))
         return true;
      else
         return false;
   }
   public boolean isRelationship(Node d)
   {
      if(d.getClass().getSimpleName().equalsIgnoreCase("Relationship"))
         return true;
      else
         return false;
   }
   public ArrayList<Node> getAttributes(Node node)
   {
      if(node.getClass().getSimpleName().equalsIgnoreCase("Entity"))
         return ((Entity)node).getArrayList();
      else if(node.getClass().getSimpleName().equalsIgnoreCase("Relationship"))
         return((Relationship)node).getArrayList();
      else
         return null;
   }

   @SuppressWarnings("null")
   public ArrayList<Relation> makingGeneral(ArrayList<Node> D)
   {
      if(D!=null&&D.size()!=0)
      {
         for(Node node:D)
         {   
            if(isEntity(node))
            {   
               Relation relation=new Relation();
               relation.setName(node.name);
               
               System.out.println("Entity - "+node.name);
               if(node.isTemporal)
               {
                  relation.addAttLists("Start");
                  System.out.print("Start, ");
               }
               for(Node tmpNode:getAttributes(node))
               {
                  if(isAttribute(tmpNode))
                  {
                     Attribute attNode = (Attribute)tmpNode;
                     
                     if(attNode.getIsKey()){
                        relation.addKeyLists(attNode.name);
                        System.out.print(attNode.name+"(key), ");
                     }
                     else{
                        relation.addAttLists(attNode.name);
                        System.out.print(attNode.name+", ");
                     }
                  }
               }//key값을 집어넣고 나머지 어트리뷰트를 relation에 집어넣음
               
               for(Node tmpNode2:getAttributes(node))
               {
                  if(isAttribute(tmpNode2))
                  {
                     Attribute attNode = (Attribute)tmpNode2;
                     if(attNode.isTemporal&&!attNode.getIsMulti())//Single Temporal attribute
                     {
                        relation.addAttLists(attNode.name+"_Start");
                        System.out.println(attNode.getName()+"_Start");
                     }
                  }
               }//temporal single valued attribute 값을 relation에 집어 넣기
               R.add(relation);
               System.out.println();
               
               
               
               for(Node tmpNode:getAttributes(node))
               {   
                  if(isAttribute(tmpNode))
                  {
                     Attribute attNode = (Attribute)tmpNode;
                     boolean tmp = false;
                     if(!attNode.isTemporal&&attNode.getIsMulti())
                     {
                        Relation relation1=new Relation();
                        if(!tmp){
                        relation1.setName(node.name);
                        System.out.println("Entity_sub - "+node.name);                        
                        tmp=true;
                        }
                           if(attNode.getTypeOfMulti()==1)//1:m
                           {
                              for(Node tmpNode2:getAttributes(node))
                              {
                                 if(isAttribute(tmpNode2))
                                 {
                                    Attribute attNode2 = (Attribute)tmpNode2;
                                    if(attNode2.getIsKey()){
                                       relation1.addAttLists(attNode2.name);
                                       System.out.print(attNode2.name+"(key), ");//k를 찾기 위한 것
                                    }
                                 }
                              }
                              relation1.addKeyLists(attNode.name);
                              System.out.print(attNode.name+"(key), ");
                              //k를 빼자
                           }
                           else
                           {
                              for(Node tmpNode2:getAttributes(node))
                              {
                                 if(isAttribute(tmpNode2))
                                 {
                                    Attribute attNode2 = (Attribute)tmpNode2;
                                    if(attNode2.getIsKey()){
                                       relation1.addKeyLists(attNode2.name);
                                       System.out.print(attNode2.name+"(key), ");//k를 찾기 위한 것
                                    }
                                 }
                              }
                              relation1.addKeyLists(attNode.name);
                              System.out.print(attNode.name+"(key), ");
                           }
                           R.add(relation1);
                     }
                     
                  }
               }//Non temporal multivalued attribute
               System.out.println();
               
               
               
               
               for(Node tmpNode:getAttributes(node))
               {
                  boolean tmp1 = false;
                  if(isAttribute(tmpNode))
                  {
                     Attribute attNode = (Attribute)tmpNode;
                     
                     if(attNode.isTemporal&&attNode.getIsMulti())
                     {
                        Relation relation2=new Relation();
                        if(!tmp1){
                        relation2.setName(node.name);
                        System.out.println("Entity_mt - "+node.name);
                        tmp1=true;
                        
                        }
                           if(attNode.getTypeOfMulti()==1)
                           {
                              for(Node tmpNode2:getAttributes(node))
                              {
                                 if(isAttribute(tmpNode2))
                                 {
                                    Attribute attNode2 = (Attribute)tmpNode2;
                                    if(attNode2.getIsKey()){
                                       System.out.print(attNode2.name+"(key), ");
                                       relation2.addAttLists(attNode2.name);
                                    }
                                 }
                              }
                              relation2.addKeyLists(attNode.name);                        
                              System.out.print(attNode.name+"(key), ");
                           }
                           else{
                              for(Node tmpNode2:getAttributes(node))
                              {
                                 if(isAttribute(tmpNode2))
                                 {
                                    Attribute attNode2 = (Attribute)tmpNode2;
                                    if(attNode2.getIsKey()){
                                       System.out.print(attNode2.name+"(key), ");
                                       relation2.addKeyLists(attNode2.name);
                                    }
                                 }
                              }
                              relation2.addKeyLists(attNode.name);                        
                              System.out.print(attNode.name+"(key), ");
                           }
                           relation2.addAttLists(attNode.name+"_Start");
                           System.out.print(attNode.name+"_Start, ");
                           R.add(relation2);
                     }
                  }
               }// temporal multivalued attribute
               System.out.println();
            
            }
            else if(isRelationship(node))
            {
               Relation relation4 = new Relation();
               ArrayList<String> relationshipKeys=new ArrayList<String>();
               relation4.setName(node.name);
               System.out.println("Relationship - "+node.name);
               
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
                              relation4.addKeyLists(attNode2.name);
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
                                    relation4.addKeyLists(attNode3.name);
                                    relationshipKeys.add(attNode3.name);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
              
               if(node.isTemporal)
               {
                  System.out.print("Start, ");
                  relation4.addAttLists("Start");
               }
               for(Node tmpNode:getAttributes(node))
               {
                  if(isAttribute(tmpNode))
                  {
                     Attribute attNode = (Attribute)tmpNode;
                     
                     if(attNode.getIsKey()){
                        System.out.print(attNode.name+"(key), ");
                        relation4.addKeyLists(attNode.name);
                     }
                     else{
                        System.out.print(attNode.name+", ");
                        relation4.addAttLists(attNode.name);
                     }
                  }
               }//key값을 집어넣고 나머지 어트리뷰트를 relation에 집어넣음
               for(Node tmpNode2:getAttributes(node))
               {
                  if(isAttribute(tmpNode2))
                  {
                     Attribute attNode = (Attribute)tmpNode2;
                     if(attNode.isTemporal&&!attNode.getIsMulti())//Single Temporal attribute
                     {
                        relation4.addAttLists(attNode.name+"_Start");
                        System.out.println(attNode.getName()+"_Start");
                     }
                  }
               }//temporal single valued attribute 값을 relation에 집어 넣기
               System.out.println();
               R.add(relation4);
               
               
               for(Node tmpNode:getAttributes(node))
               {
                  if(isAttribute(tmpNode))
                  {
                     Attribute attNode = (Attribute)tmpNode;
                     boolean tmp = false;
                     if(!attNode.isTemporal&&attNode.getIsMulti())
                     {
                        Relation relation5=new Relation();
                        if(!tmp){
                        System.out.println("Relationship_sub - "+node.name);
                        relation5.setName(node.name);
                        System.out.print(attNode.name+"(key), ");
                        relation5.addKeyLists(attNode.name);
                        tmp=true;
                        }
                           if(attNode.getTypeOfMulti()==1)
                           {
                              relation5.setAttLists(relationshipKeys);
                              for(Node tmpNode2:getAttributes(node))
                              {
                                 if(isAttribute(tmpNode2))
                                 {
                                    Attribute attNode2 = (Attribute)tmpNode2;
                                    if(attNode2.getIsKey()){
                                       System.out.print(attNode2.name+"(key), ");
                                       relation5.addAttLists(attNode2.name);
                                    }
                                 }
                              }
                              
                           }
                           else
                           {
                              for(String tmp2:relationshipKeys)
                              {
                                 relation5.addKeyLists(tmp2);
                              }
                              for(Node tmpNode2:getAttributes(node))
                              {
                                 if(isAttribute(tmpNode2))
                                 {
                                    Attribute attNode2 = (Attribute)tmpNode2;
                                    if(attNode2.getIsKey()){
                                       System.out.print(attNode2.name+"(key), ");
                                       relation5.addKeyLists(attNode2.name);
                                    }
                                 }
                              }
                           }
                           R.add(relation5);
                     }
                  }
               }//Non temporal multivalued attribute
               System.out.println();
               
               
               for(Node tmpNode:getAttributes(node))
               {
                  boolean tmp1 = false;
                  if(isAttribute(tmpNode))
                  {
                     Attribute attNode = (Attribute)tmpNode;
                     
                     if(attNode.isTemporal&&attNode.getIsMulti())
                     {
                        Relation relation6=new Relation();
                        if(!tmp1){
                        System.out.println("\nRelationship_mt - "+node.name);
                        relation6.setName(node.name);                        
                        tmp1=true;                           
                        }
                        relation6.addKeyLists(attNode.getName());
                        
                           if(attNode.getTypeOfMulti()==1)
                           {
                              relation6.setAttLists(relationshipKeys);
                              for(Node tmpNode2:getAttributes(node))
                              {
                                 if(isAttribute(tmpNode2))
                                 {
                                    Attribute attNode2 = (Attribute)tmpNode2;
                                    if(attNode2.getIsKey()){
                                       System.out.print(attNode2.name+"(key), ");
                                       relation6.addAttLists(attNode2.getName());
                                    }
                                 }
                              }      
                           }
                           else{
                              for(String tmp:relationshipKeys)
                              {
                                 relation6.addKeyLists(tmp);
                              }
                              for(Node tmpNode2:getAttributes(node))
                              {
                                 if(isAttribute(tmpNode2))
                                 {
                                    Attribute attNode2 = (Attribute)tmpNode2;
                                    if(attNode2.getIsKey()){
                                       System.out.print(attNode2.name+"(key), ");
                                       relation6.addKeyLists(attNode2.getName());
                                    }
                                 }
                              }

                           }
                           relation6.addAttLists(attNode.getName()+"_Start");
                           System.out.print(attNode.name+"_Start\n, ");
                        R.add(relation6);
                     }
                  }
               }// temporal multivalued attribute
            }
         }
         
      }
      return R;
      
   }
   

}