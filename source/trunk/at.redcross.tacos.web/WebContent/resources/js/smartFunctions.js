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

function smartEditor() {
	var legendWidth = jQuery("legend").width();
	var inputWidth = jQuery("div.input").width();

	jQuery("table.mceLayout").css( {
		'width' : legendWidth
	});
}

function smartGrid() {

	//Reset column size to a 100% once view port has been adjusted
	jQuery("ul.column").css({ 'width' : "100%"});

	var colWrap = jQuery("ul.column").width(); //Get the width of row
	var colNum = Math.floor(colWrap / 300); //Find how many columns of 300px can fit per row / then round it down to a whole number
	var colFixed = Math.floor(colWrap / colNum); //Get the width of the row and divide it by the number of columns it can fit / then round it down to a whole number. This value will be the exact width of the re-adjusted column
	
	jQuery("ul.column").css({ 'width' : colWrap}); //Set exact width of row in pixels instead of using % - Prevents cross-browser bugs that appear in certain view port resolutions.
	jQuery("ul.column li").css({ 'width' : colFixed}); //Set exact width of the re-adjusted column	
}
