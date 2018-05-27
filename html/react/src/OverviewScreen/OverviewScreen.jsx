import React, { Component } from 'react';
import { connect } from 'react-redux';
import PlainLink from '../utils/PlainLink';

import './OverviewScreen.css';
import ContentOrMessage from './ContentOrMessage';
import { TemplateWidget } from '../TemplateSelection';
import { generatedTemplatesActions } from '../actions';

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

    const puzzleLink = '/puzzle/' + (selectedGeneratedTemplate ? `${selectedGeneratedTemplate.id}` : '');

    return (
        <div className="OverviewScreen">
          <h1 className="OverviewTitle">Jiggen</h1>
          <div className="OverviewBody">
            <div>
              <PlainLink to={`/templates`}>
                <ContentOrMessage message="Select a template" showContent={!!selectedTemplate}>
                  <TemplateWidget template={selectedTemplate} />
                </ContentOrMessage>
              </PlainLink>
            </div>
            <div>
              <PlainLink to={`/backgrounds`}>
                <ContentOrMessage message="Select a background" showContent={!!selectedBackground}>
                  <TemplateWidget template={selectedBackground} />
                </ContentOrMessage>
              </PlainLink>
            </div>
            <div>
              <button>
                <PlainLink to={puzzleLink}>
                  <ContentOrMessage message="Select a template" showContent={!!selectedGeneratedTemplate}>
                    <TemplateWidget template={selectedGeneratedTemplate} />
                  </ContentOrMessage>
                </PlainLink>
              </button>
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
