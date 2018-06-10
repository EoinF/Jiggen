import React from 'react';
import PlainLink from '../../utils/PlainLink';
import { TemplateWidget } from '../../TemplateSelection';
import './BackgroundChooser.css';

import logo from './Background-icon.png';

const BackgroundChooser = ({background}) => {
	let content;
	if (background != null) {
		content = (
			<TemplateWidget background={background} isSelected={true} />
		);
	} else {
		content = (
			<div className="SelectBackgroundMessage">
				<div>
					Select a background
				</div>
				<div>
					<img src={logo} />
				</div>
			</div>
		);
	}

	return (
		<PlainLink className="BackgroundChooser" to={`/backgrounds`}>
			{content}
		</PlainLink>
	);
}

export default BackgroundChooser;