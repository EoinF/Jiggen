import React, { Component } from 'react';
import { connect } from 'react-redux';

import PlainLink from '../../widgets/PlainLink';
import CardContainer from '../../widgets/CardContainer';
import SelectedBackgroundDisplay from './SelectedBackgroundDisplay';
import ImageDisplayReel from './ImageDisplayReel';

import { 
	backgroundsActions
} from '../../store/backgrounds';

import styles from './BackgroundSelection.module.scss';
import ModalWrapper from '../ModalManager/ModalWrapper';

class BackgroundSelectionModal extends Component {
	constructor(props) {
		super(props);

		this.state= {
			suggestedInputs: JSON.parse(localStorage.getItem('suggestedInputs')) || []
		};
	}

	render () {
		return (
			<ModalWrapper>
				<div className={styles.backgroundSelection}>
					<h1>
						<span>Choose a Background</span>
					</h1>
					<this.MainContent/>
				</div>
			</ModalWrapper>
		);
	}

	MainContent = () => {
		const {
			background,
			onSelectBackground
		} = this.props;

		const {
			suggestedInputs
		} = this.state;

		return <div className={styles.contentContainer}>
			<div className={styles.backgroundTable}>
				<CardContainer className={styles.cardContainer}>
					<SelectedBackgroundDisplay
						onSelectBackground={onSelectBackground}
						initialLink={background && background.links.image} />
				</CardContainer>
				<ImageDisplayReel
					imageLinks={suggestedInputs} 
					onClickLink={onSelectBackground}
				/>
			</div>
		</div>
	}
}
const mapStateToProps = state => {
  return {
    background: state.backgrounds.resourceMap[state.backgrounds.selectedId]
  };
}

const mapDispatchToProps = dispatch => {
  return {
    onSelectBackground: link => dispatch(backgroundsActions.selectBackgroundByImageLink(link))
  }
}

const ConnectedBackgroundSelection = connect(
  mapStateToProps,
  mapDispatchToProps
)(BackgroundSelectionModal);

export default ConnectedBackgroundSelection;