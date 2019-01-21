/**
 * Jquery Plugin For DataVisualization 
 * @author Nilanjan
 * @author Kevin
 */

(function($) {
	
	var defaults = {
            data : []
        };
	
	var settings = {};

	$.fn.DataVisualization = function( options ) {
		settings = $.extend(defaults, options );

		var self = this;
		var table = $("<table class=\"table table-striped\"></table>");
		var head = $("<thead></thead>");	
		var headRow = $("<tr></tr>");	
		var body = $("<tbody></tbody>");
		
		headRow.append("<th>Subject</th>");
		headRow.append("<th>Predicate</th>");
		headRow.append("<th>Object</th>");
		
		head.append(headRow);
		table.append(head);
		table.append(body);
		this.append(table);
		
		$.each(settings.data, function(index, item) {
			var row = $("<tr></tr>");
			
			row.append("<td>" + item.s + "</td>");
			row.append("<td>" + item.p + "</td>");
			row.append("<td>" + item.o + "</td>");
			
			body.append(row);
		});
		
		return this;
    };
	
}(jQuery));

