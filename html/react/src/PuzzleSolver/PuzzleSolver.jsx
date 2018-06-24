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

		if (!this.isFullyLoadedPuzzleTemplate(generatedTemplate)) {
			fetchGeneratedTemplateById(params.id);
		} else {
			this.startPuzzleSolver(generatedTemplate);
		}

		const gwt_root = document.getElementById('jiggen-puzzle-solver');
		const react_root = document.getElementById('react-root');
		gwt_root.classList.remove('hidden');
		react_root.classList.add('hidden');

		this.zoomOutMobile();
	}

	componentWillUnmount() {
		const gwt_root = document.getElementById('jiggen-puzzle-solver');
		const react_root = document.getElementById('react-root');
		gwt_root.classList.add('hidden');
		react_root.classList.remove('hidden');
	}

	isFullyLoadedPuzzleTemplate(generatedTemplate) {
		return (generatedTemplate != null)
			&& ('vertices' in generatedTemplate);
	}

	startPuzzleSolver (generatedTemplate) {
		if (this.isFullyLoadedPuzzleTemplate(generatedTemplate)) {
			console.log("Starting puzzle solver with ", generatedTemplate);
			gwtAdapter.setGeneratedTemplate(generatedTemplate);
		} else {
			console.log("Failed to start puzzle solver with generated template: ", generatedTemplate)
		}
	}

	zoomOutMobile() {
		var viewport = document.querySelector('meta[name="viewport"]');

		if (viewport) {
			viewport.content = "initial-scale=1";
		}	
	}

	componentDidUpdate(prevProps, prevState) {
		if (this.props.generatedTemplate !== prevProps.generatedTemplate) {
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
			</div>
		);
	}
}


const mapStateToProps = state => {
  return {
    generatedTemplate: state.generatedTemplates.generatedTemplatesMap[state.generatedTemplates.selectedId] || null,
    generatedTemplateId: state.generatedTemplates.selectedId
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