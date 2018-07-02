import React, { Component } from 'react';
import { connect } from 'react-redux';

import { JiggenHeader } from '../OverviewScreen';
import PlainLink from '../utils/PlainLink';
import CardContainer from '../utils/CardContainer';
import SelectedBackgroundDisplay from './SelectedBackgroundDisplay';

import { 
	backgroundsActions
} from '../actions';

import './backgroundSelection.css';
import logo from './WIP-icon.png';

class BackgroundSelection extends Component { 
	render () {
		const {
			background,
			onSelectBackground
		} = this.props;

		console.log(background);

		return (
			<div className="backgroundSelectionContainer">
	            <PlainLink to={`/`} >
					<JiggenHeader>
						<h1>
							<span>{"◄ "}</span>
							<span>Choose a Background</span>
						</h1>
					</JiggenHeader>
				</PlainLink>
				<div className="mainBackgroundContent">
					<div className="backgroundTable">
						<div>
							<CardContainer className="cardContainer">
								<SelectedBackgroundDisplay
									onSelectBackground={onSelectBackground}
									initialLink={background && background.links.image} />
							</CardContainer>
						</div>
						<div>
							<h3>Select a background below.</h3>
							<CardContainer className="cardContainer">
								<div>Coming soon!</div>
								<div className="logoContainer">
									<img src={logo} alt="Coming soon" />
								</div>
							</CardContainer>
						</div>
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