import React, { Component } from 'react';
import { connect } from 'react-redux';

import './OverviewScreen.css';
import JiggenHeader from './JiggenHeader';
import { generatedTemplatesActions } from '../actions';

import TemplateChooser from './TemplateChooser';
import BackgroundChooser from './BackgroundChooser';
import PuzzleLauncher from './PuzzleLauncher';

class OverviewScreen extends Component {

  componentDidMount() {
    const { selectedTemplate} = this.props;
    if (selectedTemplate) { 
      this.props.fetchGeneratedTemplatesByLink(selectedTemplate.links.generatedTemplates);
    }
  };

  render() {
    const {
      selectedTemplate,
      selectedGeneratedTemplate,
      selectedBackground
    } = this.props;

    return (
        <div className="OverviewScreen">
          <div>
            <JiggenHeader>
              <h1>Jiggen</h1>
            </JiggenHeader>
          </div>
          <div className="OverviewBody">
            <div>
              <TemplateChooser template={selectedTemplate} />
            </div>
            <div>
                <BackgroundChooser background={selectedBackground} />
            </div>
          </div>
          <div className="OverviewBody">
            <div>
              <PuzzleLauncher generatedTemplate={selectedGeneratedTemplate} />
            </div>
          </div>
        </div>
    );
  }
}

const mapStateToProps = state => {
  return {
    selectedTemplate: state.templates.templatesMap[state.templates.selectedId],
    selectedGeneratedTemplate: state.generatedTemplates.generatedTemplatesMap[state.generatedTemplates.selectedId]
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
