import React from 'react';
import PlainLink from '../../../widgets/PlainLink';
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
			<div className="imageSection">
				<img src={imageSrc} alt="" />
			</div>
		</PlainLink>
	);
}

export default SelectionWidget;