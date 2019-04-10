import React from 'react';
import {ResponsiveImage} from '../../../widgets';
import styles from './SelectionWidget.module.scss';
import { Resource } from '../../../models';

interface SelectionWidgetProps {
	selection: Resource | null;
	fallbackImageSrc: string
	notSelectedCaption: string
	onClick(): void;
}

const SelectionWidget = ({selection, fallbackImageSrc, notSelectedCaption, onClick}: SelectionWidgetProps) => {
	let imageSrc = fallbackImageSrc;

	if (selection != null) {
		imageSrc = selection.links['image-compressed'] || selection.links.image;
	}
	return (
		<div className={styles.mainContainer} onClick={onClick}>
			{selection == null && 
				<div className={styles.caption}>
					{notSelectedCaption}
				</div>
			}
			<ResponsiveImage src={imageSrc} alt=""/>
		</div>
	);
}

export default SelectionWidget;