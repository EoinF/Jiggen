import React, {Component} from 'react';
import { connect } from 'react-redux';

import { backgroundsActions, Background } from '../../../store/backgrounds';
import { CompatibilityMeasure } from '../../../widgets';
import './puzzleStats.scss'
import { GeneratedTemplate } from '../../../store/generatedTemplates';
import { StateRoot } from '../../../models';

interface StateProps {
	generatedTemplate: GeneratedTemplate;
	background: Background;
}
interface OwnProps {
	loadBackgroundImageDataById(background: Background): void;
}

type PuzzleStatsProps = OwnProps & StateProps;

class PuzzleStats extends Component<PuzzleStatsProps> {

	componentDidMount() {
		this.ensureBackgroundStats(this.props.background);
	}

	componentDidUpdate(prevProps: PuzzleStatsProps, prevState: any) {
		if (prevProps.background !== this.props.background) {
			this.ensureBackgroundStats(this.props.background);
		}
	}

	ensureBackgroundStats(background: Background) {
		if (background != null && background.width == null) {
			this.props.loadBackgroundImageDataById(background);
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


const mapStateToProps = (state: StateRoot): StateProps => {
	return {
	  background: state.backgrounds.resourceMap[state.backgrounds.selectedId!],
	  generatedTemplate: state.generatedTemplates.resourceMap[state.generatedTemplates.selectedId!]
	};
  }

const mapDispatchToProps = (dispatch: Function) => {
  return {
    loadBackgroundImageDataById: (background: Background) => dispatch(backgroundsActions.loadBackgroundImageData(background))
  }
}

const ConnectedPuzzleStats = connect(
  mapStateToProps,
  mapDispatchToProps
)(PuzzleStats);

export default ConnectedPuzzleStats;