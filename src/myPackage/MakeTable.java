package myPackage;

import java.util.ArrayList;

public class MakeTable {

	public String makeTable(String jsonTString) {

		String showTable = new String();
		ArrayList<Relation> setRelation = new ArrayList<Relation>();
		ArrayList<Relation> setRelation1 = new ArrayList<Relation>();
		ArrayList<Relation> setRelation2 = new ArrayList<Relation>();

		ArrayList<Node> ERclass = new ER_Maker().getER(jsonTString); // after
																		// annotation
		Algorithm_Nested a = new Algorithm_Nested();
		Algorithm_General b = new Algorithm_General();
		Algorithm_Historical c = new Algorithm_Historical();

		setRelation = a.makingNested(ERclass);
		setRelation1 = b.makingGeneral(ERclass);
		setRelation2 = c.makingGeneral(ERclass);
		showTable += "<h2 style=" + "'font-family:verdana'" + ">Generate current Database Schema<h2>";

		for (Relation relation : setRelation1) {
			showTable += "<table style='width:50%'><caption>" + "Relation : " + relation.getName() + "</caption><tr>";
			for (String key : relation.getKeyLists()) {
				showTable += "<td><u>" + key + "</u></td>";
			}
			for (String attr : relation.getAttLists()) {
				showTable += "<td>" + attr + "</td>";
			}
			showTable += "</tr></table><br><br>";
		}

		showTable += "<h2 style=" + "'font-family:verdana'" + ">Generate Historical Database Schema<h2>";

		for (Relation relation : setRelation2) {
			showTable += "<table style='width:100%'><caption>" + "Relation : " + relation.getName() + "</caption><tr>";
			for (String key : relation.getKeyLists()) {
				showTable += "<td><u>" + key + "</u></td>";
			}

			for (String attr : relation.getAttLists()) {
				showTable += "<td>" + attr + "</td>";
			}
			showTable += "</tr></table><br><br>";
		}

		showTable += "<h2 style=" + "'font-family:verdana'"
				+ ">Generate Nested Relations for Historical Database Schema<h2>";
		for (Relation relation : setRelation) {

			showTable += "<table style='width:100%'><caption>" + "Relation : " + relation.getName() + "</caption><tr>";
			for (String key : relation.getKeyLists()) {
				showTable += "<td rowspan='3'><u>" + key + "</u></td>";
			}
			for (String attr : relation.getAttLists()) {
				showTable += "<td  rowspan='3'>" + attr + "</td>";
			}

			for (Relation attr : relation.getNestedGroup()) {
				if (attr.getNestedGroup().size() != 0)
					showTable += "<td colspan='"
							+ (attr.getKeyLists().size() + attr.getAttLists().size() + attr.getNestedGroup().size())
							+ "'>" + relation.getName() + " ++" + "</td>";
				else
					showTable += "<td colspan='" + (attr.getKeyLists().size() + attr.getAttLists().size()) + "'>"
							+ relation.getName() + " ++" + "</td>";
			}
			showTable += "</tr><tr>";
			for (Relation attr : relation.getNestedGroup()) {
				if (attr.getNestedGroup().size() == 0) {
					for (String att : attr.getKeyLists()) {
						showTable += "<td rowspan='2'><u>" + att + "</u></td>";
					}
					for (String att : attr.getAttLists()) {
						showTable += "<td rowspan='2'>" + att + "</td>";
					}
				}
			}
			for (Relation attr : relation.getNestedGroup()) {
				if (attr.getNestedGroup().size() != 0) {
					for (String att : attr.getKeyLists()) {
						showTable += "<td rowspan='2'><u>" + att + "</u></td>";
					}
					for (String att : attr.getAttLists()) {
						showTable += "<td rowspan='2'>" + att + "</td>";
					}
					for (Relation tmp : attr.getNestedGroup()) {
						showTable += "<td>" + relation.getName() + "</td>";
					}
					showTable += "</tr><tr>";
					for (Relation tmp : attr.getNestedGroup()) {
						for (String att : tmp.getKeyLists()) {
							showTable += "<td><u>" + att + "</u></td>";
						}
					}
				}
			}
			showTable += "</tr></table>";
		}
		return showTable;
	}

}
