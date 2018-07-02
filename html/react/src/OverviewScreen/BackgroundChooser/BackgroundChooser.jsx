import React from 'react';
import PlainLink from '../../utils/PlainLink';
import { TemplateWidget } from '../../TemplateSelection';
import './BackgroundChooser.css';

import logo from './Background-icon.png';

const BackgroundChooser = ({background}) => {
	let content, containerClass;
	if (background != null) {
		containerClass = "BackgroundChooser selected";
		content = (
			<TemplateWidget template={background} isSelected={false} />
		);
	} else {
		containerClass = "BackgroundChooser";
		content = (
			<div className="SelectBackgroundMessage">
				<div>
					Select a background
				</div>
				<div>
					<img src={logo} alt="" />
				</div>
			</div>
		);
	}

	return (
		<PlainLink className={containerClass} to={`/backgrounds`}>
			{content}
		</PlainLink>
	);
}

export default BackgroundChooser;