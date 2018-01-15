var exTreeviewDefaultData = [ {
	text : 'Data',
	href : '#data',
	nodes : [ {
		text : 'one Folder',
		href : '#child1',
		nodes : [ {
			text : 'a file',
			icon : 'glyphicon glyphicon-file',
			href : '#grandchild1',
		}, {
			text : 'one more file',
			icon : 'glyphicon glyphicon-file',
			href : '#grandchild2',
		} ]
	}, {
		text : 'a file',
		href : '#file',
		icon : 'glyphicon glyphicon-file',
	} ]
}, {
	text : 'Extractors',
	href : '#parent2',
	nodes : [ {
		text : 'FOX',
		href : '#fox',
		icon : 'glyphicon glyphicon-cloud',
	}, {
		text : 'RDF Fred',
		href : '#rdffred',
		icon : 'glyphicon glyphicon-cloud',
	}, {
		text : 'BOA',
		href : '#boa',
		icon : 'glyphicon glyphicon-cloud',
	} ]
}, {
	text : 'Database',
	href : '#parent3',
	nodes : [ {
		text : 'the one and only',
		href : '#db',
		icon : 'glyphicon glyphicon-hdd',
	} ]
} ];

$(document).ready(function() {
	var $tree = $('#ex-repo').treeview({
		data : exTreeviewDefaultData,
	});
});