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

/** Create a optimized grid layout based on the available size */
function smartGrid() {
	// Reset column size to a 100% once view port has been adjusted
	jQuery("ul.column").css( {
		'width' : "100%"
	});

	var colWrap = jQuery("ul.column").width(); 
	var colFixed = Math.floor(colWrap / colNum); 

	jQuery("ul.column").css( {
		'width' : colWrap
	});
	jQuery("ul.column li").css( {
		'width' : colFixed
	}); 
}

/** Returns whether or not there are any errors on the page */
function ajaxRequestContainsErrors() {
	return document.getElementById("maximumSeverity").value == "2";
}

/** Prepares the #limitChars function */
function limitCharsSetup(ed, event, infoDiv, limit) {
	event.onKeyPress.add(function(ed, e) {
		limitChars(event, infoDiv, limit);
	});
}

/** Limits the size of the given input field */
function limitChars(editor, infoDiv, limit) {
	content = editor.getBody().innerHTML;
	var contentLength = content.length;
	if (contentLength > limit) {
		jQuery('#' + infoDiv).addClass('error');
		jQuery('#' + infoDiv).removeClass('hint');
		jQuery('#' + infoDiv).html('Es sind nicht mehr als ' + limit
						+ ' Zeichen erlaubt! (Aktuell:' + contentLength + ')');
		editor.getBody().innerHTML = content.substr(0, limit - 10);
		return false;
	} else {
		jQuery('#' + infoDiv).addClass('hint');
		jQuery('#' + infoDiv).removeClass('error');
		jQuery('#' + infoDiv).html('Sie können noch ' + (limit - contentLength) + ' Zeichen verwenden.');
		return true;
	}
}

/** Limits the size of the given text field */
function limitTextChars(editor,infoDiv,limit) {
	content = editor.value;
	var contentLength = content.length;
	if (contentLength > limit) {
		jQuery('#' + infoDiv).addClass('error');
		jQuery('#' + infoDiv).removeClass('hint');
		jQuery('#' + infoDiv).html('Es sind nicht mehr als ' + limit
						+ ' Zeichen erlaubt! (Aktuell:' + contentLength + ')');
		editor.value = content.substr(0, limit - 10);
		return false;
	} else {
		jQuery('#' + infoDiv).addClass('hint');
		jQuery('#' + infoDiv).removeClass('error');
		jQuery('#' + infoDiv).html('Sie können noch ' + (limit - contentLength) + ' Zeichen verwenden.');
		return true;
	}
}


