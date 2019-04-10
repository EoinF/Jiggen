import React, { Component, ChangeEvent } from 'react';
import { connect } from 'react-redux';

import styles from './CreatePuzzlePage.module.scss';

import SelectionWidget from './SelectionWidget';

import templateLogo from './Template-icon-simple.png';
import backgroundLogo from './Background-icon.png';
import { Background } from '../../store/backgrounds';
import { StateRoot } from '../../models';
import { displayOptionsActions } from '../../store/displayOptions';
import { Template } from '../../store/templates';
import { PlainLink } from '../../widgets';
import { customPuzzleActions } from '../../store/customPuzzle';

interface StateProps {
  puzzleName: string;
  selectedTemplate: Template;
  selectedBackground: Background;
}

interface DispatchProps {
  showBackgroundsModal(): void;
  showTemplatesModal(): void;
  setName(name: string): void;
}

type CreatePuzzlePageProps = StateProps & DispatchProps;

class CreatePuzzlePage extends Component<CreatePuzzlePageProps> {

  onClickConfirm = () => {

  }

  onChangeName = (e: ChangeEvent<HTMLInputElement>) => {
    this.props.setName(e.target.value)
  }

  render() {
    const {
      puzzleName,
      selectedTemplate,
      selectedBackground
    } = this.props;

    const isReady = selectedTemplate != null 
      && selectedBackground != null 
      && puzzleName.length > 0;

    return (
        <div className={styles.mainContainer}>
        <div className={styles.contentContainer}>
            <div className={styles.inputField}>
              <div className={styles.inputContainer}>
                <input 
                  value={this.props.puzzleName}
                  onChange={this.onChangeName}
                />
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
              { isReady ? ( 
                <PlainLink to="/custom">
                  <button onClick={this.onClickConfirm} className={styles.save}>✓</button>
              </PlainLink> ) : (
                <button disabled onClick={this.onClickConfirm} className={styles.save}>✓</button>
                )
              }
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
    puzzleName: state.customPuzzle.name,
    selectedTemplate: state.templates.linkMap[state.customPuzzle.selectedTemplate!],
    selectedBackground: state.backgrounds.linkMap[state.customPuzzle.selectedBackground!]
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    setName: (name: string) => dispatch(customPuzzleActions.setName(name)),
    showBackgroundsModal: () => dispatch(displayOptionsActions.showBackgroundsModal()),
    showTemplatesModal: () => dispatch(displayOptionsActions.showTemplatesModal())
  }
}

export default  connect(
  mapStateToProps,
  mapDispatchToProps
)(CreatePuzzlePage);
