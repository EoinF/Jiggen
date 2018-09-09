import React, { Component } from 'react';
import { connect } from 'react-redux';

import { JiggenHeader } from '../OverviewScreen';
import PlainLink from '../../widgets/PlainLink';
import CardContainer from '../../widgets/CardContainer';
import SelectedBackgroundDisplay from './SelectedBackgroundDisplay';
import ImageDisplayReel from './ImageDisplayReel';

import { 
	backgroundsActions
} from '../../actions';

import './backgroundSelection.css';

class BackgroundSelection extends Component {
	constructor(props) {
		super(props);

		this.state= {
			suggestedInputs: JSON.parse(localStorage.getItem('suggestedInputs')) || []
		};
	}

	render () {
		const {
			background,
			onSelectBackground
		} = this.props;

		const {
			suggestedInputs
		} = this.state;

		return (
			<div className="backgroundSelection">
	            <div>
	            	<PlainLink to={`/`} >
					<JiggenHeader>
						<h1>
							<span>{"◄ "}</span>
							<span>Choose a Background</span>
						</h1>
					</JiggenHeader>
					</PlainLink>
				</div>
				<div className="mainBackgroundContent">
					<div className="backgroundTable">
						<div className="backgroundCard">
							<CardContainer className="cardContainer">
								<SelectedBackgroundDisplay
									onSelectBackground={onSelectBackground}
									initialLink={background && background.links.image} />
							</CardContainer>
						</div>
						<ImageDisplayReel
							imageLinks={suggestedInputs} 
							onSelectBackground={onSelectBackground}
						/>
					</div>
				</div>
			</div>
		);
	}
}
const mapStateToProps = state => {
  return {
    background: state.backgrounds.backgroundsMap[state.backgrounds.selectedId] || null
  };
}

const mapDispatchToProps = dispatch => {
  return {
    onSelectBackground: link => dispatch(backgroundsActions.setBackgroundFromUrl(link))
  }
}

const ConnectedBackgroundSelection = connect(
  mapStateToProps,
  mapDispatchToProps
)(BackgroundSelection);

export default ConnectedBackgroundSelection;