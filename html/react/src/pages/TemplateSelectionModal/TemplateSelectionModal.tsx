import React, { Component } from 'react';
import { connect } from 'react-redux';

import ImageDisplayReel from '../../widgets/ImageDisplayReel';

import styles from './TemplateSelectionModal.module.scss';
import ModalWrapper from '../ModalManager/ModalWrapper';
import { Template, templatesActions } from '../../store/templates';
import { StateRoot } from '../../models';
import { Redirect } from 'react-router';
import { customPuzzleActions } from '../../store/customPuzzle';
import TemplateWidget from '../../widgets/TemplateWidget/TemplateWidget';
import TemplateSelectionSlider from './TemplateSelectionSlider/TemplateSelectionSlider';
import { getSortedTemplates } from '../../store/selectors';


interface TemplateSelectionState {
	isSubmitted: Boolean;
	domainMin: number;
	domainMax: number;
	valueMin: number;
	valueMax: number;
	filteredTemplates: Template[];
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
		isSubmitted: false,
		domainMin: 0,
		domainMax: 0,
		valueMin: 0,
		valueMax: 100,
		filteredTemplates: [] as Template[]
	}
	onError = (resource: Template) => {
		
	};

	onChangeSlider = (valueMin: number, valueMax: number) => {
		this.setState({valueMin, valueMax});
	}
	
	componentDidMount() {
		const {
			fetchTemplates
		} = this.props;
		
		fetchTemplates();
		this.filterTemplates();
		this.calculateDomain();
	}

	componentDidUpdate(prevProps: TemplateSelectionProps, prevState: TemplateSelectionState) {
		if (prevProps.templates != this.props.templates) {
			this.filterTemplates();
			this.calculateDomain();
		} else if (prevState.valueMin != this.state.valueMin || prevState.valueMax != this.state.valueMax) {
			this.filterTemplates();
		}
	}

	selectTemplate = (link: string) => {
		this.setState({
			isSubmitted: true
		});
		this.props.selectTemplate(link);
	}

	filterTemplates = () => {
		const {templates} = this.props;
		const {valueMin, valueMax} = this.state;

		const filteredTemplates = templates.filter(template => 
			template.pieces >= valueMin 
			&& template.pieces <= valueMax
		);
		this.setState({filteredTemplates});
	}

	calculateDomain = () => {
		if (this.props.templates.length > 0) {
			const piecesList = this.props.templates.map(template => template.pieces);
			
			let domainMin = Math.min(...piecesList);
			let domainMax = Math.max(...piecesList);
			// Round down to the next multiple of 5
			domainMin = domainMin - (domainMin % 5);
			// Round up to the next multiple of 5
			domainMax = domainMax + (4 - ((domainMax-1) % 5));
			this.setState({ 
				domainMin,
				domainMax,
				valueMin: domainMin,
				valueMax: domainMax
			});
		}
	}

	render() {
		const {
			domainMin,
			domainMax,
			valueMin,
			valueMax
		} = this.state;

		const isTemplateSliderVisible = domainMin !== domainMax;

		if (this.state.isSubmitted) {
			return <Redirect to={`/custom/${this.props.customPuzzleId}`} push={true} />
		} else {
			return (
				<ModalWrapper>
					<div className={styles.mainContainer}>
						<h1>
							<span>Choose a Template</span>
						</h1>
						{isTemplateSliderVisible &&
							<TemplateSelectionSlider
								minPieces={domainMin} 
								maxPieces={domainMax}
								onChange={this.onChangeSlider}
								valueMin={valueMin}
								valueMax={valueMax}
							/>
						}
						{ (this.state.filteredTemplates.length === 0) ?
						<div className={styles.noTemplatesMessage}>No templates match your current search</div>
						: <div className={styles.templatesContainer}>
							<ImageDisplayReel 
								displayComponents={this.state.filteredTemplates.map(template => (
									<li key={template.links.self} onClick={() => this.selectTemplate(template.links.self)}>
										<TemplateWidget template={template} onError={this.onError} />
									</li>)
								)}
							/>
						</div>
						}
					</div>
				</ModalWrapper>
			);
		}
	}
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    templates: getSortedTemplates(state),
	customPuzzleId: state.customPuzzle.currentPuzzle.id
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