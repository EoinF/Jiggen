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
	constructor(props) {
		super(props);
		this.state = {
			isDemo: false
		};
	}

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

		if (queryParams.demo === "true") {
			this.setState({
				isDemo: true
			});
		} else {
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
		}

		const gwt_root = document.getElementById('jiggen-puzzle-solver');
		const react_root = document.getElementById('react-root');
		gwt_root.classList.remove('hidden');
		react_root.classList.add('hidden');
		// In chrome, the height of the table that holds the canvas is rendered incorrectly, so scrollbars appear
		// if we don't disable them 
		document.body.classList.add('overflow-hidden');
		
		this.zoomOutMobile();
	}

	componentWillUnmount() {
		const gwt_root = document.getElementById('jiggen-puzzle-solver');
		const react_root = document.getElementById('react-root');
		gwt_root.classList.add('hidden');
		react_root.classList.remove('hidden');
		document.body.classList.remove('overflow-hidden');
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

	startDemo() {
		gwtAdapter.startDemo();
	}

	zoomOutMobile() {
		var viewport = document.querySelector('meta[name="viewport"]');

		if (viewport) {
			viewport.content = "initial-scale=1, width=device-width, minimum-scale=1, maximum-scale=1";
		}
	}

	componentDidUpdate(prevProps, prevState) {
		if (this.props.generatedTemplate !== prevProps.generatedTemplate
			|| this.props.background !== prevProps.background) {
			this.startPuzzleSolver(this.props.generatedTemplate, this.props.background);
		}
		if (this.state.isDemo && !prevState.isDemo) {
			this.startDemo();
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