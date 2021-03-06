/**
 * JumpUp.Me Car Pooling Application
 * 
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */

"use strict";

// namespaces
this.de = this.de || {};
this.de.htw = this.de.htw || {};
this.de.htw.fb4 = this.de.htw.fb4 || {};
this.de.htw.fb4.imi = this.de.htw.fb4.imi || {};
this.de.htw.fb4.imi.jumpup = this.de.htw.fb4.imi.jumpup || {};
this.de.htw.fb4.imi.jumpup.trip = this.de.htw.fb4.imi.jumpup.trip || {};

var _this;

/**
 * @param divs
 *            Is a assosiativ array. Avalabile options: 'mode', 'map_canvas',
 *            'textbox', 'geocoding'
 */
(function() { 
	de.htw.fb4.imi.jumpup.trip.GoogleMap = function(options) { 

	_this = this;
	
	console.log("map options: " + options);

	this.selected = undefined;

	this.map_canvas = options['map_canvas'];
	this.textbox = options['textbox'];
	this.draggable = options["draggable"] || false;
	this.selectable = options["selectable"] || false;
	this.showDirectionsPanel = options["showDirectionsPanel"] || false;

	if (!this.map_canvas)
		throw "map_canvas is required for the map.";

	this.map;
	this.idMap = new Object();
	this.infowindow;
	this.directionsService;
	this.allRouteObjects = new Array();
	this.geocoder;
	this.infoWindow;

	this.directionsService = new google.maps.DirectionsService();
	this.geocoder = new google.maps.Geocoder();
	this.infowindow = new google.maps.InfoWindow();
	var torun = new google.maps.LatLng(53.01357, 18.597665);
	this.map = new google.maps.Map(this.map_canvas, {
		mapTypeId : google.maps.MapTypeId.ROADMAP,
		center : torun,
		zoom : 5
	});
}; // GoogleMap constructor

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.removeRoutes = function() {

	this.selected = undefined;

	while (this.allRouteObjects.length > 0) {
		var routeObject = this.allRouteObjects.pop();
		var display = routeObject["display"];
		var polyline = routeObject["polyline"];

		if (display) {
			display.setMap(null);
			display.setPanel(null);
		}

		if (polyline) {
			polyline.setPath([]);
			console.log(polyline);
		}
	}

}; // removeRoutes()

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.setAutocomplete = function(input, placeChanged) {

	var autocomplete = new google.maps.places.Autocomplete(input[0]);
	google.maps.event.addListener(autocomplete, 'place_changed', function() {
		placeChanged(autocomplete.getPlace());
	});
	// autocomplete.bindTo( 'bounds', map );

}; // setAutocomplete()

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.selectByTripId = function(id) {
	if (id)
		_this.select(_this.idMap[id]);
	else
		_this.deselect(_this.selected);
}; // selectByTripId()

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.isDraggable = function() {
	return _this.draggable;
}; // draggable()

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.isSelectable = function() {
	return _this.selectable;
}; // selectable()

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.select = function(i) {

	// deselect all
	this.deselect(this.selected);

	this.selected = i;

	var routeObject = this.allRouteObjects[i];

	if (this.showDirectionsPanel && this.textbox && routeObject)
		routeObject["display"].setPanel(this.textbox);

	if (routeObject["polyline"])
		routeObject["polyline"].setOptions({
			strokeOpacity : 0.4,
		});

}; // select()

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.deselect = function(i) {
	if (i != undefined) {
		var routeObject = this.allRouteObjects[i];

		if (routeObject["polyline"])
			routeObject["polyline"].setOptions({
				strokeOpacity : 0,
			});

		if (routeObject["display"])
			routeObject["display"].setPanel(null);
	}
}; // deselect()

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.showRoute = function(id, startLatLng, endLatLng, waypoints,
		callbackFnc, callbackSelect) {

	// convert waypoint array
	if (waypoints)
		for (var i = 0; i < waypoints.length; i++) {
			waypoints[i] = {
				"location" : waypoints[i],
				"stopover" : true,
			};
		}

	var routeObject = new Object();
	var i = this.allRouteObjects.length;
	this.idMap[id] = i;

	routeObject["display"] = new google.maps.DirectionsRenderer({
		map : _this.map,
		preserveViewport : true,
		draggable : _this.draggable,
	});

	if (_this.selectable)
		routeObject["polyline"] = new google.maps.Polyline({
			strokeOpacity : 0,
			strokeColor : "red",
			strokeWeight : 10,
			map : _this.map,
			zIndex : 0,
		});

	_this.allRouteObjects[i] = routeObject;

	// comment when not in debug
	// var overviewStrategy = new OverviewPathStrategy();

	// direction changed
	google.maps.event.addListener(routeObject["display"], "directions_changed",
			function() {
				// callback ( function param )
				var directions = routeObject["display"].getDirections();
				if (callbackFnc)
					callbackFnc(directions);

				if (routeObject["polyline"])
					// routeObject[ "polyline" ].setPath( overviewStrategy
					// .execute( directions.routes[ 0 ].overview_path ) );
					routeObject["polyline"]
							.setPath(directions.routes[0].overview_path);

			});

	// mouseover route
	if (routeObject["polyline"])
		google.maps.event.addListener(routeObject["polyline"], "mouseover",
				function() {
					_this.select(i);
					if (callbackSelect)
						callbackSelect(id);
				});

	// map.setCenter( new google.maps.LatLng( start ) );

	var sampleRequest = {
		"origin" : startLatLng,
		"destination" : endLatLng,
		"waypoints" : waypoints,
		"travelMode" : google.maps.TravelMode.DRIVING,
		"unitSystem" : google.maps.UnitSystem.METRIC,
	};

	this.directionsService.route(sampleRequest, function(response, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			routeObject["display"].setDirections(response);
		}
	});

	// only select when nothing is selected
	if (this.selectable && this.selected === undefined) {
		_this.select(i);
	}
}

de.htw.fb4.imi.jumpup.trip.GoogleMap.prototype.showMarkerAtAddress = function(lat, long, markerOptions) {
	var latLong = new google.maps.LatLng(lat, long);
	
	console.log("gmap title: " + markerOptions.title);
	var marker = new google.maps.Marker({
		position: latLong,
		map: this.map,
//		animation: google.maps.Animation.BOUNCE,
		title: markerOptions.title,
		icon: markerOptions.icon
	});
}
}());
