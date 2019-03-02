import React from 'react';
import {ResponsiveImage} from '../../../widgets';
import styles from './SelectionWidget.module.scss';


const SelectionWidget = ({selection, fallbackImageSrc, notSelectedCaption, selectedCaption, onClick}) => {
	let imageSrc = fallbackImageSrc;

	if (selection != null) {
		imageSrc = selection.links['image-compressed'] || selection.links.image;
	}
	return (
		<div className={styles.mainContainer} onClick={onClick}>
			<div className={styles.caption}>
				{selection != null ? selectedCaption : notSelectedCaption}
			</div>
			<ResponsiveImage src={imageSrc} alt=""/>
		</div>
	);
}

export default SelectionWidget;