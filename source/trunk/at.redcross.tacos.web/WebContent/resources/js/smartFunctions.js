function smartColumns() {
	var legendWidth = jQuery("legend").width();
	var inputWidth = jQuery("div.input").width();

	jQuery("div.rich-combobox-shell").css( {
		'width' : inputWidth
	});
	jQuery("div.rich-combobox-shell input:first-child").css( {
		'width' : inputWidth - 20
	});
	jQuery("table.mceLayout").css( {
		'width' : legendWidth
	});
}

function smartEditor() {
	var legendWidth = jQuery("legend").width();
	var inputWidth = jQuery("div.input").width();

	jQuery("table.mceLayout").css( {
		'width' : legendWidth
	});
}
