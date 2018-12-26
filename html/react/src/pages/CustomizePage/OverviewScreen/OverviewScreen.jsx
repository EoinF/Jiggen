import React, { Component } from 'react';
import { connect } from 'react-redux';

import './OverviewScreen.css';
import JiggenHeader from './JiggenHeader';
import { generatedTemplatesActions } from '../../../store/generatedTemplates';

import SelectionWidget from './SelectionWidget';
import PuzzleStats from './PuzzleStats';

import GameContainer from '../../../widgets/GameContainer';


import templateLogo from './Template-icon-simple.png';
import backgroundLogo from './Background-icon.png';

class OverviewScreen extends Component {
  
  componentDidMount() {
    const {
      selectedTemplate
    } = this.props;
    if (selectedTemplate) {
      // Clear out the existing generated templates in the store, and refetch them all
      this.props.fetchGeneratedTemplatesByLink(selectedTemplate.links.generatedTemplates);
    }
  };

  render() {
    const {
      generatedTemplate,
      selectedTemplate,
      selectedBackground
    } = this.props;

    return (
        <div className="OverviewScreen">
          <JiggenHeader>
            <h1>Jiggen</h1>
          </JiggenHeader>
          <div className="overviewBody">
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
          <div className="overviewBody">
              <PuzzleStats/>
          </div>
          <div className='overviewBody gameContainer'>
            <GameContainer
              generatedTemplate={generatedTemplate}
              background={selectedBackground}
            />
          </div>
        </div>
    );
  }
}

const mapStateToProps = state => {
  return {
    selectedBackground: state.backgrounds.resourceMap[state.backgrounds.selectedId],
    selectedTemplate: state.templates.templatesMap[state.templates.selectedId],
    generatedTemplate: state.generatedTemplates.resourceMap[state.generatedTemplates.selectedId],
  };
}

const mapDispatchToProps = dispatch => {
  return {
    fetchGeneratedTemplatesByLink: link => dispatch(generatedTemplatesActions.fetchByLink(link))
  }
}

const ConnectedOverviewScreen = connect(
  mapStateToProps,
  mapDispatchToProps
)(OverviewScreen);

export default ConnectedOverviewScreen;
