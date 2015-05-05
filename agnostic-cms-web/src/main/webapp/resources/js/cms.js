$(document).ready(function() {
	// Initialization of summernote wysiwyg editor in case it's library is included
	if($.fn.summernote) {
		$('.summernote').summernote({
			height : 150,
			minHeight : 150
		});
	}
});


