import React, { Component, Dispatch } from 'react';
import { connect } from 'react-redux';

import styles from './OverviewScreen.module.scss';
import { generatedTemplatesActions, GeneratedTemplate } from '../../../store/generatedTemplates';

import SelectionWidget from './SelectionWidget';
import PuzzleStats from './PuzzleStats';

import GameContainer from '../../../widgets/GameContainer';


import templateLogo from './Template-icon-simple.png';
import backgroundLogo from './Background-icon.png';
import { Background } from '../../../store/backgrounds';
import { BaseState, StateRoot } from '../../../models';
import { getSelectedTemplate, getGeneratedTemplatesForTemplate } from '../../../store/selectors';
import BackgroundSelectionModal from '../../BackgroundSelectionModal';
import TemplateSelection from '../../TemplateSelectionModal';
import { displayOptionsActions } from '../../../actions/displayOptions';

interface OverviewScreenProps {
  selectedTemplate: any;
  selectedBackground: Background;
  generatedTemplates: GeneratedTemplate[]
  fetchGeneratedTemplatesByLink(link: string): void;
  showBackgroundsModal(): void;
  showTemplatesModal(): void;
}

class OverviewScreen extends Component<OverviewScreenProps> {

  componentDidMount() {
    const {
      selectedTemplate,
      fetchGeneratedTemplatesByLink
    } = this.props;
    if (selectedTemplate != null) {
      // Clear out the existing generated templates in the store, and refetch them all
      fetchGeneratedTemplatesByLink(selectedTemplate.links.generatedTemplates);
    }
  };

  render() {
    const {
      selectedTemplate,
      selectedBackground
    } = this.props;

    const generatedTemplate = this.props.generatedTemplates[0];
    return (
        <div className={styles.mainContainer}>
          <h1>Custom Puzzle</h1>
          <div className={styles.gameContainer}>
            <GameContainer
              generatedTemplate={generatedTemplate}
              background={selectedBackground}
            />
          </div>
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
          <div className={styles.overviewBody}>
              <PuzzleStats
                background={selectedBackground}
                generatedTemplate={generatedTemplate}
              />
          </div>
        </div>
    );
  }
}

const mapStateToProps = (_state: any) => {
  const state = _state as StateRoot;
  return {
    selectedBackground: state.backgrounds.resourceMap[state.backgrounds.selectedId!],
    selectedTemplate: getSelectedTemplate(state),
    generatedTemplates: getGeneratedTemplatesForTemplate(state),
  };
}

const mapDispatchToProps = (dispatch: Function) => {
  return {
    fetchGeneratedTemplatesByLink: (link: string) => dispatch(generatedTemplatesActions.fetchAllByLink(link)),
    showBackgroundsModal: () => dispatch(displayOptionsActions.showBackgroundsModal())
  }
}

const ConnectedOverviewScreen = connect(
  mapStateToProps,
  mapDispatchToProps
)(OverviewScreen);

export default ConnectedOverviewScreen;
