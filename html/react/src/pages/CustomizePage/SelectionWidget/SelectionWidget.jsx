import React from 'react';
import {ResponsiveImage} from '../../../widgets';
import './SelectionWidget.scss';


const SelectionWidget = ({selection, fallbackImageSrc, notSelectedCaption, selectedCaption, onClick}) => {
	let imageSrc = fallbackImageSrc;

	if (selection != null) {
		imageSrc = selection.links.image;
	}
	return (
		<div className="SelectionWidget" onClick={onClick}>
			<div className="description">
				{selection != null ? selectedCaption : notSelectedCaption}
			</div>
			<ResponsiveImage src={imageSrc} alt=""/>
		</div>
	);
}

export default SelectionWidget;