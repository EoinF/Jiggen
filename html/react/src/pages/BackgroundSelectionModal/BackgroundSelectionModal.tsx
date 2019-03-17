import React, { Component } from 'react';
import { connect } from 'react-redux';

import CardContainer from '../../widgets/CardContainer';
import ImageDisplayReel from '../../widgets/ImageDisplayReel';

import { 
	backgroundsActions, Background
} from '../../store/backgrounds';

import styles from './BackgroundSelectionModal.module.scss';
import ModalWrapper from '../ModalManager/ModalWrapper';
import BackgroundSelectionForm from './BackgroundSelectionForm';
import { StateRoot, Resource } from '../../models';

interface StateProps {
	backgrounds: Background[]
}

interface DispatchProps {
	selectBackgroundById(id: string): void;
	addBackground(background: Background): void;
	addBackgrounds(backgrounds: Background[]): void;
	fetchBackgrounds(): void;
	removeBackground(background: Background): void;
}

type BackgroundSelectionModalProps = StateProps & DispatchProps;

class BackgroundSelectionModal extends Component<BackgroundSelectionModalProps> {
	constructor(props: BackgroundSelectionModalProps) {
		super(props);
	}

	componentDidMount() {
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

	onError = (background: Background) => {
		this.props.removeBackground(background);
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
					onError={this.onError}
				/>
			</div>
		</div>
	}
}
const mapStateToProps = (state: StateRoot) => {
  return {
    backgrounds: state.backgrounds.resourceList.filter(Resource.hasImage)
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
		selectBackgroundById: (id: string) => dispatch(backgroundsActions.selectById(id)),
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