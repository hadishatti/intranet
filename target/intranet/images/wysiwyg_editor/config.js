CKEDITOR.editorConfig = function(config) {
	config.resize_enabled = true;
	config.height = '1400px';
	config.toolbar = 'Complex';
	config.toolbar_Simple = [ [ 'Bold', 'Italic', '-', 'NumberedList', 'BulletedList', '-', 'Link', 'Unlink', '-', 'About' ] ];
	config.toolbar_Complex = [
			[ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript',
					'Superscript', 'TextColor', 'BGColor', '-', 'Cut', 'Copy',
					'Paste', 'Link', 'Unlink', 'Image'],
			[ 'Undo', 'Redo', '-', 'JustifyLeft', 'JustifyCenter',
					'JustifyRight', 'JustifyBlock' ],
			[ 'Table', 'Smiley', 'SpecialChar', 'PageBreak',
					'Styles', 'Format', 'Font', 'FontSize', 'Maximize'] ];
};