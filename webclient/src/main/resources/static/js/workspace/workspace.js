var exFlowchartData = {
	operators : {
		operator1 : {
			top : 20,
			left : 20,
			properties : {
				title : 'a File',
				inputs : {},
				outputs : {
					output_1 : {
						label : 'NL',
					}
				}
			}
		},
		operator2 : {
			top : 80,
			left : 300,
			properties : {
				title : 'FOX',
				inputs : {
					input_1 : {
						label : 'NL',
					}
				},
				outputs : {
					output_1 : {
						label : 'RDF',
					}
				}
			}
		},
		operator3 : {
			top : 80,
			left : 500,
			properties : {
				title : 'Target Graph',
				inputs : {
					input_1 : {
						label : 'RDF',
					}
				}
			}
		}
	},
	links : {
		link_1 : {
			fromOperator : 'operator1',
			fromConnector : 'output_1',
			toOperator : 'operator2',
			toConnector : 'input_1',
		},
		link_2 : {
			fromOperator : 'operator2',
			fromConnector : 'output_1',
			toOperator : 'operator3',
			toConnector : 'input_1',
		}
	}
};

$(document).ready(function() {
	$('#ex-workspace').flowchart({
		data : exFlowchartData
	});
});