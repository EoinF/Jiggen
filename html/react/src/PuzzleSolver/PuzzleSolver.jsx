import React, { Component } from 'react';
import { connect } from 'react-redux';
import { generatedTemplatesActions } from '../actions';

class PuzzleSolver extends Component {
	componentDidMount() {
		const { 
			match: { params },
			fetchGeneratedTemplate,
			generatedTemplate,
		} = this.props;
		
		if (!generatedTemplate) {
			fetchGeneratedTemplate(params.id);
		}

		const gwt_root = document.getElementById('jiggen-puzzle-solver');
		gwt_root.classList.remove('hidden');
	}
	render() {
		const {generatedTemplate, generatedTemplateId} = this.props;
		console.log("props");
		console.log(this.props);
		return (
			<div>
				<div>{generatedTemplateId}</div>
				{ generatedTemplate && 
					<div><img src={generatedTemplate.links.image} alt="none found"/></div>
				}
			</div>
		);
	}
}


const mapStateToProps = state => {

	console.log("state.generatedTemplates");
	console.log(state.generatedTemplates);
  return {
    generatedTemplate: state.generatedTemplates.generatedTemplatesMap[state.generatedTemplates.selectedId],
    generatedTemplateId: state.generatedTemplates.selectedId
  }
}

const mapDispatchToProps = dispatch => {
  return {
    fetchGeneratedTemplate: id => {
      dispatch(generatedTemplatesActions.fetchGeneratedTemplate(id))
    }
  }
}

const ConnectedPuzzleSolver = connect(
	mapStateToProps,
	mapDispatchToProps
)(PuzzleSolver);

export default ConnectedPuzzleSolver;