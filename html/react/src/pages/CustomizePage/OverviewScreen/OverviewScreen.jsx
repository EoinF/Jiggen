import React, { Component } from 'react';
import { connect } from 'react-redux';

import './OverviewScreen.css';
import JiggenHeader from './JiggenHeader';
import { generatedTemplatesActions } from '../../../actions/generatedTemplates';

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
                href='/templates'
              />
              <SelectionWidget
                selection={selectedBackground}
                fallbackImageSrc={backgroundLogo}
                notSelectedCaption='Select a Background' 
                selectedCaption='Background'
                href='/backgrounds'
               />
          </div>
          <div className="overviewBody">
              <PuzzleStats/>
          </div>
          <div className="overviewBody gameContainer">
            <GameContainer/>
          </div>
        </div>
    );
  }
}

const mapStateToProps = state => {
  return {
    selectedBackground: state.backgrounds.backgroundsMap[state.backgrounds.selectedId],
    selectedTemplate: state.templates.templatesMap[state.templates.selectedId],
  };
}

const mapDispatchToProps = dispatch => {
  return {
    fetchGeneratedTemplatesByLink: link => dispatch(generatedTemplatesActions.fetchGeneratedTemplatesByLink(link))
  }
}

const ConnectedOverviewScreen = connect(
  mapStateToProps,
  mapDispatchToProps
)(OverviewScreen);

export default ConnectedOverviewScreen;
