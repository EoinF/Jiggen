import React, { ReactNode } from 'react';
import {ResponsiveImage} from '../../../widgets';
import styles from './SelectionWidget.module.scss';
import { Resource } from '../../../models';

interface SelectionWidgetProps {
	selection: Resource | null;
	fallbackImageSrc: string
	notSelectedCaption: string
	onClick(): void;
	children: ReactNode;
}

const SelectionWidget = ({children, fallbackImageSrc, notSelectedCaption, onClick}: SelectionWidgetProps) => {
	return (
		<div className={styles.mainContainer} onClick={onClick}> 
			{ children != null 
				? children 
				: <React.Fragment>
					<div className={styles.caption}>
						{notSelectedCaption}
					</div>
					<ResponsiveImage src={fallbackImageSrc} alt=""/>
				</React.Fragment>
			}
		</div>
	);
}

export default SelectionWidget;