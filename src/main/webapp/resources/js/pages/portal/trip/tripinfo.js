/**
 * JumpUp.Me Car Pooling Application
 * 
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */

// namespaces
this.de = this.de || {};
this.de.htw = this.de.htw || {};
this.de.htw.fb4 = this.de.htw.fb4 || {};
this.de.htw.fb4.imi = this.de.htw.fb4.imi || {};
this.de.htw.fb4.imi.jumpup = this.de.htw.fb4.imi.jumpup || {};
this.de.htw.fb4.imi.jumpup.trip = this.de.htw.fb4.imi.jumpup.trip || {};

( function() {
	var
	BOOKING_URL = "booktrip";
	var
	PARAM_TRIP_ID = "tripId";
	var
	PARAM_RECOM_PRICE = "recommendedPrice";
	var
	PARAM_RECOM_START_POINT = "startPoint";
	var
	PARAM_RECOM_START_COORD = "startCoord";
	var
	PARAM_RECOM_END_POINT = "endPoint";
	var
	PARAM_RECOM_END_COORD = "endCoord";
	var
	PARAM_DATE_FROM = "startDate";
	var
	PARAM_DATE_TO = "endDate";
	var
	PARAM_PRICE_FROM = "priceFrom";
	var
	PARAM_PRICE_TO = "priceTo";
	var
	PARAM_MAX_DISTANCE = "maxDistance";
	
	var
	TRIPS_REF_FORM = 'form[name="lookForTripsForm"]';
	var REF_TRIPS_INPUT_START = TRIPS_REF_FORM
	+ " input[name$='start_location']";
	var REF_TRIPS_INPUT_END = TRIPS_REF_FORM
		+ " input[name$='end_location']";
	var	REF_TRIPS_INPUT_LAT_START = TRIPS_REF_FORM
	+ " input[name$='latitude_start']";
	var	REF_TRIPS_INPUT_LONG_START = TRIPS_REF_FORM
	+ " input[name$='longitude_start']";
	var	REF_TRIPS_INPUT_LAT_END = TRIPS_REF_FORM
	+ " input[name$='latitude_end']";
	var	REF_TRIPS_INPUT_LONG_END = TRIPS_REF_FORM
	+ " input[name$='longitude_end']";
	var REF_TRIPS_START_DATE = TRIPS_REF_FORM
	+ " input[name$='date_from']";
	var REF_TRIPS_END_DATE = TRIPS_REF_FORM
	+ " input[name$='date_to']";
	var REF_TRIPS_PRICE_FROM = TRIPS_REF_FORM
	+ " input[name$='price_from']";
	var REF_TRIPS_PRICE_TO = TRIPS_REF_FORM
	+ " input[name$='price_to']";
	var REF_TRIPS_MAX_DISTANCE = TRIPS_REF_FORM
	+ " input[name$='max_distance']";
	var REF_TRIPS_BTN = TRIPS_REF_FORM
	+ " input[name$='look_for_trips']";

	// was the accordion already initialized? important for the destroy()
	// function on the accordion
	var alreadyInit = false;
	var _this;

	/**
	 * Create a new TripInfo view helper.
	 * 
	 * @param options
	 *            a plain old object with the following attributes: - accordion :
	 *            the JQuery DOM Element (NOT only the selector)
	 */
	de.htw.fb4.imi.jumpup.trip.TripInfo = function(options, callbackSelect) {
		_this = this;
		this.accordion = options.accordion || "#accordion";

		this.idMap = new Object();
		this.idMapReversed = new Object();
		this.length = 0;
		this.callbackSelect = callbackSelect;
		this.inputStartPoint = $(REF_TRIPS_INPUT_START);
		this.inputEndPoint = $(REF_TRIPS_INPUT_END);
		this.inputDateFrom = $(REF_TRIPS_START_DATE);
		this.inputDateTo = $(REF_TRIPS_END_DATE);
		this.inputPriceFrom = $(REF_TRIPS_PRICE_FROM);
		this.inputPriceTo = $(REF_TRIPS_PRICE_TO);
		this.inputMaxDistance = $(REF_TRIPS_MAX_DISTANCE);
		this.options = options;

		this.tooltipItems = "";
		this.tooltipTexts = new Array();

		this.vehTooltipItems = "";
		this.vehTooltipTexts = new Array();

		// empty accordion node
		this.accordion.empty();
	};

	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.addBody = function(content) {
		this.accordion.append("<div>" + content + "</div>");
	};

	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.addHeadline = function(title) {
		this.accordion.append("<h3>" + title + "</h3>");
	};

//	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.addBookingForm = function(tripId, bodyStr, systemPrice) {
//		var messages = this.options.messages;
//		bodyStr += '<form action="' + BOOKING_ACTION + '" method="POST">' + '<input type="hidden" name="'
//				+ PARAM_TRIP_ID + '" value="' + tripId + '" />' + messages.price_recom + ': <input type="text" name="'
//				+ PARAM_RECOM_PRICE + '" value="' + systemPrice + '" />' + '<input type="hidden" name="'
//				+ PARAM_RECOM_START_POINT + '" value="' + this.inputStartPoint.val() + '" />'
//				+ '<input type="hidden" name="' + PARAM_RECOM_END_POINT + '" value="' + this.inputEndPoint.val()
//				+ '" />' + '<input type="hidden" name="' + PARAM_DATE_FROM + '" value="' + this.inputDateFrom.val()
//				+ '" />' + '<input type="hidden" name="' + PARAM_DATE_TO + '" value="' + this.inputDateTo.val()
//				+ '" />' + '<input type="hidden" name="' + PARAM_PRICE_FROM + '" value="' + this.inputPriceFrom.val()
//				+ '" />' + '<input type="hidden" name="' + PARAM_PRICE_TO + '" value="' + this.inputPriceTo.val()
//				+ '" />' + '<input type="hidden" name="' + PARAM_RECOM_START_COORD + '" value="'
//				+ this.options.startLatLng + '" />' + '<input type="hidden" name="' + PARAM_RECOM_END_COORD
//				+ '" value="' + this.options.endLatLng + '" />' + '<input type="hidden" name="' + PARAM_MAX_DISTANCE
//				+ '" value="' + this.inputMaxDistance.val() + '" />'
//				+ '<input class="booking_submit_button" type="submit" value="' + messages.book + '" />' + '</form>';
//		return bodyStr;
//	};

	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.clearContents = function() {
		this.idMap = {};
		this.idMapReversed = new Object();
		this.length = 0;
		this.accordion.empty();
	};

	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.reloadAccordion = function() {
		// destroy accordion so it goes back to its init state
		if (!alreadyInit) {
			alreadyInit = true;
		} else { // reset accordion
			this.accordion.accordion("destroy");
		}
		this.accordion.accordion({
			collapsible : true,
			activate : function(event, ui) {
				_this.callbackSelect(_this.idMapReversed[_this.accordion.accordion("option", "active")]);
			},
		});
	};

	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.select = function(tripid) {
		_this.accordion.accordion("option", "active", _this.idMap[tripid]);
	};

	/**
	 * Build a jquery UI tooltip for the given driver.
	 */
	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.buildTooltip = function(id, driver) {
		var messages = this.options.messages;
		var prefix = "";
		if ("" != this.tooltipItems) {
			prefix = ", ";
		}

		/* crazy shit I know, but it didn't work any other way... */
		this.tooltipItems += prefix + "li[class=drivertooltip][id=" + id + "]";
		this.tooltipTexts[id] = "<div class=\"driver-tooltip\"><p><span class=\"ui-tooltip-key\">" + messages.birth_date + ":</span>"
				+ driver.birthDate + "</p>" + "<p><span class=\"ui-tooltip-key\">" + messages.email + ":</span>"
				+ driver.eMail + "</p>" + "<p><span class=\"ui-tooltip-key\">" + messages.spoken_langs + ":</span>"
				+ driver.spokenLanguages + "</p>" + "<p><span class=\"ui-tooltip-key\">" + messages.home_town
				+ ":</span>" + driver.homeCity + "</p>" + "<p><img src=\"" + driver.pathProfilePic + "\" /></p></div>";
		;
		var __this = this;
		$(document).tooltip({
			items : __this.tooltipItems,
			// position : {
			// my : "right-5 center",
			// at : "right center"
			// },
			content : function() {
				var $this = $(this);
				var id = $this.attr("id");

				// if (id >= 100) {
				// $this.addClass("vehicle-tooltip");
				// } else {
				// $this.addClass("driver-tooltip");
				// }

				console.log("_buildToolTip: " + id);
				if (undefined != id) {
					return __this.tooltipTexts[id];
				} else {
					return "no chance...";
				}
				;
			},
		});
	};

	/**
	 * Build a jquery UI tooltip for the given vehicle.
	 */
	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.buildVehicleTooltip = function(id, vehicle) {
		var messages = this.options.messages;
		var prefix = "";
		if ("" != this.tooltipItems) {
			prefix = ", ";
		}

		/* crazy shit I know, but it didn't work any other way... */
		this.tooltipItems += prefix + "li[class=vehicletooltip][id=" + id + "]";
		this.tooltipTexts[id] = "<div class=\"vehicle-tooltip\"><p><span class=\"ui-tooltip-key\">" + messages.leg_space + ":</span>"
				+ vehicle.legspace + "</p>" + "<p><span class=\"ui-tooltip-key\">" + messages.wastage + ":</span>"
				+ vehicle.wastage + "</p>" + "<p><span class=\"ui-tooltip-key\">" + messages.avg_speed + ":</span>"
				+ vehicle.avgspeed + "</p>" + "<p><span class=\"ui-tooltip-key\">" + messages.number_seats + ":</span>"
				+ vehicle.numberseats + "</p>" + "<p><span class=\"ui-tooltip-key\">" + messages.air_condition
				+ ":</span>" + vehicle.aircondition + "</p>" + "<p><span class=\"ui-tooltip-key\">"
				+ messages.actual_wheel + ":</span>" + vehicle.actualwheel + "</p>" + "<p><img src=\""
				+ vehicle.pathPic + "\" /></p></div>";
		;
		var __this = this;
		$(document).tooltip({
			items : __this.tooltipItems,
			// position : {
			// my : "right-5 center",
			// at : "right center"
			// },
			content : function() {
				var $this = $(this);
				var id = $this.attr("id");

				// if (id >= 100) {
				// $this.addClass("vehicle-tooltip");
				// } else {
				// $this.addClass("driver-tooltip");
				// }

				if (undefined != id) {
					return __this.tooltipTexts[id];
				} else {
					return "no chance...";
				}
				;
			},
		});
	};
	
	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.addBookingForm = function(trip, bodyStr) {
		var messages = this.options.messages;
		
		bodyStr += '<p><a href="'+ trip.bookingUrl + '" title="' + messages.bookTooltip + '">' + messages.book + '</a></p>'; 
		
		return bodyStr;
	};

	/**
	 * Render one incoming trip.
	 * 
	 * @param tripQueryResult
	 *            the returned trip.
	 */
	de.htw.fb4.imi.jumpup.trip.TripInfo.prototype.addTrip = function(tripQueryResult) {
		var trip = tripQueryResult.trip;
		var driver = tripQueryResult.driver;
		var vehicle = tripQueryResult.vehicle;

		var messages = this.options.messages;

		var id = trip.identity;
		var startPoint = trip.startpoint;
		var endPoint = trip.endpoint;
		var startDate = trip.startDateTime;
//		var priceForPassenger = trip.priceRecommendation; // price
		// TODO replace by recommendation price
		var priceForPassenger = 20; 
//		// recommendation by
//		// the backend
		var driversPrice = trip.price;
		var startLat = trip.latStartpoint;
		var startLong =trip.longStartpoint;
		var endLat = trip.latEndpoint;
		var endLong =trip.longEndpoint;
		// TODO return number of bookings
		var numberBookings = trip.numberOfBookings;
		var maxSeats =trip.numberOfSeats;
//		var vehicle = trip.vehicle;
		this.idMap[id] = this.length;
		this.idMapReversed[this.length++] = id;
//		var distFromPassLoc = Math.round(trip.distanceFromPassengersLocation);
		var distFromPassLoc = Math.round(trip.distanceFromPassengersLocation);
//		var distFromPassDest = Math.round(trip.distanceFromPassengersDestination);
		var distFromPassDest = Math.round(trip.distanceFromPassengersDestination);

		//fugly?! mfg fu js
		this.addHeadline("<span class=\"highlighting\">" + startPoint + "</span> " + messages.to
				+ " <span class=\"highlighting\">" + endPoint + "</span>");
		var bodyStr = "<ul class=\"bookinglist\">" + "<li><span class=\"ui-accordion-content-key\">"
				+ messages.location_distance + ":</span>" + distFromPassLoc + "</li>"
				+ "<li><span class=\"ui-accordion-content-key\">" + messages.destination_distance + ":</span>"
				+ distFromPassDest + "</li>" + "<li class=\"drivertooltip\" id=\"" + id + '\">'
				+ "<span class=\"ui-accordion-content-key\">" + messages.driver
				+ ":</span><span class=\"tooltip-highlight\">" + "<a target=\"_blank\" href=\"" + driver.url + "\">" + driver.prename + " " + driver.lastname
				+ "</a> </span></li>" + "<li><span class=\"ui-accordion-content-key\">" + messages.start_date + ":</span>"
				+ startDate + "</li>" + "<li><span class=\"ui-accordion-content-key\">" + messages.overall_price
				+ ":</span>" + driversPrice + "</li>" + "<li><span class=\"ui-accordion-content-key\">"
				+ messages.current_bookings + ":</span>" + numberBookings + "/" + maxSeats + "</li>"
				+ "<li class=\"vehicletooltip\" id=\"" + (id + 100) + "\"><span class=\"ui-accordion-content-key\">"
				+ messages.vehicle + ":</span><span class=\"tooltip-highlight\">" + vehicle.manufactor 
				+ "</span></li> " + "</ul>";
		bodyStr = this.addBookingForm(trip, bodyStr);
		this.addBody(bodyStr);

//		this.buildTooltip(id, driver);
//		this.buildVehicleTooltip(id + 100, vehicle);
	};
}());

