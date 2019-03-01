import React, { Component } from 'react';
import { connect } from 'react-redux';
import PlainLink from '../../widgets/PlainLink';
import { templatesActions } from '../../actions/templates';

import ImageDisplayReel from '../../widgets/ImageDisplayReel';

import styles from './TemplateSelectionModal.module.scss';
import ModalWrapper from '../ModalManager/ModalWrapper';
import TemplateWidget from './TemplateWidget';
import { Template } from '../../store/templates';
import { StateRoot, Resource } from '../../models';

interface StateProps {
	templates: Template[];
}

interface DispatchProps {
	fetchTemplates(): void;
	selectTemplate(id: string): void;
}

type TemplateSelectionProps = StateProps & DispatchProps;

class TemplateSelectionModal extends Component<TemplateSelectionProps> {
	onError = (resource: Template) => {
		
	};
	
	componentDidMount() {
		const {
			fetchTemplates,
			templates
		} = this.props;
		
		if (templates.length === 0) {
			fetchTemplates();
		}
	}

	render() {
		return (
			<ModalWrapper>
				<div className={styles.mainContainer}>
					<h1>
						<span>Choose a Template</span>
					</h1>
					<ImageDisplayReel 
						resourceList={this.props.templates}
						onClickLink={this.props.selectTemplate}
						onError={this.onError}
					/>
				</div>
			</ModalWrapper>
		);
	}
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    templates: state.templates.templates
  }
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    fetchTemplates: () => dispatch(templatesActions.fetchTemplates()),
    selectTemplate: (id: string) => dispatch(templatesActions.selectTemplate(id))
  }
}

const ConnectedTemplateSelectionModal = connect(
	mapStateToProps,
	mapDispatchToProps
)(TemplateSelectionModal);

export default ConnectedTemplateSelectionModal;