import React, { Component } from 'react';
import { connect } from 'react-redux';

import ImageDisplayReel from '../../widgets/ImageDisplayReel';

import styles from './TemplateSelectionModal.module.scss';
import ModalWrapper from '../ModalManager/ModalWrapper';
import { Template, templatesActions } from '../../store/templates';
import { StateRoot } from '../../models';
import { Redirect } from 'react-router';
import { customPuzzleActions } from '../../store/customPuzzle';

interface TemplateSelectionState {
	isSubmitted: Boolean;
}

interface StateProps {
	templates: Template[];
	customPuzzleId: string;
}

interface DispatchProps {
	fetchTemplates(): void;
	selectTemplate(link: string): void;
}

type TemplateSelectionProps = StateProps & DispatchProps;

class TemplateSelectionModal extends Component<TemplateSelectionProps, TemplateSelectionState> {
	state = {
		isSubmitted: false
	}
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

	selectTemplate = (link: string) => {
		this.setState({
			isSubmitted: true
		});
		this.props.selectTemplate(link);
	}

	render() {
		if (this.state.isSubmitted) {
			return <Redirect to={`/custom/${this.props.customPuzzleId}`} push={true} />
		} else {
			return (
				<ModalWrapper>
					<div className={styles.mainContainer}>
						<h1>
							<span>Choose a Template</span>
						</h1>
						<ImageDisplayReel 
							resourceList={this.props.templates}
							onClickLink={this.selectTemplate}
							onError={this.onError}
						/>
					</div>
				</ModalWrapper>
			);
		}
	}
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    templates: Object.values(state.templates.linkMap),
	customPuzzleId: state.customPuzzle.id
  }
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    fetchTemplates: () => dispatch(templatesActions.fetchTemplates()),
    selectTemplate: (link: string) => dispatch(customPuzzleActions.selectTemplate(link))
  }
}

const ConnectedTemplateSelectionModal = connect(
	mapStateToProps,
	mapDispatchToProps
)(TemplateSelectionModal);

export default ConnectedTemplateSelectionModal;