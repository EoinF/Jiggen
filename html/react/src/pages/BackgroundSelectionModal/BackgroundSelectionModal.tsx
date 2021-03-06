import React, { Component } from 'react';
import { connect } from 'react-redux';

import CardContainer from '../../widgets/CardContainer';
import ImageDisplayReel from '../../widgets/ImageDisplayReel';

import { 
	backgroundsActions, Background
} from '../../store/backgrounds';

import styles from './BackgroundSelectionModal.module.scss';
import BackgroundSelectionForm from './BackgroundSelectionForm';
import { StateRoot, Resource } from '../../models';
import { Redirect } from 'react-router';
import { customPuzzleActions } from '../../store/customPuzzle';

interface BackgroundSelectionModalState {
	isSubmitted: Boolean;
}

interface StateProps {
	backgrounds: Background[];
	customPuzzleId: string;
}

interface DispatchProps {
	selectBackground(background: Background): void;
	addBackground(background: Background): void;
	addBackgrounds(backgrounds: Background[]): void;
	fetchBackgrounds(): void;
	removeBackground(background: Background): void;
}

type BackgroundSelectionModalProps = StateProps & DispatchProps;

class BackgroundSelectionModal extends Component<BackgroundSelectionModalProps, BackgroundSelectionModalState> {
	constructor(props: BackgroundSelectionModalProps) {
		super(props);
		this.state = {
			isSubmitted: false
		}
	}

	componentDidMount() {
		this.props.fetchBackgrounds();
	}

	render () {
		return (
			<div className={styles.mainContainer}>
				<h1>Choose a Background</h1>
				<this.MainContent/>
			</div>
		);
	}

	onSelectBackground = (background: Background) => {
		this.props.addBackground(background);
		this.props.selectBackground(background);
		this.setState({
			isSubmitted: true
		});
	}

	onSelectBackgroundLink = (link: string) => {
		const selectedBackground = this.props.backgrounds.find(background => background.links.self === link);
		if (selectedBackground != null) {
			this.onSelectBackground(selectedBackground);
		} else {
			console.log("Could not find background: ", link);
		}
	}

	onError = (background: Background) => {
		this.props.removeBackground(background);
	}

	MainContent = () => {
		const {
			customPuzzleId
		} = this.props;

		if (this.state.isSubmitted) {
			return <Redirect to={`/custom/${customPuzzleId}`} push={true} />
		} else {
			return <div className={styles.contentContainer}>
				<div className={styles.backgroundTable}>
					<CardContainer className={styles.cardContainer}>
						<BackgroundSelectionForm
								onSelectBackground={this.onSelectBackground} />
					</CardContainer>
					<ImageDisplayReel 
							displayComponents={this.props.backgrounds.map(background  => (
								<li key={background.links.self}>
									<div onClick={() => this.onSelectBackgroundLink(background.links.self)}>
										<img 
											src={background.links['image-compressed'] || background.links.image} 
											alt={background.name}
											onError={() => this.onError(background)}
										/>
									</div>
								</li>)
							)}
						/>
				</div>
			</div>
		}
	}
}
const mapStateToProps = (state: StateRoot) => {
  return {
	backgrounds: Object.values(state.backgrounds.linkMap).filter(Resource.hasImage),
	customPuzzleId: state.customPuzzle.currentPuzzle.id
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
		selectBackground: (background: Background) => dispatch(customPuzzleActions.selectBackground(background)),
		addBackground: (background: Background) => dispatch(backgroundsActions.setBackground(background)),
		addBackgrounds: (backgrounds: Background[]) => dispatch(backgroundsActions.addBackgrounds(backgrounds)),
		fetchBackgrounds: () => dispatch(backgroundsActions.fetchBackgrounds()),
		removeBackground: (background: Background) => dispatch(backgroundsActions.removeBackground(background))
  }
}

const ConnectedBackgroundSelectionModal = connect(
  mapStateToProps,
  mapDispatchToProps
)(BackgroundSelectionModal);

export default ConnectedBackgroundSelectionModal;