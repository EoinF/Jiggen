import React, { Component } from 'react';
import { connect } from 'react-redux';
import { 
	generatedTemplatesActions,
	puzzleSolverActions
} from '../actions';

import gwtAdapter from '../gwtAdapter';

class PuzzleSolver extends Component {
	componentDidMount() {
		const { 
			match: { params },
			generatedTemplate,
			fetchGeneratedTemplateById,
		} = this.props;

		if (!generatedTemplate) {
			fetchGeneratedTemplateById(params.id);
		} else {
			this.startPuzzleSolver(generatedTemplate);
		}

		const gwt_root = document.getElementById('jiggen-puzzle-solver');
		const react_root = document.getElementById('react-root');
		gwt_root.classList.remove('hidden');
		react_root.classList.add('hidden');
	}

	componentWillUnmount() {
		const gwt_root = document.getElementById('jiggen-puzzle-solver');
		const react_root = document.getElementById('react-root');
		gwt_root.classList.add('hidden');
		react_root.classList.remove('hidden');
	}

	startPuzzleSolver (generatedTemplate) {
		if (generatedTemplate != null) {
			console.log("Starting puzzle solver with ", generatedTemplate);
			gwtAdapter.setGeneratedTemplate(generatedTemplate);
		} else {
			console.log("Failed to start puzzle solver with generated template: ", generatedTemplate)
		}
	}

	componentDidUpdate(nextProps, nextState) {
		console.log("updating props", this.props);
		console.log("updating props", nextProps);
		if (this.props.generatedTemplate !== nextProps.generatedTemplate) {
			this.startPuzzleSolver(this.props.generatedTemplate);
		}
	}

	render() {
		const {
			generatedTemplate,
			template,
			generatedTemplateId
		} = this.props;
		
		return (
			<div>
				{ generatedTemplate && <img src = {generatedTemplate.links.image} /> }
				{ template && <img src = {template.links.image} /> }
			</div>
		);
	}
}


const mapStateToProps = state => {
  return {
    generatedTemplate: state.generatedTemplates.generatedTemplatesMap[state.generatedTemplates.selectedId],
    generatedTemplateId: state.generatedTemplates.selectedId,
    template: state.templates.templatesMap[state.templates.selectedId]
  }
}

const mapDispatchToProps = dispatch => {
  return {
    fetchGeneratedTemplateById: id => {
    	dispatch(generatedTemplatesActions.fetchGeneratedTemplateById(id))
    },
    loadGeneratedTemplateIntoPuzzleSolver: (generatedTemplate, id) => {
    	dispatch(puzzleSolverActions.loadPuzzleSolver(generatedTemplate, id));
    }
  }
}

const ConnectedPuzzleSolver = connect(
	mapStateToProps,
	mapDispatchToProps
)(PuzzleSolver);

export default ConnectedPuzzleSolver;