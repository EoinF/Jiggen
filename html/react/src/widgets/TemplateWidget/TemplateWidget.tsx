import React, { Component } from 'react';

import styles from './templateWidget.module.scss';
import { Template } from '../../store/templates';
import PieceCountDisplay from '../PieceCountDisplay/PieceCountDisplay';

interface TemplateWidgetProps {
	template: Template;
	onError(template: Template): void;
}

interface TemplateWidgetState {
	isLoaded: boolean;
}

class TemplateWidget extends Component<TemplateWidgetProps, TemplateWidgetState> {
	state = {
		isLoaded: false
	}

	onLoad = () => {
		this.setState({isLoaded: true})
	}

	onError = () => {
		this.props.onError(this.props.template);
	}

	render() {
		const {template} = this.props;
		const {isLoaded} = this.state;
		return (
			<div className={isLoaded ? styles.mainContainer: styles.loading}>
				<img
					src={template.links['image-compressed'] || template.links.image} 
					alt={template.name}
					onLoad={this.onLoad}
					onError={this.onError}
				/>
				<PieceCountDisplay count={template.pieces}/>
			</div>
		);
	}
}

export default TemplateWidget;