/** Dynamically resizes columns so that they span the available size */
function smartColumns() {
	var legendWidth = jQuery("legend").width();
	var inputWidth = jQuery("div.input").width();

	jQuery("div.rich-combobox-shell").css( {
		'width' : inputWidth
	});
	jQuery("div.rich-combobox-shell input:first-child").css( {
		'width' : inputWidth - 20
	});
}

/** Dynamically resizes an editor so that he spans the available size */
function smartEditor() {
	var legendWidth = jQuery("legend").width();
	var inputWidth = jQuery("div.input").width();
	jQuery("table.mceLayout").css( {
		'width' : legendWidth
	});
}

/** Create a optimized grid layout based on the available size*/
function smartGrid() {

	//Reset column size to a 100% once view port has been adjusted
	jQuery("ul.column").css({ 'width' : "100%"});

	var colWrap = jQuery("ul.column").width(); //Get the width of row
	var colNum = Math.floor(colWrap / 300); //Find how many columns of 300px can fit per row / then round it down to a whole number
	var colFixed = Math.floor(colWrap / colNum); //Get the width of the row and divide it by the number of columns it can fit / then round it down to a whole number. This value will be the exact width of the re-adjusted column
	
	jQuery("ul.column").css({ 'width' : colWrap}); //Set exact width of row in pixels instead of using % - Prevents cross-browser bugs that appear in certain view port resolutions.
	jQuery("ul.column li").css({ 'width' : colFixed}); //Set exact width of the re-adjusted column	
}

/** Returns whether or not there are any errors on the page */
function ajaxRequestContainsErrors() {
    return document.getElementById("maximumSeverity").value == "2";
}

/** Prepares the #limitChars function */
function limitCharsSetup(ed,event,infoDiv,limit) {
	event.onKeyPress.add(function(ed,e) {
		limitChars(event,infoDiv,limit);
	});
}

/** Limits the size of the given input field */
function limitChars(editor,infoDiv,limit) {
	content = editor.getBody().innerHTML;
	var contentLength = content.length;
	if(contentLength > limit) {
		jQuery('#' + infoDiv).addClass('error');
		jQuery('#' + infoDiv).removeClass('hint');
		jQuery('#' + infoDiv).html('Es sind nicht mehr als '+limit+' Zeichen erlaubt!');
		editor.getBody().innerHTML = content.substr(0,limit);
		return false;
	} else {
		jQuery('#' + infoDiv).addClass('hint');
		jQuery('#' + infoDiv).removeClass('error');
		jQuery('#' + infoDiv).html('Sie können noch '+ (limit - contentLength) +' Zeichen verwenden.');
		return true;
	}
}
