import React from 'react';

import './templateWidget.scss';

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
			<div className="templateNameContainer">
				<span>{name || "no name"}</span>
				{ isSelected && (<span className="templateSelectedText"> (selected)</span>) }
			</div>
			<div><img src={imageLink} alt="none found"/></div>
		</div>
	);
}

export default TemplateWidget;