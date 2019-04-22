import React from 'react';

import styles from './templateWidget.module.scss';
import { Template } from '../../store/templates';
import PieceCountDisplay from '../PieceCountDisplay/PieceCountDisplay';

interface TemplateWidgetProps {
	template: Template;
	onError(template: Template): void;
}

const TemplateWidget = ({template, onError}: TemplateWidgetProps) => {
	return (
		<div className={styles.templateSelectionContainer}>
			<img
				src={template.links['image-compressed'] || template.links.image} 
				alt={template.name}
				onError={() => onError(template)}
			/>
			<PieceCountDisplay count={template.pieces}/>
		</div>
	);
}

export default TemplateWidget;