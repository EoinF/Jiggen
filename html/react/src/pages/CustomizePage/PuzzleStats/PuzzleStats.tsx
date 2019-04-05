import React, {Component} from 'react';
import { connect } from 'react-redux';

import { backgroundsActions, Background } from '../../../store/backgrounds';
import './puzzleStats.scss'
import { GeneratedTemplate } from '../../../store/generatedTemplates';
import { StateRoot } from '../../../models';

interface StateProps {
	generatedTemplate: GeneratedTemplate;
	background: Background;
}

type PuzzleStatsProps = StateProps;

class PuzzleStats extends Component<PuzzleStatsProps> {
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
					<span>{`Pieces: ${numPieces}`}</span>
				</div>
			);
		}
	}
}

const mapStateToProps = (state: StateRoot): StateProps => {
	return {
	  background: state.backgrounds.linkMap[state.backgrounds.selectedId!],
	  generatedTemplate: state.generatedTemplates.linkMap[state.generatedTemplates.selectedId!]
	};
}

const ConnectedPuzzleStats = connect(
  mapStateToProps
)(PuzzleStats);

export default ConnectedPuzzleStats;