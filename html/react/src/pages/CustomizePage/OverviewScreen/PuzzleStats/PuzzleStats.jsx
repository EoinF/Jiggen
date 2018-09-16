import React, {Component} from 'react';
import { connect } from 'react-redux';

import { backgroundsActions } from '../../../../actions/backgrounds';
import { CompatibilityMeasure } from '../../../../widgets';
import './puzzleStats.css'


class PuzzleStats extends Component {

	componentDidMount() {
		this.ensureBackgroundStats(this.props.background);
	}

	componentDidUpdate(prevProps, prevState) {
		if (prevProps.background !== this.props.background) {
			this.ensureBackgroundStats(this.props.background);
		}
	}

	ensureBackgroundStats(background) {
		if (background != null && !('width' in background)) {
			this.props.loadBackgroundImageDataById(background.id, background.links.image);
		}
	}

	render() {
		const {
			generatedTemplate,
			background
		} = this.props;
		if (generatedTemplate == null) {
			return (<div>
				Please select a template first
			</div>);
		} else if (background == null) {
			return (<div>
				Pick a background!
			</div>);
		} else if (!('vertices' in generatedTemplate)) {
			return <div>Loading generated template...</div>
		}
		else {
			const numPieces = Object.keys(generatedTemplate.vertices).length;

			return (
				<div className="puzzleStats">
					<div className="compatibilitySection">
						<span className="description">Compatibility:&nbsp;</span>
						<CompatibilityMeasure 
							className={'compatibilityMeasure'}
							generatedTemplate={generatedTemplate} 
							background={background} 
						/>
					</div>
					<span>{`Pieces: ${numPieces}`}</span>
				</div>
			);
		}
	}
}

const mapStateToProps = state => {
  return {
    background: state.backgrounds.backgroundsMap[state.backgrounds.selectedId],
    template: state.templates.templatesMap[state.templates.selectedId],
    generatedTemplate: state.generatedTemplates.generatedTemplatesMap[state.generatedTemplates.selectedId]
  };
}

const mapDispatchToProps = dispatch => {
  return {
    loadBackgroundImageDataById: (id, link) => dispatch(backgroundsActions.loadBackgroundImageDataById(id, link))
  }
}

const ConnectedPuzzleStats = connect(
  mapStateToProps,
  mapDispatchToProps
)(PuzzleStats);

export default ConnectedPuzzleStats;