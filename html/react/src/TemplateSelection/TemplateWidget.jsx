import React from 'react';

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
				<span>{name || "no name"}</span>
				<div><img src={imageLink} alt="none found"/></div>
			</div>
	);
}

export default TemplateWidget;