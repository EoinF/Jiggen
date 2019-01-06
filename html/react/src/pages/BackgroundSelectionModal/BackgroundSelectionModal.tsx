import React, { Component } from 'react';
import { connect } from 'react-redux';

import CardContainer from '../../widgets/CardContainer';
import ImageDisplayReel from './ImageDisplayReel';

import { 
	backgroundsActions, Background
} from '../../store/backgrounds';

import styles from './BackgroundSelectionModal.module.scss';
import ModalWrapper from '../ModalManager/ModalWrapper';
import BackgroundSelectionForm from './BackgroundSelectionForm';
import { StateRoot } from '../../models';

interface StateProps {
	backgrounds: Background[]
}

interface DispatchProps {
	selectBackgroundById(id: string): void;
	addBackground(background: Background): void;
	addBackgrounds(background: Background[]): void;
	fetchBackgrounds(): void;
}

type BackgroundSelectionModalProps = StateProps & DispatchProps;

class BackgroundSelectionModal extends Component<BackgroundSelectionModalProps> {
	constructor(props: BackgroundSelectionModalProps) {
		super(props);
	}

	componentDidMount() {
		const suggestedInputs: string[] = JSON.parse(localStorage.getItem('suggestedInputs') || '[]')
		this.props.addBackgrounds(suggestedInputs
			.map(suggestedInput => new Background(suggestedInput))
		);
		this.props.fetchBackgrounds();
	}

	render () {
		return (
			<ModalWrapper>
				<div className={styles.mainContainer}>
					<h1>Choose a Background</h1>
					<this.MainContent/>
				</div>
			</ModalWrapper>
		);
	}

	onSelectBackground = (background: Background) => {
		this.props.addBackground(background);
		this.props.selectBackgroundById(background.id);
	}

	MainContent = () => {
		const {
			backgrounds,
			selectBackgroundById: selectBackground
		} = this.props;

		return <div className={styles.contentContainer}>
			<div className={styles.backgroundTable}>
				<CardContainer className={styles.cardContainer}>
					<BackgroundSelectionForm
							onSelectBackground={this.onSelectBackground} />
					</CardContainer>
				<ImageDisplayReel
					resourceList={backgrounds}
					onClickLink={selectBackground}
				/>
			</div>
		</div>
	}
}
const mapStateToProps = (state: StateRoot) => {
  return {
    backgrounds: state.backgrounds.resourceList
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
		selectBackgroundById: (id: string) => dispatch(backgroundsActions.selectById(id)),
		addBackground: (background: Background) => dispatch(backgroundsActions.setBackground(background)),
		addBackgrounds: (backgrounds: Background[]) => dispatch(backgroundsActions.addBackgrounds(backgrounds)),
		fetchBackgrounds: () => dispatch(backgroundsActions.fetchBackgrounds())
  }
}

const ConnectedBackgroundSelectionModal = connect(
  mapStateToProps,
  mapDispatchToProps
)(BackgroundSelectionModal);

export default ConnectedBackgroundSelectionModal;