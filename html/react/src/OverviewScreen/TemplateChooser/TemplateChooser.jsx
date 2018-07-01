import React from 'react';
import PlainLink from '../../utils/PlainLink';
import { TemplateWidget } from '../../TemplateSelection';
import './TemplateChooser.css';

import logo from './Template-icon-simple.png';

const TemplateChooser = ({template}) => {
	let content, containerClass;
	if (template != null) {
		containerClass = "TemplateChooser selected";
		content = (
			<TemplateWidget template={template} isSelected={false} />
		);
	} else {
		containerClass = "TemplateChooser";
		content = (
			<div className="SelectTemplateMessage">
				<div>
					Select a Template
				</div>
				<div>
					<img src={logo} alt="" />
				</div>
			</div>);
	}

	return (
		<PlainLink className={containerClass} to={`/templates`}>
			{content}
		</PlainLink>
	);
}

export default TemplateChooser;