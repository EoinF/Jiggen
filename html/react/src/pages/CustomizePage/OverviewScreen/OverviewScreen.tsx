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

interface OverviewScreenProps {
  selectedTemplate: any;
  selectedBackground: Background;
  generatedTemplate: GeneratedTemplate
  fetchGeneratedTemplatesByLink(link: string): void;
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
      generatedTemplate,
      selectedTemplate,
      selectedBackground
    } = this.props;

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
                notSelectedCaption='Select a Template'
                selectedCaption='Template'
                href='/custom/templates'
              />
              <SelectionWidget
                selection={selectedBackground}
                fallbackImageSrc={backgroundLogo}
                notSelectedCaption='Select a Background'
                selectedCaption='Background'
                href='/custom/backgrounds'
               />
          </div>
          <div className={styles.overviewBody}>
              <PuzzleStats/>
          </div>
        </div>
    );
  }
}

const mapStateToProps = (_state: any) => {
  const state = _state as StateRoot;
  return {
    selectedBackground: state.backgrounds.resourceMap[state.backgrounds.selectedId!],
    selectedTemplate: state.templates.templatesMap[state.templates.selectedId],
    generatedTemplate: state.generatedTemplates.resourceMap[state.generatedTemplates.selectedId],
  };
}

const mapDispatchToProps = (dispatch: Function) => {
  return {
    fetchGeneratedTemplatesByLink: (link: string) => dispatch(generatedTemplatesActions.fetchAllByLink(link))
  }
}

const ConnectedOverviewScreen = connect(
  mapStateToProps,
  mapDispatchToProps
)(OverviewScreen);

export default ConnectedOverviewScreen;
