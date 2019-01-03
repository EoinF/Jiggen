import React, { Component } from 'react';
import { connect } from 'react-redux';
import PlainLink from '../../widgets/PlainLink';
import { templatesActions } from '../../actions/templates';

import TemplateWidget from './TemplateWidget';

import styles from './TemplateSelectionModal.module.scss';
import ModalWrapper from '../ModalManager/ModalWrapper';
 
class TemplateSelectionModal extends Component {
	componentDidMount() {
		const {
			fetchTemplates,
			templates
		} = this.props;
		
		if (templates.length === 0) {
			fetchTemplates();
		}
	}

	onSelectTemplate = (e, id) => {
		this.props.onSelectTemplate(id);
	};

	render() {
		return (
			<ModalWrapper>
				<div className={styles.mainContainer}>
					<h1>
						<span>Choose a Template</span>
					</h1>
					<this.MainContent/>
				</div>
			</ModalWrapper>
		);
	}

	MainContent = () => {
		const {
			templates,
			selectedTemplateId
		} = this.props;
		return <div className={styles.templateTable}>
			{ templates && templates.map(template => { return (
					<PlainLink to={`/custom`} key={template.id} >
						<TemplateWidget
							template={template}
							isSelected={template.id === selectedTemplateId}
							onClick={(e) => this.onSelectTemplate(e, template.id)}
						/>
					</PlainLink>
				)})
			}
		</div>
	}
}

const mapStateToProps = state => {
  return {
    templates: state.templates.templates,
    selectedTemplateId: state.templates.selectedId
  }
}

const mapDispatchToProps = dispatch => {
  return {
    fetchTemplates: id => {
    	dispatch(templatesActions.fetchTemplates())
    },
    onSelectTemplate: id => {
    	dispatch(templatesActions.selectTemplate(id))
    }
  }
}

const ConnectedTemplateSelectionModal = connect(
	mapStateToProps,
	mapDispatchToProps
)(TemplateSelectionModal);

export default ConnectedTemplateSelectionModal;