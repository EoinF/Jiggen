import React, { Component } from 'react';
import { connect } from 'react-redux';

import styles from './CreatePuzzlePage.module.scss';
import { generatedTemplatesActions, GeneratedTemplate } from '../../store/generatedTemplates';

import SelectionWidget from './SelectionWidget';
import PuzzleStats from './PuzzleStats';

import templateLogo from './Template-icon-simple.png';
import backgroundLogo from './Background-icon.png';
import { Background } from '../../store/backgrounds';
import { StateRoot } from '../../models';
import { getSelectedTemplate, getGeneratedTemplatesForTemplate } from '../../store/selectors';
import { displayOptionsActions } from '../../store/displayOptions';
import { puzzleSolverActions } from '../../store/puzzleSolverScreen';
import { Template } from '../../store/templates';

interface StateProps {
  selectedTemplate: Template;
  selectedBackground: Background;
  generatedTemplates: GeneratedTemplate[];
}

interface DispatchProps {
  fetchGeneratedTemplatesByLink(link: string): void;
  selectGeneratedTemplateByLink(link: string): void;
  showBackgroundsModal(): void;
  showTemplatesModal(): void;
}

type CreatePuzzlePageProps = StateProps & DispatchProps;

class CreatePuzzlePage extends Component<CreatePuzzlePageProps> {

  componentDidUpdate(prevProps: CreatePuzzlePageProps) {
    if (this.props.selectedTemplate != prevProps.selectedTemplate) {
      this.updateGeneratedTemplate();
    }
    if (this.props.generatedTemplates != null &&
        this.props.generatedTemplates.length > 0 &&
        this.props.generatedTemplates != prevProps.generatedTemplates) {
      this.props.selectGeneratedTemplateByLink(this.props.generatedTemplates[0].links.self);
    }
  }
  
  updateGeneratedTemplate = () => {
    const {
      selectedTemplate,
      fetchGeneratedTemplatesByLink
    } = this.props;
    if (selectedTemplate != null) {
      fetchGeneratedTemplatesByLink(selectedTemplate.links.generatedTemplates);
    }
  }


  render() {
    const {
      selectedTemplate,
      selectedBackground
    } = this.props;

    return (
        <div className={styles.mainContainer}>
          <h1>Custom Puzzle</h1>
          <div className={styles.contentContainer}>
            <div className={styles.selectionContainer}>
                <SelectionWidget
                  selection={selectedTemplate}
                  fallbackImageSrc={templateLogo}
                  notSelectedCaption='Select Template'
                  selectedCaption='Template'
                  onClick={this.props.showTemplatesModal}
                />
                <SelectionWidget
                  selection={selectedBackground}
                  fallbackImageSrc={backgroundLogo}
                  notSelectedCaption='Select Background'
                  selectedCaption='Background'
                  onClick={this.props.showBackgroundsModal}
                />
            </div>
            <div className={styles.statsContainer}>
                <PuzzleStats />
            </div>
          </div>
        </div>
    );
  }
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    selectedTemplate: getSelectedTemplate(state),
    selectedBackground: state.backgrounds.linkMap[state.puzzleSolverScreen.selectedBackground!],
    generatedTemplates: getGeneratedTemplatesForTemplate(state)
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    fetchGeneratedTemplatesByLink: (link: string) => dispatch(generatedTemplatesActions.fetchAllByLink(link)),
    selectGeneratedTemplateByLink: (link: string) => dispatch(puzzleSolverActions.selectAndDownloadGeneratedTemplate(link)),
    showBackgroundsModal: () => dispatch(displayOptionsActions.showBackgroundsModal()),
    showTemplatesModal: () => dispatch(displayOptionsActions.showTemplatesModal())
  }
}

export default  connect(
  mapStateToProps,
  mapDispatchToProps
)(CreatePuzzlePage);
