import React from 'react';
import {PlainLink, ResponsiveImage} from '../../../../widgets';
import './SelectionWidget.css';


const SelectionWidget = ({selection, fallbackImageSrc, notSelectedCaption, selectedCaption, href}) => {
	let imageSrc = fallbackImageSrc;

	if (selection != null) {
		imageSrc = selection.links.image;
	}
	return (
		<PlainLink className="SelectionWidget" to={href}>
			<div className="description">
				{selection != null ? selectedCaption : notSelectedCaption}
			</div>
			<ResponsiveImage src={imageSrc} alt=""/>
		</PlainLink>
	);
}

export default SelectionWidget;