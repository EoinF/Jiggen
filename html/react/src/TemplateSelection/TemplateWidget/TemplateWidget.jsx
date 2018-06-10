import React from 'react';

import './templateWidget.css';

const TemplateWidget = ({template, isSelected, onClick}) => {
	const {
		name,
		links: {
			image: imageLink
		}
	} = template;

	let classNames = "templateWidget" + (isSelected ? " selected" : "");
	
	return (
		<div onClick={onClick} className={classNames}>
			<div>{name || "no name"}</div>
			<div><img src={imageLink} alt="none found"/></div>
		</div>
	);
}

export default TemplateWidget;