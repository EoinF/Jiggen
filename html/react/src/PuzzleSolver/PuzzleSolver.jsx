import { Component } from 'react';
import { connect } from 'react-redux';
import queryString from 'query-string';

import { 
	backgroundsActions,
	generatedTemplatesActions,
	puzzleSolverActions
} from '../actions';

import gwtAdapter from '../gwtAdapter';

class PuzzleSolver extends Component {
	componentDidMount() {
		const {
			location: { search },
			match: { params },
			background,
			generatedTemplate,
			fetchGeneratedTemplateById,
			setBackgroundFromUrl
		} = this.props;

		const queryParams = queryString.parse(search);

		const isTemplateReady = this.isFullyLoadedPuzzleTemplate(generatedTemplate);
		const isBackgroundReady = background !== null;
		if (!isTemplateReady) {
			fetchGeneratedTemplateById(params.id);
		}
		if (!isBackgroundReady) {
			setBackgroundFromUrl(queryParams.background);
		}

		if (isTemplateReady && isBackgroundReady) {
			this.startPuzzleSolver(generatedTemplate, background);
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

	startPuzzleSolver (generatedTemplate, background) {
		if (this.isFullyLoadedPuzzleTemplate(generatedTemplate) && background !== null) {
			console.log("Starting puzzle solver with ", generatedTemplate, background);
			gwtAdapter.startPuzzle(generatedTemplate, background);
		} else {
			console.log("Failed to start puzzle solver with generated template: ", generatedTemplate);
			console.log("background: ", background);
		}
	}

	zoomOutMobile() {
		var viewport = document.querySelector('meta[name="viewport"]');

		if (viewport) {
			viewport.content = "initial-scale=1";
		}
	}

	componentDidUpdate(prevProps, prevState) {
		if (this.props.generatedTemplate !== prevProps.generatedTemplate 
			|| this.props.background !== prevProps.background) {
			this.startPuzzleSolver(this.props.generatedTemplate, this.props.background);
		}
	}

	render() {
		return null;
	}
}


const mapStateToProps = state => {
  return {
    generatedTemplate: state.generatedTemplates.generatedTemplatesMap[state.generatedTemplates.selectedId] || null,
    generatedTemplateId: state.generatedTemplates.selectedId,
    background: state.backgrounds.backgroundsMap[state.backgrounds.selectedId] || null,
    backgroundId: state.backgrounds.selectedId
  }
}

const mapDispatchToProps = dispatch => {
  return {
    fetchGeneratedTemplateById: id => {
    	dispatch(generatedTemplatesActions.fetchGeneratedTemplateById(id))
    },
    loadGeneratedTemplateIntoPuzzleSolver: (generatedTemplate, id) => {
    	dispatch(puzzleSolverActions.loadPuzzleSolver(generatedTemplate, id));
    },
    setBackgroundFromUrl: url => {
    	dispatch(backgroundsActions.setBackgroundFromUrl(url));
    }
  }
}

const ConnectedPuzzleSolver = connect(
	mapStateToProps,
	mapDispatchToProps
)(PuzzleSolver);

export default ConnectedPuzzleSolver;