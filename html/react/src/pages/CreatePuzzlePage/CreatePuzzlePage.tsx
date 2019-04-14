import React, { Component, ChangeEvent } from 'react';
import { connect } from 'react-redux';

import styles from './CreatePuzzlePage.module.scss';

import SelectionWidget from './SelectionWidget';

import templateLogo from '../../assets/template-icon.png';
import backgroundLogo from './Background-icon.png';
import { Background, backgroundsActions } from '../../store/backgrounds';
import { StateRoot } from '../../models';
import { displayOptionsActions } from '../../store/displayOptions';
import { Template, templatesActions } from '../../store/templates';
import { PlainLink } from '../../widgets';
import { customPuzzleActions } from '../../store/customPuzzle';
import { RouteComponentProps } from 'react-router';

interface StateProps {
  puzzleName: string;
  selectedTemplate: Template;
  selectedBackground: Background;
  templateLink: string | null;
  backgroundLink: string | null;
}

interface DispatchProps {
  showBackgroundsModal(): void;
  showTemplatesModal(): void;
  setName(name: string): void;
  saveCustomPuzzle(): void;
  selectCustomPuzzle(id: string | null): void;
  fetchBackgroundByLink(link: string): void;
  fetchTemplateByLink(link: string): void;
}

interface CreatePuzzlePageRouteParams {
  id: string;
}

type CreatePuzzlePageProps = StateProps & DispatchProps & RouteComponentProps<CreatePuzzlePageRouteParams>;

class CreatePuzzlePage extends Component<CreatePuzzlePageProps> {

  componentDidMount() {
    const params = this.props.match.params;
    if (params.id !== 'new') {
      this.props.selectCustomPuzzle(params.id);
    } else {
      this.props.selectCustomPuzzle(null);
    }
  }

  componentDidUpdate = (prevProps: CreatePuzzlePageProps) => {
    if (this.props.templateLink != null && this.props.templateLink != prevProps.templateLink) {
      this.props.fetchTemplateByLink(this.props.templateLink);
    }
    if (this.props.backgroundLink != null && this.props.backgroundLink != prevProps.backgroundLink) {
      this.props.fetchBackgroundByLink(this.props.backgroundLink);
    }
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
                  maxLength={24}
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
                  <button onClick={this.props.saveCustomPuzzle} className={styles.save}>✓</button>
              </PlainLink> ) : (
                <button disabled onClick={this.props.saveCustomPuzzle} className={styles.save}>✓</button>
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
    templateLink: state.customPuzzle.selectedTemplate,
    backgroundLink: state.customPuzzle.selectedBackground,
    selectedTemplate: state.templates.linkMap[state.customPuzzle.selectedTemplate!],
    selectedBackground: state.backgrounds.linkMap[state.customPuzzle.selectedBackground!]
  };
}

const mapDispatchToProps = (dispatch: Function): DispatchProps => {
  return {
    setName: (name: string) => dispatch(customPuzzleActions.setName(name)),
    selectCustomPuzzle: (id: string) => dispatch(customPuzzleActions.selectPuzzle(id)),
    showBackgroundsModal: () => dispatch(displayOptionsActions.showBackgroundsModal()),
    showTemplatesModal: () => dispatch(displayOptionsActions.showTemplatesModal()),
    saveCustomPuzzle: () => dispatch(customPuzzleActions.savePuzzle()),
    fetchBackgroundByLink: (link: string) => dispatch(backgroundsActions.fetchByLink(link)),
    fetchTemplateByLink: (link: string) => dispatch(templatesActions.fetchByLink(link))
  }
}

export default  connect(
  mapStateToProps,
  mapDispatchToProps
)(CreatePuzzlePage);
