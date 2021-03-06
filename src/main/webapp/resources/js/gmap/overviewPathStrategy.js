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

( function() {
	var vec2 = de.htw.fb4.imi.jumpup.trip.vec2;
	
	/*
	 * Abstract super class.
	 */
	de.htw.fb4.imi.jumpup.trip.OverviewPathStrategy = function() {
	};
	/*
	 * Take an array (overviewPath) and return a string in the form
	 * latitude,longitude;latitude,longitude ...
	 */
	de.htw.fb4.imi.jumpup.trip.OverviewPathStrategy.prototype.execute = function(overviewPath) {
		throw new Error( 'Strategy#execute needs to be overridden.' );
	};

	/**
	 * Array to String.
	 * 
	 * @param overviewPath
	 *          Array for toString.
	 * @returns {String} Formatted String.
	 */
	de.htw.fb4.imi.jumpup.trip.OverviewPathStrategy.prototype.toString = function(overviewPath) {
		var stringConcat = "";
		for ( var index = 0; index < overviewPath.length; index++ ) {
			stringConcat += overviewPath[ index ].lat() + ","
					+ overviewPath[ index ].lng() + ";";
		}
		return stringConcat;
	};

	de.htw.fb4.imi.jumpup.trip.EveryTenthStrategy = function() {
	};

	/*
	 * Let EveryTenthStrategy extend OverviewPathStrategy
	 */
	de.htw.fb4.imi.jumpup.trip.EveryTenthStrategy.prototype = Object.create( de.htw.fb4.imi.jumpup.trip.OverviewPathStrategy.prototype );

	/*
	 * Take every tenth overviewPath.
	 */
	de.htw.fb4.imi.jumpup.trip.EveryTenthStrategy.prototype.execute = function(overviewPath) {
		var out = new Array();
		for ( var index = 0; index < overviewPath.length; index += 10 ) {
			// kb = latitude / breite; lb = longitude / länge
			out.push( overviewPath[ index ] );
		}
		return out;

	};

	/**
	 * Line from a to b. Return distance of p from line.
	 */
	function distance(a, b, p) {
		console.log("overviewPath: distance() function");
		
		var vecA = vec2.fromLatLngToVec2( a );
		var vecB = vec2.fromLatLngToVec2( b );
		var vecP = vec2.fromLatLngToVec2( p );

		// project point on line, get parameter of that projection point
		var t = vec2.projectPointOnLine( vecP, vecA, vecB );

		// outside the line segment?
		if ( t < 0.0 || t > 1.0 ) {
			// infinite
			return Number.MAX_VALUE;
		}

		// coordinates of the projected point
		var pp = vec2.add( vecA, vec2.mult( vec2.sub( vecB, vecA ), t ) );

		// distance of the point from the line
		var d = vec2.length( vec2.sub( pp, vecP ) );

		return d;
	}

	de.htw.fb4.imi.jumpup.trip.ByDistanceStrategy = function() {
	};

	/*
	 * Let ByDistanceStrategy extend OverviewPathStrategy
	 */
	de.htw.fb4.imi.jumpup.trip.ByDistanceStrategy.prototype = Object.create( de.htw.fb4.imi.jumpup.trip.OverviewPathStrategy.prototype );

	/*
	 * Take when distance is to high.
	 */
	de.htw.fb4.imi.jumpup.trip.ByDistanceStrategy.prototype.execute = function(overviewPath) {
		var out = new Array();
		var maxDistance = 0.01;
		var currentA = overviewPath[ 0 ];
		var currentB = overviewPath[ overviewPath.length - 1 ];

		// add start
		out.push( currentA );

		for ( var index = 1; index < overviewPath.length - 1; ++index ) {
			if ( distance( currentA, currentB, overviewPath[ index ] ) > maxDistance ) {
				currentA = overviewPath[ index ];
				out.push( currentA );
			}
		}

		// add end
		out.push( currentB );

		console.log( "     before: " + overviewPath.length + " after: "
				+ out.length );

		return out;

	};

	// return EveryTenthStrategy;
} ());