import React, { Component } from 'react';
import { connect } from 'react-redux';

import styles from './CustomizePage.module.scss';
import { generatedTemplatesActions, GeneratedTemplate } from '../../store/generatedTemplates';

import SelectionWidget from './SelectionWidget';
import PuzzleStats from './PuzzleStats';

import GameContainer from '../../widgets/GameContainer';

import templateLogo from './Template-icon-simple.png';
import backgroundLogo from './Background-icon.png';
import { Background } from '../../store/backgrounds';
import { StateRoot } from '../../models';
import { getSelectedTemplate, getGeneratedTemplatesForTemplate } from '../../store/selectors';
import { displayOptionsActions } from '../../store/displayOptions';

interface StateProps {
  selectedTemplate: any;
  selectedBackground: Background;
  generatedTemplates: GeneratedTemplate[]
}

interface DispatchProps {
  fetchGeneratedTemplatesByLink(link: string): void;
  showBackgroundsModal(): void;
  showTemplatesModal(): void;
}

type CustomizePageProps = StateProps & DispatchProps;

class CustomizePage extends Component<CustomizePageProps> {

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

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    selectedBackground: state.backgrounds.resourceMap[state.backgrounds.selectedId!],
    selectedTemplate: getSelectedTemplate(state),
    generatedTemplates: getGeneratedTemplatesForTemplate(state)
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    fetchGeneratedTemplatesByLink: (link: string) => dispatch(generatedTemplatesActions.fetchAllByLink(link)),
    showBackgroundsModal: () => dispatch(displayOptionsActions.showBackgroundsModal()),
    showTemplatesModal: () => dispatch(displayOptionsActions.showTemplatesModal())
  }
}

const ConnectedCustomizePage = connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomizePage);

export default ConnectedCustomizePage;
