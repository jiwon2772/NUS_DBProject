package myPackage;



import java.util.ArrayList;



public class SearchHelper {



   public void allNode(ArrayList<Node> er1)

   {

      for(int i=0;i<er1.size();i++)

      {

         System.out.println(er1.get(i).getName());

      }

   }

   public void allRelationship(ArrayList<Node> er1)

   {

      for(int i=0;i<er1.size();i++)

      {

         String type = er1.get(i).getClass().getSimpleName();

         if(type.equals("Relationship"))

         {

            System.out.println(er1.get(i).getName());

         }

      }

   }

   public void allEntity(ArrayList<Node> er1)

   {

      for(int i=0;i<er1.size();i++)

      {

         String type = er1.get(i).getClass().getSimpleName();

         if(type.equals("Entity"))

         {

            System.out.println(er1.get(i).getName());

         }

      }

   }

   public void showAttribute(ArrayList<Node> er1)

   {

      for(int i=0;i<er1.size();i++)

      {

         Node node = er1.get(i);

         String nodeType = node.getClass().getSimpleName();

         ArrayList<Node> nodeList=new ArrayList<Node>();

         if(nodeType.equalsIgnoreCase("Entity"))

         {

            System.out.println(node.getName()+ " - Entity [Temporal]: " +node.getIsTemporal());

            nodeList = ((Entity)node).getArrayList();

         }

         else if(nodeType.equalsIgnoreCase("Relationship"))

         {

            System.out.println(node.getName()+" - Relationship [Temporal]: " +node.getIsTemporal());

            nodeList =((Relationship)node).getArrayList();

         } 

         for(int j=0;j<nodeList.size();j++)

         {

            System.out.println("<"+nodeList.get(j).getName() +"> Temporal: "+nodeList.get(j).isTemporal);

         }

         System.out.println("");

         System.out.println("");

      }

   }

   public void annotateTemporal(ArrayList<Node> er1,String nodeName)

   {

      for(int i=0;i<er1.size();i++)

      {

         Node node = er1.get(i);

         String nodeType = node.getClass().getSimpleName();

         if(node.getName().equalsIgnoreCase(nodeName))//check node

            node.setIsTemporal(true);

         if(nodeType.equalsIgnoreCase("Entity"))

         {

            ArrayList<Node> entityList = ((Entity)node).getArrayList();

            for(int j=0;j<entityList.size();j++)

            {

               if(entityList.get(j).getName().equalsIgnoreCase(nodeName)) //check attribute

               {

                  entityList.get(j).setIsTemporal(true);

               }

            }

         }

         else if(nodeType.equalsIgnoreCase("Relationship"))

         {

            ArrayList<Node> relationshipList =((Relationship)node).getArrayList();

            for(int j=0;j<relationshipList.size();j++)

            {

               if(relationshipList.get(j).getName().equalsIgnoreCase(nodeName))

               {

                  relationshipList.get(j).setIsTemporal(true);

               }

            }

         } 

      }

   }

}