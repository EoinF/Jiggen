import React, { Component } from 'react';
import { connect } from 'react-redux';

import styles from './CreatePuzzlePage.module.scss';
import {GeneratedTemplate } from '../../store/generatedTemplates';

import SelectionWidget from './SelectionWidget';

import templateLogo from './Template-icon-simple.png';
import backgroundLogo from './Background-icon.png';
import { Background } from '../../store/backgrounds';
import { StateRoot } from '../../models';
import { displayOptionsActions } from '../../store/displayOptions';
import { Template } from '../../store/templates';
import { PlainLink } from '../../widgets';

interface StateProps {
  selectedTemplate: Template;
  selectedBackground: Background;
}

interface DispatchProps {
  showBackgroundsModal(): void;
  showTemplatesModal(): void;
}

type CreatePuzzlePageProps = StateProps & DispatchProps;

class CreatePuzzlePage extends Component<CreatePuzzlePageProps> {

  onClickConfirm = () => {

  }

  render() {
    const {
      selectedTemplate,
      selectedBackground
    } = this.props;

    return (
        <div className={styles.mainContainer}>
        <div className={styles.contentContainer}>
            <div className={styles.inputField}>
              <div className={styles.inputContainer}>
                <input value="Custom Puzzle"/>
              </div>
            </div>
            <div className={styles.selectionContainer}>
                <SelectionWidget
                  selection={selectedTemplate}
                  fallbackImageSrc={templateLogo}
                  notSelectedCaption='Choose a Template'
                  onClick={this.props.showTemplatesModal}
                />
                <SelectionWidget
                  selection={selectedBackground}
                  fallbackImageSrc={backgroundLogo}
                  notSelectedCaption='Choose a Background'
                  onClick={this.props.showBackgroundsModal}
                />
            </div>
            <div className={styles.buttonControls}>
              <PlainLink to="/custom">
                <button onClick={this.onClickConfirm} className={styles.save}>✓</button>
              </PlainLink>
              <PlainLink to="/custom">
                <button className={styles.cancel}>X</button>
              </PlainLink>
            </div>
          </div>
        </div>
    );
  }
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    selectedTemplate: state.templates.linkMap[state.customPuzzle.selectedTemplate!],
    selectedBackground: state.backgrounds.linkMap[state.customPuzzle.selectedBackground!]
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    showBackgroundsModal: () => dispatch(displayOptionsActions.showBackgroundsModal()),
    showTemplatesModal: () => dispatch(displayOptionsActions.showTemplatesModal())
  }
}

export default  connect(
  mapStateToProps,
  mapDispatchToProps
)(CreatePuzzlePage);
