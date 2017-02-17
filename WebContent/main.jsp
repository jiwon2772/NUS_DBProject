<!DOCTYPE html>
<%@ page import="java.util.*" %>
<html>
<head>
<title>NUS_Web</title>
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<style>
th, td {
	padding: 5px;
	text-align: left;
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<script src="go.js"></script>
<script src="go-debug.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

</head>
<body onload="init()">

	<div id="sample">
		<div style="width: 100%; white-space: nowrap;">
			<span
				style="display: inline-block; vertical-align: top; padding: 5px; width: 280px">
				<div id="myPaletteDiv"
					style="border: solid 1px black; height: 620px"></div>
			</span> <span
				style="display: inline-block; vertical-align: top; padding: 5px; width: 64%">
				<div id="myDiagramDiv"
					style="border: solid 1px black; height: 620px"></div>
			</span> <span
				style="display: inline-block; vertical-align: top; padding: 7px; width: 250px">
				<div style="height: 620px; vertical-align: top">
					<table>
						<tr>
							<td><Input type="submit" class="btn btn-warning"
								name="Create" id="CreateButton" value="Create"
								onclick="location.href='main.jsp'"
								style="font-size: 50px; width: 230px; height: 90px"></td>
						</tr>
						<tr>
							<td><Input type="submit" class="btn btn-warning" name="Load"
								id="LoadButton" value="Load" onclick="openChild()"
								style="font-size: 50px; width: 230px; height: 90px"></td>
						</tr>
						<tr>
							<a id="down" href="" download="">
							<td><Input type="submit" class="btn btn-warning" name="Save"
								id="SavedButton" value="Save" onclick="save()"
								disabled="disabled"
								style="font-size: 50px; width: 230px; height: 90px"></td>
							</a>
						</tr>
						<tr>
							<form ACTION="http://localhost:8080/DBProject2/ERCreater" method="POST">
							<input type="hidden" id="ERJson" name="ERJson" value="" />
							<input type="submit" id="submit" style="display:none;"/>
							</form>  
							<td><Input type="submit" class="btn btn-warning"
								name="Validate" id="ValidateButton"
								value="Validate" disabled="disabled" onclick="validClick()"
								style="font-size: 50px; width: 230px; height: 90px"></td>
							</form>
						</tr>
						<tr>
							<form ACTION="http://localhost:8080/DBProject2/annotate.jsp" method="POST">
							<input type="hidden" id="ERJson2" name="diagram" value="" />
							<input type="submit" id="submit_anno" style="display:none;"/>
							<td><Input type="submit" class="btn btn-warning"
								name="Annotate" id="AnnotateButton"
								value="Annotate" disabled="disabled" onClick="annotateClick()"
								style="font-size: 50px; width: 230px; height: 90px"></td>
						</tr>
						<tr>
							<td><Input type="submit" class="btn btn-warning"
								name="Translate" id="TranslateButton"
								value="Translate" disabled="disabled"
								style="font-size: 50px; width: 230px; height: 90px"></td>
						</tr>
						<tr>
							<td><Input type="submit" class="btn btn-warning"
								name="Query" id="QueryButton" value="Query"
								disabled="disabled"
								style="font-size: 50px; width: 230px; height: 90px"></td>
						</tr>
					</table>
				</div>

			</span>
		</div>
		<Span
			style="display: inline-block; vertical-align: top; padding: 5px; width: 83%">
			<div>

				<div
					style="overflow: scroll; border: solid 1px black; height: 100px">
					<span style="color:red; font-size:20px">${error}</span>
				</div>

			</div>
		</Span>

		<div id="invisible">
			<div>
				<button id="SaveButton">Save</button>
				Diagram Model saved in JSON format:
			</div>
			<textarea id="mySavedModel" style="width: 100%; height: 300px">
			<% 
				String diagram = (String)(request.getAttribute("diagram"));
				if(diagram != null) { %>
					${diagram}
			<%  } else { %>{ "class": "go.GraphLinksModel","linkFromPortIdProperty": "fromPort","linkToPortIdProperty": "toPort","nodeDataArray": [],"linkDataArray": []}<% } %>
    </textarea>
		</div>
		<Span
			style="display: inline-block; vertical-align: top; padding: 5px;">
				<textarea id="mySavedModel2" style="width: 100%; height: 300px"
					name="ERJson">{ "class": "go.GraphLinksModel","linkFromPortIdProperty": "fromPort","linkToPortIdProperty": "toPort","nodeDataArray": [],"linkDataArray": []}</textarea>
	</div>
	</Span>
</body>
<script>
	$(document).ready(function(){
	//jQuery methods go here ...
    $("#invisible").hide();
	$("#mySavedModel2").hide();
	});

	var openWin;
	var initial = true;
	var defaultJson = '{ "class": "go.GraphLinksModel","linkFromPortIdProperty": "fromPort","linkToPortIdProperty": "toPort","nodeDataArray": [],"linkDataArray": []}';
	
	function init() {

		var str = document.getElementById("mySavedModel2").innerHTML;
		
		//check whether it is after validate or not
		
		
		if (str == defaultJson && <%= (request.getAttribute("diagram")==null) %>) {
			document.getElementById("SavedButton").disabled = true;
			document.getElementById("ValidateButton").disabled = true;
		} else {
			document.getElementById("SavedButton").disabled = false;
			document.getElementById("ValidateButton").disabled = false;
		}
		
		//check whether there is error or not
		if(<%= request.getAttribute("isValid") %>) {
			document.getElementById("AnnotateButton").disabled = false;
		}
		//if (window.goSamples) goSamples();  // init for these samples -- you don't need to call this
		var $ = go.GraphObject.make; // for conciseness in defining templates
		myDiagram = $(go.Diagram, "myDiagramDiv", // must name or refer to the DIV HTML element
		{
			grid : $(go.Panel, "Grid", $(go.Shape, "LineH", {
				stroke : "lightgray",
				strokeWidth : 0.5
			}), $(go.Shape, "LineH", {
				stroke : "gray",
				strokeWidth : 0.5,
				interval : 10
			}), $(go.Shape, "LineV", {
				stroke : "lightgray",
				strokeWidth : 0.5
			}), $(go.Shape, "LineV", {
				stroke : "gray",
				strokeWidth : 0.5,
				interval : 10
			})),
			allowDrop : true, // must be true to accept drops from the Palette
			"draggingTool.dragsLink" : true,
			"draggingTool.isGridSnapEnabled" : true,
			"linkingTool.isUnconnectedLinkValid" : false,
			"linkingTool.portGravity" : 20,
			"relinkingTool.isUnconnectedLinkValid" : true,
			"relinkingTool.portGravity" : 20,
			"relinkingTool.fromHandleArchetype" : $(go.Shape, "Diamond", {
				segmentIndex : 0,
				cursor : "pointer",
				desiredSize : new go.Size(8, 8),
				fill : "tomato",
				stroke : "darkred"
			}),
			"relinkingTool.toHandleArchetype" : $(go.Shape, "Diamond", {
				segmentIndex : -1,
				cursor : "pointer",
				desiredSize : new go.Size(8, 8),
				fill : "darkred",
				stroke : "tomato"
			}),
			"linkReshapingTool.handleArchetype" : $(go.Shape, "Diamond", {
				desiredSize : new go.Size(7, 7),
				fill : "lightblue",
				stroke : "deepskyblue"
			}),
			rotatingTool : $(TopRotatingTool), // defined below
			"rotatingTool.snapAngleMultiple" : 15,
			"rotatingTool.snapAngleEpsilon" : 15,
			"undoManager.isEnabled" : true,
			"ModelChanged": function(e) { if (e.isTransactionFinished) {
					saveDiagramProperties(); // do this first, before writing to JSON
					var current = myDiagram.model.toJson();
					if(initial==true) {
						initial = false;
					} else {
						if(current == <%= (String)(request.getAttribute("diagram"))%> ) {
						} else {
							document.getElementById("AnnotateButton").disabled = true;
						}
					}
				}
			}

		});

		// when the document is modified, add a "*" to the title and enable the "Save" button
		myDiagram
				.addDiagramListener(
						"Modified",
						function(e) {
							var button = document.getElementById("SaveButton");
							if (button)
								button.disabled = !myDiagram.isModified;
							var idx = document.title.indexOf("*");
							if (myDiagram.isModified) {
								if (idx < 0) {
									document.title += "*";
									document.title = document.title.substr(0,
											idx);
									saveDiagramProperties(); // do this first, before writing to JSON
									document.getElementById("mySavedModel2").innerHTML = myDiagram.model
											.toJson();
									var str = document
											.getElementById("mySavedModel2").innerHTML;
									if (str === defaultJson) {
										document.getElementById("SavedButton").disabled = true;
										document.getElementById("ValidateButton").disabled = true;
									} else {
										document.getElementById("SavedButton").disabled = false;
										document.getElementById("ValidateButton").disabled = false;
									}
								} else if (idx >= 0)
									document.title = document.title.substr(0,
											idx);
							}
						});

		// Define a function for creating a "port" that is normally transparent.
		// The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
		// and where the port is positioned on the node, and the boolean "output" and "input" arguments
		// control whether the user can draw links from or to the port.
		function makePort(name, spot, output, input) {
			// the port is basically just a small transparent square
			return $(go.Shape, "Circle", {
				fill : null, // not seen, by default; set to a translucent gray by showSmallPorts, defined below
				stroke : null,
				desiredSize : new go.Size(7, 7),
				alignment : spot, // align the port on the main Shape
				alignmentFocus : spot, // just inside the Shape
				portId : name, // declare this object to be a "port"
				fromSpot : spot,
				toSpot : spot, // declare where links may connect at this port
				fromLinkable : output,
				toLinkable : input, // declare whether the user may draw links to/from here
				cursor : "pointer" // show a different cursor to indicate potential link point
			});
		}

		var nodeSelectionAdornmentTemplate = $(go.Adornment, "Auto", $(
				go.Shape, {
					fill : null,
					stroke : "deepskyblue",
					strokeWidth : 1.5,
					strokeDashArray : [ 4, 2 ]
				}), $(go.Placeholder));

		var nodeResizeAdornmentTemplate = $(go.Adornment, "Spot", {
			locationSpot : go.Spot.Right
		}, $(go.Placeholder), $(go.Shape, {
			alignment : go.Spot.TopLeft,
			cursor : "nw-resize",
			desiredSize : new go.Size(6, 6),
			fill : "lightblue",
			stroke : "deepskyblue"
		}), $(go.Shape, {
			alignment : go.Spot.Top,
			cursor : "n-resize",
			desiredSize : new go.Size(6, 6),
			fill : "lightblue",
			stroke : "deepskyblue"
		}), $(go.Shape, {
			alignment : go.Spot.TopRight,
			cursor : "ne-resize",
			desiredSize : new go.Size(6, 6),
			fill : "lightblue",
			stroke : "deepskyblue"
		}),

		$(go.Shape, {
			alignment : go.Spot.Left,
			cursor : "w-resize",
			desiredSize : new go.Size(6, 6),
			fill : "lightblue",
			stroke : "deepskyblue"
		}), $(go.Shape, {
			alignment : go.Spot.Right,
			cursor : "e-resize",
			desiredSize : new go.Size(6, 6),
			fill : "lightblue",
			stroke : "deepskyblue"
		}),

		$(go.Shape, {
			alignment : go.Spot.BottomLeft,
			cursor : "se-resize",
			desiredSize : new go.Size(6, 6),
			fill : "lightblue",
			stroke : "deepskyblue"
		}), $(go.Shape, {
			alignment : go.Spot.Bottom,
			cursor : "s-resize",
			desiredSize : new go.Size(6, 6),
			fill : "lightblue",
			stroke : "deepskyblue"
		}), $(go.Shape, {
			alignment : go.Spot.BottomRight,
			cursor : "sw-resize",
			desiredSize : new go.Size(6, 6),
			fill : "lightblue",
			stroke : "deepskyblue"
		}));

		var nodeRotateAdornmentTemplate = $(go.Adornment, {
			locationSpot : go.Spot.Center,
			locationObjectName : "CIRCLE"
		}, $(go.Shape, "Circle", {
			name : "CIRCLE",
			cursor : "pointer",
			desiredSize : new go.Size(7, 7),
			fill : "lightblue",
			stroke : "deepskyblue"
		}), $(go.Shape, {
			geometryString : "M3.5 7 L3.5 30",
			isGeometryPositioned : true,
			stroke : "deepskyblue",
			strokeWidth : 1.5,
			strokeDashArray : [ 4, 2 ]
		}));

		myDiagram.nodeTemplate = $(go.Node, "Spot", {
			locationSpot : go.Spot.Center
		}, new go.Binding("location", "loc", go.Point.parse)
				.makeTwoWay(go.Point.stringify), {
			selectable : true,
			selectionAdornmentTemplate : nodeSelectionAdornmentTemplate
		}, {
			resizable : true,
			resizeObjectName : "PANEL",
			resizeAdornmentTemplate : nodeResizeAdornmentTemplate
		}, {
			rotatable : true,
			rotateAdornmentTemplate : nodeRotateAdornmentTemplate
		}, new go.Binding("angle").makeTwoWay(),
		// the main object is a Panel that surrounds a TextBlock with a Shape
		$(go.Panel, "Auto", {
			name : "PANEL"
		}, new go.Binding("desiredSize", "size", go.Size.parse)
				.makeTwoWay(go.Size.stringify), $(go.Shape, "Rectangle", // default figure
		{
			portId : "", // the default port: if no spot on link data, use closest side
			fromLinkable : false,
			toLinkable : false,
			cursor : "pointer",
			fill : "white", // default color
			strokeWidth : 2
		}, new go.Binding("figure"), new go.Binding("fill")), $(go.TextBlock, {
			font : "bold 11pt Helvetica, Arial, sans-serif",
			margin : 8,
			maxSize : new go.Size(160, NaN),
			wrap : go.TextBlock.WrapFit,
			editable : true
		}, new go.Binding("text").makeTwoWay())),
		// four small named ports, one on each side:
		makePort("T", go.Spot.Top, true, true), makePort("L", go.Spot.Left,
				true, true), makePort("R", go.Spot.Right, true, true),
				makePort("B", go.Spot.Bottom, true, true), { // handle mouse enter/leave events to show/hide the ports
				//mouseEnter: function(e, node) { showSmallPorts(node, true); },
				//mouseLeave: function(e, node) { showSmallPorts(node, false); }
				});

		function showSmallPorts(node, show) {
			node.ports.each(function(port) {
				if (port.portId !== "") { // don't change the default port, which is the big shape
					port.fill = show ? "rgba(0,0,0,.3)" : null;
				}
			});
		}

		var linkSelectionAdornmentTemplate = $(go.Adornment, "Link", $(
				go.Shape,
				// isPanelMain declares that this Shape shares the Link.geometry
				{
					isPanelMain : true,
					fill : null,
					stroke : "deepskyblue",
					strokeWidth : 0
				}) // use selection object's strokeWidth
		);

		myDiagram.linkTemplate = $(go.Link, // the whole link panel
		{
			selectable : true,
			selectionAdornmentTemplate : linkSelectionAdornmentTemplate
		}, {
			relinkableFrom : true,
			relinkableTo : true,
			reshapable : true
		}, {
			routing : go.Link.AvoidsNodes,
			curve : go.Link.JumpOver,
			corner : 5,
			toShortLength : 4
		}, new go.Binding("points").makeTwoWay(), $(go.Shape, // the link path shape
		{
			isPanelMain : true,
			strokeWidth : 2
		}), $(go.Shape, // the "from" arrowhead
		new go.Binding("fromArrow", "fromArrow")), $(go.Shape, // the "to" arrowhead
		new go.Binding("toArrow", "toArrow")), $(go.TextBlock, // the label text
		{
			textAlign : "center",
			font : "bold 13pt helvetica, arial, sans-serif",
			stroke : "blue",
			margin : 8,
			segmentOffset : new go.Point(NaN, NaN),
			editable : true
		// enable in-place editing
		}, new go.Binding("text", "multi").makeTwoWay()), $(go.Panel, "Auto",
				new go.Binding("visible", "isSelected").ofObject(), $(go.Shape,
						"RoundedRectangle", // the link shape
						{
							fill : "#F8F8F8",
							stroke : null
						}), $(go.TextBlock, {
					minSize : new go.Size(NaN, NaN),
					editable : false
				}, new go.Binding("text").makeTwoWay())));

		load(); // load an initial diagram from some JSON text

		// initialize the Palette that is on the left side of the page
		myPalette = $(go.Palette, "myPaletteDiv", // must name or refer to the DIV HTML element
		{
			maxSelectionCount : 1,
			nodeTemplateMap : myDiagram.nodeTemplateMap, // share the templates used by myDiagram
			linkTemplate : // simplify the link template, just in this Palette
			$(go.Link, { // because the GridLayout.alignment is Location and the nodes have locationSpot == Spot.Center,
				// to line up the Link in the same manner we have to pretend the Link has the same location spot
				locationSpot : go.Spot.Center,
				selectionAdornmentTemplate : $(go.Adornment, "Link", {
					locationSpot : go.Spot.Center
				}, $(go.Shape, {
					isPanelMain : true,
					fill : null,
					stroke : "deepskyblue",
					strokeWidth : 0
				}), $(go.Shape, // the arrowhead
				{
					toArrow : "",
					stroke : null
				}))
			}, {
				routing : go.Link.AvoidsNodes,
				curve : go.Link.JumpOver,
				corner : 5,
				toShortLength : 4
			}, new go.Binding("points"), $(go.Shape, // the link path shape
			{
				isPanelMain : true,
				strokeWidth : 2
			}), $(go.Shape, // the "from" arrowhead
			new go.Binding("fromArrow", "fromArrow")), $(go.Shape, // the "to" arrowhead
			new go.Binding("toArrow", "toArrow"))),
			model : new go.GraphLinksModel([ // specify the contents of the Palette
			{
				text : "Entity",
				figure : "RoundedRectangle",
				fill : "lightyellow",
				size : "150 70",
				type : "E",
				isTemp : "false"
			}, {
				text : "Relationship",
				figure : "Diamond",
				fill : "lightskyblue",
				type : "R",
				isTemp : "false"
			}, {
				text : "Attribute",
				figure : "Ellipse",
				fill : "#00AD5F",
				type : "A",
				isTemp : "false"
			} ], [
					// the Palette also has a disconnected Link, which the user can drag-and-drop
					{
						points : new go.List(go.Point).addAll([
								new go.Point(0, 0), new go.Point(30, 0),
								new go.Point(30, 40), new go.Point(60, 40) ]),
						toArrow : "",
						fromArrow : "",
						type : "r",
						multi : "m"
					},
					{
						points : new go.List(go.Point).addAll([
								new go.Point(0, 0), new go.Point(30, 0),
								new go.Point(30, 40), new go.Point(60, 40) ]),
						toArrow : "Standard",
						fromArrow : "",
						type : "a"
					},
					{
						points : new go.List(go.Point).addAll([
								new go.Point(0, 0), new go.Point(30, 0),
								new go.Point(30, 40), new go.Point(60, 40) ]),
						toArrow : "DoubleFeathers",
						fromArrow : "",
						type : "m"
					},
					{
						points : new go.List(go.Point).addAll([
								new go.Point(0, 0), new go.Point(30, 0),
								new go.Point(30, 40), new go.Point(60, 40) ]),
						toArrow : "Standard",
						fromArrow : "Backward",
						type : "k"
					} ])
		});
	}

	function TopRotatingTool() {
		go.RotatingTool.call(this);
	}
	go.Diagram.inherit(TopRotatingTool, go.RotatingTool);

	/** @override */
	TopRotatingTool.prototype.updateAdornments = function(part) {
		go.RotatingTool.prototype.updateAdornments.call(this, part);
		var adornment = part.findAdornment("Rotating");
		if (adornment !== null) {
			adornment.location = part.rotateObject
					.getDocumentPoint(new go.Spot(0.5, 0, 0, -30)); // above middle top
		}
	};
	RoundedRectangleate = function(newangle) {
		go.RotatingTool.prototype.rotate.call(this, newangle + 90);
	};
	// end of TopRotatingTool class

	// Show the diagram's model in JSON format that the user may edit
	function save() {
		saveDiagramProperties(); // do this first, before writing to JSON
		document.getElementById("mySavedModel2").value = myDiagram.model
				.toJson();
		myDiagram.isModified = false;
		var json = document.getElementById("mySavedModel2").value;
		//var data = "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(json));
		var data = "text/json;charset=utf-8," + json;
		
		var today = new Date();
		var date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
		
		document.getElementById("down").href = 'data:' + data;
		document.getElementById("down").download = date + '.json';
		document.getElementById("down").click();
	}
	function load() {
		myDiagram.model = go.Model.fromJson(document
				.getElementById("mySavedModel").value);
		loadDiagramProperties(); // do this after the Model.modelData has been brought into memory
	}

	function saveDiagramProperties() {
		myDiagram.model.modelData.position = go.Point
				.stringify(myDiagram.position);
	}
	function loadDiagramProperties(e) {
		// set Diagram.initialPosition, not Diagram.position, to handle initialization side-effects
		var pos = myDiagram.model.modelData.position;
		if (pos)
			myDiagram.initialPosition = go.Point.parse(pos);
	}
	function openWin() {
		window
				.open(
						"http://localhost:8080/DBProject2/load.html",
						"_blank",
						"toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=400, height=530");
	}
	function openChild() {
		window.name = "parentForm";
		openWin = window
				.open(
						"http://localhost:8080/DBProject2/load.html",
						"childForm",
						"toolbar=no, location=yes, directories=no, status=no, menubar=no, scrollbars=yes, resizable=no, copyhistory=yes, width=400, height=530");
	}
	function validClick() {
		saveDiagramProperties();
		document.getElementById("mySavedModel2").value = myDiagram.model.toJson();
		document.getElementById("ERJson").value = document.getElementById("mySavedModel2").value;
		document.getElementById("submit").click();
	}
	function annotateClick() {
		saveDiagramProperties();
		document.getElementById("mySavedModel2").value = myDiagram.model.toJson();
		document.getElementById("ERJson2").value = document.getElementById("mySavedModel2").value;
		document.getElementById("submit_anno").click();
	}
</script>
</html>