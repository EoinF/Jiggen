import React, { Component, ChangeEvent } from 'react';
import { connect } from 'react-redux';

import styles from './CreatePuzzlePage.module.scss';

import SelectionWidget from './SelectionWidget';

import templateLogo from '../../assets/template-icon.png';
import backgroundLogo from './Background-icon.png';
import saveIcon from '../../assets/tick-icon.png';
import cancelIcon from '../../assets/back-icon.png';

import { Background, backgroundsActions } from '../../store/backgrounds';
import { StateRoot } from '../../models';
import { displayOptionsActions } from '../../store/displayOptions';
import { Template, templatesActions } from '../../store/templates';
import { PlainLink, ResponsiveImage } from '../../widgets';
import { customPuzzleActions, CustomPuzzle } from '../../store/customPuzzle';
import { RouteComponentProps } from 'react-router';
import PieceCountDisplay from '../../widgets/PieceCountDisplay/PieceCountDisplay';
import TemplateDisplay from '../../widgets/TemplateDisplay/TemplateDisplay';

interface StateProps {
  customPuzzle: CustomPuzzle;
  selectedTemplate: Template | null;
  selectedBackground: Background | null;
}

interface DispatchProps {
  showBackgroundsModal(): void;
  showTemplatesModal(): void;
  setName(name: string): void;
  saveCustomPuzzle(): void;
  selectCustomPuzzle(id: string | null): void;
  fetchBackgroundByLink(link: string): void;
  addBackground(background: Background): void;
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
    if (this.props.customPuzzle.template != null && this.props.customPuzzle.template != prevProps.customPuzzle.template) {
      this.props.fetchTemplateByLink(this.props.customPuzzle.template);
    }
    if (this.props.customPuzzle.background != null && prevProps.customPuzzle.background != null
       && (this.props.customPuzzle.background.links.self != prevProps.customPuzzle.background.links.self)) {
      if (this.props.customPuzzle.background.isCustom) {
        this.props.addBackground(this.props.customPuzzle.background)
      } else {
        this.props.fetchBackgroundByLink(this.props.customPuzzle.background.links.self);
      }
    }
  }

  onChangeName = (e: ChangeEvent<HTMLInputElement>) => {
    this.props.setName(e.target.value)
  }

  render() {
    const {
      customPuzzle,
      selectedTemplate,
      selectedBackground
    } = this.props;

    const isReady = selectedTemplate != null 
      && selectedBackground != null
      && customPuzzle.name.length > 0;

    return (
        <div className={styles.mainContainer}>
          <div className={styles.controlsContainer}>
            <div className={styles.inputField}>
              <div className={styles.inputContainer}>
                <input 
                  maxLength={24}
                  value={customPuzzle.name}
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
                >
                  {
                    selectedTemplate && <TemplateDisplay template={selectedTemplate} />
                  }
                </SelectionWidget>
                <SelectionWidget
                  selection={selectedBackground}
                  fallbackImageSrc={backgroundLogo}
                  notSelectedCaption='Choose a Background'
                  onClick={this.props.showBackgroundsModal}
                >
                  {selectedBackground && <ResponsiveImage 
                      src={selectedBackground.links['image-compressed'] || selectedBackground.links.image}
                      alt={selectedBackground.name}
                    />
                  }
                </SelectionWidget>
            </div>
            <div className={styles.buttonControls}>
              <PlainLink to="/custom">
                <button className={styles.iconSmall}>
                  <img src={cancelIcon}/>
                </button>
              </PlainLink>
              { isReady ? ( 
                <PlainLink to="/custom">
                  <button onClick={this.props.saveCustomPuzzle} className={styles.iconSmall}>
                  <img src={saveIcon}/></button>
              </PlainLink> ) : (
                <button disabled onClick={this.props.saveCustomPuzzle} className={styles.iconSmall}>
                <img src={saveIcon}/></button>
                )
              }
            </div>
          </div>
        </div>
    );
  }
}

const mapStateToProps = (state: StateRoot): StateProps => {
  return {
    customPuzzle: state.customPuzzle.currentPuzzle,
    selectedTemplate: state.customPuzzle.currentPuzzle.template != null
      ? state.templates.linkMap[state.customPuzzle.currentPuzzle.template]: null,
    selectedBackground: state.customPuzzle.currentPuzzle.background != null 
      ? state.backgrounds.linkMap[state.customPuzzle.currentPuzzle.background.links.self]: null
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
    addBackground: (background: Background) => dispatch(backgroundsActions.setBackground(background)),
    fetchTemplateByLink: (link: string) => dispatch(templatesActions.fetchByLink(link))
  }
}

export default  connect(
  mapStateToProps,
  mapDispatchToProps
)(CreatePuzzlePage);
