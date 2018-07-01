import React from 'react';
import { JiggenHeader } from '../OverviewScreen';
import PlainLink from '../utils/PlainLink';
import MessageBox from '../utils/MessageBox';

import './backgroundSelection.css';
import logo from './WIP-icon.png';

const BackgroundSelection = () => {
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

			<div className="backgroundTable">
				<MessageBox className="messageBoxContainer">
					<div>Coming soon...</div>
					<div>Check back later!</div>
					<div className="logoContainer">
						<img src={logo} alt="" />
					</div>
				</MessageBox>
			</div>
		</div>
	);
}

export default BackgroundSelection;